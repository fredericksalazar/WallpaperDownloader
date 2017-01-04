package es.estoes.wallpaperDownloader.provider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Response;

import es.estoes.wallpaperDownloader.exception.ProviderException;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.window.WallpaperDownloader;

public abstract class Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(Provider.class);
	
	// Attributes
	private List<String> keywords;
	private boolean areKeywordsDone;
	protected String activeKeyword;
	private int activeIndex;
	protected String baseURL;
	protected String keywordsProperty;
	protected String resolution;
	
	// Getters & Setters
	/**
	 * Gets areKeywordsDone.
	 * @return areKeywordsDone
	 */
	public boolean getAreKeywordsDone() {
		return this.areKeywordsDone;
	}
	
	/**
	 * Sets areKeywordsDone.
	 * @param areKeywordsDone areKeywordsDone
	 */
	public void setAreKeywordsDone(boolean areKeywordsDone) {
		this.areKeywordsDone = areKeywordsDone;
	}

	// Methods
	/**
	 * Constructor.
	 */
	public Provider () {
		PreferencesManager prefm = PreferencesManager.getInstance();
		this.activeIndex = 0;
		
		// Obtaining resolution
		this.resolution = prefm.getPreference("wallpaper-resolution");
		
		// Obtaining keywords
		this.keywordsProperty = "provider-wallhaven-keywords";
	}
	
	/**
	 * This method gets the keywords defined by the user for a specific provider
	 * and split them using delimiter 'zero or more whitespace, ; , zero or more whitespace'.
	 */
	public void obtainKeywords() {
		PreferencesManager prefm = PreferencesManager.getInstance();
		String keywordsFromPreferences = prefm.getPreference(this.keywordsProperty);
		this.keywords = Arrays.asList(keywordsFromPreferences.split("\\s*" + WDUtilities.PROVIDER_SEPARATOR + "\\s*"));
		if (this.keywords.isEmpty()) {
			this.setAreKeywordsDone(true);
		} else {
			this.setAreKeywordsDone(false);
		}
	}

	/**
	 * This method gets the active keyword which will be used for the search.
	 */
	protected void obtainActiveKeyword() {
		this.activeKeyword = this.keywords.get(this.activeIndex);
		if (this.keywords.size() == this.activeIndex + 1) {
			// The end of the keywords list has been reached. Starts again
			this.activeIndex = 0;
			this.areKeywordsDone = true;
		} else {
			this.activeIndex++;
		}
	}
	
	/**
	 * Gets a wallpaper.
	 */
	public void getWallpaper() {
	}
	
	/**
	 * This method checks the disk space within download directory. If it is full, it will remove one or several wallpapers
	 * in order to prepare it to a new download.
	 */
	protected void checkAndPrepareDownloadDirectory () {
		if (LOG.isInfoEnabled()) {
			LOG.info("Checking download directory. Removing some wallpapers if it is necessary...");
		}
		PreferencesManager prefm = PreferencesManager.getInstance();
		Long maxSize = Long.parseLong(prefm.getPreference("application-max-download-folder-size"));
		long downloadFolderSize = WDUtilities.getDirectorySpaceOccupied(WDUtilities.getDownloadsPath(), WDUtilities.UNIT_MB);
		while (downloadFolderSize > maxSize) {
			File fileToRemove = WDUtilities.pickRandomFile(Boolean.FALSE, WDUtilities.DOWNLOADS_DIRECTORY);
			try {
				if (fileToRemove != null) {
					FileUtils.forceDelete(fileToRemove);
					WallpaperDownloader.refreshProgressBar();
					WallpaperDownloader.refreshJScrollPane();
					if (LOG.isInfoEnabled()) {
						LOG.info("The current size of the downloads folder is " + downloadFolderSize + " which is greater than " + maxSize + ". "  + fileToRemove.getPath() + " deleted");
						LOG.info("Refreshing space occupied progress bar...");
					}
				}
			} catch (IOException exception) {
				throw new ProviderException("Error deleting file " + fileToRemove.getPath() + ". Error: " + exception.getMessage());
			}
			downloadFolderSize = WDUtilities.getDirectorySpaceOccupied(WDUtilities.getDownloadsPath(), WDUtilities.UNIT_MB);
		}
	}

	/**
	 * Stores a remote file.
	 * @param wallpaper File for the wallpaper
	 * @param wallpaperURL remote URL of the wallpaper
	 * @return boolean
	 */
	protected boolean storeRemoteFile(File wallpaper, String wallpaperURL) {
	    
		boolean success = false;
		OutputStream out = null;
		
		try {
			// Open a URL stream (an image) using JSoup. There was a problem with the old method (commented) and the server, because
			// the request didn't have userAgent and an 403 error was produced.
	        Response resultImageResponse = (Response) Jsoup.connect(wallpaperURL)
	        								.userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:35.0) Gecko/20100101 Firefox/35.0")
	        								.referrer("http://www.google.com")
	        								.ignoreHttpErrors(true)
	        								.followRedirects(true)
	        								.timeout(0)
	        								.ignoreContentType(true)
	        								.execute();
	
	        
	        if (resultImageResponse.statusCode() == WDUtilities.STATUS_CODE_200) {
	        	// Wallpaper has been successfully found
		        // Open a URL Stream
		        URL url = new URL(wallpaperURL);
		        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
		        httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
		        InputStream in = httpcon.getInputStream();
		        	 
		        out = new BufferedOutputStream(new FileOutputStream(wallpaper.getAbsolutePath()));
		        	 
		        	        for (int b; (b = in.read()) != -1;) {
		        	            out.write(b);
		        	        }
		        	        out.close();
		        	        in.close();

	        	success = true;
			    return true;
	        } else {
	        	// Wallpaper was not found
	        	return false;
	        }
		} catch (Exception exception) {
			if (LOG.isInfoEnabled()) {
		    	LOG.error("There was an error reading " + wallpaperURL + " image. Error: " + exception.getMessage());
			}
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
					if (LOG.isInfoEnabled()) {
						LOG.error("IOException. Error: " + e.getMessage());
					}
			    	return false;
				}
			}
		}		
	}
}
