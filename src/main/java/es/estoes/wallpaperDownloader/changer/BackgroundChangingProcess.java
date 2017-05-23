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

import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

/**
* This class implements SwingWorker abstract class. It is used to execute a independent 
* Thread from the main one (Event Dispatch Thread) which renders the GUI, listens to the
* events produced by keyboard and elements of the GUI, etc...
* The Thread created by SwingWorker class is a Thread in which a heavy process can be 
* executed in background and return control to EDT until it finishes
* More information: http://chuwiki.chuidiang.org/index.php?title=Ejemplo_sencillo_con_SwingWorker
 */
public class BackgroundChangingProcess extends SwingWorker<Void, Void> {

	// Constants
	private static final Logger LOG = Logger.getLogger(BackgroundChangingProcess.class);
	
	// Attributes
	private Long timer;
	private Boolean changeWallpaper;
	
	// Getters & Setters
	/**
	 * Gets changeWallpaper.
	 * @return changeWallpaper
	 */
	public Boolean getChangeWallpaper() {
		return this.changeWallpaper;
	}

	/**
	 * Sets changeWallpaper.
	 * @param changeWallpaper changeWallpaper
	 */
	public void setChangeWallpaper(Boolean changeWallpaper) {
		this.changeWallpaper = changeWallpaper;
	}	

	// Methods
	/**
	 * This method executes the background process and returns control to EDT.
	 * @return 
	 */
	@Override
	protected Void doInBackground() throws Exception {
		PreferencesManager prefm = PreferencesManager.getInstance();
		this.setChangeWallpaper(Boolean.TRUE);
		// Getting timer
		switch (new Integer(prefm.getPreference("application-changer"))) {
		case 0: this.setChangeWallpaper(Boolean.FALSE);
				if (LOG.isInfoEnabled()) {
					LOG.info("Automated wallpaper changer process is OFF");
				}
		case 1: this.timer = Long.valueOf(60000);
				break;
		case 2: this.timer = Long.valueOf(300000);
				break;
		case 3: this.timer = Long.valueOf(600000);
				break;
		case 4: this.timer = Long.valueOf(1800000);
				break;
		case 5: this.timer = Long.valueOf(3600000);
			break;
		}
		if (LOG.isInfoEnabled() && this.getChangeWallpaper()) {
			LOG.info("Timer set every " + ((this.timer/1000)/60) + " min");
		}
		// 1.- Getting random wallpaper
		// 2.- Set wallpaper
		// 3.- Starting again after timer is consumed
		while (this.getChangeWallpaper()) {
			WDUtilities.getWallpaperChanger().setRandomWallpaper();
			Thread.sleep(this.timer);
		}
		
		return null;
	}
}
