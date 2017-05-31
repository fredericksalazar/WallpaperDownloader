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

package es.estoes.wallpaperDownloader.harvest;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.provider.*;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

/**
 * This class uses the Singleton principle and design pattern. It can only be instantiated 
 * one time during the execution of the application and it gathers all the methods related
 * to the harvester which will find, download and store the wallpapers. It will be 
 * a Provider aggregator. It implements Factory Method design pattern at initializeProviders
 * method. 
 *  
 * @author egarcia
 *
 */
public class Harvester {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(Harvester.class);
	private static volatile Harvester instance;
	public static final String STATUS_DISABLED = "DISABLED";
	public static final String STATUS_ENABLED = "ENABLED";
	
	// Attributes
	private BackgroundHarvestingProcess backgroundHarvestingProcess = null;
	private LinkedList<Provider> providers = null;
	public String status;
	
	// Getters & Setters	
	/**
	 * Gets status.
	 * status will be DISABLED when no providers are defined
	 * @return status
	 */
	public String getStatus() {
		return this.status;
	}
	
	// Methods
	/**
	 * Constructor
	 */
	private Harvester () {
		initializeProviders();
		this.status = STATUS_ENABLED;
	}
	
	private void initializeProviders() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Initializing providers...");
		}
		PreferencesManager prefm = PreferencesManager.getInstance();
		this.providers = new LinkedList<Provider>();
		// Reading user preferences
		// -----------------------------------------------
		// Wallhaven.cc
		// -----------------------------------------------
		String wallhavenEnable = prefm.getPreference("provider-wallhaven");
		if (wallhavenEnable.equals(WDUtilities.APP_YES)) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Wallhaven provider added");
			}
			this.providers.add(new WallhavenProvider());	// Factory method
		}				
		// -----------------------------------------------
		// Devianart
		// -----------------------------------------------
		String devianartEnable = prefm.getPreference("provider-devianart");
		if (devianartEnable.equals(WDUtilities.APP_YES)) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Devianart provider added");
			}
			this.providers.add(new DevianartProvider());	// Factory method
		}				
		// -----------------------------------------------
		// Bing
		// -----------------------------------------------
		String bingEnable = prefm.getPreference("provider-bing");
		if (bingEnable.equals(WDUtilities.APP_YES)) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Bing provider added");
			}
			this.providers.add(new BingProvider());	// Factory method
		}				
		// -----------------------------------------------
		// Social Wallpapering
		// -----------------------------------------------
		String socialWallpaperingEnable = prefm.getPreference("provider-socialWallpapering");
		if (socialWallpaperingEnable.equals(WDUtilities.APP_YES)) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Social Wallpapering provider added");
			}
			this.providers.add(new SocialWallpaperingProvider());	// Factory method
		}
		// -----------------------------------------------
		// WallpaperFusion
		// -----------------------------------------------
		String wallpaperFusionEnable = prefm.getPreference("provider-wallpaperFusion");
		if (wallpaperFusionEnable.equals(WDUtilities.APP_YES)) {
			if (LOG.isInfoEnabled()) {
				LOG.info("WallpaperFusion provider added");
			}
			this.providers.add(new WallpaperFusionProvider());	// Factory method
		}
	}
	
	public static Harvester getInstance() {
		if (instance == null) {
			synchronized (Harvester.class) {
				if (instance == null) {
					instance = new Harvester();
					
				}
			}
		}
		return instance;
	}
	
	/**
	 * This method stops the harvesting process
	 */
	public void stop () {
		if (LOG.isInfoEnabled()) {
			LOG.info("Stoping harvesting process...");
		}
		providers = null;
		status = STATUS_DISABLED;
		if (backgroundHarvestingProcess != null) {
			backgroundHarvestingProcess.cancel(true);			
			backgroundHarvestingProcess = null;
		}
	}
	
	/**
	 * This method starts the harvesting process
	 */
	public void start () {
		PreferencesManager prefm = PreferencesManager.getInstance();
		if (LOG.isInfoEnabled()) {
			LOG.info("Starting harvesting process...");
		}
		
		if (providers == null) {
			initializeProviders();
		}
		
		if (providers.size() > 0) {
			status = STATUS_ENABLED;
			if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_YES)) {
				if (LOG.isInfoEnabled()) {
					LOG.info("Providers configured and downloading process enabled. Starting to download...");
				}
				backgroundHarvestingProcess = new BackgroundHarvestingProcess();
				backgroundHarvestingProcess.setProviders(providers);
				backgroundHarvestingProcess.execute();								
			} else {
				if (LOG.isInfoEnabled()) {
					LOG.info("Downloading process disabled by the user...");
				}
			}
		} else {
			status = STATUS_DISABLED;
			if (LOG.isInfoEnabled()) {
				LOG.info("No providers configured. Nothing to do.");
			}
		}
	}
}