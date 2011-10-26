/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.ecldirect;

import java.util.ArrayList;

/**
 *
 * @author ChalaAX
 */
public class Dataset implements EclCommand {
    private String name;
    private String logicalFileName;
    private String recordName;
    private String fileType;
    private ArrayList recordFormatList;
    private String recordFormatString;

    public String getLogicalFileName() {
        return logicalFileName;
    }

    public void setLogicalFileName(String logicalFileName) {
        this.logicalFileName = logicalFileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getRecordFormatList() {
        return recordFormatList;
    }

    public void setRecordFormatList(ArrayList recordDefinition) {
        this.recordFormatList = recordDefinition;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRecordFormatString() {
        return recordFormatString;
    }

    public void setRecordFormatString(String recordFormatString) {
        this.recordFormatString = recordFormatString;
    }
    
    
    
    
    @Override
    public String ecl() {
        String recordFmt;
        if (recordFormatString != null && recordFormatString.length() > 0)  {
            recordFmt = recordFormatString;
        } else {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recordFormatList.size(); i++) {
            sb.append(recordFormatList.get(i)).append(";");
        }
        recordFmt =  sb.toString();             
        }
        
        String recordDef = recordName + ":= record \r\n" + recordFmt + "\r\nend; \r\n";
        String datasetDef = name + ":= dataset('" + logicalFileName + "'," + recordName + "," +  fileType + "); \r\n";
        return recordDef + datasetDef;
    }

    @Override
    public CheckResult check() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
}
