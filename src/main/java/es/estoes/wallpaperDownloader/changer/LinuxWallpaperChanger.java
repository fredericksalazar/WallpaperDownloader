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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.window.DialogManager;

public class LinuxWallpaperChanger extends WallpaperChanger {
	
	// Constants
	private static final String PLASMA_ERROR = "org.freedesktop.DBus.Error.Failed";

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
		if (LOG.isInfoEnabled()) {
			LOG.info("Checking XDG_CURRENT_DESKTOP environment variable. Value = " + System.getenv("XDG_CURRENT_DESKTOP"));
		}
		String currentDesktop = System.getenv("XDG_CURRENT_DESKTOP");
		switch (currentDesktop) {
			case WDUtilities.DE_UNITY:
				this.setDesktopEnvironment(WDUtilities.DE_UNITY);
				break;
			case WDUtilities.DE_GNOME:
				this.setDesktopEnvironment(WDUtilities.DE_GNOME3);	
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
			case WDUtilities.DE_PANTHEON:
				this.setDesktopEnvironment(WDUtilities.DE_PANTHEON);
				break;
			default:
				if (currentDesktop.contains(WDUtilities.DE_GNOME) || 
					currentDesktop.contains(WDUtilities.DE_GNOME.toLowerCase())) {
					this.setDesktopEnvironment(WDUtilities.DE_GNOME3);
				} else if (currentDesktop.contains(WDUtilities.DE_CINNAMON)) {
					this.setDesktopEnvironment(WDUtilities.DE_CINNAMON);
				} else {
					this.setDesktopEnvironment(WDUtilities.DE_UNKNOWN);					
				}
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
			this.setKDEWallpaper(wallpaperPath);
			break;
		case WDUtilities.DE_XFCE:
			this.setXfceWallpaper(wallpaperPath);
			break;
		case WDUtilities.DE_CINNAMON:
			this.setCinnamonWallpaper(wallpaperPath);
			break;
		case WDUtilities.DE_PANTHEON:
			this.setUnityGnome3Wallpaper(wallpaperPath);
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

	/**
	 * Sets wallpaper for KDE (Plasma 5) desktop.
	 * @param wallpaperPath
	 */
	private void setKDEWallpaper(String wallpaperPath) {
      Process process;
      Boolean plasmaError = Boolean.FALSE;
      try {
    	  process = Runtime.getRuntime().exec("/bin/sh " + WDUtilities.getAppPath() + WDUtilities.URL_SLASH + WDUtilities.PLASMA_SCRIPT + " " +  wallpaperPath);
          
    	  BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	  BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

    	  // Read the output from the command
    	  String processOutput = null;
    	  while ((processOutput = stdInput.readLine()) != null) {
        	  if (LOG.isInfoEnabled()) {
        		  LOG.info(processOutput);
        	  }
        	  if (processOutput.contains(PLASMA_ERROR)) {
        		  // Probably Widgets are locked
        		  plasmaError = Boolean.TRUE;
        	  }
    	  }
			
    	  // Read any errors from the attempted command
    	  if (plasmaError) {
        	  if (LOG.isInfoEnabled()) {
        		  LOG.error("Wallpaper couldn't be changed. Widgets are probably locked");
        	  }
        	  // Information
			  DialogManager info = new DialogManager("Wallpaper couldn't be changed. Widgets must be unlocked", 2500);
			  info.openDialog();
    	  } else {
        	  if ((processOutput = stdError.readLine()) != null) {
            	  while ((processOutput = stdError.readLine()) != null) {
                	  if (LOG.isInfoEnabled()) {
                		  LOG.error(processOutput);
                	  }
            	  }    		  
        	  } else {
        		  // Everything is OK
            	  if (LOG.isInfoEnabled()) {
              		LOG.info("Wallpaper set in KDE (Plasma 5): " + wallpaperPath);  
              	  }
        	  }    		  
    	  }
          process.waitFor();
          process.destroy();
      } catch (Exception exception) {
    	  if (LOG.isInfoEnabled()) {
    		LOG.error("Wallpaper couldn't be set in KDE. Error: " + exception.getMessage());  
    	  }
      }	
	}

	/**
	 * Sets wallpaper for Unity, Gnome 3 and Budgie desktop.
	 * @param wallpaperPath
	 */
	private void setCinnamonWallpaper(String wallpaperPath) {
      Process process;
      try {
          process = Runtime.getRuntime().exec("gsettings set org.cinnamon.desktop.background picture-uri file://" + wallpaperPath);

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
    		LOG.info("Wallpaper set in Cinnamon: " + wallpaperPath);  
    	  }

          process.waitFor();
          process.destroy();
      } catch (Exception exception) {
    	  if (LOG.isInfoEnabled()) {
    		LOG.error("Wallpaper couldn't be set in Cinnamon. Error: " + exception.getMessage());  
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
			if (WDUtilities.isSnapPackage()) {
				// Snap package installation doens't allow to change wallpapers in KDE for the moment
				result = false;
			} else {
				if (this.plasmaVersionSupportsChange()) {
					result = true;
				} else {
					result = false;
				}
			}
			break;
		case WDUtilities.DE_XFCE:
			if (WDUtilities.isSnapPackage()) {
				// Snap package installation doens't allow to change wallpapers in XFCE
				result = false;
			} else {
				result = true;				
			}
			break;
		case WDUtilities.DE_CINNAMON:
			result = true;
			break;
		case WDUtilities.DE_PANTHEON:
			result = true;
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	/**
	 * Checks KDE Plasma version and detects if it has support for changing wallpaper from command line.
	 * Only KDE Plasma version 5.8 or greater supports wallpaper changer functionality from command line
	 * @return boolean
	 */
	private boolean plasmaVersionSupportsChange() {
		Process process;
		boolean result = false;
		try {
			process = Runtime.getRuntime().exec("plasmashell --version");
		  
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
			// Read the output from the command
			String processOutput = null;
	    	while ((processOutput = stdInput.readLine()) != null) {
	    		String[] plasmaVersion = processOutput.split("\\.");
	    		Integer firstNumberVersion = Integer.valueOf(plasmaVersion[0].substring(plasmaVersion[0].length() - 1));
	    		Integer secondNumberVersion = Integer.valueOf(plasmaVersion[1]);
	    		if (firstNumberVersion > 5) {
	    			result = true;
	    		} else {
	    			if (secondNumberVersion > 7) {
	    				result = true;
	    			}
	    		}
	    	}
		} catch (Exception exception) {
			if (LOG.isInfoEnabled()) {
				LOG.error("Error checking KDE Plasma version: " + exception.getMessage());  
		  	}
		}	
		
		return result;
	}
}