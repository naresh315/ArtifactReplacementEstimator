package org.ape.Model;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ape.properties.PropertySingleton;

public class ModelObject {
	static public PropertySingleton props = PropertySingleton.getInstance();

	public static final String SEACH_EXTENSION = ".jar" + props.getProperty("FILE_EXTENSION_TOSEARCH");
 	public String COM_FRAMEWORK = "\"" + props.getProperty("PACKAGE_IDENTIFIER");
	public SortedMap<String, Map> appAndPackageMapList = new TreeMap<>();
	public SortedMap<String, Integer> appFileChangedCount = new TreeMap<>();

	public SortedMap<String, Integer> tmpFileTracker = new TreeMap<>();

	public HashSet<String> packages = new HashSet<>();

	public HashSet<String> applicationsScanned = new HashSet<>();

	public SortedMap<String, String> pomMap = new TreeMap<>();

	public SortedMap<String, Integer> appsFileChangeCountForThatPackage = new TreeMap();

	public SortedMap<String, String> appsItsFileListMap = new TreeMap();

	public String[] getAnalysisColumnHeader() {

		return new String[] { "Count of Framework Packages used", "Rewrite efforts in Days", "Unit", "Integ",
				"Acceptance", "Production", "Management", "Total Efforts in Days", "Framework Version" };

	}

	public final String APP_NAME_PACKAGE_NAME = props.getProperty("APP_NAME_PACKAGE_NAME");

	public final String FILE_CHANGES_COUNT = props.getProperty("FILE_CHANGES_COUNT");

	public final String APP_NAME_EFFORTSTYPE_INDAY = props.getProperty("APP_NAME_EFFORTSTYPE_INDAY");
	public String getFileName() {

				;
		return EXCEL_FILENAME;

	}
	  
	public String  EXCEL_FILENAME = System.getProperty("user.home") + File.separator+((int) (Math.random() * 50 + 1)) +"-"+  props.getProperty("EXCEL_FILE_NAME");
; 
}
