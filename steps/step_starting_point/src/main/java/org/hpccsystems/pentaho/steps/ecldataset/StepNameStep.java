package org.hpccsystems.pentaho.steps.ecldataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hpccsystems.ecldirect.Dataset;
<<<<<<< HEAD
=======
import org.hpccsystems.ecldirect.Output;
import org.hpccsystems.pentaho.steps.ecloutput.ECLOutputStepData;
import org.hpccsystems.pentaho.steps.ecloutput.ECLOutputStepMeta;
>>>>>>> e3817dab9afa0cee261ee0d81604e8c6f414dd6a
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

public class ECLDatasetStep extends BaseStep implements StepInterface {

    private ECLDatasetStepData data;
    private ECLDatasetStepMeta meta;

    public ECLDatasetStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
<<<<<<< HEAD
    	meta = (ECLDatasetStepMeta) smi;
        data = (ECLDatasetStepData) sdi;
=======
    	meta = (ECLOutputStepMeta) smi;
        data = (ECLOutputStepData) sdi;
>>>>>>> e3817dab9afa0cee261ee0d81604e8c6f414dd6a
        
        
        Object[] r = getRow(); 
        String input = "";
        if (r == null) 
        {
        } else {
            logBasic("Found Row = " + r[r.length-1]);
            input = r[r.length-1].toString() + "\r\n";
        }
        
        Object[] newRow = new Object[1]; 
<<<<<<< HEAD
         
         Dataset dataset = new Dataset();
         dataset.setLogicalFileName(meta.getLogicalFileName());
         dataset.setName(meta.getDatasetName());
        // dataset.setRecordFormatString(getRecordDef());
         dataset.setRecordFormatString(meta.resultListToString());
         dataset.setRecordName(meta.getRecordName());
         dataset.setFileType(meta.getFileType());
         dataset.setRecordSet(meta.getRecordSet());
         
         newRow[0] = input + dataset.ecl();
         
         putRow(data.outputRowMeta, newRow);
         
         logBasic("{Dataset Step} Output = " + newRow[0]);
         
         return false;
=======
        
        //call the related direct library and fetch the ecl code
        
        //Create a line like htis where op is the direct library that referecnes the direct 
        //newRow[0] = input + op.ecl();
        
        putRow(data.outputRowMeta, newRow);
        
        logBasic("{Dataset Step} Output = " + newRow[0]);
        
        return false;
>>>>>>> e3817dab9afa0cee261ee0d81604e8c6f414dd6a
    }

    
    
    
    
    
    
    
    
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLDatasetStepMeta) smi;
        data = (ECLDatasetStepData) sdi;
<<<<<<< HEAD

=======
        super.setStepname(meta.getStepName());
        
>>>>>>> e3817dab9afa0cee261ee0d81604e8c6f414dd6a
        return super.init(smi, sdi);
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ECLDatasetStepMeta) smi;
        data = (ECLDatasetStepData) sdi;

        super.dispose(smi, sdi);
    }

    //
    // Run is were the action happens!
    public void run() {
        logBasic("Starting to run...");
        try {
            while (processRow(meta, data) && !isStopped())
				;
        } catch (Exception e) {
            logError("Unexpected error : " + e.toString());
            logError(Const.getStackTracker(e));
            setErrors(1);
            stopAll();
        } finally {
            dispose(meta, data);
            logBasic("Finished, processing " + getLinesRead() + " rows");
            markStop();
        }
    }
}
