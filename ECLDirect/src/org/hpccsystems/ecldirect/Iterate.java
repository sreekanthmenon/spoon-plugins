/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.ecldirect;

/**
 *
 * @author ChalaAX
 */
public class Iterate implements EclCommand {

    private String name;
    private String transform;
    private String transformName;
    private String recordset;//Comma seperated list of fieldNames. a "-" prefix to the field name will indicate descending order
    private Boolean runLocal;

    public Boolean getRunLocal() {
        return runLocal;
    }

    public void setRunLocal(Boolean runLocal) {
        this.runLocal = runLocal;
    }

    public String getTransformName() {
        return transformName;
    }

    public void setTransformName(String transformName) {
        this.transformName = transformName;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecordset() {
        return recordset;
    }

    public void setRecordset(String recordset) {
        this.recordset = recordset;
    }

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    

    @Override
    public String ecl() {
        //ITERATE(recordset, transform [, LOCAL ] )
        
         
        String ecl = "";
        ecl += transformName + ":= TRANSFORM \r\n" + transform + "\r\nEND;\r\n";
     
        ecl += name + ":=ITERATE(" + recordset + "," + transformName;
        //add local if its set if not its optional
        if (runLocal != null && runLocal) {
           ecl += ", local";
        }
        //close out the ecl call
        ecl += ");\r\n";
        
        return ecl;
    }

    @Override
    public CheckResult check() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
