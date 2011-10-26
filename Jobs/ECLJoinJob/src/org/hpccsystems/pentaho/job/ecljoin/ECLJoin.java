/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecljoin;

import java.util.ArrayList;
import java.util.List;
import org.hpccsystems.ecldirect.Join;
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
public class ECLJoin extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String joinName;
    private String joinCondition;
    private String joinType;
    private String leftRecordSet;
    private String rightRecordSet;
    private String joinRecordSet;

    public String getName() {
        return this.joinName;
    }

    public void setName(String joinName) {
        this.joinName = joinName;
    }

    public String getJoinCondition() {
        return this.joinCondition;
    }

    public void setJoinCondition(String joinCondition) {
        this.joinCondition = joinCondition;
    }
    
    public String getJoinType() {
        return this.joinType;
    }
    
    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getLeftRecordSet() {
        return this.leftRecordSet;
    }

    public void setLeftRecordSet(String leftRecordSet) {
        this.leftRecordSet = leftRecordSet;
    }

    public String getRightRecordSet() {
        return this.rightRecordSet;
    }

    public void setRightRecordSet(String rightRecordSet) {
        this.rightRecordSet = rightRecordSet;
    }
    
    public String getJoinRecordSet() {
        return this.joinRecordSet;
    }

    public void setJoinRecordSet(String joinRecordSet) {
        this.joinRecordSet = joinRecordSet;
    }
    
    

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
        
        Result result = prevResult;
        
        if(result.isStopped()){
            
            
        }else{
        
            Join join = new Join();
            join.setName(this.joinRecordSet);
            join.setJoinCondition(this.joinCondition);
            join.setJoinType(this.joinType);
            join.setLeftRecordSet(this.leftRecordSet);
            join.setRightRecordSet(this.rightRecordSet);

            String joinResults = join.ecl();
            logBasic("{Join Job} Execute = " + joinResults);

            //logBasic("{Join Job} Previous =" + result.getLogText());

            result.setResult(true);

            RowMetaAndData data = new RowMetaAndData();
            data.addValue("ecl", Value.VALUE_TYPE_STRING, joinResults);


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
                logBasic("{Join Job} ECL Code =" + eclCode);
            }

            result.setRows(list);
        }
        
        return result;
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            setName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_name")));
            setJoinCondition(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_condition")));
            setJoinType(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_type")));
            setLeftRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "left_recordset")));
            setRightRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "right_recordset")));
            setJoinRecordSet(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "join_recordset")));

        } catch (Exception e) {
            throw new KettleXMLException("ECL Join Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        
        retval += "		<join_name>" + this.joinName + "</join_name>" + Const.CR;
        retval += "		<join_condition>" + this.joinCondition + "</join_condition>" + Const.CR;
        retval += "		<join_type>" + this.joinType + "</join_type>" + Const.CR;
        retval += "		<left_recordset>" + this.leftRecordSet + "</left_recordset>" + Const.CR;
        retval += "		<right_recordset>" + this.rightRecordSet + "</right_recordset>" + Const.CR;
        retval += "		<join_recordset>" + this.joinRecordSet + "</join_recordset>" + Const.CR;
  
        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            this.joinName = rep.getStepAttributeString(id_jobentry, "joinName"); //$NON-NLS-1$
            this.joinCondition = rep.getStepAttributeString(id_jobentry, "joinCondition"); //$NON-NLS-1$
            this.joinType = rep.getStepAttributeString(id_jobentry, "joinType"); //$NON-NLS-1$
            this.leftRecordSet = rep.getStepAttributeString(id_jobentry, "leftRecordSet"); //$NON-NLS-1$
            this.rightRecordSet = rep.getStepAttributeString(id_jobentry, "rightRecordSet"); //$NON-NLS-1$
            this.joinRecordSet = rep.getStepAttributeString(id_jobentry, "joinRecordSet"); //$NON-NLS-1$
                
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "joinName", joinName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "joinCondition", joinCondition); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "joinType", joinType); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "leftRecordSet", leftRecordSet); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "rightRecordSet", rightRecordSet); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "joinRecordSet", joinRecordSet); //$NON-NLS-1$
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
