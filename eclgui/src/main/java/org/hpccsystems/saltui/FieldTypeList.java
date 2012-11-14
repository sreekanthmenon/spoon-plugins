package org.hpccsystems.saltui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FieldTypeList {
	private ArrayList<FieldTypeBO> fields = new ArrayList<FieldTypeBO>();
	private ArrayList<String> displayTitles = new ArrayList<String>();
	private Set<IFieldTypeListViewer> changeListeners = new HashSet<IFieldTypeListViewer>();
	
	public FieldTypeList(){
		
	}
	public ArrayList<FieldTypeBO> getFields() {
		return fields;
	}

	public String[] getTitles(){
		return displayTitles.toArray(new String[displayTitles.size()]);
	}

	public void setFields(ArrayList<FieldTypeBO> fields) {
		this.fields = fields;
	}

	public int getIndex(String rule){
		return displayTitles.indexOf(rule);
	}
	public void add(FieldTypeBO ft){
		fields.add(ft);
		displayTitles.add(ft.getDisplayTitle());
		
		Iterator<IFieldTypeListViewer> iterator = changeListeners.iterator();
        while (iterator.hasNext()){
                iterator.next().addFieldType(ft);
        }
	}
	public void update(int index, FieldTypeBO ft){
		fields.set(index, ft);
		displayTitles.set(index, ft.getDisplayTitle());
		//Iterator<IFieldTypeListViewer> iterator = changeListeners.iterator();
		//while (iterator.hasNext())
		//	iterator.next().modifyFieldType(ft);
	}
	
	public FieldTypeBO get(int index){
		return fields.get(index);
	}
	
	public void createDefault(){
		if(this.fields.size() == 0){
			FieldTypeBO caps = new FieldTypeBO("To Uppercase");
			caps.setCaps(true);
			caps.setTypename("caps");
			this.add(caps);
			
			FieldTypeBO leftTrim = new FieldTypeBO("Left Trim");
			leftTrim.setTypename("DEFAULT");
			leftTrim.setLefttrim(true);
			this.add(leftTrim);
		}
	}
	
	
}
