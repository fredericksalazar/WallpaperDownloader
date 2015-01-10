package es.estoes.wallpaperDownloader.provider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
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
		PropertiesManager pm = PropertiesManager.getInstance();
		PreferencesManager prefm = PreferencesManager.getInstance();
		keywordsProperty = "provider-wallhaven-keywords";
		baseURL = pm.getProperty("provider.wallhaven.baseurl");
		switch (new Integer(prefm.getPreference("wallpaper-search-type"))) {
			case 0: this.order = "relevance";
					break;
			case 1: this.order = "date";
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
		obtainActiveKeyword();
		String completeURL = composeCompleteURL();
		try {
			checkAndPrepareDownloadDirectory();			
			LOG.info("Downloading wallpaper with keyword -> " + activeKeyword);
			// 1.- Getting HTML document
			Document doc = Jsoup.connect(completeURL).get();
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
				File wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
				if (!wallpaper.exists()) {
					// Storing the image. It is necessary to download the remote file
					// First try: JPG format will be used
					boolean isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
					if (!isWallpaperSuccessfullyStored) {
						// Second try: PNG format will be used
						wallpaperURL = wallpaperURL.replace("jpg", "png");
						LOG.info("JPG format wasn't found. Trying PNG format...");
						wallpaperName = wallpaperName.replace("jpg", "png");
						wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
						if (!wallpaper.exists()) {
							isWallpaperSuccessfullyStored = storeRemoteFile(wallpaper, wallpaperURL);
							if (!isWallpaperSuccessfullyStored) {
								LOG.info("Error trying to store wallpaper " + wallpaperURL + ". Skipping...");							
							} else {
								LOG.info("Wallpaper " + wallpaper.getName() + " successfully stored");
								LOG.info("Refreshing space occupied progress bar...");
								WallpaperDownloader.refreshProgressBar();
								// Exit the process because one wallpaper was downloaded successfully
								break;
							}							
						} else {
							LOG.info("Wallpaper " + wallpaper.getName() + " is already stored. Skipping...");
						}
					} else {
						LOG.info("Wallpaper " + wallpaper.getName() + " successfully stored");
						LOG.info("Refreshing space occupied progress bar...");
						WallpaperDownloader.refreshProgressBar();
						// Exit the process because one wallpaper was downloaded successfully
						break;
					}
				} else {
					LOG.info("Wallpaper " + wallpaper.getName() + " is already stored. Skipping...");
				}
			}
		} catch (IOException e) {
			throw new ProviderException("There was a problem downloading a wallpaper. Complete URL -> " + completeURL + ". Message: " + e.getMessage());
		} catch (ProviderException pe) {
			throw pe;
		}
	}
		
	private boolean storeRemoteFile(File wallpaper, String wallpaperURL) {
		URL url;
		boolean success = false;
		BufferedInputStream bufIn = null;
		OutputStream out = null;
		try {
			url = new URL(wallpaperURL);
		} catch (MalformedURLException e) {
			LOG.error("Malformed URL. Error: " + e.getMessage());
			return false;
		}
		URLConnection uc;
		try {
			uc = url.openConnection();
			int contentLength = uc.getContentLength();
			InputStream input = uc.getInputStream();
			bufIn = new BufferedInputStream(input);
			out = new FileOutputStream(wallpaper);
			byte[] data = new byte[contentLength];
			int bytesRead = 0;
			int offset = 0;
			while (offset < contentLength) {
			      bytesRead = bufIn.read(data, offset, data.length - offset);
			      if (bytesRead == -1)
			        break;
			      offset += bytesRead;		
			}
			bufIn.close();
		    if (offset != contentLength) {
		    	LOG.error("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		    	return false;
		    }
		    out.write(data);
		    success = true;
		    return true;
		} catch (IOException e) {
	    	LOG.error("IOException. Error: " + e.getMessage());
	    	return false;
		} finally {
			if (out!=null) {
			    try {
					out.flush();
				    out.close();
				    if (!success) {
				    	if (wallpaper.exists()) {
				    		FileUtils.forceDelete(wallpaper);
				    	}
				    	return false;
				    }
				} catch (IOException e) {
			    	LOG.error("IOException. Error: " + e.getMessage());
			    	return false;
				}
			}
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
		LOG.info(baseURL + "search" + WDUtilities.QM + keywordString + "categories" + WDUtilities.EQUAL + "111" + WDUtilities.AND + "purity" + WDUtilities.EQUAL + "110" + WDUtilities.AND + resolutionString + "thpp" + WDUtilities.EQUAL + "60" + 
				   WDUtilities.AND + "order_mode" + WDUtilities.EQUAL + "desc" + WDUtilities.AND + "sorting" + WDUtilities.EQUAL + order);
		return baseURL + "search" + WDUtilities.QM + keywordString + "categories" + WDUtilities.EQUAL + "111" +WDUtilities.AND + "purity" + WDUtilities.EQUAL + "110" + WDUtilities.AND + resolutionString + "thpp" + WDUtilities.EQUAL + "60" + 
				   WDUtilities.AND + "order_mode" + WDUtilities.EQUAL + "desc" + WDUtilities.AND + "sorting" + WDUtilities.EQUAL + order;
	}

}
