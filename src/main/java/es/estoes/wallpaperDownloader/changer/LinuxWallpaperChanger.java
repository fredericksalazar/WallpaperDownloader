package es.estoes.wallpaperDownloader.changer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
					this.setDesktopEnvironment(WDUtilities.DE_GNOME3);	
				} else {
					this.setDesktopEnvironment(WDUtilities.DE_GNOME2);
				}
				break;
			case WDUtilities.DE_MATE:
				this.setDesktopEnvironment(WDUtilities.DE_MATE);
				break;
			case WDUtilities.DE_KDE:
				this.setDesktopEnvironment(WDUtilities.DE_KDE);
				break;
			case WDUtilities.DE_XFCE:
				this.setDesktopEnvironment(WDUtilities.DE_XFCE);
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
		case WDUtilities.DE_MATE:
			this.setMateWallpaper(wallpaperPath);
			break;
		case WDUtilities.DE_KDE:
			break;
		case WDUtilities.DE_XFCE:
			this.setXfceWallpaper(wallpaperPath);
			break;
		default:
			break;
		}
	}

	/**
	 * Sets wallpaper for Mate desktop.
	 * @param wallpaperPath
	 */
	private void setMateWallpaper(String wallpaperPath) {
	      Process process;
	      try {
	          process = Runtime.getRuntime().exec("gsettings set org.mate.background picture-filename " + wallpaperPath);

	    	  BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	  BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

	    	  // Read the output from the command
	    	  String processOutput = null;
	    	  while ((processOutput = stdInput.readLine()) != null) {
	        	  if (LOG.isInfoEnabled()) {
	        		  LOG.info(processOutput);
	        	  }
	    	  }
				
	    	  // Read any errors from the attempted command
	    	  while ((processOutput = stdError.readLine()) != null) {
	        	  if (LOG.isInfoEnabled()) {
	        		  LOG.error(processOutput);
	        	  }
	    	  }

	    	  if (LOG.isInfoEnabled()) {
	    		LOG.info("Wallpaper set in Mate: " + wallpaperPath);  
	    	  }

	          process.waitFor();
	          process.destroy();
	      } catch (Exception exception) {
	    	  if (LOG.isInfoEnabled()) {
	    		LOG.error("Wallpaper couldn't be set in Mate. Error: " + exception.getMessage());  
	    	  }
	      }	
	}

	/**
	 * Sets wallpaper for Unity and Gnome 3 desktop.
	 * @param wallpaperPath
	 */
	private void setUnityGnome3Wallpaper(String wallpaperPath) {
      Process process;
      try {
          process = Runtime.getRuntime().exec("gsettings set org.gnome.desktop.background picture-uri file://" + wallpaperPath);

    	  BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	  BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

    	  // Read the output from the command
    	  String processOutput = null;
    	  while ((processOutput = stdInput.readLine()) != null) {
        	  if (LOG.isInfoEnabled()) {
        		  LOG.info(processOutput);
        	  }
    	  }
			
    	  // Read any errors from the attempted command
    	  while ((processOutput = stdError.readLine()) != null) {
        	  if (LOG.isInfoEnabled()) {
        		  LOG.error(processOutput);
        	  }
    	  }

    	  if (LOG.isInfoEnabled()) {
    		LOG.info("Wallpaper set in Unity or Gnome 3: " + wallpaperPath);  
    	  }

          process.waitFor();
          process.destroy();
      } catch (Exception exception) {
    	  if (LOG.isInfoEnabled()) {
    		LOG.error("Wallpaper couldn't be set in Unity or Gnome 3. Error: " + exception.getMessage());  
    	  }
      }	
	}

	/**
	 * Sets wallpaper for XFCE desktop.
	 * @param wallpaperPath
	 */
	private void setXfceWallpaper(String wallpaperPath) {
	      Process process;
	      try {
	          process = Runtime.getRuntime().exec("xfconf-query --channel xfce4-desktop --property /backdrop/screen0/monitor0/workspace0/last-image --set " + wallpaperPath);

	    	  BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	  BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

	    	  // Read the output from the command
	    	  String processOutput = null;
	    	  while ((processOutput = stdInput.readLine()) != null) {
	        	  if (LOG.isInfoEnabled()) {
	        		  LOG.info(processOutput);
	        	  }
	    	  }
				
	    	  // Read any errors from the attempted command
	    	  while ((processOutput = stdError.readLine()) != null) {
	        	  if (LOG.isInfoEnabled()) {
	        		  LOG.error(processOutput);
	        	  }
	    	  }

	    	  if (LOG.isInfoEnabled()) {
	    		LOG.info("Wallpaper set in XFCE: " + wallpaperPath);  
	    	  }

	          process.waitFor();
	          process.destroy();
	      } catch (Exception exception) {
	    	  if (LOG.isInfoEnabled()) {
	    		LOG.error("Wallpaper couldn't be set in XFCE. Error: " + exception.getMessage());  
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
		case WDUtilities.DE_MATE:
			result = true;
			break;
		case WDUtilities.DE_KDE:
			result = false;
			break;
		case WDUtilities.DE_XFCE:
			if (WDUtilities.isSnapPackage()) {
				// Snap package installation doens't allow to change wallpapers in XFCE
				result = false;
			} else {
				result = true;				
			}
			break;
		default:
			result = false;
			break;
		}
		return result;
	}
}