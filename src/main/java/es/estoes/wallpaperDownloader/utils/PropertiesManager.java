package es.estoes.wallpaperDownloader.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import es.estoes.wallpaperDownloader.exceptions.WDPropertyException;

/**
 * This class uses the Singleton principle and design pattern. It can only be instantiated 
 * one time during the execution of the application and it gathers all the methods related
 * to the properties of the wallpaper downloader application
 * 
 * @author egarcia
 *
 */
public class PropertiesManager {

	// Constants
	private static volatile PropertiesManager instance;
	private static final Logger LOG = Logger.getLogger(PropertiesManager.class);
	private static final String APP_PROPERTIES_FILE_NAME = "application.properties";
	private static final String LOG4J_PROPERTIES_FILE_NAME = "log4j.properties";
	public static final String APP_PROP_TYPE = "app";
	public static final String USER_PROP_TYPE = "user";
	
	// Atributes
	private String userConfigurationFilePath;
	
	// Getters & Setters	

	// Methods
	/**
	 * Constructor
	 */
	private PropertiesManager () {
		userConfigurationFilePath = WDUtilities.getUserConfigurationFilePath();
	}
	
	public static PropertiesManager getInstance() {
		if (instance == null) {
			synchronized (PropertiesManager.class) {
				if (instance == null) {
					instance = new PropertiesManager();
					
				}
			}
		}
		return instance;
	}
	
	/**
	 * This method gets any property from properties file 
	 * @param property
	 * @return
	 */
	public String getProperty(String property, String type) {
		InputStream input = null;
		Properties prop = new Properties();
		String value = null;
		String resource = null;
		try {
			if (type.equals(APP_PROP_TYPE)) {
				resource = APP_PROPERTIES_FILE_NAME;
			} else if (type.equals(USER_PROP_TYPE)) {
				resource = userConfigurationFilePath;
			}
			input = this.getClass().getClassLoader().getResourceAsStream(resource);
			prop.load(input);
			value = prop.getProperty(property);
		} catch (FileNotFoundException e) {
			throw new WDPropertyException(resource + " properties file wasn't found. Error: " + e.getMessage());
		} catch (IOException e) {
			throw new WDPropertyException("Error loading InputStream for reading properties file. Error: " + e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new WDPropertyException("Error closing InputStream. Error: " + e.getMessage());
				}	
			}		
		}
		return value;
	}
	
	/**
	 * This class sets a user property
	 * @param property
	 * @param value
	 */
	public void setUserProperty(String property, String value) {
		Properties prop = new Properties();
		OutputStream output = null;
		
		try {
			output = new FileOutputStream(userConfigurationFilePath);
			
			// Set the property value
			prop.setProperty(property, value);
			prop.store(output, null);
		} catch (IOException e) {
			throw new WDPropertyException("Error while setting property " + property + " with value " + value + " within properties file " + userConfigurationFilePath);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					throw new WDPropertyException("Error closing outputFile. Error: " + e.getMessage());
				}
			}
		}
	}
	
	/**
	 * This class sets a log4j property
	 * @param property
	 * @param value
	 */
	public void setLog4jProperty(String property, String value) {
		InputStream input = null;
		Properties log4jProperties = new Properties();
		try {
			input = this.getClass().getClassLoader().getResourceAsStream(LOG4J_PROPERTIES_FILE_NAME);
			log4jProperties.load(input);
			input.close();	
		} catch (FileNotFoundException e) {
			LOG.error("log4j properties file wasn't found. Error: " + e.getMessage());
		} catch (IOException e) {
			LOG.error("Error while loading InputStream for reading properties file. Error: " + e.getMessage());
		}
		
		log4jProperties.setProperty(property, value);
		PropertyConfigurator.configure(log4jProperties);
	}
}
