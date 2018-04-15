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
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import es.estoes.wallpaperDownloader.exception.ProviderException;
import es.estoes.wallpaperDownloader.harvest.Harvester;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.PropertiesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.window.WallpaperDownloader;

public class SocialWallpaperingProvider extends Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(SocialWallpaperingProvider.class);

	// Attributes
	private int page;
	
	// Getters & Setters
	
	/**
	 * Gets page.
	 * @return page
	 */
	public int getPage() {
		return this.page;
	}

	/**
	 * Sets page.
	 * @param page page
	 */
	public void setPage(int page) {
		this.page = page;
	}

	// Methods
	/**
	 * Constructor
	 */
	public SocialWallpaperingProvider () {
		super();
		PropertiesManager pm = PropertiesManager.getInstance();
		this.baseURL = pm.getProperty("provider.socialWallpapering.baseurl");
		
		// Initial page
		this.setPage(1);
	}
	
	public void getWallpaper() throws ProviderException {
		Boolean wallpaperFound = Boolean.FALSE;
		this.obtainActiveKeyword();
		String completeURL = this.composeCompleteURL();
		try {
			this.checkAndPrepareDownloadDirectory();	
			if (LOG.isInfoEnabled()) {
				LOG.info("Downloading wallpaper with keyword -> " + this.activeKeyword);
			}
			while (!wallpaperFound) {
				// 1.- Getting HTML document (New method including userAgent and other options)
				Document doc = Jsoup.connect(completeURL).userAgent("Mozilla").get();
				// 2.- Getting all thumbnails. They are identified because they have 'wallpaer' classed li elements
				Elements thumbnails = doc.select("li.wallpaper");

				if (!thumbnails.isEmpty()) {
					// 3.- Getting a wallpaper which is not already stored in the filesystem
					for (Element thumbnail : thumbnails) {
						if (WallpaperDownloader.harvester.getStatus().equals(Harvester.STATUS_ENABLED)) {
							// First, it is necessary to retrieve the <a> element inside the span which contains the link to 
							// downloads page for this wallpaper
							Elements linkURLs = thumbnail.select("a");
							Element linkURL = linkURLs.first();
							// Retrieves the entire document for this link
							Document wallpaperDoc = Jsoup.connect(this.baseURL + linkURL.attr("href")).userAgent("Mozilla").get();
							// Retrieves the source for the full image
							Elements images = wallpaperDoc.getElementsByTag("img");
							if (images.size() > 1) {
								Element image = images.get(1);
								String wallpaperURL = this.baseURL + image.attr("src");
								int index = wallpaperURL.lastIndexOf(WDUtilities.URL_SLASH);
								// Obtaining wallpaper's name (string after the last slash)
								String wallpaperName = wallpaperURL.substring(index + 1);
								index = wallpaperName.lastIndexOf(WDUtilities.DASH);
								wallpaperName = WDUtilities.WD_PREFIX + wallpaperName.substring(0, index) + WDUtilities.PERIOD + "jpg";
								String wallpaperNameFavorite = WDUtilities.WD_FAVORITE_PREFIX + wallpaperName.substring(0, index) + WDUtilities.PERIOD + "jpg";
								
								File wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
								File wallpaperFavorite = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperNameFavorite);
								if (!wallpaper.exists() && !wallpaperFavorite.exists() && !WDUtilities.isWallpaperBlacklisted(wallpaperName) && !WDUtilities.isWallpaperBlacklisted(wallpaperNameFavorite)) {
									// Storing the image. It is necessary to download the remote file
									// First try: JPG format will be used
									boolean isWallpaperSuccessfullyStored = false;
									// Checking download policy
									// 0 -> Download any wallpaper and keep the original resolution
									// 1 -> Download any wallpaper and resize it (if it is bigger) to the resolution defined
									// 2 -> Download only wallpapers with the resolution set by the user
									switch (this.downloadPolicy) {
									case "0":
										isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
										break;
									case "1":
										String[] userResolution = this.resolution.split("x");
										isWallpaperSuccessfullyStored = storeAndResizeRemoteFile(wallpaper, wallpaperURL, 
												Integer.valueOf(userResolution[0]), 
												Integer.valueOf(userResolution[1]));
										break;
									case "2":
										String remoteImageResolution = getRemoteImageResolution(wallpaperURL);
									    if (this.resolution.equals(remoteImageResolution)) {
									    	// Wallpaper resolution fits the one set by the user
											isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
									    } else {
									    	isWallpaperSuccessfullyStored = false;
									    }
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
										wallpaperFound = Boolean.TRUE;
										// Exit the process because one wallpaper was downloaded successfully
										break;
									}
								} else {
									LOG.info("Wallpaper " + wallpaper.getName() + " is already stored or blacklisted. Skipping...");
								}
							} else {
								if (LOG.isInfoEnabled()) {
									LOG.info("Wallpaper with URL " + this.baseURL + linkURL.attr("href") + " has not been reviewed yet, so it can't be downloaded");
								}
							}
						} else {
							// Harvester is disabled so provider stops getting wallpapers
							if (LOG.isInfoEnabled()) {
								LOG.info("Harvesting process has been disabled. Stopping provider " + this.getClass().getName());
							}
							wallpaperFound = Boolean.TRUE;
							break;							
						}
					}
					if (!wallpaperFound) {
						// If no wallpaper is found in this page, the offset is incremented
						if (LOG.isInfoEnabled()) {
							LOG.info("No more wallpapers found in page " + this.getPage() + ". Starting to search in page " + this.getPage() + 1);
						}
						this.setPage(this.getPage() + 1);
						completeURL = composeCompleteURL();
					}
				} else {
					wallpaperFound = Boolean.TRUE;
					// Reseting offset
					this.setPage(1);
					if (LOG.isInfoEnabled()) {
						LOG.info("No more wallpapers found. Skipping...");
					}
				}
			}
		} catch (IOException e) {
			throw new ProviderException("There was a problem downloading a wallpaper. Complete URL -> " + completeURL + ". Message: " + e.getMessage());
		} catch (ProviderException pe) {
			throw pe;
		}
	}
		
	private String composeCompleteURL() {
		PreferencesManager prefm = PreferencesManager.getInstance();
		// If activeKeyword is empty or user wants to ignore keywords for this provider, 
		// the search operation will be done within the whole repository 
		String keywordString = "";
		if (!this.activeKeyword.equals(PreferencesManager.DEFAULT_VALUE) && prefm.getPreference("provider-socialWallpapering-ignore-keywords").equals(WDUtilities.APP_NO)) {
			// Replacing blank spaces with +
			this.activeKeyword = this.activeKeyword.replace(" ", "+");
			// Removing double quotes
			this.activeKeyword = this.activeKeyword.replace("\"", "");
			keywordString = "search" + WDUtilities.COLON + this.activeKeyword + WDUtilities.URL_SLASH;
		}
		if (LOG.isInfoEnabled()) {
			LOG.info(this.baseURL + "wallpapers" + WDUtilities.URL_SLASH + "page" + WDUtilities.COLON + 
					this.getPage() + WDUtilities.URL_SLASH + keywordString);		
		}
		return this.baseURL + "wallpapers" + WDUtilities.URL_SLASH + "page" + WDUtilities.COLON + 
				this.getPage() + WDUtilities.URL_SLASH + keywordString;
	}
}
