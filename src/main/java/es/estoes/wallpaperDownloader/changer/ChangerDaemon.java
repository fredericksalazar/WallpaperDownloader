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

package es.estoes.wallpaperDownloader.changer;

import org.apache.log4j.Logger;

/**
 * This class uses the Singleton principle and design pattern. It can only be instantiated 
 * one time during the execution of the application and it gathers all the methods related
 * to the changer daemon which will change the wallpaper automatically every X minutes. 
 *  
 * @author egarcia
 *
 */
public class ChangerDaemon {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(ChangerDaemon.class);
	private static volatile ChangerDaemon instance;
	
	// Attributes
	private BackgroundChangingProcess backgroundChangingProcess = null;
		
	// Methods
	/**
	 * Constructor
	 */
	private ChangerDaemon () {
	}

	public static ChangerDaemon getInstance() {
		if (instance == null) {
			synchronized (ChangerDaemon.class) {
				if (instance == null) {
					instance = new ChangerDaemon();
					
				}
			}
		}
		return instance;
	}
		
	/**
	 * This method stops the automatically wallpaper changing process.
	 */
	public void stop () {
		LOG.info("Stopping automatically changing process...");
		if (backgroundChangingProcess != null) {
			backgroundChangingProcess.setChangeWallpaper(Boolean.FALSE);
			backgroundChangingProcess.cancel(true);			
			backgroundChangingProcess = null;
		}
	}
	
	/**
	 * This method starts the automatically wallpaper changing process.
	 */
	public void start () {
		if (LOG.isInfoEnabled()) {
			LOG.info("Starting automatically changing process..." );
		}
		backgroundChangingProcess = new BackgroundChangingProcess();
		backgroundChangingProcess.execute();								
	}
}
