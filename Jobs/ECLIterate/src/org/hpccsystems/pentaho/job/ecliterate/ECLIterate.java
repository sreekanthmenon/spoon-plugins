/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecliterate;

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
public class ECLIterate extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    //private String jobName;
    private String name;
    private String transform;
    private String transformName;
    private String recordset;//Comma seperated list of fieldNames. a "-" prefix to the field name will indicate descending order
    private Boolean runLocal = false;
    
    private String record;
    private String recordName;
    private String recordsetName;
    
    private String transformCall;

    public String getTransformCall() {
        return transformCall;
    }

    public void setTransformCall(String transformCall) {
        this.transformCall = transformCall;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordsetName() {
        return recordsetName;
    }

    public void setRecordsetName(String recordsetName) {
        this.recordsetName = recordsetName;
    }


    public String getTransformName() {
        return transformName;
    }

    public void setTransformName(String transformName) {
        this.transformName = transformName;
    }


    
    public String getRecordset() {
        return recordset;
    }

    public void setRecordset(String recordset) {
        this.recordset = recordset;
    }

    public Boolean getRunLocal() {
        return runLocal;
    }

    public void setRunLocal(Boolean runLocal) {
        this.runLocal = runLocal;
    }
    
    public void setRunLocalString(String runLocal) {
        
        if(runLocal.equals("true")){
            this.runLocal = true;
        }else{
            this.runLocal = false;
        }
        
    }
    
    public String getRunLocalString() {
        if(runLocal){
            return "true";
        }else{
            return "false";
        }
    }



    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }
    
   
    
    

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
        
        Result result = prevResult;
        

    
    
        Iterate iterate = new Iterate();
        iterate.setName(this.getName());
        iterate.setRecordset(this.getRecordset());
        iterate.setRunLocal(this.getRunLocal());
        
        iterate.setTransform(this.getTransform());
        iterate.setTransformName(this.getTransformName());
        iterate.setRecord(this.getRecord());
        iterate.setRecordName(this.getRecordName());
        iterate.setRecordsetName(this.getRecordsetName());
        iterate.setTransformCall(this.getTransformCall());

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
            this.setTransformName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transformName")));
            this.setTransform(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transform")));

            this.setRecord(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"record")));
            this.setRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordName")));
            this.setRecordsetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordsetName")));
           
           this.setTransformCall(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"transformCall")));
           
            
            
            
            this.setRecordset(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"recordset")));
            this.setRunLocalString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"runLocal")));
        } catch (Exception e) {
            throw new KettleXMLException("ECL Distribute Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        //retval += "		<job_name>" + jobName + "</jobName>" + Const.CR;
        //retval += "		<name>" + name + "</name>" + Const.CR;
                                       /*
    private Text record;
    private Text recordName;
    private Text recordsetName;
    */
        retval += "             <record>"+this.record+"</record>"+Const.CR;
        retval += "             <recordName>"+this.recordName+"</recordName>"+Const.CR;
        retval += "             <recordsetName>"+this.recordsetName+"</recordsetName>"+Const.CR;
        
        
        retval += "             <transformName>"+this.transformName+"</transformName>"+Const.CR;
        retval += "             <transform>"+this.transform+"</transform>"+Const.CR;
        retval += "             <transformCall>"+this.transformCall+"</transformCall>"+Const.CR;
        retval += "             <recordset>"+this.recordset+"</recordset>"+Const.CR;
        retval += "             <runLocal>"+this.getRunLocalString()+"</runLocal>"+Const.CR;
       
       
       
       
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            //jobName = rep.getStepAttributeString(id_jobentry, "jobName"); //$NON-NLS-1$

            //name = rep.getStepAttributeString(id_jobentry, "name"); //$NON-NLS-1$

            record = rep.getStepAttributeString(id_jobentry, "record");
            recordName = rep.getStepAttributeString(id_jobentry, "recordName");
            recordsetName = rep.getStepAttributeString(id_jobentry, "recordsetName");
            
            
            transformName = rep.getStepAttributeString(id_jobentry, "transformName");
            transform = rep.getStepAttributeString(id_jobentry, "transform");
            transformCall = rep.getStepAttributeString(id_jobentry, "transformCall");
            recordset = rep.getStepAttributeString(id_jobentry, "recordset");
            this.setRunLocalString(rep.getStepAttributeString(id_jobentry, "runLocal"));

        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            //rep.saveStepAttribute(id_job, getObjectId(), "jobName", jobName); //$NON-NLS-1$

            rep.saveStepAttribute(id_job, getObjectId(), "record", record);
            rep.saveStepAttribute(id_job, getObjectId(), "recordName", recordName);
            rep.saveStepAttribute(id_job, getObjectId(), "recordsetName", recordsetName);
            
            
            //rep.saveStepAttribute(id_job, getObjectId(), "name", name); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "transformName", transformName);
            rep.saveStepAttribute(id_job, getObjectId(), "transform", transform);
            rep.saveStepAttribute(id_job, getObjectId(), "transformCall", transformCall);
            rep.saveStepAttribute(id_job, getObjectId(), "recordset", recordset);
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
