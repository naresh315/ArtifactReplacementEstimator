package org.ape.excel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.ape.EstimatorMain;
import org.ape.Model.ModelObject;
public class ExcelCalculationUtility extends ModelObject {

	protected static HashMap<String, Double[]> getFinalEfforts(SortedMap<String, Integer> appFileChangedCount2,
			HashMap<String, Map> fileChangeCountappAndPackageMapList) {

		Set<String> keySet = fileChangeCountappAndPackageMapList.keySet();

		HashMap<String, Double[]> tmpMap = new HashMap<String, Double[]>();

		// loadProperties();

		for (String appNames : keySet) {

			Double[] effortsArray = new Double[8];

			double totalEffortsInDays = props.getPropertyDouble("default_totalEffortsInDays");// 10;

			Map packageFileChangeMap = fileChangeCountappAndPackageMapList.get(appNames);

			// System.out.println(packageFileChangeMap);

			Set<String> keySet2 = packageFileChangeMap.keySet();

			Set<String> typeOfpackage = new HashSet<>();// packageFileChangeMap.keySet();

			double packageCount = 0;

			for (String object : keySet2) {

				Integer object1 = (Integer) packageFileChangeMap.get(object);

				if (object1 == 0) {

					continue;

				}

				packageCount++;

			}

			if (typeOfpackage.contains(props.getProperty("package1"))
					&& typeOfpackage.contains(props.getProperty("package2"))
					&& typeOfpackage.contains(props.getProperty("package3"))) {

				totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble(props.getProperty("package_MIXED"));
				;// 6

			} else {// package1

				if (typeOfpackage.contains(props.getProperty("package1"))) {

					totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble(props.getProperty("package1"));
					;// 3

				}

				if (typeOfpackage.contains(props.getProperty("package2"))) {

					totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble(props.getProperty("package2"));
					;// 3

				}

				if (typeOfpackage.contains(props.getProperty("package3"))) {

					totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble(props.getProperty("package3"));
					;// 3

				}

			}

			if (typeOfpackage.contains(props.getProperty("package6"))
					|| typeOfpackage.contains(props.getProperty("package7"))
					|| typeOfpackage.contains(props.getProperty("package5"))) {

				totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble(props.getProperty("package6")); // 3;
																													// //com.framework.security.cryptography

			}

			if (typeOfpackage.contains(props.getProperty("package8"))) {

				totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble(props.getProperty("package8")); // 3

			}

			if (typeOfpackage.contains(props.getProperty("package9"))) {

				totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble(props.getProperty("package9")); // 10

			}

			int totalFileCount = (int) appFileChangedCount2.get(appNames);

			if (totalFileCount > props.getPropertyDouble("totalFileCount_EXCCEDS")) {// 50

				totalEffortsInDays = totalEffortsInDays
						+ ((totalFileCount - 50) / props.getPropertyDouble("daysForFilesChangesCount_Factor"));// 10

			}

			if (packageCount >= props.getPropertyDouble("PACKAGE_EXCEEDSCOUNT")) {// 4

				totalEffortsInDays = totalEffortsInDays + props.getPropertyDouble("PACKAGE_EXCEEDSCOUNT_DAYS");// 10

			}

			effortsArray[0] = totalEffortsInDays;

			effortsArray[1] = (totalEffortsInDays / 100) * props.getPropertyDouble("percentageForUnitEnvTesting");// unit
																													// totalEffortsInDays*100/100;percentage
																													// =
																													// (float)((15
																													// /
																													// totalEffortsInDays)
																													// *
																													// 100);

			effortsArray[2] = (totalEffortsInDays / 100) * props.getPropertyDouble("percentageForIntegTesting");// integ
																												// totalEffortsInDays/100=

			effortsArray[3] = (totalEffortsInDays / 100) * props.getPropertyDouble("percentageForAcceptanceTesting");// acceptance

			effortsArray[4] = 2.0;// prod

			effortsArray[5] = (totalEffortsInDays / 100) * props.getPropertyDouble("percentageForManagement");// Manamenet

			effortsArray[6] = effortsArray[0] + effortsArray[1] + effortsArray[2] + effortsArray[3] + effortsArray[4]
					+ effortsArray[5];

			effortsArray[7] = packageCount;

			tmpMap.put(appNames, effortsArray);

			// System.out.println( appNames+" :"+effortsArray );

			// System.out.println( values.stream().mapToInt(i->(Integer)i).count());

		}

		return tmpMap;

	}

}
