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
			randomWallpapers.add(randomWallpaper);
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
