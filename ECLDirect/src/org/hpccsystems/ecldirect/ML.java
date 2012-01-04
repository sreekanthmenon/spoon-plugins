/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.ecldirect;

import java.io.*;

/**
 *
 * @author ChalaAX
 */
public class ML implements EclCommand {

    private String name;
    private String record;
    private String recordName;
   
    private String record2;
    private String record2Name;
    
    private String fieldType;
    private String fieldType2;
    private String mlFunction;
   
    
    private String recordsetName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getRecord2() {
        return record2;
    }

    public void setRecord2(String record2) {
        this.record2 = record2;
    }

    public String getRecord2Name() {
        return record2Name;
    }

    public void setRecord2Name(String record2Name) {
        this.record2Name = record2Name;
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

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldType2() {
        return fieldType2;
    }

    public void setFieldType2(String fieldType2) {
        this.fieldType2 = fieldType2;
    }

    public String getMlFunction() {
        return mlFunction;
    }

    public void setMlFunction(String mlFunction) {
        this.mlFunction = mlFunction;
    }


    public String mlLibrary(){
        String ecl = "";
        String $dir = "C:\\Program Files\\data-integration\\plugins\\jobentries\\ECLMLJob\\ecl\\";
        ecl += openFile($dir + "Associate.ecl");
        ecl += openFile($dir + "Classify.ecl");
        
        ecl += openFile($dir + "Cluster.ecl");
        ecl += openFile($dir + "Config.ecl");
        ecl += openFile($dir + "Correlate.ecl");
        ecl += openFile($dir + "Discretize.ecl");
        ecl += openFile($dir + "Distribution.ecl");
        ecl += openFile($dir + "FieldAggregates.ecl");
        ecl += openFile($dir + "FromField.ecl");
        ecl += openFile($dir + "Generate.ecl");
        ecl += openFile($dir + "Regression.ecl");
        ecl += openFile($dir + "ToField.ecl");
        ecl += openFile($dir + "Types.ecl");
        ecl += openFile($dir + "Utils.ecl");
        System.out.println("load mlLibrary");
        System.out.println(ecl);
        return ecl;
    }
    
    private static String openFile(String filePath){
        StringBuffer fileData = new StringBuffer(1000);
        System.out.println("++++++++++++++++ Open File: " + filePath);
         try{
        
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
         }catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
     }
        return fileData.toString();
    }
    
    private String openFile1(String fileName){
      String outStr = "";
      String strLine = "";
      try{
          System.out.println("++++++++++++++++ Open File: " + fileName);
          // Open the file that is the first 
          // command line parameter
          FileInputStream fstream = new FileInputStream(fileName);
          // Get the object of DataInputStream
          DataInputStream in = new DataInputStream(fstream);
          BufferedReader br = new BufferedReader(new InputStreamReader(in));
          

          boolean first = true;
          //Read File Line By Line
          
          while ((strLine += br.readLine()) != null)   {
              // we may want to filter out comments
              outStr += strLine;
              System.out.println(strLine);
          }
          
          //Close the input stream
          in.close();
          
          
          
          
    }catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
     }
  
      return outStr;
      
  }

    @Override
    public String ecl() {
        //ITERATE(recordset, transform [, LOCAL ] )
        
         
        String ecl = "";
        ecl += mlLibrary();
       // ecl += "IMPORT * FROM ML.Cluster;\r\n\r\n";
        //ecl += "IMPORT * FROM ML.Types;\r\n\r\n";

        ecl += recordName + " := DATASET([\r\n" + record + "]," + fieldType + ");\r\n\r\n";
        ecl += record2Name + " := DATASET([\r\n" + record2 + "]," + fieldType2 + ");\r\n\r\n";
        ecl += recordsetName + " := " + mlFunction + "(" + recordName + "," + record2Name + ")";
        
       
        //close out the ecl call
        ecl += "\r\n\r\n";
        
        return ecl;
    }

    @Override
    public CheckResult check() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
