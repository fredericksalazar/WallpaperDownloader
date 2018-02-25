/**
 * Copyright 2016-2018 Eloy García Almadén <eloy.garcia.pca@gmail.com>
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

package es.estoes.wallpaperDownloader.provider;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import es.estoes.wallpaperDownloader.exception.ProviderException;
import es.estoes.wallpaperDownloader.harvest.Harvester;
import es.estoes.wallpaperDownloader.util.PropertiesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.window.WallpaperDownloader;

public class BingProvider extends Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(BingProvider.class);

	// Methods
	/**
	 * Constructor
	 */
	public BingProvider () {
		super();
		PropertiesManager pm = PropertiesManager.getInstance();
		this.baseURL = pm.getProperty("provider.bing.baseurl");
	}
	
	public void getWallpaper() throws ProviderException {
		String completeURL = this.composeCompleteURL();
		// Set keywords done to true because this provider only has one daily wallpaper and
		// it is not affected by keywords typed by the user
		this.setAreKeywordsDone(true);
		try {
			checkAndPrepareDownloadDirectory();	
			if (LOG.isInfoEnabled()) {
				LOG.info("Downloading Bing daily wallpaper");
				
			}
			// 1.- Getting HTML document (New method including userAgent and other options)
			Document doc = Jsoup.connect(completeURL).userAgent("Mozilla").get();
			// 2.- Getting all images
			Elements thumbnails = doc.getElementsByTag("link");
			// Removing first element because it is not an image
			thumbnails.remove(0);
			// 3.- Getting a wallpaper which is not already stored in the filesystem
			for (Element thumbnail : thumbnails) {
				if (WallpaperDownloader.harvester.getStatus().equals(Harvester.STATUS_ENABLED)) {
					String thumbnailURL = thumbnail.text();
					if (thumbnailURL != null && !thumbnailURL.isEmpty()) {
						// Composing wallpaper URL
						String wallpaperURL = "http://www.bing.com" + thumbnailURL;
						int index = wallpaperURL.lastIndexOf(WDUtilities.URL_SLASH);
						// Obtaining base URL without name and resolution
						String wallpaperURLNoName = wallpaperURL.substring(0, index + 1);
						String wallpaperOriginalNameWithResolution = wallpaperURL.substring(index + 1);
						// Obtaining wallpaper's extension
						index = wallpaperURL.lastIndexOf(WDUtilities.PERIOD);
						String extension = wallpaperURL.substring(index + 1);
						// Obtaining wallpaper's name
						index = wallpaperOriginalNameWithResolution.lastIndexOf(WDUtilities.UNDERSCORE);
						String wallpaperOriginalName = wallpaperOriginalNameWithResolution.substring(0, index);
						String wallpaperName = WDUtilities.WD_PREFIX + wallpaperOriginalName + WDUtilities.PERIOD + extension;
						String wallpaperNameFavorite = WDUtilities.WD_FAVORITE_PREFIX + wallpaperOriginalName + WDUtilities.PERIOD + extension;
						// Storing the image. It is necessary to download the remote file
						File wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
						File wallpaperFavorite = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperNameFavorite);
						if (!wallpaper.exists() && !wallpaperFavorite.exists() && !WDUtilities.isWallpaperBlacklisted(wallpaperName) && !WDUtilities.isWallpaperBlacklisted(wallpaperNameFavorite)) {
							// Storing the image. It is necessary to download the remote file
							boolean isWallpaperSuccessfullyStored = false;
							// Checking download policy
							// 0 -> Download any wallpaper and keep the original resolution
							// 1 -> Download any wallpaper and resize it (if it is bigger) to the resolution defined
							// 2 -> Download only wallpapers with the resolution set by the user
							switch (this.downloadPolicy) {
							case "0":
								wallpaperURL = wallpaperURLNoName + wallpaperOriginalNameWithResolution;
								isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
								break;
							case "1":
								String[] userResolution = this.resolution.split("x");
								wallpaperURL = wallpaperURLNoName + wallpaperOriginalNameWithResolution;
								isWallpaperSuccessfullyStored = storeAndResizeRemoteFile(wallpaper, wallpaperURL, 
										Integer.valueOf(userResolution[0]), 
										Integer.valueOf(userResolution[1]));
								break;
							case "2":
								wallpaperURL = wallpaperURLNoName + wallpaperOriginalName + WDUtilities.UNDERSCORE + resolution + WDUtilities.PERIOD + extension;
								isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
								break;
							default:
								break;
							}
							
							if (!isWallpaperSuccessfullyStored) {
								if (LOG.isInfoEnabled()) {
									LOG.info("Error trying to store wallpaper " + wallpaperURL + ". Skipping...");							
								}
							} else {
								LOG.info("Wallpaper " + wallpaper.getName() + " successfully stored");
								LOG.info("Refreshing space occupied progress bar...");
								WallpaperDownloader.refreshProgressBar();
								WallpaperDownloader.refreshJScrollPane();
								// Exit the process because one wallpaper was downloaded successfully
								break;
							}
						} else {
							LOG.info("Wallpaper " + wallpaper.getName() + " is already stored or blacklisted. Skipping...");
						}
					}
				} else {
					// Harvester is disabled so provider stops getting wallpapers
					if (LOG.isInfoEnabled()) {
						LOG.info("Harvesting process has been disabled. Stopping provider " + this.getClass().getName());
					}
					break;					
				}
			}
		} catch (IOException e) {
			throw new ProviderException("There was a problem downloading a wallpaper. Complete URL -> " + completeURL + ". Message: " + e.getMessage());
		} catch (ProviderException pe) {
			throw pe;
		}
	}
		
	private String composeCompleteURL() {
		// Getting locale and composing complete URL
		Locale currentLocale = Locale.getDefault();
		return this.baseURL + WDUtilities.AND + "mkt" + WDUtilities.EQUAL + currentLocale.getLanguage() + WDUtilities.DASH + currentLocale.getCountry();
	}

}
