package org.hpccsystems.saltui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hpccsystems.saltui.IEntryListViewer;

public class EntryList {
	private ArrayList<EntryBO> entries = new ArrayList<EntryBO>();
	private Set<IEntryListViewer> changeListeners = new HashSet<IEntryListViewer>();

	public ArrayList<EntryBO> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<EntryBO> entries) {
		this.entries = entries;
	}
	
	public void add(EntryBO eb){
		entries.add(eb);
	}
	
	public EntryBO getEntry(int index){
		return entries.get(index);
	}
	
	//Add a new Record to the existing list
	public void addEntry(int index) {
		EntryBO entry = new EntryBO();
		if(index >= 0){
			entries.add(index+1, entry);
		} else {
			entries.add(entries.size(), entry);
		}
		Iterator<IEntryListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext()){
			iterator.next().addEntry(entry);
		}
	}

	public void removeEntry(int index) {
		if(entries.size()>index){
			entries.remove(index);
			Iterator<IEntryListViewer> iterator = changeListeners.iterator();
			while (iterator.hasNext())
				iterator.next().removeEntry(index);
		}
	}

	public void modifyEntry(EntryBO record) {
		Iterator<IEntryListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			iterator.next().modifyEntry(record);
	}
	
	public void removeChangeListener(IEntryListViewer viewer) {
		changeListeners.remove(viewer);
	}

	public void addChangeListener(IEntryListViewer viewer) {
		changeListeners.add(viewer);
	}
        
     public void addEntryBO(EntryBO r){
    	 entries.add(entries.size(), r);
        Iterator<IEntryListViewer> iterator = changeListeners.iterator();
        while (iterator.hasNext()){
                iterator.next().addEntry(r);
        }
    }
}
