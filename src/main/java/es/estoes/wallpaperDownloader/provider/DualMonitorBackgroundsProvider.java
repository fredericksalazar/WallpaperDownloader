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

public class DualMonitorBackgroundsProvider extends Provider {
	
	/**
	 * Constants.
	 */
	private static final Logger LOG = Logger.getLogger(DualMonitorBackgroundsProvider.class);

	/**
	 * Attributes.
	 */

	// Sorting
	private String sorting;
	
	// Page
	private int page;

	// Id for div element which will have all the thumbnails
	private String thumbnailsDivId;
	
	/**
	 * Getters & Setters.
	 */
	
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

	/**
	 * Methods.
	 */
	
	/**
	 * Constructor.
	 */
	public DualMonitorBackgroundsProvider () {
		super();
		PreferencesManager prefm = PreferencesManager.getInstance();

		PropertiesManager pm = PropertiesManager.getInstance();
		this.baseURL = pm.getProperty("provider.dualMonitorBackgrounds.baseurl");
		switch (new Integer(prefm.getPreference("provider-dualMonitorBackgrounds-search-type"))) {
			case 0: 
				this.sorting = WDUtilities.QM + "sortby=date";
				this.thumbnailsDivId = "latest";
				break;
			case 1: 
				this.sorting = WDUtilities.QM +"sortby=rating";
				this.thumbnailsDivId = "toprated";
				break;
			case 2: 
				this.sorting = WDUtilities.QM + "sortby=popularity";
				this.thumbnailsDivId = "popular";
				break;
			case 3: 
				this.sorting = "random/" + WDUtilities.QM;
				this.thumbnailsDivId = "random";
				break;
		}
		
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
				LOG.info("Downloading wallpaper with keyword -> " + activeKeyword);
				
			}
	
			while (!wallpaperFound) {
				// 1.- Getting HTML document (New method including userAgent and other options)
				Document doc = Jsoup.connect(completeURL).header("Accept-Encoding", "gzip, deflate")
						.userAgent("Mozilla")
						.maxBodySize(0)
						.timeout(600000)
						.get();
				// 2.- Getting all thumbnails
				Elements thumbnails = doc.select("div#" + this.thumbnailsDivId + " a");
				if (!thumbnails.isEmpty()) {
					// 3.- Getting a wallpaper which is not already stored in the filesystem
					for (Element thumbnail : thumbnails) {
						if (WallpaperDownloader.harvester.getStatus().equals(Harvester.STATUS_ENABLED)) {
							String thumbnailText = thumbnail.text().trim();
							String thumbnailTitle = thumbnail.attr("title").trim();
							String thumbnailURL = "";
							if (thumbnailText.equals(thumbnailTitle)) {
								if (thumbnail.attr("href").contains(".php")) {
									thumbnailURL = this.baseURL + thumbnail.attr("href").substring(1);
								}
							}
							
							if (!thumbnailURL.isEmpty()) {
								// Retrieves the entire document for this link
								Document wallpaperDoc = Jsoup.connect(thumbnailURL).userAgent("Mozilla").get();
								// Retrieves the source for the full image
								Elements imageDiv = wallpaperDoc.select("div#SingleImageContainer a");
								// The first a element is the main one
								String wallpaperURL = this.baseURL + imageDiv.get(0).attr("href").substring(1);
								int index = wallpaperURL.lastIndexOf(WDUtilities.URL_SLASH);
								// Obtaining wallpaper's name (string after the last slash)
								String wallpaperName = WDUtilities.WD_PREFIX + wallpaperURL.substring(index + 1);
								String wallpaperNameFavorite = WDUtilities.WD_FAVORITE_PREFIX + wallpaperURL.substring(index + 1);
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
										if (LOG.isInfoEnabled()) {
											LOG.info("Wallpaper " + wallpaper.getName() + " successfully stored");
											LOG.info("Refreshing space occupied progress bar...");
										}
										WallpaperDownloader.refreshProgressBar();
										WallpaperDownloader.refreshJScrollPane();
										wallpaperFound = Boolean.TRUE;
										// Exit the process because one wallpaper was downloaded successfully
										break;
									}
									
								} else {
									if (LOG.isInfoEnabled()) {
										LOG.info("Wallpaper " + wallpaper.getName() + " is already stored or blacklisted. Skipping...");
									}
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
				}  else {
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
		String completeUrl = this.baseURL;
		// If activeKeyword is empty, the search operation will be done within the whole repository 
		if (activeKeyword.equals(PreferencesManager.DEFAULT_VALUE)) {
			if (this.thumbnailsDivId != "random") {
				completeUrl = completeUrl + "index.php";
			}
			// Resolution
			// 0 -> Download any wallpaper and keep the original resolution
			// 1 -> Download any wallpaper and resize it (if it is bigger) to the resolution defined
			// 2 -> Download only wallpapers with the resolution set by the user
			switch (this.downloadPolicy) {
			case "0":
				completeUrl = completeUrl + this.sorting;
				break;
			case "1":
				completeUrl = completeUrl + this.sorting;
				break;
			case "2":
				String[] userResolution = this.resolution.split("x");
				String resolutionString = "userwidth" + WDUtilities.EQUAL + userResolution[0] + WDUtilities.AND + 
						"userheight" + WDUtilities.EQUAL + userResolution[1];
				completeUrl = completeUrl + WDUtilities.QM + resolutionString;
				break;
			default:
				break;
			}
			
			// Adding page
			completeUrl = completeUrl + WDUtilities.AND + "latestImagesPage" + WDUtilities.EQUAL + this.getPage();
		} else {
			this.activeKeyword = this.activeKeyword.replace("\"", "");
			this.activeKeyword = this.activeKeyword.replace(" ", "+AND+");
				// http://www.dualmonitorbackgrounds.com/page/search/marvel/2/?userwidth=3200&userheight=1200
				// http://www.dualmonitorbackgrounds.com/page/search/star+AND+wars/2
			completeUrl = completeUrl + "/page/search/" + this.activeKeyword + WDUtilities.URL_SLASH + this.getPage();

			// Resolution
			// 0 -> Download any wallpaper and keep the original resolution
			// 1 -> Download any wallpaper and resize it (if it is bigger) to the resolution defined
			// 2 -> Download only wallpapers with the resolution set by the user
			switch (this.downloadPolicy) {
			case "2":
				String[] userResolution = this.resolution.split("x");
				String resolutionString = "userwidth" + WDUtilities.EQUAL + userResolution[0] + WDUtilities.AND + 
						"userheight" + WDUtilities.EQUAL + userResolution[1];
				completeUrl = completeUrl + WDUtilities.URL_SLASH + WDUtilities.QM + resolutionString;
				break;
			default:
				break;
			}
			
			// Id for div element which contains all thumbnails
			this.thumbnailsDivId = "albumsSearch";
		}
		LOG.info("Searching in " + completeUrl);
		return completeUrl;
	}
}
