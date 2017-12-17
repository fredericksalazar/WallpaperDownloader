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

package es.estoes.wallpaperDownloader.changer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

/**
 * Abstract class which defines a wallpaper changer.
 * @author egarcia
 *
 */
public abstract class WallpaperChanger {
	
	// Constants
	protected static final Logger LOG = Logger.getLogger(WallpaperChanger.class);
	
	// Attributes
	
	// Getters & Setters

	// Methods
	/**
	 * Constructor
	 */
	public WallpaperChanger () {
		if (LOG.isInfoEnabled()) {
			LOG.info("Instanciating wallpaper changer...");
		}
	}
	
	/**
	 * Sets Desktop's wallpaper.
	 * @param wallpaperPath path of the wallpaper to be set
	 */
	public abstract void setWallpaper(String wallpaperPath);

	/**
	 * Checks if the desktop environment allows to change the wallpaper.
	 * @return boolean
	 */
	public abstract boolean isWallpaperChangeable();

	/**
	 * Sets a random wallpaper.
	 */
	public void setRandomWallpaper() {
		PreferencesManager prefm = PreferencesManager.getInstance();
		List<File> randomWallpapers = new ArrayList<File>();
		String changerFoldersProperty = prefm.getPreference("application-changer-folder");
		String[] changerFolders = changerFoldersProperty.split(";");
		for (int i = 0; i < changerFolders.length; i ++) {
			File randomWallpaper = WDUtilities.pickRandomImage(changerFolders[i]);
			if (randomWallpaper != null) {
				randomWallpapers.add(randomWallpaper);				
			}
		}
		if (randomWallpapers.size() > 0) {
			Random generator = new Random();
			int index = generator.nextInt(randomWallpapers.size());
			this.setWallpaper(randomWallpapers.get(index).getAbsolutePath());
			if (LOG.isInfoEnabled()) {
				LOG.info("Setting random wallpaper: " + randomWallpapers.get(index).getAbsolutePath());
			}
		}
	}

}
