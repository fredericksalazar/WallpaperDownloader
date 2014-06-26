package es.estoes.wallpaperDownloader.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesManager {

	// Constants
	private static final Logger LOG = Logger.getLogger(PropertiesManager.class);
	private static final String propertiesFileName = "application.properties";
	
	// Attributes
	private Properties properties;
	private InputStream input;
	private String fileName;
	
	// Getters & Setters	
	// properties
	private void setProperties(Properties properties) {
		this.properties = properties;
	}

	// input
	private InputStream getInput() {
		return input;
	}

	private void setInput(InputStream input) {
		this.input = input;
	}

	// fileName
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	// Methods
	/**
	 * Constructor
	 */
	public PropertiesManager () {
		
	}

	/**
	 * Constructor
	 * @param propertiesFileName
	 * @throws FileNotFoundException 
	 */
	public PropertiesManager (String fileName) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		if (fileName.isEmpty()) {
			this.setFileName(PropertiesManager.propertiesFileName);
		} else {
			this.setFileName(fileName);			
		}
		try {
			this.setInput(new FileInputStream(this.getFileName()));
			prop.load(this.getInput());
			this.setProperties(prop);
		} catch (FileNotFoundException e) {
			LOG.error(this.getFileName() + " properties file wasn't found. Error: " + e.getMessage());
			throw e;
		} catch (IOException e) {
			LOG.error("Error while loading InputStream for reading properties file. Error: " + e.getMessage());
			throw e;
		}
	}

}
