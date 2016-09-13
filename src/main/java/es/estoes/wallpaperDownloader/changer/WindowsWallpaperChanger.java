package es.estoes.wallpaperDownloader.changer;

import es.estoes.wallpaperDownloader.jna.User32;

public class WindowsWallpaperChanger extends WallpaperChanger {
	
	// Constants

	// Attributes

	// Getters & Setters
	
	// Methods
	/**
	 * Constructor
	 */
	public WindowsWallpaperChanger () {
		super();
	}

	@Override
	public void setWallpaper(String wallpaperPath) {
		User32.INSTANCE.SystemParametersInfo(0x0014, 0, wallpaperPath , 1);
	}

	@Override
	public boolean isWallpaperChangeable() {
		return true;
	}
}
