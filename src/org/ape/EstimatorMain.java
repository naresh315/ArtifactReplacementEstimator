package org.ape;
	import java.io.File; 
	import java.io.FileNotFoundException; 
	import java.io.IOException; 
	import java.nio.file.Files; 
	import java.nio.file.Path; 
	import java.nio.file.Paths; 
	import java.util.HashMap; 
	import java.util.Map; 
	import java.util.Scanner; 
	import java.util.Set; 
	import java.util.SortedMap; 
	import java.util.TreeMap;

import org.ape.Model.ModelObject;
import org.ape.excel.ProcessAndWriteToExcel;
import org.ape.util.CommonUtil; 
	
	public class EstimatorMain  {
	    private boolean appEntryFound = false;
	    private ModelObject modelObjectInst = new ModelObject();

	    public static void main(String[] args) throws IOException { 
	       new EstimatorMain(). execute(); 
	    }

		private   void execute() throws IOException {
			long start = System.currentTimeMillis(); 

	        System.out.println("start time :" +start); 
	        System.out.println("Extracting packages from framework"); 
	         findPackages(modelObjectInst.props.getProperty("JAR_SOURCE_CODE_PATH")); 
	        System.out.println("Analysing Applications from this Dir for package usage :" +modelObjectInst.props.getProperty("INPUT_APPS_SOURCE")); 

	        try { 

	             System.out.println(" Scanning for  File types: .java,.jsp,.xml,.properties .wsdd "); 

	              searchPackUsageInProject(modelObjectInst.props.getProperty("INPUT_APPS_SOURCE")); 

	             System.out.println("Analysis is Completed Successfully and Data is written to Excel sheet : "+modelObjectInst.props.getProperty("EXCEL_FILE_NAME")); 

	             System.out.println("Total Application scanned "+modelObjectInst.applicationsScanned.size()); 

	             System.out.println("Comma seperated list of the scanned apps :"+modelObjectInst.applicationsScanned);//.retainAll( EstimatorMain .appsFileChangeCountForThatPackage.keySet())); 

	             System.out.println("Total Time Taken :" +CommonUtil.timeTaken(System.currentTimeMillis() - start)); 

	              

	        } catch (Exception e) { 

	            e.printStackTrace(); 

	        }
		} 

	private   void searchPackUsageInProject(String sourceCodeDirectory) throws Exception { 

	        SortedMap<String,Map> appAndPackageMapList = new TreeMap<>(); 

	        Files.walk(Paths.get(sourceCodeDirectory)). 

	                filter(s -> fileFilters(s)).forEach(files -> { try { packageAndAppMapping(appAndPackageMapList,files.toFile()); } catch (Exception e) { // TODO Auto-generated catch block 

	                        e.printStackTrace(); 

	                    } 

	                } ); 
	        modelObjectInst.appAndPackageMapList=appAndPackageMapList;
	       new ProcessAndWriteToExcel(modelObjectInst).processData(); 
	    } 

	     

	    private boolean fileFilters(Path s) { 

	        String applicationDirPath = s.getFileName().toString(); 

	        int dot = s.getFileName().toString().lastIndexOf("."); 
	      /* if(applicationDirPath.contains("version-1.5.4-javadoc"))
	    	   System.out.println(applicationDirPath);*/

	         if(dot>0 && modelObjectInst.SEACH_EXTENSION.contains(applicationDirPath.substring(dot).trim())) { 
	        	 
	            return true; 
	         } 

	          

	        return false; 

	    } 

	    private   void  packageAndAppMapping(SortedMap<String,Map> appAndPackageMapList, File file) throws Exception { 

	             String applicationDirPath = file.getAbsolutePath(); 

	              if(!applicationDirPath.contains("app-list")) {//C:\var 

	                 throw new Exception("Application Directory Path doesnt contain app-list:  sample path is    C:\\ccstg\\app-list\\"); 

	             } 

	             int beginIndex = applicationDirPath.indexOf("app-list")+9; 

	             String tmpString = applicationDirPath.substring(beginIndex); 

	             String applicationName = tmpString.split("\\\\")[0]; 

	             HashMap<String,Integer> packageCounts = CommonUtil.getPreFilledMap(modelObjectInst.packages); 

	             //System.out.println(applicationDirPath+"  :"+applicationName);ia3 
	             //Filteres_FromPropertiesFIle 

	               getPackageCountMap(applicationName,file,packageCounts); 
 

	             if(appAndPackageMapList.containsKey(applicationName)) { 

	                  HashMap<String,Integer> packageCounts1 = (HashMap<String, Integer>) appAndPackageMapList.get(applicationName); 

	                 Set<String> keySet = packageCounts1.keySet(); 

	                 for (String key : keySet) { 

	                     Integer integer = packageCounts.get(key); 

	                     integer = integer==null?0:integer; 

	                    Integer integer2 = packageCounts1.get(key); 

	                     integer2 = integer2==null ? 0 :integer2; 

	                    packageCounts.put(key, integer+ integer2); 

	                } 

	             } 

	             if(appEntryFound) { 

	                appAndPackageMapList.put(applicationName,packageCounts); 
	                appEntryFound = false; 

	                if(modelObjectInst.appFileChangedCount.containsKey(applicationName)) { 

	                	modelObjectInst.appFileChangedCount.put(applicationName, modelObjectInst.appFileChangedCount.get(applicationName)+1); 

	                 }else { 

	                	 modelObjectInst.appFileChangedCount.put(applicationName,1); 

	                 } 

	                

	             } 

	             modelObjectInst.applicationsScanned.add(applicationName); 

	             String fileName = file.getName().toLowerCase(); 

	             if(fileName.contains("pom.xml") ) {  

	                 CommonUtil.scanForPomDependency(applicationName,file,modelObjectInst.pomMap)    ;                 

	                } 
	             
	    } 

	     

	  

	  

	    private   void getPackageCountMap(String applicationName,File file, HashMap<String, Integer> packageCounts) throws IOException { 

	         try { 

	            Scanner scanner = new Scanner(file); 
	            String name = file.getName();
	            if(name.endsWith(".jar") ) { 
					 CommonUtil.putinPomMap(applicationName, name,modelObjectInst.pomMap); 
                      return;
	             }  
	            if(name.contains(".wsdl")  || name.contains(".wsdd") ) { 
	            	updateCountsOfFiles(applicationName, file, packageCounts,"wsdl.or.wsdd"); 
                      return;
	             }  
	            
				 // updateCountsOfFiles(applicationName, file, packageCounts, packageName); 
				 
	            while(scanner.hasNextLine()) { 

	                packageScanner(applicationName, file, packageCounts, scanner); 

	            } 

	        } catch (FileNotFoundException e) { 

	            // TODO Auto-generated catch block 

	            e.printStackTrace(); 

	        } 

	    } 

	    private void packageScanner(String applicationName, File file, HashMap<String, Integer> packageCounts, 

	            Scanner scanner) { 

	        String tmpStr = scanner.nextLine(); 

	        scanPackages(applicationName, file, packageCounts, tmpStr); 

	    } 

	    private void scanPackages(String applicationName, File file, HashMap<String, Integer> packageCounts, 

	            String tmpStr) { 

	        if(tmpStr !=null && tmpStr.contains("class")&& tmpStr.contains("{")){ 

	            return; 

	        } 

	        try { 

	            //value="com.framework. 

	            //class="com.framework. 

	            if(tmpStr !=null && tmpStr.contains("import")){ 

	              String packKey = CommonUtil.subextractPackageKey(tmpStr); 

	                if(modelObjectInst.packages.contains(packKey)){ 

	                    updateCountsOfFiles(applicationName, file, packageCounts, packKey); 

	            } } 

	             

	            if(tmpStr !=null && (tmpStr.contains("value="+modelObjectInst.COM_FRAMEWORK)|| tmpStr.contains("class="+modelObjectInst.COM_FRAMEWORK))){ 

	                for (String packageName : modelObjectInst.packages) { 

	                    if(tmpStr.contains(packageName)) { 

	                            updateCountsOfFiles(applicationName, file, packageCounts, packageName); 

	                    } 

	                } 

	            } 

	            if(tmpStr !=null && (file.getName().contains(".jsp"))){ 

	                  

	                for (String packageName : modelObjectInst.packages) { 

	                    if(tmpStr.contains(packageName)) { 

	                            updateCountsOfFiles(applicationName, file, packageCounts, packageName); 

	                    } 

	                } 

	            } 

	             

	        }catch (Exception e) { 

	            //System.out.println(" newStr "+newStr); 

	            //System.out.println(" tmpStr "+tmpStr); 

	            //e.printStackTrace(); 

	        } 

	    } 

	    private void updateCountsOfFiles(String applicationName, File file, HashMap<String, Integer> packageCounts, 

	            String packKey) { 

	        appEntryFound = true; 

	        if(packageCounts.containsKey(packKey)) { 

	            packageCounts.put(packKey,packageCounts.get(packKey)+1); 

	        }else { 

	            packageCounts.put(packKey,1); 

	        } 

	        String fileName = file.getAbsolutePath().toString(); 

	        if(modelObjectInst.appsItsFileListMap.containsKey(applicationName)) { 

	            String string = modelObjectInst.appsItsFileListMap.get(applicationName); 

	            if(!string.contains(fileName)) 

	            	modelObjectInst.appsItsFileListMap.put(applicationName,string+","+fileName); 

	        }else { 

	        	modelObjectInst.appsItsFileListMap.put(applicationName,fileName); 

	        } 

	        String key = applicationName+file.getName()+packKey; 

	        if(!modelObjectInst.tmpFileTracker.containsKey(key)) { 

	        	modelObjectInst.tmpFileTracker.put(key,1); 

	            String key2 = applicationName +packKey; 

	            if(modelObjectInst.appsFileChangeCountForThatPackage.containsKey(key2)) { 

	            	modelObjectInst.appsFileChangeCountForThatPackage.put(key2, modelObjectInst.appsFileChangeCountForThatPackage.get(key2)+1); 

	                }else { 

	                	modelObjectInst.appsFileChangeCountForThatPackage.put(key2, 1); 

	                } 

	        } 

	    } 

	     

	    private   void findPackages(String filePath) throws IOException { 

	         Files.walk(Paths.get(filePath)).filter(s -> CommonUtil.javaFileFilter(s)).forEach(paths-> { try { 

	             Files.lines(paths) 

	                 .map(tmpStr -> tmpStr.trim()) 

	                 .filter(tmpStr ->  tmpStr !=null && tmpStr.contains("package ")&& tmpStr.endsWith(";")) 

	                 .forEach(tmpStr -> CommonUtil.addPackages(tmpStr, modelObjectInst.packages)); 

	        } catch (IOException e) { 

	            // TODO Auto-generated catch block 

	            e.printStackTrace(); 

	        } 

	              }); 

	         System.out.println("Packages Read from   Framework"); 

	    } 

	} 

