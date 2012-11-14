package org.hpccsystems.saltui;

public interface IFieldTypeListViewer {
	
	//Update the view after the record has been added to record list
	public void addFieldType(FieldTypeBO fieldType);
	
	//Update the view after the record has been removed from record list
	public void removeFieldType(int index);
	
	//Update the view after the record has been modified in the record list
	public void modifyFieldType(FieldTypeBO fieldType);
}
