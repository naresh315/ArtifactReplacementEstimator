package org.ape.excel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.ape.Model.ModelObject;
public class ProcessAndWriteToExcel {

	static HSSFWorkbook workbook1 = new HSSFWorkbook();
	ModelObject modelObjectInst;
	

	public ProcessAndWriteToExcel(ModelObject modelObjectInst) {
		this.modelObjectInst = modelObjectInst;
	}

	

	public void processData() throws FileNotFoundException, IOException {

		System.out.println("Analyzing and Writing to Excel Sheet");

		// Create a blank sheet

		HSSFSheet spreadsheet1 = workbook1.createSheet(modelObjectInst.props.getProperty("SHEET_NAME_1"));

		HSSFSheet spreadsheet2 = workbook1.createSheet(modelObjectInst.props.getProperty("SHEET_NAME_2"));

		HSSFSheet spreadsheet3 = workbook1.createSheet(modelObjectInst.props.getProperty("SHEET_NAME_3"));

		HSSFSheet spreadsheet4 = workbook1.createSheet(modelObjectInst.props.getProperty("SHEET_NAME_4"));

		writeDataAboutPackageCounts(spreadsheet1);

		analysisAsPerFileChangeCount(spreadsheet2);

		consolidatedEffortsExcel(spreadsheet3);

		writesFilesChangesRequiredToExcel(spreadsheet4);

		// Write the workbook in file system

		FileOutputStream out = new FileOutputStream(

				new File(modelObjectInst.getFileName()));

		workbook1.write(out);

		out.close();

		System.out.println("Auto Generated File Name =" + modelObjectInst.EXCEL_FILENAME);

	}

	

	public void writeDataAboutPackageCounts(HSSFSheet spreadsheet) throws FileNotFoundException, IOException {

		System.out.println("writeDataAboutPackageCounts: Started");

		// Create row object

		HSSFRow row;

		// Iterate over data and write to sheet

		Set<String> appNameList = modelObjectInst.appAndPackageMapList.keySet();

		int rowid = 0;

		row = spreadsheet.createRow(rowid++);

		Object[] objectArr = modelObjectInst.packages.toArray();

		int cellid = 0;

		HSSFCell cell = row.createCell(cellid++);

		cell.setCellValue(modelObjectInst.APP_NAME_PACKAGE_NAME);

		/*cell = row.createCell(cellid++);

		cell.setCellValue(modelObjectInst.FILE_CHANGES_COUNT);*/

		for (Object obj : objectArr) {

			cell = row.createCell(cellid++);

			// String string = obj.toString();

			// String value = string.contains(COM_FRAMEWORK)? string.substring(14):string;

			cell.setCellValue(obj.toString());

		}

		for (String appNameKeys : appNameList) {

			Map packageCountMap = modelObjectInst.appAndPackageMapList.get(appNameKeys);

			row = spreadsheet.createRow(rowid++);

			cellid = 0;

			cell = row.createCell(cellid++);

			cell.setCellValue("" + appNameKeys);

			cell = row.createCell(cellid++);

			cell.setCellValue(Double.valueOf("" + modelObjectInst.appFileChangedCount.get(appNameKeys)));

			objectArr = packageCountMap.values().toArray();

			for (Object obj : objectArr) {

				cell = row.createCell(cellid++);

				cell.setCellValue(Double.valueOf("" + obj));

			}

		}

		System.out.println("writeDataAboutPackageCounts: :Completed");

	}

	public void analysisAsPerFileChangeCount(HSSFSheet spreadsheet) throws FileNotFoundException, IOException {

		System.out.println("analysisAsPerFileChangeCount: Started");

		// Create row object

		HSSFRow row;

		// Iterate over data and write to sheet

		Set<String> appNameList = modelObjectInst.appAndPackageMapList.keySet();

		HashMap<String, Map> fileChangeCountappAndPackageMapList = getPreFilledMap(appNameList);

		int rowid = 0;
		row = spreadsheet.createRow(rowid++);

		Object[] objectArr = modelObjectInst.packages.toArray();

		int cellid = 0;

		HSSFCell cell = row.createCell(cellid++);

		cell.setCellValue(modelObjectInst.APP_NAME_PACKAGE_NAME);

	/*	cell = row.createCell(cellid++);

		cell.setCellValue(modelObjectInst.FILE_CHANGES_COUNT);*/

		for (Object obj : objectArr) {

			cell = row.createCell(cellid++);

			String string = obj.toString();

			String value = string.contains(modelObjectInst.COM_FRAMEWORK) ? string.substring(14) : string;

			cell.setCellValue(value);

		}

		for (String appNameKeys : appNameList) {

			Map packageCountMap = fileChangeCountappAndPackageMapList.get(appNameKeys);

			row = spreadsheet.createRow(rowid++);

			cellid = 0;

			cell = row.createCell(cellid++);

			cell.setCellValue("" + appNameKeys);

			cell = row.createCell(cellid++);

			cell.setCellValue(Double.valueOf("" + modelObjectInst.appFileChangedCount.get(appNameKeys)));

			if (packageCountMap == null || packageCountMap.values() == null) {

				continue;

			}

			objectArr = packageCountMap.values().toArray();

			for (Object obj : objectArr) {

				cell = row.createCell(cellid++);

				cell.setCellValue(Double.valueOf("" + obj));

			}

		}

		System.out.println("analysisAsPerFileChangeCount: :Completed");

	}

	public void writesFilesChangesRequiredToExcel(HSSFSheet spreadsheet) throws FileNotFoundException, IOException {

		System.out.println("analysisAsPerFileChangeCount: Started");

		// Create row object

		HSSFRow row;

		// Iterate over data and write to sheet

		int rowid = 0;

		row = spreadsheet.createRow(rowid++);

		int cellid = 0;

		HSSFCell cell = row.createCell(cellid++);

		cell.setCellValue("App Names");

		cell = row.createCell(cellid++);

		cell.setCellValue("File Names");

		System.out.println("analysisAsPerFileChangeCount: :Completed");

		Set<Entry<String, String>> entrySet = modelObjectInst.appsItsFileListMap.entrySet();

		for (Entry<String, String> entry : entrySet) {

			row = spreadsheet.createRow(rowid++);

			cellid = 0;

			cell = row.createCell(cellid++);

			cell.setCellValue("" + entry.getKey());

			cell = row.createCell(cellid++);

			String string = "" + entry.getValue();

			cell.setCellValue(string.replaceAll(",", "\n"));

		}

	}

	public void consolidatedEffortsExcel(HSSFSheet spreadsheet) throws FileNotFoundException, IOException {

		System.out.println("Doing Final analysis: finalAnalysis()");

		// Create row object

		HSSFRow row;

		// Iterate over data and write to sheet

		Set<String> appNameList = modelObjectInst.appAndPackageMapList.keySet();

		HashMap<String, Map> fileChangeCountappAndPackageMapList = getPreFilledMap(appNameList);

		int rowid = 0;

		row = spreadsheet.createRow(rowid++);

		int cellid = 0;

		HSSFCell cell = row.createCell(cellid++);

		cell.setCellValue(modelObjectInst.APP_NAME_EFFORTSTYPE_INDAY);

		cell = row.createCell(cellid++);

		cell.setCellValue(modelObjectInst.FILE_CHANGES_COUNT);

		for (Object obj : modelObjectInst.getAnalysisColumnHeader()) {

			cell = row.createCell(cellid++);
			String string = obj.toString();

			// String value = string.contains(COM_FRAMEWORK)? string.substring(14):string;

			cell.setCellValue(string);

		}

		HashMap<String, Double[]> finalEfforts2 = ExcelCalculationUtility
				.getFinalEfforts(modelObjectInst.appFileChangedCount, fileChangeCountappAndPackageMapList);

		double countOfExpectedFileChanges = 0;

		double totalRewriteCutOfEfforts = 0;

		double totalEffortsInDay = 0;

		for (String appNameKeys : appNameList) {

			Double[] finalEfforts = finalEfforts2.get(appNameKeys);

			row = spreadsheet.createRow(rowid++);

			cellid = 0;

			cell = row.createCell(cellid++);

			cell.setCellValue("" + appNameKeys);

			Double valueOf = Double.valueOf("" + modelObjectInst.appFileChangedCount.get(appNameKeys));

			countOfExpectedFileChanges += valueOf;

			totalRewriteCutOfEfforts += finalEfforts[0];

			totalEffortsInDay += finalEfforts[6];

			cellid = addCell(row, cellid, valueOf);

			cellid = addCell(row, cellid, finalEfforts[7]);

			cellid = addCell(row, cellid, finalEfforts[0]);

			cellid = addCell(row, cellid, finalEfforts[1]);

			cellid = addCell(row, cellid, finalEfforts[2]);

			cellid = addCell(row, cellid, finalEfforts[3]);

			cellid = addCell(row, cellid, finalEfforts[4]);

			cellid = addCell(row, cellid, finalEfforts[5]);

			cellid = addCell(row, cellid, finalEfforts[6]);

			cell = row.createCell(cellid++);

			cell.setCellValue(getValue(appNameKeys));

		}

		row = spreadsheet.createRow(rowid++);

		cellid = 0;

		cell = row.createCell(cellid++);

		cell.setCellValue("Total Files expected to be changed");

		cellid = addCell(row, cellid, countOfExpectedFileChanges);

		cell = row.createCell(cellid++);

		cell.setCellValue("   ");

		cell = row.createCell(cellid++);

		cell.setCellValue("Total Rewrite Efforts in Days");

		cellid = addCell(row, cellid, totalRewriteCutOfEfforts);

		cell = row.createCell(cellid++);

		cell.setCellValue("Per Hour Billing : " + modelObjectInst.props.getProperty("perHourBillingRate"));

		cell = row.createCell(cellid++);

		cell.setCellValue("$Cost for all efforts");

		cell = row.createCell(cellid++);

		cell.setCellValue(
				"$" + (totalEffortsInDay * 8 * modelObjectInst.props.getPropertyDouble("perHourBillingRate")));

		cell = row.createCell(cellid++);

		cell.setCellValue("Total Number of Days Required");

		cellid = addCell(row, cellid, totalEffortsInDay);

		row = spreadsheet.createRow(rowid++);

		row = spreadsheet.createRow(rowid++);

		for (String appNameKeys : modelObjectInst.applicationsScanned) {

			if (modelObjectInst.appAndPackageMapList.containsKey(appNameKeys)) {

				continue;

			}

			row = spreadsheet.createRow(rowid++);

			cellid = 0;

			cell = row.createCell(cellid++);

			cell.setCellValue("" + appNameKeys);

			cell = row.createCell(cellid++);

			cell.setCellValue("Application does not uses given framework");

		}

		System.out.println("finalAnalysis : Completed");

	}

	private String getValue(String appNameKeys) {

		String mainStr = modelObjectInst.pomMap.get(appNameKeys);// .to
        StringJoiner strj = new StringJoiner(",");
        String val = "";
		if (mainStr != null && mainStr.indexOf("<version>") > 0 && mainStr.indexOf("</version>") > 0) {

			int beginIndex = mainStr.indexOf("<version>") + 9;

			val = mainStr.substring(beginIndex);

			int endindex = val.indexOf("</version>");

			strj.add(val.substring(0, endindex));

			 

		}  

			if (mainStr != null && mainStr.contains(modelObjectInst.props.getProperty("JAR_NAME1"))) {

				strj.add(modelObjectInst.props.getProperty("JAR_NAME1"));
			}

			if (mainStr != null && mainStr.contains(modelObjectInst.props.getProperty("JAR_NAME2"))) {

				strj.add(modelObjectInst.props.getProperty("JAR_NAME2"));

			}

		return strj.toString();

	}

	private int addCell(HSSFRow row, int cellid, Double appNameKeys) {

		HSSFCell cell = row.createCell(cellid++);

		cell.setCellValue(Math.round(appNameKeys));

		return cellid;

	}

	private HashMap<String, Map> getPreFilledMap(Set<String> appNameList) {

		HashMap<String, Map> appFileChangeCountForPackage = new HashMap<>();

		for (String appName : appNameList) {

			HashMap<String, Integer> prefilledMap = getPreFilledMap();
			;// new HashMap<>();

			for (String packageKey : modelObjectInst.packages) {

				String newKey = appName + packageKey;

				Integer integer = modelObjectInst.appsFileChangeCountForThatPackage.get(newKey);

				integer = integer == null ? 0 : integer;

				prefilledMap.put(packageKey, integer);

			}

			appFileChangeCountForPackage.put(appName, prefilledMap);

		}

		return appFileChangeCountForPackage;

	}

	private HashMap<String, Integer> getPreFilledMap() {

		HashMap<String, Integer> prefilledMap = new HashMap<String, Integer>();// new HashMap<>();

		for (String string : modelObjectInst.packages) {

			prefilledMap.put(string, 0);

		}

		return prefilledMap;

	}

}