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

public class WallhavenProvider extends Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(WallhavenProvider.class);

	// Attributes
	private String order;
	// Methods
	/**
	 * Constructor
	 */
	public WallhavenProvider () {
		super();
		PreferencesManager prefm = PreferencesManager.getInstance();

		PropertiesManager pm = PropertiesManager.getInstance();
		this.baseURL = pm.getProperty("provider.wallhaven.baseurl");
		switch (new Integer(prefm.getPreference("wallpaper-search-type"))) {
			case 0: this.order = "relevance";
					break;
			case 1: this.order = "date_added";
					break;
			case 2: this.order = "views";
					break;
			case 3: this.order = "favorites";
					break;
			case 4: this.order = "random";
					break;
			
		}
	}
	
	public void getWallpaper() throws ProviderException {
		this.obtainActiveKeyword();
		String completeURL = this.composeCompleteURL();
		try {
			this.checkAndPrepareDownloadDirectory();	
			if (LOG.isInfoEnabled()) {
				LOG.info("Downloading wallpaper with keyword -> " + activeKeyword);
				
			}
			// 1.- Getting HTML document (New method including userAgent and other options)
			Document doc = Jsoup.connect(completeURL).userAgent("Mozilla").get();
			// 2.- Getting all thumbnails. They are identified because they have 'lazyload' classed img elements
			Elements thumbnails = doc.select("img.lazyload");
			// 3.- Getting a wallpaper which is not already stored in the filesystem
			for (Element thumbnail : thumbnails) {
				String thumbnailURL = thumbnail.attr("data-src");
				// Replacing word 'thumb/small' by word 'full'
				String wallpaperURL = thumbnailURL.replace("thumb/small", "full");
				// Replacing word 'th-' by word 'wallhaven-'
				wallpaperURL = wallpaperURL.replace("th-", "wallhaven-");
				int index = wallpaperURL.lastIndexOf(WDUtilities.URL_SLASH);
				// Obtaining wallpaper's name (string after the last slash)
				String wallpaperName = WDUtilities.WD_PREFIX + wallpaperURL.substring(index + 1);
				String wallpaperNameFavorite = WDUtilities.WD_FAVORITE_PREFIX + wallpaperURL.substring(index + 1);
				File wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
				File wallpaperFavorite = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperNameFavorite);
				if (!wallpaper.exists() && !wallpaperFavorite.exists() && !WDUtilities.isWallpaperBlacklisted(wallpaperName) && !WDUtilities.isWallpaperBlacklisted(wallpaperNameFavorite)) {
					// Storing the image. It is necessary to download the remote file
					// First try: JPG format will be used
					boolean isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
					if (!isWallpaperSuccessfullyStored) {
						// Second try: PNG format will be used
						wallpaperURL = wallpaperURL.replace("jpg", "png");
						if (LOG.isInfoEnabled()) {
							LOG.info("JPG format wasn't found. Trying PNG format...");
						}
						wallpaperName = wallpaperName.replace("jpg", "png");
						wallpaperNameFavorite = wallpaperNameFavorite.replace("jpg", "png");
						wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
						wallpaperFavorite = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperNameFavorite);
						if (!wallpaper.exists() && !wallpaperFavorite.exists() && !WDUtilities.isWallpaperBlacklisted(wallpaperName) && !WDUtilities.isWallpaperBlacklisted(wallpaperNameFavorite)) {
							isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
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
								// Exit the process because one wallpaper was downloaded successfully
								break;
							}							
						} else {
							if (LOG.isInfoEnabled()) {
								LOG.info("Wallpaper " + wallpaper.getName() + " is already stored. Skipping...");
							}
						}
					} else {
						if (LOG.isInfoEnabled()) {
							LOG.info("Wallpaper " + wallpaper.getName() + " successfully stored");
							LOG.info("Refreshing space occupied progress bar...");
						}
						WallpaperDownloader.refreshProgressBar();
						WallpaperDownloader.refreshJScrollPane();
						// Exit the process because one wallpaper was downloaded successfully
						break;
					}
				} else {
					if (LOG.isInfoEnabled()) {
						LOG.info("Wallpaper " + wallpaper.getName() + " is already stored or blacklisted. Skipping...");
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
		// If activeKeyword is empty, the search operation will be done within the whole repository 
		String keywordString = "";
		if (!activeKeyword.equals(PreferencesManager.DEFAULT_VALUE)) {
			keywordString = "q" + WDUtilities.EQUAL + activeKeyword + WDUtilities.AND;
		}
		String resolutionString = "";
		if (!resolution.equals(PreferencesManager.DEFAULT_VALUE)) {
			resolutionString = "resolutions" + WDUtilities.EQUAL + resolution + WDUtilities.AND;
		}
		if (LOG.isInfoEnabled()) {
			LOG.info(baseURL + "search" + WDUtilities.QM + keywordString + "categories" + WDUtilities.EQUAL + "111" + WDUtilities.AND + "purity" + WDUtilities.EQUAL + "110" + WDUtilities.AND + resolutionString + 
					WDUtilities.AND + "order" + WDUtilities.EQUAL + "desc" + WDUtilities.AND + "sorting" + WDUtilities.EQUAL + order);
		}
		return baseURL + "search" + WDUtilities.QM + keywordString + "categories" + WDUtilities.EQUAL + "111" +WDUtilities.AND + "purity" + WDUtilities.EQUAL + "110" + WDUtilities.AND + resolutionString + 
				   WDUtilities.AND + "order" + WDUtilities.EQUAL + "desc" + WDUtilities.AND + "sorting" + WDUtilities.EQUAL + order;
	}

}
