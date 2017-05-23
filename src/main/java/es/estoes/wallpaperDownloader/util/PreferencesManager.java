/**
 * Copyright 2016-2017 Eloy García Almadén <eloy.garcia.pca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 * This class uses the Singleton principle and design pattern. It can only be instantiated 
 * one time during the execution of the application and it gathers all the methods related
 * to the user preferences of the wallpaper downloader application
 * 
 * @author egarcia
 *
 */
public class PreferencesManager {

	// Constants
	private static volatile PreferencesManager instance;
	private static final Logger LOG = Logger.getLogger(PreferencesManager.class);
	public static final String DEFAULT_VALUE = "NONE";
	
	// Atributes
	private Preferences pref;
	
	// Getters & Setters	

	// Methods
	/**
	 * Constructor
	 */
	private PreferencesManager () {
		LOG.info("Creating PreferenceManager instance for the first time...");
		setUserConfigurationPath();
	    System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
	    System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, WDUtilities.getUserConfigurationFilePath());
	    pref = Preferences.userNodeForPackage(es.estoes.wallpaperDownloader.window.WallpaperDownloader.class);
	}
	
	/**
	 * This method sets the user's configuration file path
	 */
	private void setUserConfigurationPath() {
		PropertiesManager pm = PropertiesManager.getInstance();
		Path appPath = Paths.get(WDUtilities.getAppPath());
		Path userConfigPath = Paths.get(appPath.toString());
		userConfigPath = userConfigPath.resolve(pm.getProperty("app.user.conf.path"));
		// Setting the user's configuration file path 
		LOG.info("Setting user's configuration file path to " + userConfigPath.toString());
		WDUtilities.setUserConfigurationFilePath(userConfigPath.toString());		
	}

	public static PreferencesManager getInstance() {
		if (instance == null) {
			synchronized (PreferencesManager.class) {
				if (instance == null) {
					instance = new PreferencesManager();
					
				}
			}
		}
		return instance;
	}
	
	/**
	 * This method gets any preferences from user's configuration file 
	 * @param preference
	 * @return
	 */
	public String getPreference(String preference) {
	    String value = pref.get(preference, DEFAULT_VALUE);
		return value;
	}
	
	/**
	 * This class sets a user property
	 * @param property
	 * @param value
	 */
	public void setPreference(String property, String value) {
		pref.put(property, value);
	}
}
