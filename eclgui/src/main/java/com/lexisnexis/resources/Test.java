package com.lexisnexis.resources;

import java.io.IOException;

public class Test {
	
	public static void main(String[] args) {

		
	        
		//System.out.println("ORIGINAL VALUE: "+PropertiesReader.getProperty("path.salt.library"));
		PropertiesReader.setProperty("path.ml.librarys", "C:/ml/Dinesh");
		System.out.println("MODIFIED VALUE: "+PropertiesReader.getProperty("path.ml.library"));
		
		//PropertiesReader.getAllKeys();
		//PropertiesReader.getAllValues();
	}
	
}
