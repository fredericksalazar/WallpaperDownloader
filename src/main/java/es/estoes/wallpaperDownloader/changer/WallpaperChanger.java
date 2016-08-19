package es.estoes.wallpaperDownloader.changer;

import org.apache.log4j.Logger;

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

}
