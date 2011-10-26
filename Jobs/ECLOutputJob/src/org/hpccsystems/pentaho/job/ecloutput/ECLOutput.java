/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecloutput;

import java.util.ArrayList;
import java.util.List;
import org.hpccsystems.ecldirect.Output;
import org.hpccsystems.ecldirect.EclDirect;
import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.compatibility.Value;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;
import org.hpccsystems.ecldirect.Column;
import java.io.*;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.core.*;
import org.pentaho.di.core.gui.SpoonFactory;

import org.pentaho.di.plugins.perspectives.eclresults.*;
/**
 *
 * @author ChalaAX
 */
public class ECLOutput extends JobEntryBase implements Cloneable, JobEntryInterface {
    

    private String attributeName;
    private String fileName;
    private String serverAddress;
    
    public static boolean isReady = false;


    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    
    
    

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
       
        Result result = prevResult;
        if(result.isStopped()){
            logBasic("{Output Job is Stopped}");
           // ECLOutput.isReady=false;
           // int counter = 0;
           // while(!ECLOutput.isReady && counter < 1000){
                //waiting loop until other thread catches up.
               // logBasic("waiting(" + counter + "): " + ECLOutput.isReady);
           //     counter++;
           // }
           if(!ECLOutput.isReady){
             //  result.setResult(false);
           } 
        }else{
            ECLOutput.isReady=true;
            logBasic("not waiting: " + ECLOutput.isReady);
            Output op = new Output();
            op.setDefinitionName(getAttributeName());

            String opResults = op.ecl();

            logBasic("{Output Job} Execute = " + opResults);

            logBasic("{Output Job} Previous =" + result.getLogText());

            result.setResult(true);

            RowMetaAndData data = new RowMetaAndData();
            data.addValue("ecl", Value.VALUE_TYPE_STRING, opResults);


            List list = result.getRows();
            list.add(data);
            String eclCode = "";
            if (list == null) {
                list = new ArrayList();
            } else {

                for (int i = 0; i < list.size(); i++) {
                    RowMetaAndData rowData = (RowMetaAndData) list.get(i);
                    String code = rowData.getString("ecl", null);
                    if (code != null) {
                        eclCode += code;
                    }
                }
                logBasic("{Output Job} Output Code =" + eclCode);
            }


            EclDirect eclDirect = new EclDirect(this.serverAddress, "thor");
            ArrayList dsList;
            String outStr = "";
            try{
                eclCode = "IMPORT Std;\n" + eclCode;
                dsList = eclDirect.execute(eclCode);
           
                //logBasic("Start Log Results");
            
                String newline = System.getProperty("line.separator");
                for (int iList = 0; iList < dsList.size(); iList++) {
                    //logBasic("----------Outer-------------");
                    ArrayList rowList = (ArrayList) dsList.get(iList);

                    for (int jRow = 0; jRow < rowList.size(); jRow++) {
                        //logBasic("----------Row-------------");
                        ArrayList columnList = (ArrayList) rowList.get(jRow);

                        for (int lCol = 0; lCol < columnList.size(); lCol++) {
                         //   logBasic("----------Column-------------");
                            Column column = (Column) columnList.get(lCol);
                            logBasic(column.getName() + "=" + column.getValue() + "|");
                            outStr += column.getValue();
                            if(lCol< (columnList.size()-1)){
                                outStr += ",";
                            }
                        }
                        logBasic("newline");
                        outStr += newline;
                    }
                }
                result.setRows(list);
            
             }catch (Exception e){
                logError("Failed to execute code on Cluster.");
                result.setResult(false);
            }
               
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(this.fileName));
                out.write(outStr);
                out.close();
           
            } catch (IOException e) {
               logError("Failed to write file: " + this.fileName); 
               result.setResult(false);
            }  
            
            try {
                
               // System.setProperty("fileName", this.fileName);
                //Spoon spoon = ((Spoon)SpoonFactory.getInstance());
                //spoon.variables.addValue("fileName", 1, fileName);
                        //("fileName",filenName);
                System.getProperties().setProperty("fileName",fileName);
                
                BufferedWriter out = new BufferedWriter(new FileWriter(this.fileName + ".ecl"));
                out.write(eclCode);
                out.close();
            } catch (IOException e) {
            }   
             
        
       }
       return result;
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);

            setAttributeName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "attribute_name")));
            setServerAddress(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "server_address")));
            setFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_name")));


        } catch (Exception e) {
            throw new KettleXMLException("ECL Output Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();

        retval += "		<attribute_name>" + attributeName + "</attribute_name>" + Const.CR;
        retval += "		<server_address>" + serverAddress + "</server_address>" + Const.CR;
        retval += "		<file_name>" + fileName + "</file_name>" + Const.CR;
  
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
          
            attributeName = rep.getStepAttributeString(id_jobentry, "attributeName"); //$NON-NLS-1$
            serverAddress = rep.getStepAttributeString(id_jobentry, "serverAddress"); //$NON-NLS-1$
            fileName = rep.getStepAttributeString(id_jobentry, "fileName"); //$NON-NLS-1$
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
       
            rep.saveStepAttribute(id_job, getObjectId(), "attributeName", attributeName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "serverAddress", serverAddress); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "fileName", fileName); //$NON-NLS-1$
           
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_job, e);
        }
    }

    public boolean evaluates() {
        return true;
    }

    public boolean isUnconditional() {
        return true;
    }
}
