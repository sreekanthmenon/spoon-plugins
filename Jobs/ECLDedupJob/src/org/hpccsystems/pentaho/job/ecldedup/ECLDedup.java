/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecldedup;

import java.util.ArrayList;
import java.util.List;
import org.hpccsystems.ecldirect.Iterate;
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

/**
 *
 * @author ChalaAX
 */
public class ECLDedup extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    //private String jobName;
    private String name;
    
    private String recordset;//Comma seperated list of fieldNames. a "-" prefix to the field name will indicate descending order
    private String condition;
    private Boolean isAll;
    private Boolean isHash;
    private String keep;
    private String keeper;
    private Boolean runLocal;

    
    

    
    public void setRunLocalString(String runLocal) {
        
        if(runLocal.equals("true")){
            this.runLocal = true;
        }else{
            this.runLocal = false;
        }
        
    }
    
    public String getRunLocalString() {
        if(this.runLocal != null && this.runLocal){
            return "true";
        }else{
            return "false";
        }
    }



    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
        
        Result result = prevResult;
        
    
        Iterate iterate = new Iterate();
        iterate.setName(this.getName());
        iterate.setRecordset(this.getRecordset());
        iterate.setRunLocal(this.getRunLocal());

        logBasic("{Iterate Job} Execute = " + iterate.ecl());
        
        logBasic("{Iterate Job} Previous =" + result.getLogText());
        
        result.setResult(true);
        
        RowMetaAndData data = new RowMetaAndData();
        data.addValue("ecl", Value.VALUE_TYPE_STRING, iterate.ecl());
        
        
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
            logBasic("{Iterate Job} ECL Code =" + eclCode);
        }
        
        result.setRows(list);
        
        
        return result;
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            //this.setName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "name")));
            

            this.setRecordset(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset")));
            this.setCondition(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"condition")));
            this.setTransform(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transform")));
            this.setFieldlist(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"fieldlist")));
            this.setGroup(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"group")));
            this.setRunLocalString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"runLocal")));

        } catch (Exception e) {
            throw new KettleXMLException("ECL Distribute Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        
        retval += "             <recordset>"+this.recordset+"</recordset>"+Const.CR;
        retval += "             <condition>"+this.condition+"</condition>"+Const.CR;
        retval += "             <transform>"+this.transform+"</transform>"+Const.CR;
        retval += "             <fieldlist>"+this.fieldlist+"</fieldlist>"+Const.CR;
        retval += "             <group>"+this.group+"</group>"+Const.CR;
        retval += "             <runLocal>"+this.getRunLocalString()+"</runLocal>"+Const.CR;
       

       
       
       
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            //jobName = rep.getStepAttributeString(id_jobentry, "jobName"); //$NON-NLS-1$

            //name = rep.getStepAttributeString(id_jobentry, "name"); //$NON-NLS-1$
            

            recordset = rep.getStepAttributeString(id_jobentry, "recordset");
            condition = rep.getStepAttributeString(id_jobentry, "condition");
            transform = rep.getStepAttributeString(id_jobentry, "transform");
            fieldlist = rep.getStepAttributeString(id_jobentry, "fieldlist");
            group = rep.getStepAttributeString(id_jobentry, "group");            
            this.setRunLocalString(rep.getStepAttributeString(id_jobentry, "runLocal"));

        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            //rep.saveStepAttribute(id_job, getObjectId(), "jobName", jobName); //$NON-NLS-1$

            //rep.saveStepAttribute(id_job, getObjectId(), "name", name); //$NON-NLS-1$

            rep.saveStepAttribute(id_job, getObjectId(), "recordset", recordset);
            rep.saveStepAttribute(id_job, getObjectId(), "condition", condition);
            rep.saveStepAttribute(id_job, getObjectId(), "transform", transform);
            rep.saveStepAttribute(id_job, getObjectId(), "fieldlist", fieldlist);
            rep.saveStepAttribute(id_job, getObjectId(), "group", group);
            rep.saveStepAttribute(id_job, getObjectId(), "runLocal", this.getRunLocalString());
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
