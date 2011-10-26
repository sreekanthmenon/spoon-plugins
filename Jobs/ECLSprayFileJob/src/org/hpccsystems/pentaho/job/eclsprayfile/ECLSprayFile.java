/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.pentaho.job.eclsprayfile;

import java.util.ArrayList;
import java.util.List;
import org.hpccsystems.ecldirect.Spray;
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

/**
 *
 * @author ChalaAX
 */
public class ECLSprayFile extends JobEntryBase implements Cloneable, JobEntryInterface {
    
    private String outputField;
    private String ipAddress;
    private String filePath;
    private String logicalFileName;
    private String fileType;
    private String csvSeperator;
    private String csvTerminator;
    private String csvQuote;
    private String fixedRecordSize;

    
    
    public String getOutputField() {
        return outputField;
    }

    public void setOutputField(String outputField) {
        this.outputField = outputField;
    }

    public String getCsvQuote() {
        return csvQuote;
    }

    public void setCsvQuote(String csvQuote) {
        this.csvQuote = csvQuote;
    }

    public String getCsvSeperator() {
        return csvSeperator;
    }

    public void setCsvSeperator(String csvSeperator) {
        this.csvSeperator = csvSeperator;
    }

    public String getCsvTerminator() {
        return csvTerminator;
    }

    public void setCsvTerminator(String csvTerminator) {
        this.csvTerminator = csvTerminator;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLogicalFileName() {
        return logicalFileName;
    }

    public void setLogicalFileName(String logicalFileName) {
        this.logicalFileName = logicalFileName;
    }
    
    public String getFixedRecordSize() {
        return fixedRecordSize;
    }

    public void setFixedRecordSize(String fixedRecordSize) {
        this.fixedRecordSize = fixedRecordSize;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public Result execute(Result prevResult, int i) throws KettleException {
        
        Result result = prevResult;
        
        Spray spray = new Spray();
        spray.setClusterName("mythor");
        spray.setFilePath(getFilePath());
        spray.setIpAddress(getIpAddress());
        spray.setLogicalFileName(getLogicalFileName());
        spray.setFileType(getFileType());
        spray.setCsvQuote(getCsvQuote());
        spray.setCsvSeperator(getCsvSeperator());
        spray.setCsvTerminator(getCsvTerminator());
        spray.setRecordSize(getFixedRecordSize());

         
        //logBasic(spray.ecl());
        logBasic("{Spray Job} Execute = " + spray.ecl());
        
        
        result.setLogText(spray.ecl());//Will do for now. This is to pass the information forward
        result.setResult(true);

        logBasic("{Spray Job} Called IP = " + (String)spray.getIpAddress());
        
        EclDirect eclDirect = new EclDirect(((String)spray.getIpAddress()).trim(), "thor");
        ArrayList dsList = eclDirect.execute(spray.ecl());
       
        RowMetaAndData data = new RowMetaAndData();
        data.addValue("ecl", Value.VALUE_TYPE_STRING, spray.ecl());
        
        
        
        List list = result.getRows();
        if (list == null) {
            list = new ArrayList();
        }
        //list.add(data);
        result.setRows(list);
        
        return result;
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> list, List<SlaveServer> list1, Repository rpstr) throws KettleXMLException {
        try {
            super.loadXML(node, list, list1);
            
            setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
            setIpAddress(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "ip_address")));
            setFilePath(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_path")));
            setLogicalFileName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "logical_file_name")));
            setFileType(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "file_type")));
            setCsvSeperator(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_seperator")));
            setCsvTerminator(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_terminator")));
            setCsvQuote(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "csv_quote")));
            setFixedRecordSize(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "fixed_record_size")));

        } catch (Exception e) {
            throw new KettleXMLException("ECL Spray Plugin Unable to read step info from XML node", e);
        }

    }

    public String getXML() {
        String retval = "";
        
        retval += super.getXML();
        
        retval += "		<outputfield>" + outputField + "</outputfield>" + Const.CR;
        retval += "		<ip_address>" + ipAddress + "</ip_address>" + Const.CR;
        retval += "		<file_path>" + filePath + "</file_path>" + Const.CR;
        retval += "		<file_type>" + fileType + "</file_type>" + Const.CR;
        retval += "		<csv_seperator>" + csvSeperator + "</csv_seperator>" + Const.CR;
        retval += "		<csv_terminator>" + csvTerminator + "</csv_terminator>" + Const.CR;
        retval += "		<csv_quote>" + csvQuote + "</csv_quote>" + Const.CR;
        retval += "		<csv_quote>" + csvQuote + "</csv_quote>" + Const.CR;
        retval += "		<fixed_record_size>" + fixedRecordSize + "</fixed_record_size>" + Const.CR;
        retval += "		<logical_file_name>" + logicalFileName + "</logical_file_name>" + Const.CR;

        return retval;

    }

    public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers)
            throws KettleException {
        try {
            outputField = rep.getStepAttributeString(id_jobentry, "otuputField"); //$NON-NLS-1$
            ipAddress = rep.getStepAttributeString(id_jobentry, "ipAddress"); //$NON-NLS-1$
            filePath = rep.getStepAttributeString(id_jobentry, "filePath"); //$NON-NLS-1$
            fileType = rep.getStepAttributeString(id_jobentry, "fileType"); //$NON-NLS-1$
            csvSeperator = rep.getStepAttributeString(id_jobentry, "csvSeperator"); //$NON-NLS-1$
            csvTerminator = rep.getStepAttributeString(id_jobentry, "csvTerminator"); //$NON-NLS-1$
            csvQuote = rep.getStepAttributeString(id_jobentry, "csvQuote"); //$NON-NLS-1$
            fixedRecordSize = rep.getStepAttributeString(id_jobentry, "fixedRecordSize"); //$NON-NLS-1$
            logicalFileName = rep.getStepAttributeString(id_jobentry, "logicalFileName"); //$NON-NLS-1$
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }

    public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
        try {
            rep.saveStepAttribute(id_job, getObjectId(), "outputField", outputField); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "ipAddress", ipAddress); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "filePath", filePath); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "fileType", fileType); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "csvSeperator", csvSeperator); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "csvTerminator", csvTerminator); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "csvQuote", csvQuote); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "fixedRecordSize", fixedRecordSize); //$NON-NLS-1$
            rep.saveStepAttribute(id_job, getObjectId(), "logicalFileName", logicalFileName); //$NON-NLS-1$
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
