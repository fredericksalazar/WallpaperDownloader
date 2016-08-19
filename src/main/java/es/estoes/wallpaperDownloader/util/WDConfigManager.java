package es.estoes.wallpaperDownloader.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.changer.LinuxWallpaperChanger;
import es.estoes.wallpaperDownloader.changer.UnknownWallpaperChanger;
import es.estoes.wallpaperDownloader.changer.WindowsWallpaperChanger;
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
	 * This method checks the configuration.
	 * If something is wrong, it throws an exception.
	 */
	public static void checkConfig() throws WDConfigurationException {
		
	     LOG.info("Checking configuration...");
	     LOG.info("Checking application's folder");
	     PreferencesManager prefm = PreferencesManager.getInstance();
	     Path appPath = Paths.get(WDUtilities.getAppPath());
	     Path absoluteDownloadsPath = null;
    	 LOG.info("Checking downloads folder...");
    	 absoluteDownloadsPath = Paths.get(appPath.toString());
    	 try {
    		 File userConfigFile = new File(WDUtilities.getUserConfigurationFilePath());

    		 // Configuration file
    		 if (!userConfigFile.exists()) {
        		 /**
        		  *  Downloads directory
        		  */
        		 absoluteDownloadsPath = absoluteDownloadsPath.resolve(WDUtilities.DEFAULT_DOWNLOADS_DIRECTORY);
        		 File downloadsDirectory = new File(absoluteDownloadsPath.toString());

        		 if (!downloadsDirectory.exists()) {
        			 LOG.info("Downloads folder doesn't exist. Creating...");
        			 FileUtils.forceMkdir(downloadsDirectory);
        		 }  		 
        		 // Setting the downloads path 
        		 WDUtilities.setDownloadsPath(absoluteDownloadsPath.toString());
        		 LOG.info("Downloads directory -> " + absoluteDownloadsPath.toString());
 
           		 /**
        		  * User's configuration file
        		  */
    			 LOG.info("User configuration file doesn't exist. Creating...");
    			 FileUtils.touch(userConfigFile);

        		 // Initializing user configuration file
    			 // Downloads directory
        		 prefm.setPreference("application-downloads-folder", absoluteDownloadsPath.toString());
        		 
    			 // Providers
        		 prefm.setPreference("provider-wallhaven", WDUtilities.APP_NO);
        		 
        		 // Initializing timer
        		 // By default, the application will download a new wallpaper every 5 minutes
        		 // 0 -> 5 min
        		 // 1 -> 10 min
        		 // 2 -> 20 min
        		 // 3 -> 30 min
        		 prefm.setPreference("application-timer", "0");

        		 // Initializing maximum download directory size
        		 // By default, it will be 40 MBytes
        		 prefm.setPreference("application-max-download-folder-size", "40");

        		 // Initializing resolution
        		 // By default, the application will download all kind of resolutions
        		 prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);

        		 // Initializing search type
        		 // By default, the application will download Favorites wallpapers
        		 prefm.setPreference("wallpaper-search-type", "3");
        		 
        		 /**
        		  * WallpaperDownloader icon
        		  */
        		 //app.icon.path
        		 // TODO: Copy application icon to folder      		 
        		 
    		 } else {	
    			 // Configuration file exists
    			 // Setting the downloads path
    			 // Important: Due to the nature of snap packages, if this application
    			 // has been installed via snap, then it can be a problem with downloads directory
    			 // When a new version of the same snap package is installed, the previous files
    			 // and configuration is copied to the new confinement directory. Because of every
    			 // snap version has its own permissions to write only in the directory created for
    			 // it, if user didn't move the downloads directory, it still will be the last one,
    			 // so permission errors will be thrown when the application tries to write a new
    			 // wallpaper.
    			 String downloadsDirectoryString = prefm.getPreference("application-downloads-folder");
    			 if (downloadsDirectoryString.contains(WDUtilities.SNAP_KEY)) {
    				 // It is assumed that wallpaperdownloader has been installed via snap
    				 // Downloads directory is moved to the new directory just in case it is a new
    				 // version
    				 if (LOG.isInfoEnabled()) {
    					 LOG.info("It has been detected that wallpaperdownloader application has been installed via snap package. Reconfiguring downloads directory just in case it is a new version and it is needed to move downloads directory to the new confinement space...");
    				 }
            		 absoluteDownloadsPath = absoluteDownloadsPath.resolve(WDUtilities.DEFAULT_DOWNLOADS_DIRECTORY);
            		 File downloadsDirectory = new File(absoluteDownloadsPath.toString());

            		 if (downloadsDirectory.exists()) {
                		 // Setting the downloads path 
                		 WDUtilities.setDownloadsPath(absoluteDownloadsPath.toString());
                		 prefm.setPreference("application-downloads-folder", absoluteDownloadsPath.toString());
            		 }  		 
    			 } else {
        			 WDUtilities.setDownloadsPath(prefm.getPreference("application-downloads-folder"));
    			 }
    			 LOG.info("Downloads directory -> " + prefm.getPreference("application-downloads-folder"));
    		 }
    		 
    		 // Blacklist directory
			 if (LOG.isInfoEnabled()) {
				 LOG.info("Checking blacklist directory...");
			 } 			 
			 File blacklistDirectory = new File(WDUtilities.getBlacklistDirectoryPath());
 			 if (!blacklistDirectory.exists()) {
 				 if (LOG.isInfoEnabled()) {
 					 LOG.info("Blacklist directory doesn't exist. Creating...");
 				 }
 				FileUtils.forceMkdir(blacklistDirectory);
 			 } else {
 				 if (LOG.isInfoEnabled()) {
 					 LOG.info("Blacklist directory already exists. Skipping...");
 				 }
 			 }
 			 
 			 // Operating System
 			 switch (System.getProperty("os.name")) {
 			 case WDUtilities.OS_LINUX:
 				 WDUtilities.setOperatingSystem(WDUtilities.OS_LINUX);
 				 break;
 			 case WDUtilities.OS_WINDOWS:
 				 WDUtilities.setOperatingSystem(WDUtilities.OS_WINDOWS);
 				 break;
 			 default:
				 WDUtilities.setOperatingSystem(WDUtilities.OS_UNKNOWN);
				 break;
 			 }
			 if (LOG.isInfoEnabled()) {
 				 LOG.info("Operating System detected: " + WDUtilities.getOperatingSystem());
 			 }
 			 
 			 // Wallpaper changer (Factory method)
			 if (WDUtilities.getOperatingSystem().equals(WDUtilities.OS_LINUX)) {
				 WDUtilities.setWallpaperChanger(new LinuxWallpaperChanger());
			 } else if (WDUtilities.getOperatingSystem().equals(WDUtilities.OS_LINUX)) {
				 WDUtilities.setWallpaperChanger(new WindowsWallpaperChanger());
			 } else {
				 WDUtilities.setWallpaperChanger(new UnknownWallpaperChanger());
			 }

    	 } catch (Exception e) {
    		 throw new WDConfigurationException("Error setting up the downloads folder. Error: " + e.getMessage());
    	 }    	 
	}
	
	/**
	 * This method configures the application's log
	 */
	public static void configureLog() throws WDConfigurationException {

		// Check application's folder. If it doesn't exit, create it
	    PropertiesManager pm = PropertiesManager.getInstance();
	    Path appPath = null;
	    String absoluteAppPath = null;
	    // Getting user's home directory
	    appPath = Paths.get(System.getProperty("user.home"));
	    
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
