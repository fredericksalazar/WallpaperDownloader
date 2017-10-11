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

package es.estoes.wallpaperDownloader.provider;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import es.estoes.wallpaperDownloader.exception.ProviderException;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.PropertiesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.window.WallpaperDownloader;

public class WallpaperFusionProvider extends Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(WallpaperFusionProvider.class);
	private static final String WF_DOWNLOAD_URL = "https://www.wallpaperfusion.com/Download/";
	private static final String WF_ORIGINAL_PARAMETERS = "?W=-1&H=-1";
	private static final String WF_SEARCH_URL = "https://www.wallpaperfusion.com/Ajax/BuildSearchResultsHTML/";
	private static final String WF_PREFIX = "wf-";

	// Attributes
	private String page;
	
	// Getters & Setters
	
	/**
	 * Gets page.
	 * @return page
	 */
	public String getPage() {
		return this.page;
	}

	/**
	 * Sets page.
	 * @param page page
	 */
	public void setPage(String page) {
		this.page = page;
	}

	// Methods
	/**
	 * Constructor
	 */
	public WallpaperFusionProvider () {
		super();
		PropertiesManager pm = PropertiesManager.getInstance();
		this.baseURL = pm.getProperty("provider.wallpaperFusion.baseurl");
		
		// Initial page
		this.setPage("0");
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
				Document doc = null;
				if (this.activeKeyword.equals(PreferencesManager.DEFAULT_VALUE)) {
					// No keywords defined by the user. Downloading wallpapers from the Main page
					doc = Jsoup.connect(completeURL).data("Page", this.getPage()).userAgent("Mozilla").post();
				} else {
					// Keywords defined by the user. searching wallpapers using AJAX
					doc = Jsoup.connect(WF_SEARCH_URL)
		                    .data("ReqID", this.getPage() + 3)
							.data("Page", this.getPage())
							.data("Sort", "0")
							.data("Text", this.activeKeyword)
							.data("TagShow", "")
							.data("TagHide", "")
							.data("Mon", "0")
							.data("RateAll", "0")
							.data("RateUser", "undefined")
							.data("SearchWidth", "0")
							.data("SearchWidthMod", "0")
							.data("SearchHeight", "0")
							.data("SearchHeightMod", "0")
							.data("SubmittersMode", "undefined")
							.data("ArtistsMode", "undefined")
							.data("ShowFavs", "0")
							.data("TagSearchMode", "0")
							.userAgent("Mozilla")
							.post();
				}
				// 2.- Getting all thumbnails. They are identified because they have 'wallpaer' classed li elements
				Elements thumbnails = doc.select("a.ImageThumbBorder");

				if (!thumbnails.isEmpty()) {
					// 3.- Getting a wallpaper which is not already stored in the filesystem
					for (Element thumbnail : thumbnails) {
						// First, it is necessary to retrieve href value inside a element which contains the link to 
						// downloads page for this wallpaper
						String wallpaperMainURLDownloadPage = thumbnail.attr("href");
						// Retrieves the ID
						// First, removes last slash
						wallpaperMainURLDownloadPage = wallpaperMainURLDownloadPage.substring(0, (wallpaperMainURLDownloadPage.length() - 1));
						// Gets the ID
						String wallpaperId = wallpaperMainURLDownloadPage.substring(wallpaperMainURLDownloadPage.lastIndexOf(WDUtilities.URL_SLASH) + 1);
						// Retrieves the source for the full image
						String wallpaperURL = WF_DOWNLOAD_URL + wallpaperId + WDUtilities.URL_SLASH + "?W=" + WDUtilities.getWidthResolution() + "&H=" + WDUtilities.getHeightResolution();
						String wallpaperName = WDUtilities.WD_PREFIX + WF_PREFIX + wallpaperId + WDUtilities.PERIOD + "jpg";
						String wallpaperNameFavorite = WDUtilities.WD_FAVORITE_PREFIX + WF_PREFIX + wallpaperId + WDUtilities.PERIOD + "jpg";
						// Storing the image. It is necessary to download the remote file
						File wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
						File wallpaperFavorite = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperNameFavorite);
						if (!wallpaper.exists() && !wallpaperFavorite.exists() && !WDUtilities.isWallpaperBlacklisted(wallpaperName) && !WDUtilities.isWallpaperBlacklisted(wallpaperNameFavorite)) {
							// Storing the image. It is necessary to download the remote file
							boolean isWallpaperSuccessfullyStored = false;
							String[] userResolution = this.resolution.split("x");
							// Checking download policy
							// 0 -> Download any wallpaper and keep the original resolution
							// 1 -> Download any wallpaper and resize it (if it is bigger) to the resolution defined
							// 2 -> Download only wallpapers with the resolution set by the user
							switch (this.downloadPolicy) {
							case "0":
								wallpaperURL = WF_DOWNLOAD_URL + wallpaperId + WDUtilities.URL_SLASH + WF_ORIGINAL_PARAMETERS;
								isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
								break;
							case "1":
								wallpaperURL = WF_DOWNLOAD_URL + wallpaperId + WDUtilities.URL_SLASH + WF_ORIGINAL_PARAMETERS;
								isWallpaperSuccessfullyStored = storeAndResizeRemoteFile(wallpaper, wallpaperURL, 
										Integer.valueOf(userResolution[0]), 
										Integer.valueOf(userResolution[1]));
								break;
							case "2":
								wallpaperURL = WF_DOWNLOAD_URL + wallpaperId + WDUtilities.URL_SLASH + "?W=" + Integer.valueOf(userResolution[0]) + "&H=" + Integer.valueOf(userResolution[1]);
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
								wallpaperFound = Boolean.TRUE;
								// Exit the process because one wallpaper was downloaded successfully
								break;
							}
						} else {
							LOG.info("Wallpaper " + wallpaper.getName() + " is already stored or blacklisted. Skipping...");
						}
					}
					if (!wallpaperFound) {
						// If no wallpaper is found in this page, the offset is incremented
						this.setPage(this.getPage() + 1);
						completeURL = composeCompleteURL();
					}
				} else {
					wallpaperFound = Boolean.TRUE;
					// Reseting page
					this.setPage("0");
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
		return this.baseURL;
	}
}
