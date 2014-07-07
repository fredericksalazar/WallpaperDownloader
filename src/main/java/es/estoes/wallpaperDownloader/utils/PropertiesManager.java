package es.estoes.wallpaperDownloader.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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
	private static final String appPropertiesFileName = "application.properties";
	private static final String userPropertiesFileName = "config.txt";
	private static final String log4jPropertiesFileName = "log4j.properties";
	
	// Getters & Setters	

	// Methods
	/**
	 * Constructor
	 */
	private PropertiesManager () {
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
	 * This method gets any property from application properties file 
	 * @param property
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getAppProperty(String property) throws FileNotFoundException, IOException {
		InputStream input = null;
		Properties prop = new Properties();
		String value = null;
		try {
			input = this.getClass().getClassLoader().getResourceAsStream(appPropertiesFileName);
			prop.load(input);
			value = prop.getProperty(property);
		} catch (FileNotFoundException e) {
			LOG.error(appPropertiesFileName + " properties file wasn't found. Error: " + e.getMessage());
			throw e;
		} catch (IOException e) {
			LOG.error("Error while loading InputStream for reading properties file. Error: " + e.getMessage());
			throw e;
		} finally {
			if (input != null) {
				input.close();	
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
			output = new FileOutputStream(userPropertiesFileName);
			
			// Set the property value
			prop.setProperty(property, value);
			prop.store(output, null);
		} catch (IOException e) {
			LOG.error("Error while setting property " + property + " with value " + value + " within properties file " + appPropertiesFileName);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
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
			input = this.getClass().getClassLoader().getResourceAsStream(log4jPropertiesFileName);
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
