/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hpccsystems.ecldirect;

/**
 *
 * @author ChalaAX
 */
public class Rollup implements EclCommand {

    private String name;
    private String recordset;
    private String condition;
    private String transform;
    private String fieldlist;
    private String group;
    private Boolean runLocal;//optional

    public String getGroup() {
        return group;
    }

    public void setGroup(String GROUP) {
        this.group = GROUP;
    }

    public Boolean getRunLocal() {
        return runLocal;
    }

    public void setRunLocal(Boolean LOCAL) {
        this.runLocal = LOCAL;
    }



    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getFieldlist() {
        return fieldlist;
    }

    public void setFieldlist(String fieldlist) {
        this.fieldlist = fieldlist;
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
        String ecl = name + ":=ROLLUP(" + recordset;
        //add local if its set if not its optional
        //ROLLUP(recordset, condition, transform [, LOCAL] )
        if(!condition.equals("")){
            ecl+= "," +condition;
            if(!transform.equals("")){
                ecl += "," + transform;
                if (runLocal) {
                        ecl += ", local";
                    }
            }
        }else{
            if (!group.equals("")) {
                //ROLLUP(recordset, GROUP, transform )
                ecl += "," + group;
                if (!transform.equals("")){
                    ecl += "," + transform;
                }
            }else{
                //ROLLUP(recordset, transform, fieldlist [, LOCAL] )
                if(!transform.equals("")){
                    ecl += "," + transform;
                    if (!fieldlist.equals("")) {
                       ecl += "," + fieldlist;

                        if (runLocal) {
                            ecl += ", local";
                        }
                    }
                }
            }
        }
        //close out the ecl call
        ecl += ")\r\n";
        
        return ecl;
    }

    @Override
    public CheckResult check() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
