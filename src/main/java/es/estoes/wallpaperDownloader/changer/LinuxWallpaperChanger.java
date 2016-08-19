package es.estoes.wallpaperDownloader.changer;

import es.estoes.wallpaperDownloader.util.WDUtilities;

public class LinuxWallpaperChanger extends WallpaperChanger {
	
	// Constants

	// Attributes
	private String desktopEnvironment;

	// Getters & Setters
	public String getDesktopEnvironment() {
		return desktopEnvironment;
	}

	public void setDesktopEnvironment(String desktopEnvironment) {
		this.desktopEnvironment = desktopEnvironment;
	}
	
	// Methods
	/**
	 * Constructor
	 */
	public LinuxWallpaperChanger () {
		super();
		switch (System.getenv("XDG_CURRENT_DESKTOP")) {
			case WDUtilities.DE_UNITY:
				this.setDesktopEnvironment(WDUtilities.DE_UNITY);
				break;
			case WDUtilities.DE_GNOME:
				this.setDesktopEnvironment(WDUtilities.DE_GNOME);
				break;
			case WDUtilities.DE_KDE:
				this.setDesktopEnvironment(WDUtilities.DE_KDE);
				break;
			default:
				this.setDesktopEnvironment(WDUtilities.DE_UNKNOWN);
				break;
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("Desktop environment detected: " + this.getDesktopEnvironment());
		}
	}

	@Override
	public void setWallpaper(String wallpaperPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWallpaperChangeable() {
		boolean result;
		switch (this.getDesktopEnvironment()) {
		case WDUtilities.DE_UNITY:
			result = true;
			break;
		case WDUtilities.DE_GNOME:
			result = true;
			break;
		case WDUtilities.DE_KDE:
			result = false;
			break;
		default:
			result = false;
			break;
		}
		return result;
	}
}
