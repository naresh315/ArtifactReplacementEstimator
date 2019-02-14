package org.ape.properties;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;

import java.io.InputStream;

import java.util.Properties;

public class PropertySingleton {

	private static final String PROPERTIES_FILE_PATH = System.getProperty("user.home") + File.separator + "inputs-ARE.properties";

	static private Properties propertyReader = new Properties();

	static public PropertySingleton instance = null;

	private PropertySingleton() {

		// Properties file path.

		try (InputStream inputStream = new FileInputStream(PROPERTIES_FILE_PATH)) {

			// Loading the properties.

			propertyReader.load(inputStream);

			/*
			 * COM_FRAMEWORK ="\""+ input_props.getProperty("PACKAGE_IDENTIFIER");
			 * 
			 * Filteres_FromPropertiesFIle
			 * =".jar"+input_props.getProperty("FILE_EXTENSION_TOSEARCH");
			 * 
			 * JARNAMES1_FromPropertiesFIle =".jar"+input_props.getProperty("JAR_NAME1");
			 * 
			 * JARNAMES2_FromPropertiesFIle =".jar"+input_props.getProperty("JAR_NAME2");
			 * 
			 * 
			 * 
			 */

			System.out.println("-------------------->Properties file loaded  from  :" + PROPERTIES_FILE_PATH);

		} catch (IOException ex) {

			System.out.println("-------------------->Problem occurs when reading file !");

			ex.printStackTrace();

		}

	}

	public static PropertySingleton getInstance() {

		synchronized (PropertySingleton.class) {

			if (instance == null) {

				instance = new PropertySingleton();

			}

		}

		return instance;

	}

	public static double getPropertyDouble(String property) {

		return Integer.parseInt(propertyReader.getProperty(property).trim());

	}

	public static String getProperty(String property) {

		return propertyReader.getProperty(property).trim();

	}

}
