package es.estoes.wallpaperDownloader.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * This class checks for the right configuration of the application and tries to initialize
 * all the infrastructure needed to support the application. It implements a kind of
 * Singleton pattern not pure and only based on static methods. Actually, there won't be 
 * an object of this class ever 
 * @author egarcia
 *
 */
public class WDConfigManager {
	
	// Constants
	protected static final Logger LOG = Logger.getLogger(WDConfigManager.class);

	// Attributes
	
	// Getters & Setters
	
	// Methods (All the methods are static)

	/**
	 * Constructor
	 */
	private WDConfigManager() {
		
	}
	
	/**
	 * This method checks the configuration. If it is OK, then returns true value.
	 * If something is wrong, it returns a false value.
	 * @return boolean
	 */
	public static boolean checkConfig() {
		
	     LOG.info("Checking configuration...");
	     LOG.info("Checking application's folder");
	     PropertiesManager pm = PropertiesManager.getInstance();
	     boolean value = true;
	     String appPath = WDUtilities.getAppPath();
	     Path absoluteDownloadsPath = null;
	     if (!appPath.isEmpty()) {
	    	 LOG.info("Checking downloads folder...");
	    	 absoluteDownloadsPath = Paths.get(appPath);
	    	 try {
	    		 absoluteDownloadsPath = absoluteDownloadsPath.resolve(pm.getProperty("app.downloads.path"));
	    		 File downloadsDirectory = new File(absoluteDownloadsPath.toString());
		    	 
	    		 // If the directory doesn't exist, then create it
	    		 if (!downloadsDirectory.exists()) {
	    			 LOG.info("Downloads folder doesn't exist. Creating...");
	    			 FileUtils.forceMkdir(downloadsDirectory);
	    		 }
		    	
	    		 // Setting the application's path 
	    		 WDUtilities.setDownloadsPath(absoluteDownloadsPath.toString());

	    	 } catch (FileNotFoundException e) {
				LOG.error("Error while setting up the downloads folder. Error: " + e.getMessage());
				value = false;
			} catch (IOException e) {
				LOG.error("Error while setting up the downloads folder. Error: " + e.getMessage());
				value = false;
			}
	    	 
	     } else {
	    	 value = false;
	     }
		return value;
	}
	
	/**
	 * This class configures the application's log
	 */
	public static boolean configureLog() {

		// Check application's folder. If it doesn't exit, create it
	    PropertiesManager pm = PropertiesManager.getInstance();
	    Path appPath = null;
	    String absoluteAppPath = null;
	    JFileChooser fr = new JFileChooser();
	    FileSystemView fw = fr.getFileSystemView();
	    appPath = Paths.get(fw.getDefaultDirectory().toURI());
	    try {
	     
	    	// Application's path
	    	absoluteAppPath = appPath.resolve(pm.getProperty("app.path")).toString();
	    	File appDirectory = new File(absoluteAppPath);
	    	 
	    	// If the directory doesn't exist, then create it
	    	if (!appDirectory.exists()) {
	    		FileUtils.forceMkdir(appDirectory);
	    	}
	    	
	    	// Setting the application's path 
	    	WDUtilities.setAppPath(absoluteAppPath);
	    	// Setting the application's log name
	    	pm.setLog4jProperty("log4j.appender.logfile.File", absoluteAppPath + File.separator + pm.getProperty("log.name")); 
		} catch (FileNotFoundException e) {
			LOG.error("The log4j properties file was not found. Error: " + e.getMessage());
			return false;
		} catch (IOException e) {
			LOG.error("There was some error while setting log4j configuration. Error: " + e.getMessage());
			return false;
		}
	    return true;
	}
	
}
