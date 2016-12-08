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

public class DevianartProvider extends Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(DevianartProvider.class);

	// Attributes
	private String order;
	private String offset;
	
	// Getters & Setters
	
	/**
	 * Gets offset.
	 * @return offset
	 */
	public String getOffset() {
		return this.offset;
	}

	/**
	 * Sets offset.
	 * @param offset offset
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}

	/**
	 * Gets order.
	 * @return order
	 */
	public String getOrder() {
		return this.order;
	}

	/**
	 * Sets order.
	 * @param order order
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	
	// Methods
	/**
	 * Constructor
	 */
	public DevianartProvider () {
		super();
		PropertiesManager pm = PropertiesManager.getInstance();
		PreferencesManager prefm = PreferencesManager.getInstance();
		keywordsProperty = "provider-wallhaven-keywords";
		baseURL = pm.getProperty("provider.devianart.baseurl");
		// Order
		switch (new Integer(prefm.getPreference("wallpaper-devianart-search-type"))) {
			// Newest
			case 0: this.setOrder("5");
					break;
			// What's hot
			case 1: this.setOrder("67108864");
					break;
			// Popular
			case 2: this.setOrder("9");
					break;
		}
		
		// Offset
		this.setOffset("0");
	}
	
	public void getWallpaper() throws ProviderException {
		Boolean wallpaperFound = Boolean.FALSE;
		this.obtainActiveKeyword();
		String completeURL = composeCompleteURL();
		try {
			this.checkAndPrepareDownloadDirectory();	
			if (LOG.isInfoEnabled()) {
				LOG.info("Downloading wallpaper with keyword -> " + this.activeKeyword);
			}
			while (!wallpaperFound) {
				// 1.- Getting HTML document (New method including userAgent and other options)
				Document doc = Jsoup.connect(completeURL).userAgent("Mozilla").get();
				// 2.- Getting all thumbnails. They are identified because they have 'thumb' classed span elements
				Elements thumbnails = doc.select("span.thumb");

				if (!thumbnails.isEmpty()) {
					// 3.- Getting a wallpaper which is not already stored in the filesystem
					for (Element thumbnail : thumbnails) {
						// First, it is necessary to retrieve the <a> element inside the span which contains the link to 
						// downloads page for this wallpaper
						Elements linkURLs = thumbnail.select("a");
						Element linkURL = linkURLs.first();
						// Retrieves the entire document for this link
						Document wallpaperDoc = Jsoup.connect(linkURL.attr("href")).userAgent("Mozilla").get();
						// Retrieves the source for the full image
						Elements images = wallpaperDoc.select("img.dev-content-full");
						Element image = images.first();
						String wallpaperURL = image.attr("src");
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
						this.setOffset(this.getOffset() + 1);
						completeURL = composeCompleteURL();
					}
				} else {
					wallpaperFound = Boolean.TRUE;
					// Reseting offset
					this.setOffset("0");
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
		// If activeKeyword is empty, the search operation will be done within the whole repository 
		String keywordString = "";
		if (!this.activeKeyword.equals(PreferencesManager.DEFAULT_VALUE)) {
			// Replacing blank spaces with +
			this.activeKeyword = this.activeKeyword.replace(" ", "+");
			keywordString = "q" + WDUtilities.EQUAL + this.activeKeyword + WDUtilities.AND;
		}
		LOG.info(baseURL + WDUtilities.QM + keywordString + "order" + WDUtilities.EQUAL + this.order + 
				WDUtilities.AND + "offset" + WDUtilities.EQUAL + this.offset);
		return baseURL + WDUtilities.QM + keywordString + "order" + WDUtilities.EQUAL + this.order + 
				WDUtilities.AND + "offset" + WDUtilities.EQUAL + this.offset;
	}
}
