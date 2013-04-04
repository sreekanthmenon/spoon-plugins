package com.hpccsystems.ui.concepts.table;

public class ConceptsRecord {
	
	private String children;
	
	private boolean nonNull;
	
	private boolean select;
	
	private int counter;

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public boolean isNonNull() {
		return nonNull;
	}

	public void setNonNull(boolean nonNull) {
		this.nonNull = nonNull;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
}
