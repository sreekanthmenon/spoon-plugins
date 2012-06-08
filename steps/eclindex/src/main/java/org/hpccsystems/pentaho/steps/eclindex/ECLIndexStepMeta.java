package org.hpccsystems.pentaho.steps.eclindex;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hpccsystems.eclguifeatures.CreateTable;
import org.hpccsystems.eclguifeatures.RecordBO;
import org.hpccsystems.eclguifeatures.RecordList;

public class ECLIndexStepMeta extends BaseStepMeta implements StepMetaInterface {
	private String stepName;
	private String outputField;

	private RecordList keys = new RecordList();
    private RecordList payload = new RecordList();
    
    private String baserecset;
    private String indexfile;
    private String sorted;
    private String preload;
    private String opt;
    private String compressed;
    private String distributed;
    private String index;
    private String newindexfile;
    private String isDuplicate;
    private String overwrite;
  
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
    public String getOutputField() {
        return outputField;
    }

    public void setOutputField(String outputField) {
        this.outputField = outputField;
    }
    
    public String getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(String overwrite) {
        this.overwrite = overwrite;
    }

    public String getIsDuplicate() {
        return isDuplicate;
    }

    public void setIsDuplicate(String isDuplicate) {
        this.isDuplicate = isDuplicate;
    }
    
    public RecordList getKeys() {
        return keys;
    }

    public void setKeys(RecordList keys) {
        this.keys = keys;
    }

    public RecordList getPayload() {
        return payload;
    }

    public void setPayload(RecordList payload) {
        this.payload = payload;
    }

    public String getBaserecset() {
        return baserecset;
    }

    public void setBaserecset(String baserecset) {
        this.baserecset = baserecset;
    }

    public String getCompressed() {
        return compressed;
    }

    public void setCompressed(String compressed) {
        this.compressed = compressed;
    }

    public String getDistributed() {
        return distributed;
    }

    public void setDistributed(String distributed) {
        this.distributed = distributed;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndexfile() {
        return indexfile;
    }

    public void setIndexfile(String indexfile) {
        this.indexfile = indexfile;
    }


    public String getNewindexfile() {
        return newindexfile;
    }

    public void setNewindexfile(String newindexfile) {
        this.newindexfile = newindexfile;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }


    public String getPreload() {
        return preload;
    }

    public void setPreload(String preload) {
        this.preload = preload;
    }

    public String getSorted() {
        return sorted;
    }

    public void setSorted(String sorted) {
        this.sorted = sorted;
    }
    
    public String saveKeys(){
        String out = "";
        ArrayList list = keys.getRecords();
        Iterator<RecordBO> itr = list.iterator();
        boolean isFirst = true;
        while(itr.hasNext()){
            if(!isFirst){out+="|";}
            
            out += itr.next().toCSV();
            isFirst = false;
        }
        return out;
    }
    
    public void openKeys(String in){
        String[] strLine = in.split("[|]");
        
        int len = strLine.length;
        if(len>0){
            keys = new RecordList();
            System.out.println("Open Record List");
            for(int i =0; i<len; i++){
                System.out.println("++++++++++++" + strLine[i]);
                //this.recordDef.addRecord(new RecordBO(strLine[i]));
                RecordBO rb = new RecordBO(strLine[i]);
                System.out.println(rb.getColumnName());
                keys.addRecordBO(rb);
            }
        }
    }
    
    public String savePayload(){
        String out = "";
        ArrayList list = payload.getRecords();
        Iterator<RecordBO> itr = list.iterator();
        boolean isFirst = true;
        while(itr.hasNext()){
            if(!isFirst){out+="|";}
            
            out += itr.next().toCSV();
            isFirst = false;
        }
        return out;
    }
    
    public void openPayload(String in){
        String[] strLine = in.split("[|]");
        
        int len = strLine.length;
        if(len>0){
            payload = new RecordList();
            System.out.println("Open Record List");
            for(int i =0; i<len; i++){
                System.out.println("++++++++++++" + strLine[i]);
                //this.recordDef.addRecord(new RecordBO(strLine[i]));
                RecordBO rb = new RecordBO(strLine[i]);
                System.out.println(rb.getColumnName());
                payload.addRecordBO(rb);
            }
        }
    }
    
    public String resultListToString(RecordList recordList){
        String out = "";
        
        if(recordList != null){
            if(recordList.getRecords() != null && recordList.getRecords().size() > 0) {
                    System.out.println("Size: "+recordList.getRecords().size());
                    for (Iterator<RecordBO> iterator = recordList.getRecords().iterator(); iterator.hasNext();) {
                            RecordBO record = (RecordBO) iterator.next();
                            if(!out.equals("")){
                                out += ",";
                            }
                            out += record.getColumnName();
                    }
            }
        }
        
        return out;
    }
    
    public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {

        // append the outputField to the output
        ValueMetaInterface v = new ValueMeta();
        v.setName("outputfield");
        v.setType(ValueMeta.TYPE_STRING);
        v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
        v.setOrigin(origin);

        r.addValueMeta(v);
        
    }

    public String getXML() throws KettleValueException {
    	String retval = "";
        
    	retval += "		<stepName>" + stepName + "</stepName>" + Const.CR;
    	retval += "		<outputfield>" + outputField + "</outputfield>" + Const.CR;
    	retval += getXMLElement("baserecset", this.getBaserecset());
        retval += "		<keys>" + this.saveKeys() + "</keys>" + Const.CR;
        retval += "		<payload>" + this.savePayload() + "</payload>" + Const.CR;
        retval += getXMLElement("indexfile", this.getIndexfile());
        retval += getXMLElement("sorted", this.getSorted());
        retval += getXMLElement("preload", this.getPreload());
        retval += getXMLElement("opt", this.getOpt());
        retval += getXMLElement("compressed", this.getCompressed());
        retval += getXMLElement("distributed", this.getDistributed());
        retval += getXMLElement("index", this.getIndex());
        retval += getXMLElement("newindexfile", this.getNewindexfile());
        retval += getXMLElement("isDuplicate", this.getIsDuplicate());
        retval += getXMLElement("overwrite", this.getOverwrite());
        
        return retval;
    }
    
    public String loadXMLElement(Node node,String key){
        String rStr = "";
        if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, key)) != null){
                rStr = XMLHandler.getNodeValue(XMLHandler.getSubNode(node, key));
                //System.out.println("load XML Element " + key + " = " + XMLHandler.getNodeValue(XMLHandler.getSubNode(node, key)) + "|");
        }
        return rStr;
    }
    
    public void loadXML(Node node, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
    	 try {
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")) != null)
    			setStepName(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "stepName")));
    		 if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")) != null)
   		  		setOutputField(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "outputfield")));
    		 this.setBaserecset(loadXMLElement(node,"baserecset"));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "keys")) != null)
            	 openKeys(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "keys")));
             if(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "payload")) != null)
            	 openPayload(XMLHandler.getNodeValue(XMLHandler.getSubNode(node, "payload")));
             this.setIndexfile(loadXMLElement(node,"indexfile"));
             this.setSorted(loadXMLElement(node,"sorted"));
             this.setPreload(loadXMLElement(node,"preload"));
             this.setOpt(loadXMLElement(node,"opt"));
             this.setCompressed(loadXMLElement(node,"compressed"));
             this.setDistributed(loadXMLElement(node,"distributed"));
             this.setIndex(loadXMLElement(node,"index"));
             this.setNewindexfile(loadXMLElement(node,"newindexfile"));
             this.setIsDuplicate(loadXMLElement(node,"isDuplicate"));
             this.setOverwrite(loadXMLElement(node,"overwrite"));
    		 
         } catch (Exception e) {
             throw new KettleXMLException("ECL Index Job Plugin Unable to read step info from XML node", e);
         }

    }

    
    
    public Object clone() {
        Object retval = super.clone();
        return retval;
    }

    
    public void setDefault() {
        outputField = "template_outfield";
    }

    public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) {
        CheckResult cr;

        // See if we have input streams leading to this step!
        if (input.length > 0) {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
            remarks.add(cr);
        } else {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
            remarks.add(cr);
        }

    }

    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
        return new ECLIndexStepDialog(shell, meta, transMeta, name);
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ECLIndexStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ECLIndexStepData(outputField);
    	
    }

    public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
    	try {
    		if(rep.getStepAttributeString(id_step, "stepName") != null)
    			stepName = rep.getStepAttributeString(id_step, "stepName"); //$NON-NLS-1$ 
    		if(rep.getStepAttributeString(id_step, "outputField") != null)
        		outputField = rep.getStepAttributeString(id_step, "outputField"); //$NON-NLS-1$
    		this.setBaserecset(getRepElement(rep, id_step, "baserecset"));
            if(rep.getStepAttributeString(id_step, "keys") != null)
            	this.openKeys(rep.getStepAttributeString(id_step, "keys")); //$NON-NLS-1$
            if(rep.getStepAttributeString(id_step, "payload") != null)
                this.openPayload(rep.getStepAttributeString(id_step, "payload")); //$NON-NLS-1$
            this.setIndexfile(getRepElement(rep, id_step, "indexfile"));
            this.setSorted(getRepElement(rep, id_step, "sorted"));
            this.setPreload(getRepElement(rep, id_step, "preload"));
            this.setOpt(getRepElement(rep, id_step, "opt"));
            this.setCompressed(getRepElement(rep, id_step, "compressed"));
            this.setDistributed(getRepElement(rep, id_step, "distributed"));
            this.setIndex(getRepElement(rep, id_step, "index"));
            this.setNewindexfile(getRepElement(rep, id_step, "newindexfile"));
            this.setIsDuplicate(getRepElement(rep, id_step, "isDuplicate"));
            this.setOverwrite(getRepElement(rep, id_step, "overwrite"));
    		
        } catch (Exception e) {
            throw new KettleException("Unexpected Exception", e);
        }
    }
    
    public void saveRepElement(Repository rep, ObjectId id_step,String key,String value) throws KettleException{
        try{
            rep.saveStepAttribute(id_step, getObjectId(), key, value); //$NON-NLS-1$
        }catch (Exception e){
             e.printStackTrace();
             throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }
    
    public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
    	try {
    		rep.saveStepAttribute(id_transformation, id_step, "stepName", stepName); //$NON-NLS-1$
    		rep.saveStepAttribute(id_transformation, id_step, "outputField", outputField); //$NON-NLS-1$
    		
            saveRepElement(rep,id_step,"baserecset",this.getBaserecset());
            rep.saveStepAttribute(id_transformation, id_step, "keys", this.saveKeys()); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "payload", this.savePayload()); //$NON-NLS-1$
            saveRepElement(rep,id_step,"indexfile",this.getIndexfile());
            saveRepElement(rep,id_step,"sorted",this.getSorted());
            saveRepElement(rep,id_step,"preload",this.getPreload());
            saveRepElement(rep,id_step,"opt",this.getOpt());
            saveRepElement(rep,id_step,"compressed",this.getCompressed());
            saveRepElement(rep,id_step,"distributed",this.getDistributed());
            saveRepElement(rep,id_step,"index",this.getIndex());
            saveRepElement(rep,id_step,"newindexfile",this.getNewindexfile());
            saveRepElement(rep,id_step,"isDuplicate",this.getIsDuplicate());
            saveRepElement(rep,id_step,"overwrite",this.getOverwrite());
    		
        } catch (Exception e) {
            throw new KettleException("Unable to save info into repository" + id_step, e);
        }
    }


}
