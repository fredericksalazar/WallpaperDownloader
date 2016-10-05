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
	public boolean getAreKeywordsDone() {
		return areKeywordsDone;
	}

	// Methods
	/**
	 * Constructor
	 */
	public Provider () {
		PreferencesManager prefm = PreferencesManager.getInstance();
		activeIndex = 0;
		
		// Obtaining resolution
		resolution = prefm.getPreference("wallpaper-resolution");		
	}
	
	/**
	 * This method gets the keywords defined by the user for a specific provider
	 * and split them using delimiter 'zero or more whitespace, ; , zero or more whitespace'
	 */
	public void obtainKeywords() {
		PreferencesManager prefm = PreferencesManager.getInstance();
		String keywordsFromPreferences = prefm.getPreference(keywordsProperty);
		keywords = Arrays.asList(keywordsFromPreferences.split("\\s*" + WDUtilities.PROVIDER_SEPARATOR + "\\s*"));
		if (keywords.isEmpty()) {
			areKeywordsDone = true;
		} else {
			areKeywordsDone = false;
		}
	}

	/**
	 * This method gets the active keyword which will be used for the search
	 */
	protected void obtainActiveKeyword() {
		activeKeyword = keywords.get(activeIndex);
		if (keywords.size() == activeIndex + 1) {
			// The end of the keywords list has been reached. Starts again
			activeIndex = 0;
			areKeywordsDone = true;
		} else {
			activeIndex++;
		}
	}
	
	public void getWallpaper() {
	}
	
	/**
	 * This method checks the disk space within download directory. If it is full, it will remove one or several wallpapers
	 * in order to prepare it to a new download
	 */
	protected void checkAndPrepareDownloadDirectory () {
		LOG.info("Checking download directory. Removing some wallpapers if it is necessary...");
		PreferencesManager prefm = PreferencesManager.getInstance();
		Long maxSize = Long.parseLong(prefm.getPreference("application-max-download-folder-size"));
		long downloadFolderSize = WDUtilities.getDirectorySpaceOccupied(WDUtilities.getDownloadsPath(), WDUtilities.UNIT_MB);
		while (downloadFolderSize > (maxSize * 1024)) {
			File fileToRemove = WDUtilities.pickRandomFile(Boolean.FALSE);
			try {
				if (fileToRemove != null) {
					FileUtils.forceDelete(fileToRemove);
					LOG.info(fileToRemove.getPath() + " deleted");
					LOG.info("Refreshing space occupied progress bar...");
					WallpaperDownloader.refreshProgressBar();
					WallpaperDownloader.refreshJScrollPane();
				}
			} catch (IOException e) {
				throw new ProviderException("Error deleting file " + fileToRemove.getPath() + ". Error: " + e.getMessage());
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
		} catch (Exception e) {
	    	LOG.error("There was an error reading " + wallpaperURL + " image. Error: " + e.getMessage());
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
}
