/**
 * Copyright 2016-2017 Eloy García Almadén <eloy.garcia.pca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.estoes.wallpaperDownloader.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import es.estoes.wallpaperDownloader.exception.WDPropertyException;

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
	
	// Atributes
	
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
	 * This method gets any property from properties file 
	 * @param property
	 * @return
	 */
	public String getProperty(String property) {
		InputStream input = null;
		Properties prop = new Properties();
		String value = null;
		String resource = null;
		try {
			input = this.getClass().getClassLoader().getResourceAsStream(APP_PROPERTIES_FILE_NAME);
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
