package es.estoes.wallpaperDownloader.utils;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

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
	protected final Logger LOG = Logger.getLogger(WDConfigManager.class);

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
		
		/**
		 * TODO:
		 * - Check OS
		 * - Check application's folder. If it doesn't exit, create it
		 * - Check internal structure. If it doesn't exit, create it
		 * - Store paths in application.properties
		 * - Store default sets in apllication.properties
		 */
	     JFileChooser fr = new JFileChooser();
	     FileSystemView fw = fr.getFileSystemView();
	     System.out.println(fw.getDefaultDirectory());
		//System.out.println(System.getProperty("user.home"));
		
		return true;
	}
	
}
