package org.hpccsystems.saltui.concept;

import org.hpccsystems.saltui.concept.table.ConceptsRecordList;

public class ConceptEntryBO {
	
	private String conceptName = "";
	private String effectOnSpecificity = "";
	private String threshold = "";
	private boolean useBagOfWords = false;
	private String reOrderType = "";
	private String segmentType = "";
	private String scale = "";
	private String specificity = "";
	private String switchValue = "";
	private ConceptsRecordList recordList;
	
	private int entryIndex = 0;
	
	public ConceptEntryBO(){}
	public ConceptEntryBO(String conceptName){
		this.conceptName = conceptName;
	}

	
	
	
	
	
	public int getEntryIndex() {
		return entryIndex;
	}
	public void setEntryIndex(int entryIndex) {
		this.entryIndex = entryIndex;
	}
	public String getConceptName() {
		return conceptName;
	}
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	public String getEffectOnSpecificity() {
		return effectOnSpecificity;
	}
	public void setEffectOnSpecificity(String effectOnSpecificity) {
		this.effectOnSpecificity = effectOnSpecificity;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public boolean isUseBagOfWords() {
		return useBagOfWords;
	}
	public void setUseBagOfWords(boolean useBagOfWords) {
		this.useBagOfWords = useBagOfWords;
	}
	public String getReOrderType() {
		return reOrderType;
	}
	public void setReOrderType(String reOrderType) {
		this.reOrderType = reOrderType;
	}
	public String getSegmentType() {
		return segmentType;
	}
	public void setSegmentType(String segmentType) {
		this.segmentType = segmentType;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getSpecificity() {
		return specificity;
	}
	public void setSpecificity(String specificity) {
		this.specificity = specificity;
	}
	public String getSwitchValue() {
		return switchValue;
	}
	public void setSwitchValue(String switchValue) {
		this.switchValue = switchValue;
	}
	public ConceptsRecordList getRecordList() {
		return recordList;
	}
	public void setRecordList(ConceptsRecordList recordList) {
		this.recordList = recordList;
	}
	public String toCSV(){
		String csv = new String();
		String delm = ",";
		csv += conceptName + delm;//0
		csv += effectOnSpecificity + delm;//1
		csv += threshold + delm;//2
		if(useBagOfWords){
			csv += true + delm;//3
		}else{
			csv += "false"+delm;//5
		}
		csv += reOrderType + delm;//4
		
		csv += segmentType + delm;//5
		csv += scale + delm;//6
		csv += specificity + delm;//7
		csv += switchValue + delm;//8
		csv += recordList.saveListAsString() + delm;//9
		
		
        return csv;
    }
    
    public void fromCSV(String in){
        String[] strArr = in.split(",");
        try{
        	conceptName = strArr[0];
        	effectOnSpecificity = strArr[1];
        	threshold = strArr[2];
        	if(strArr[3].equalsIgnoreCase("true")){
        		useBagOfWords = true;
            }else{
            	useBagOfWords = false;
            }
        	reOrderType = strArr[4];
        	segmentType = strArr[5];
        	scale = strArr[6];
        	specificity = strArr[7];
        	switchValue = strArr[8];
        	recordList.openFromString(strArr[9]);
        
        
        
        }catch (Exception e){
        	System.out.println("Failed to open fromCSV");
        	System.out.println(e.toString());
        }
        
    }
	
}
