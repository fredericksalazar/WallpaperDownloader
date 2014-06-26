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
	private String fileName;
	
	// Getters & Setters	
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
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public PropertiesManager (String fileName) {
		if (fileName.isEmpty()) {
			this.setFileName(PropertiesManager.propertiesFileName);
		} else {
			this.setFileName(fileName);			
		}
	}
	
	/**
	 * 
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
//			input = new FileInputStream(this.getFileName());
			input = getClass().getResourceAsStream(this.getFileName());
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
