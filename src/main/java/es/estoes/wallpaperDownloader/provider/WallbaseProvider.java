package es.estoes.wallpaperDownloader.provider;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import es.estoes.wallpaperDownloader.exception.ProviderException;
import es.estoes.wallpaperDownloader.util.PropertiesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

public class WallbaseProvider extends Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(WallbaseProvider.class);
	
	// Methods
	/**
	 * Constructor
	 */
	public WallbaseProvider () {
		super();
		PropertiesManager pm = PropertiesManager.getInstance();
		keywordsProperty = "provider-wallbase-keywords";
		baseURL = pm.getProperty("provider.wallbase.baseurl");
	}
	
	public void getWallpaper() throws ProviderException {
		obtainActiveKeyword();
		LOG.info("Downloading wallpaper with keyword -> " + activeKeyword);
		String completeURL = composeCompleteURL();
		try {
			// 1.- Getting HTML document
			Document doc = Jsoup.connect(completeURL).get();
			// 2.- Getting all thumbnails. They are distinguished because they are 'lazy' classed img elements
			Elements thumbnails = doc.select("img.lazy");
			// 3.- Getting a wallpaper which is not already stored in the filesystem
			for (Element thumbnail : thumbnails) {
				String thumbnailURL = thumbnail.attr("data-original");
				// Replacing thumb by wallpaper
				String wallpaperURL = thumbnailURL.replace("thumb", "wallpaper");
				int index = wallpaperURL.lastIndexOf(WDUtilities.URL_SLASH);
				// Obtaining the wallpaper name (string after the last slash)
				String wallpaperName = wallpaperURL.substring(index + 1);
				File wallpaper = new File(WDUtilities.getDownloadsPath() + File.separator + wallpaperName);
				if (!wallpaper.exists()) {
					// TODO: !!!!
					LOG.info("");
					break;
				}
				/*
				http://wallpapers.wallbase.cc/manga-anime/wallpaper-44633.jpg
				http://thumbs.wallbase.cc//manga-anime/thumb-44633.jpg
				*/
				
			}
		} catch (IOException e) {
			throw new ProviderException("There was a problem downloading a wallpaper. Complete URL -> " + completeURL + ". Message: " + e.getMessage());
		}
	}
		
	private String composeCompleteURL() {
		return baseURL + "search" + WDUtilities.QM + "q" + WDUtilities.EQUAL + 
			   activeKeyword + WDUtilities.AND + "res" + WDUtilities.EQUAL + resolution;
	}

}
