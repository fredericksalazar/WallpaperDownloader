package es.estoes.wallpaperDownloader.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

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
	private static final String propertiesFileName = "application.properties";
	
	// Attributes
	private String fileName;
	
	// Getters & Setters	
	// fileName
	public String getFileName() {
		return fileName;
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}


	// Methods
	/**
	 * Constructor
	 */
	private PropertiesManager () {
		this.setFileName(PropertiesManager.propertiesFileName);
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
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getProperty(String property) throws FileNotFoundException, IOException {
		InputStream input = null;
		Properties prop = new Properties();
		String value = null;
		try {
			input = this.getClass().getClassLoader().getResourceAsStream(this.getFileName());
			prop.load(input);
			value = prop.getProperty(property);
		} catch (FileNotFoundException e) {
			LOG.error(this.getFileName() + " properties file wasn't found. Error: " + e.getMessage());
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
}
