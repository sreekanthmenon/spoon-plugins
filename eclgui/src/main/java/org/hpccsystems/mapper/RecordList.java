package org.hpccsystems.mapper;

import java.util.ArrayList;
import java.util.Iterator;

public class RecordList {
	
	private ArrayList<MapperBO> arlRecords = new ArrayList<MapperBO>();
	
	//Returns the Record List
	public ArrayList<MapperBO> getRecords() {
		return arlRecords;
	}
	
	public void addRecord(MapperBO record) {
		arlRecords.add(arlRecords.size(), record);
	}
	
	public void removeRecord(int index) {
		arlRecords.remove(index);
	}
	
	public MapperBO getRecord(int index) {
		return (MapperBO)arlRecords.get(index);
	}
}
