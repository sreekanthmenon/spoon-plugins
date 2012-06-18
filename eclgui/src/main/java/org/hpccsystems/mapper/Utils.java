package org.hpccsystems.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Utils {

	public static Map<String, List<String>> getFunctionValueMap(){
		
		Map<String, List<String>> mapFunctions = new TreeMap<String, List<String>>();
	
List<String> arlListMath = new ArrayList<String>();
		arlListMath.add("ABS");
		arlListMath.add("ACOS");
		arlListMath.add("ASIN");
		arlListMath.add("ATAN");
		arlListMath.add("ATAN2");
		arlListMath.add("COS");
		
		mapFunctions.put("Math", arlListMath);


		List<String> arlListDate = new ArrayList<String>();
		arlListDate.add("STD.Date.Date_rec");
		arlListDate.add("STD.Date.Date_t");
		arlListDate.add("STD.Date.Days_t");
		arlListDate.add("STD.Date.Year");
		arlListDate.add("STD.Date.Month");
		arlListDate.add("STD.Date.Day");
		arlListDate.add("STD.Date.DateFromParts");
		arlListDate.add("STD.Date.IsLeapYear");
		arlListDate.add("STD.Date.FromGregorianYMD");
		arlListDate.add("STD.Date.ToGregorianYMD");
		mapFunctions.put("Date", arlListDate);
		
		List<String> arlListLogical = new ArrayList<String>();
		arlListLogical.add("IF");
		arlListLogical.add("MAP");
		
		mapFunctions.put("Logical", arlListLogical);
		
		
		List<String> arlListString = new ArrayList<String>();
		

		arlListString.add("STD.Str.CleanSpaces");
		arlListString.add("STD.Str.CompareIgnoreCase");
		arlListString.add("STD.Str.Contains");
		arlListString.add("STD.Str.CountWords");
		arlListString.add("STD.Str.EditDistance");
		arlListString.add("STD.Str.EditDistanceWithinRadius");
		arlListString.add("STD.Str.EndsWith");
		arlListString.add("STD.Str.EqualIgnoreCase");
		arlListString.add("STD.Str.Extract");
		arlListString.add("STD.Str.Filter");
		arlListString.add("STD.Str.FilterOut");
		arlListString.add("STD.Str.Find");
		arlListString.add("STD.Str.FindCount");
		arlListString.add("STD.Str.FindReplace");
		arlListString.add("STD.Str.GetNthWord");
		arlListString.add("STD.Str.RemoveSuffix");
		arlListString.add("STD.Str.Reverse");
		arlListString.add("STD.Str.SplitWords");
		arlListString.add("STD.Str.SubstituteExcluded");
		arlListString.add("STD.Str.SubstituteIncluded");
		arlListString.add("STD.Str.StartsWith");
		arlListString.add("STD.Str.ToLowerCase");
		arlListString.add("STD.Str.ToTitleCase");
		arlListString.add("STD.Str.ToUpperCase");
		arlListString.add("STD.Str.WildMatch");
		arlListString.add("STD.Str.WordCount");
		
		
		mapFunctions.put("String", arlListString);
		
		List<String> arlListStringUni = new ArrayList<String>();
		
		arlListStringUni.add("STD.Uni.CleanAccents");
		arlListStringUni.add("STD.Uni.CleanSpaces");
		arlListStringUni.add("STD.Uni.CompareAtStrength");
		arlListStringUni.add("STD.Uni.LocaleCompareAtStrength");
		arlListStringUni.add("STD.Uni.CompareIgnoreCase");
		arlListStringUni.add("STD.Uni.LocaleCompareIgnoreCase");
		arlListStringUni.add("STD.Uni.Contains");
		arlListStringUni.add("STD.Uni.EditDistance");
		arlListStringUni.add("STD.Uni.EditDistanceWithinRadius");
		arlListStringUni.add("STD.Uni.Extract");
		arlListStringUni.add("STD.Uni.Filter");
		arlListStringUni.add("STD.Uni.FilterOut");
		arlListStringUni.add("STD.Uni.Find");
		arlListStringUni.add("STD.Uni.LocaleFind");
		arlListStringUni.add("STD.Uni.LocaleFindAtStrength");
		arlListStringUni.add("STD.Uni.LocaleFindAtStrengthReplace");
		arlListStringUni.add("STD.Uni.FindReplace");
		arlListStringUni.add("STD.Uni.LocaleFindReplace");
		arlListStringUni.add("STD.Uni.GetNthWord");
		arlListStringUni.add("STD.Uni.Reverse");
		arlListStringUni.add("STD.Uni.SubstituteExcluded");
		arlListStringUni.add("STD.Uni.SubstituteIncluded");
		arlListStringUni.add("STD.Uni.ToLowerCase");
		arlListStringUni.add("STD.Uni.LocaleToLowerCase");
		arlListStringUni.add("STD.Uni.ToTitleCase");
		arlListStringUni.add("STD.Uni.LocaleToTitleCase");
		arlListStringUni.add("STD.Uni.ToUpperCase");
		arlListStringUni.add("STD.Uni.LocaleToUpperCase");
		arlListStringUni.add("STD.Uni.WildMatch");
		arlListStringUni.add("STD.Uni.WordCount");
		
		
		
		mapFunctions.put("String Unicode", arlListStringUni);
		
		
		
		return mapFunctions;
	}
	
	public static void fillTreeForFunctions(Tree tree, Map<String, List<String>> mapTreeElements) {
		
		tree.setRedraw(false);
		
		for (Map.Entry<String, List<String>> entry : mapTreeElements.entrySet()) {
		    TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(entry.getKey());
			//item.setImage(new Image(tree.getDisplay(), "c:\\icons\\folder_blue.gif"));
			
			List<String> arlList = entry.getValue();
			for (String strValue : arlList) {
				TreeItem child = new TreeItem(item, SWT.NONE);
				child.setText(strValue);
				//child.setImage(new Image(tree.getDisplay(), "c:\\icons\\folder_open_blue.gif"));
			}
		}
		
		// Turn drawing back on!
		tree.setRedraw(true);
	}
	
	public static void fillTree(Tree tree, Map<String, String[]> mapDataSets) {
		
		for (Map.Entry<String, String[]> entry : mapDataSets.entrySet()) {
		    String key = entry.getKey();
		    TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(key);
		    
			String[] value = entry.getValue();
			for (int i = 0; i < value.length; i++) {
				TreeItem child = new TreeItem(item, SWT.NONE);
				child.setText(value[i]);
			}
		}
		// Turn drawing back on!
		tree.setRedraw(true);
	}
	
	public static void fillTreeForOperators(Tree tree) {
		// Turn off drawing to avoid flicker
		tree.setRedraw(false);
		
		String operatorList[] = {"+", "-", "*", "/", "%", "||", "(", ")", "=", "<>", ">", "<", "<=", ">="};
		// Create five root items
		for (int i = 0; i < operatorList.length; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(operatorList[i]);
		}
		// Turn drawing back on!
		tree.setRedraw(true);
	}
	
}
