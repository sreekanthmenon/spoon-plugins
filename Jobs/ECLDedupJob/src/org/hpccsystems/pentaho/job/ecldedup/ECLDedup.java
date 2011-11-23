/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecldedup;

import java.util.ArrayList;
import java.util.List;
import org.hpccsystems.ecldirect.Dedup;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getIsAll() {
        return isAll;
    }

    public void setIsAll(Boolean isAll) {
        this.isAll = isAll;
    }

    public Boolean getIsHash() {
        return isHash;
    }

    public void setIsHash(Boolean isHash) {
        this.isHash = isHash;
    }

    public String getKeep() {
        return keep;
    }

    public void setKeep(String keep) {
        this.keep = keep;
    }

    public String getKeeper() {
        return keeper;
    }

    public void setKeeper(String keeper) {
        this.keeper = keeper;
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
        if(this.runLocal != null && this.runLocal){
            return "true";
        }else{
            return "false";
        }
    }
    
    public void setIsHashString(String isHash) {
        
        if(isHash.equals("true")){
            this.isHash = true;
        }else{
            this.isHash = false;
        }
        
    }
    
    public String getIsHashString() {
        if(this.isHash != null && this.isHash){
            return "true";
        }else{
            return "false";
        }
    }
    
    public void setIsAllString(String isall) {
        
        if(isall.equals("true")){
            this.isAll = true;
        }else{
            this.isAll = false;
        }
        
    }
    
    public String getIsAllString() {
        if(this.isAll != null && this.isAll){
            return "true";
        }else{
            return "false";
        }
    }



    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
        
        Result result = prevResult;
      

        Dedup dedup = new Dedup();
        dedup.setName(this.getName());
        dedup.setRecordset(this.getRecordset());
        dedup.setRunLocal(this.getRunLocal());
        dedup.setCondition(this.getCondition());
        dedup.setIsAll(this.getIsAll());
        dedup.setIsHash(this.getIsHash());
        dedup.setKeep(this.getKeep());
        dedup.setKeeper(this.getKeeper());
        

        logBasic("{Iterate Job} Execute = " + dedup.ecl());
        
        logBasic("{Iterate Job} Previous =" + result.getLogText());
        
        result.setResult(true);
        
        RowMetaAndData data = new RowMetaAndData();
        data.addValue("ecl", Value.VALUE_TYPE_STRING, dedup.ecl());
        
        
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
            this.setIsAllString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"isAll")));
            this.setIsHashString(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"isHash")));
            this.setKeep(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"keep")));
            this.setKeeper(XMLHandler.getNodeValue(XMLHandler.getSubNode(node,"keeper")));
            
            
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
        retval += "             <runLocal>"+this.getRunLocalString()+"</runLocal>"+Const.CR;
        retval += "             <isAll>"+this.getIsAllString()+"</isAll>"+Const.CR;
        retval += "             <isHash>"+this.getIsHashString()+"</isHash>"+Const.CR;
        retval += "             <keep>"+this.getKeep()+"</keep>"+Const.CR;
        retval += "             <keeper>"+this.getKeeper()+"</keeper>"+Const.CR;
        

       
       
       
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            //jobName = rep.getStepAttributeString(id_jobentry, "jobName"); //$NON-NLS-1$

            //name = rep.getStepAttributeString(id_jobentry, "name"); //$NON-NLS-1$
            
 
            
            recordset = rep.getStepAttributeString(id_jobentry, "recordset");
            condition = rep.getStepAttributeString(id_jobentry, "condition");
            this.setRunLocalString(rep.getStepAttributeString(id_jobentry, "runLocal"));
            this.setIsAllString(rep.getStepAttributeString(id_jobentry, "isAll"));
            this.setIsHashString(rep.getStepAttributeString(id_jobentry, "isHash"));
            keep = rep.getStepAttributeString(id_jobentry, "keep");
            keeper = rep.getStepAttributeString(id_jobentry, "keeper");
            
            
            
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            //rep.saveStepAttribute(id_job, getObjectId(), "jobName", jobName); //$NON-NLS-1$

            //rep.saveStepAttribute(id_job, getObjectId(), "name", name); //$NON-NLS-1$
                
                   /*
        private String recordset;//Comma seperated list of fieldNames. a "-" prefix to the field name will indicate descending order
    private String condition;
    private Boolean isAll;
    private Boolean isHash;
    private String keep;
    private String keeper;
    private Boolean runLocal;
    */
            
            rep.saveStepAttribute(id_job, getObjectId(), "recordset", recordset);
            rep.saveStepAttribute(id_job, getObjectId(), "condition", condition);
            rep.saveStepAttribute(id_job, getObjectId(), "runLocal", this.getRunLocalString());
            rep.saveStepAttribute(id_job, getObjectId(), "isAll", this.getIsAllString());
            rep.saveStepAttribute(id_job, getObjectId(), "isHash", this.getIsHashString());
            rep.saveStepAttribute(id_job, getObjectId(), "keep", keep);
            rep.saveStepAttribute(id_job, getObjectId(), "keeper", keeper);
        
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
