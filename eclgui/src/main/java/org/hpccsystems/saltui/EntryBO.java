package org.hpccsystems.saltui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class EntryBO {
	private String field = "";
	private String ruleName = "";
	private FieldTypeList fieldTypeList = new FieldTypeList();
	private int	fieldTypeListIndex = -1;
	
	public EntryBO(){}
	
	public EntryBO(String in){
        super();
        fromCSV(in);
    }
	
	
	

	public int getFieldTypeListIndex() {
		return fieldTypeListIndex;
	}

	public void setFieldTypeListIndex(int fieldTypeListIndex) {
		this.fieldTypeListIndex = fieldTypeListIndex;
	}

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public FieldTypeList getFieldTypeList() {
		return fieldTypeList;
	}

	public void setFieldTypeList(FieldTypeList fieldTypeList) {
		this.fieldTypeList = fieldTypeList;
	}

	
		public String toCSV(){
			String csv = new String();
			
			csv += field;
			csv += ","+ruleName;
			csv += ","+fieldTypeListIndex;
			//csv += ","+fieldTypeList.toPipeDelm();
			
            return csv;
        }
        
        public void fromCSV(String in){
            String[] strArr = in.split("[,]");//"\\,"
            //System.out.println("in ---- " + in);
            if(strArr.length == 3){
                field = strArr[0];
                ruleName = strArr[1];
                fieldTypeListIndex = Integer.parseInt(strArr[2]);
                //fieldTypeList.fromPipeDelm(strArr[2]);
            } else{
            	field = "";
                ruleName = "";
                fieldTypeListIndex = -1;
                //fieldTypeList.fromPipeDelm("");
            }
        }
	

}
