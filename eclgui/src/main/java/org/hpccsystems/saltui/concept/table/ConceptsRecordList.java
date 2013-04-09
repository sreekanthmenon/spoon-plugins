package org.hpccsystems.saltui.concept.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConceptsRecordList {
	
	List<ConceptsRecord> listConcepts = null;
	private Set<IConceptsViewer> changeListeners = new HashSet<IConceptsViewer>();
	private String delim = "|";
	
	public ConceptsRecordList() {
		listConcepts = new ArrayList<ConceptsRecord>();
		//initData();
	}
	
	public String saveListAsString(){
		String out = "";
		
		for(int i =0; i<listConcepts.size(); i++){
			
			ConceptsRecord obj = listConcepts.get(i);
			if(!out.equals("")){
				out += delim;
			}
			out += obj.saveAsString();
		}
		
		return out;
	}
	
	public void openFromString(String in){
		String[] tokens = in.split(delim);
		for(int i = 0; i<tokens.length; i++){
			ConceptsRecord obj = new ConceptsRecord();
			obj.loadFromString(tokens[i]);
			listConcepts.add(obj);
		}
	}
	
	/**
	 * Set the Table Data Here
	 */
	public void initTestData() {
		ConceptsRecord obj = new ConceptsRecord();
		obj.setChildren("ProductName");
		obj.setNonNull(true);
		obj.setCounter(1);
		obj.setSelect(true);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("ProductCode");
		obj.setNonNull(false);
		obj.setCounter(2);
		obj.setSelect(false);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("ProductType");
		obj.setNonNull(true);
		obj.setCounter(3);
		obj.setSelect(true);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("Test1");
		obj.setNonNull(true);
		obj.setCounter(4);
		obj.setSelect(true);
		listConcepts.add(obj);
		
		obj = new ConceptsRecord();
		obj.setChildren("Test2");
		obj.setNonNull(true);
		obj.setCounter(5);
		obj.setSelect(true);
		listConcepts.add(obj);
	}
	
	public List<ConceptsRecord> getConcepts() {
		return listConcepts;
	}
	
	public void conceptChanged(ConceptsRecord record) {
		Iterator<IConceptsViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			IConceptsViewer conceptViewer = (iterator.next());
			conceptViewer.conceptChanged(record);
		}
	}
	
	/**
	 * @param viewer
	 */
	public void removeChangeListener(IConceptsViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IConceptsViewer viewer) {
		changeListeners.add(viewer);
	}
	
}
