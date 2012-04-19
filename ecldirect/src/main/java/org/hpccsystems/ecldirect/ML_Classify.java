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
public class ML_Classify implements EclCommand {

    private String name;
    
    private String independentVar; // 1
    private String model; // 2
    private String classifyType;
    private String dataType;
   
    
    private String recordsetName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRecordsetName() {
        return recordsetName;
    }

    public void setRecordsetName(String recordsetName) {
        this.recordsetName = recordsetName;
    }



    public String getIndependentVar() {
        return independentVar;
    }

    public void setIndependentVar(String independentVar) {
        this.independentVar = independentVar;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(String classifyType) {
        this.classifyType = classifyType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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


    
    @Override
    public String ecl() {
        //ITERATE(recordset, transform [, LOCAL ] )
        
         
        String ecl = "";
        
        //need to define and rework all the imports
        String type = "ClassifyD";
        if(dataType.equals("ClassifyD")){
            type = "ClassifyD";
        }else if(dataType.equals("ClassifyC")){
            type = "ClassifyC";
        }
        
        if(classifyType.equals("NaiveBayes")){
            ecl += "NaiveBayesMod := ML.Classify.NaiveBayes;\r\n";
            ecl += this.name + " := ";
            ecl += "NaiveBayesMod."+type+"("+this.independentVar+ "," + this.model + ");";

        }else if(classifyType.equals("Logistic Regression")){
            ecl += "LogisticMod := ML.Classify.Logistic(.00001,.000000001,200); \r\n";
            ecl += this.name + " := ";
            ecl += "LogisticMod."+type+"("+this.independentVar+ "," + this.model + ");";

        }else if(classifyType.equals("Perceptron")){
            ecl += "PerceptronMod := ML.Classify.Perceptron(10,0.1); \r\n";
            ecl += this.name + " := ";
            ecl += "PerceptronMod."+type+"("+this.independentVar+ "," + this.model + ");";

        }
        
        
        
      
        //Perception
       
        //close out the ecl call
        ecl += "\r\n\r\n";
        
       

        
        return ecl;
    }

    @Override
    public CheckResult check() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
