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
				if (System.getenv("GDMSESSION").equals(WDUtilities.GDM_SESSION_GNOME) ||
					System.getenv("GDMSESSION").equals(WDUtilities.GDM_SESSION_GNOME_SHELL) || 
					System.getenv("GDMSESSION").equals(WDUtilities.GDM_SESSION_GNOME_CLASSIC) ||
					System.getenv("GDMSESSION").equals(WDUtilities.GDM_SESSION_GNOME_FALLBACK)) {
					// TODO: Comprobar esto porque no está detectando bien GNOME3
					this.setDesktopEnvironment(WDUtilities.DE_GNOME3);	
				} else {
					this.setDesktopEnvironment(WDUtilities.DE_GNOME2);
				}
				
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
		switch (this.getDesktopEnvironment()) {
		case WDUtilities.DE_UNITY:
			this.setUnityGnome3Wallpaper(wallpaperPath);
			break;
		case WDUtilities.DE_GNOME3:
			this.setUnityGnome3Wallpaper(wallpaperPath);
			break;
		case WDUtilities.DE_KDE:
			break;
		default:
			break;
		}
	}

	/**
	 * Sets wallpaper for Unity and Gnome 3 desktop
	 * @param wallpaperPath
	 */
	private void setUnityGnome3Wallpaper(String wallpaperPath) {
      Process process;
      try {
          process = Runtime.getRuntime().exec("gsettings set org.gnome.desktop.background picture-uri file://" + wallpaperPath);
          process.waitFor();
          process.destroy();
    	  if (LOG.isInfoEnabled()) {
    		LOG.error("Wallpaper set in Unity or Gnome 3: " + wallpaperPath);  
    	  }
      } catch (Exception exception) {
    	  if (LOG.isInfoEnabled()) {
    		LOG.error("Wallpaper couldn't be set in Unity or Gnome 3. Error: " + exception.getMessage());  
    	  }
      }	
	}

	@Override
	public boolean isWallpaperChangeable() {
		boolean result;
		switch (this.getDesktopEnvironment()) {
		case WDUtilities.DE_UNITY:
			result = true;
			break;
		case WDUtilities.DE_GNOME3:
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