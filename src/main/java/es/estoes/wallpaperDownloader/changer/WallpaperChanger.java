package es.estoes.wallpaperDownloader.changer;

import java.io.File;

import org.apache.log4j.Logger;

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
		File randomWallpaper = WDUtilities.pickRandomFile(Boolean.FALSE);
		this.setWallpaper(randomWallpaper.getAbsolutePath());
		if (LOG.isInfoEnabled()) {
			LOG.info("Setting random wallpaper: " + randomWallpaper.getAbsolutePath());
		}
		randomWallpaper = null;
	}

}
