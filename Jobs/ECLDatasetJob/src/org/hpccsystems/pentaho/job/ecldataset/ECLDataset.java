/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.ecldataset;

import java.util.ArrayList;
import java.util.List;
import org.hpccsystems.ecldirect.Dataset;
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
public class ECLDataset extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String logicalFileName;
    private String datasetName;
    private String recordName;
    private String recordDef;

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getLogicalFileName() {
        return logicalFileName;
    }

    public void setLogicalFileName(String fileName) {
        this.logicalFileName = fileName;
    }

    public String getRecordDef() {
        return recordDef;
    }

    public void setRecordDef(String recordDef) {
        this.recordDef = recordDef;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
    
    

    @Override
    public Result execute(Result prevResult, int k) throws KettleException {
        
        Result result = prevResult;
        
        Dataset dataset = new Dataset();
        dataset.setLogicalFileName(getLogicalFileName());
        dataset.setName(getDatasetName());
        dataset.setRecordFormatString(getRecordDef());
        dataset.setRecordName(getRecordName());
        dataset.setFileType("CSV");

        logBasic("{Dataset Job} Execute = " + dataset.ecl());
        
        logBasic("{Dataset Job} Previous =" + result.getLogText());
        
        result.setResult(true);
        
        RowMetaAndData data = new RowMetaAndData();
        data.addValue("ecl", Value.VALUE_TYPE_STRING, dataset.ecl());
        
        
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
            logBasic("{Dataset Job} ECL Code =" + eclCode);
        }
        
        result.setRows(list);
        
        
        return result;
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            setLogicalFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")));
            setRecordName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_name")));
            setDatasetName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "dataset_name")));
            setRecordDef(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "record_def")));

        } catch (Exception e) {
            throw new KettleXMLException("ECL Dataset Job Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        
        retval += "		<logical_file_name>" + logicalFileName + "</logical_file_name>" + Const.CR;
        retval += "		<record_name>" + recordName + "</record_name>" + Const.CR;
        retval += "		<dataset_name>" + datasetName + "</dataset_name>" + Const.CR;
        retval += "		<record_def>" + recordDef + "</record_def>" + Const.CR;

        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            logicalFileName = rep.getStepAttributeString(id_jobentry, "logicalFileName"); //$NON-NLS-1$
            datasetName = rep.getStepAttributeString(id_jobentry, "datasetName"); //$NON-NLS-1$
            recordName = rep.getStepAttributeString(id_jobentry, "recordName"); //$NON-NLS-1$
            recordDef = rep.getStepAttributeString(id_jobentry, "recordDef"); //$NON-NLS-1$
        
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "logicalFileName", logicalFileName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "datasetName", datasetName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "recordName", recordName); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "recordDef", recordDef); //$NON-NLS-1$
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
