package org.ape.util;

import java.io.File;

import java.io.IOException;

import java.nio.file.Path;

import java.util.HashMap;

import java.util.HashSet;

import java.util.Scanner;

import java.util.SortedMap;

import java.util.StringJoiner;

import org.ape.properties.PropertySingleton;

public class CommonUtil {

	static public PropertySingleton instProp = PropertySingleton.getInstance();
	private static final String STRING2 = "<groupId>" + instProp.getProperty("GROUP_ID2") + "</groupId>";
	private static final String STRING = "<groupId>" + instProp.getProperty("GROUP_ID1")+ "</groupId>";

	static public void addPackages(String tmpStr, HashSet<String> packages) {

		tmpStr = tmpStr.substring(tmpStr.indexOf("package "));

		tmpStr = tmpStr.replaceAll("package ", "").replaceAll(";", "");

		if (tmpStr.contains("+")) {

			return;

		}

		packages.add(tmpStr.trim());

		return;

	}

	static public boolean javaFileFilter(Path s) {

		return s.toString().endsWith(".java");

	}

	static public String subextractPackageKey(String tmpStr) {

		String newStr;

		newStr = tmpStr.substring(tmpStr.indexOf("import") + 7);

		String[] split = newStr.split("\\.");

		String packKey = "";

		for (int i = 0; i < split.length - 1; i++) {

			if (packKey.length() == 0)

				packKey = split[i];

			else

				packKey = packKey + "." + split[i];

		}

		return packKey;

	}

	static public HashMap<String, Integer> getPreFilledMap(HashSet<String> packages) {

		HashMap<String, Integer> prefilledMap = new HashMap<String, Integer>();

		packages.forEach(action -> {
			prefilledMap.put(action, 0);
		});

		return prefilledMap;

	}

	static public void putinPomMap(String applicationName, String fileName, SortedMap<String, String> pomMap) {

		if (pomMap.containsKey(applicationName)) {

			pomMap.put(applicationName, pomMap.get(applicationName) + fileName);

		} else {

			pomMap.put(applicationName, fileName);

		}

	}

	static public void scanForPomDependency(String applicationName, File file, SortedMap<String, String> pomMap)
			throws IOException {
 		Scanner scanner = new Scanner(file);

		StringJoiner sj = new StringJoiner(",");

		while (scanner.hasNextLine()) {

			String tmpStr = scanner.nextLine();
 			
			if (tmpStr != null && (tmpStr.contains(STRING)
					|| tmpStr.contains(STRING2))) {

				sj.add(tmpStr.trim());

				tmpStr = scanner.nextLine();

				sj.add(tmpStr.trim());

				tmpStr = scanner.nextLine();

				sj.add(tmpStr.trim());

				CommonUtil.putinPomMap(applicationName, sj.toString(), pomMap);

				break;

			}

		}

	}

	public static String timeTaken(long endTime) {

		int seconds = (int) endTime / 1000;

		int hours = seconds / 3600;

		int minutes = (seconds % 3600) / 60;

		seconds = (seconds % 3600) % 60;

		return minutes + ":" + seconds;

	}

}