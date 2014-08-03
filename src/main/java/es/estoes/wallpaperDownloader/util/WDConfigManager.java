package es.estoes.wallpaperDownloader.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.exception.WDConfigurationException;

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
	public static void checkConfig() throws WDConfigurationException {
		
	     LOG.info("Checking configuration...");
	     LOG.info("Checking application's folder");
	     PropertiesManager pm = PropertiesManager.getInstance();
	     PreferencesManager prefm = PreferencesManager.getInstance();
	     Path appPath = Paths.get(WDUtilities.getAppPath());
	     Path absoluteDownloadsPath = null;
    	 LOG.info("Checking downloads folder...");
    	 absoluteDownloadsPath = Paths.get(appPath.toString());
    	 try {
    		 /**
    		  *  Downloads directory
    		  */
    		 absoluteDownloadsPath = absoluteDownloadsPath.resolve(pm.getProperty("app.downloads.path"));
    		 File downloadsDirectory = new File(absoluteDownloadsPath.toString());
	    	 
    		 if (!downloadsDirectory.exists()) {
    			 LOG.info("Downloads folder doesn't exist. Creating...");
    			 FileUtils.forceMkdir(downloadsDirectory);
    		 }
	    	
    		 // Setting the downloads path 
    		 WDUtilities.setDownloadsPath(absoluteDownloadsPath.toString());
    		 
    		 /**
    		  * User's configuration file
    		  */
    		 LOG.info("Checking user configuration file...");
    		 File userConfigFile = new File(WDUtilities.getUserConfigurationFilePath());
    		 
    		 if (!userConfigFile.exists()) {
    			 LOG.info("User configuration file doesn't exist. Creating...");
    			 FileUtils.touch(userConfigFile);

        		 // Initializing user configuration file
    			 // Providers
        		 prefm.setPreference("provider-wallbase", WDUtilities.APP_NO);
        		 
        		 // Initializing timer (miliseconds)
        		 // By default, the application will download a new wallpaper every 5 minutes
        		 prefm.setPreference("application-timer", "300000");

        		 // Initializing maximum download folder size
        		 // By default, it will be 40 MBytes
        		 prefm.setPreference("application-max-download-folder-size", "40");

        		 // Initializing resolution
        		 // By default, the application will download all kind of resolutions
        		 prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);

        		 // Initializing search type
        		 // By default, the application will download Favorites wallpapers
        		 prefm.setPreference("wallpaper-search-type", "3");
    		 }

    	 } catch (Exception e) {
    		 throw new WDConfigurationException("Error setting up the downloads folder. Error: " + e.getMessage());
    	 }    	 

	}
	
	/**
	 * This class configures the application's log
	 */
	public static void configureLog() throws WDConfigurationException {

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
			throw new WDConfigurationException("The log4j properties file was not found. Error: " + e.getMessage());
		} catch (IOException e) {
			throw new WDConfigurationException("There was some error while setting log4j configuration. Error: " + e.getMessage());
		}
	}
	
}
