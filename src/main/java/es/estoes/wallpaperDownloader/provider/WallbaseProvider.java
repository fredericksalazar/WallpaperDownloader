package es.estoes.wallpaperDownloader.provider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import es.estoes.wallpaperDownloader.exception.ProviderException;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.PropertiesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

public class WallbaseProvider extends Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(WallbaseProvider.class);

	// Attributes
	private String order;
	// Methods
	/**
	 * Constructor
	 */
	public WallbaseProvider () {
		super();
		PropertiesManager pm = PropertiesManager.getInstance();
		PreferencesManager prefm = PreferencesManager.getInstance();
		keywordsProperty = "provider-wallbase-keywords";
		baseURL = pm.getProperty("provider.wallbase.baseurl");
		switch (new Integer(prefm.getPreference("wallpaper-search-type"))) {
			case 0: this.order = "relevance";
					break;
			case 1: this.order = "date";
					break;
			case 2: this.order = "views";
					break;
			case 3: this.order = "favs";
					break;
			case 4: this.order = "random";
					break;
			
		}
	}
	
	public void getWallpaper() throws ProviderException {
		String completeURL = composeCompleteURL();
		try {
			checkAndPrepareDownloadDirectory();			
			obtainActiveKeyword();
			LOG.info("Downloading wallpaper with keyword -> " + activeKeyword);
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
					// Storing the image. It is necessary to download the remote file
					URL url = new URL(wallpaperURL);
					URLConnection uc = url.openConnection();
					int contentLength = uc.getContentLength();
					InputStream input = uc.getInputStream();
					BufferedInputStream bufIn = new BufferedInputStream(input);
					OutputStream out = new FileOutputStream(wallpaper);
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
					    out.flush();
					    out.close();
				    	throw new ProviderException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
				    }
				    out.write(data);
				    out.flush();
				    out.close();				      
					LOG.info("Wallpaper " + wallpaperName + " successfully stored");
					// Exit the process because one wallpaper was downloaded successfully
					break;
				} else {
					LOG.info("This wallpaper is already stored. Skipping...");
				}
			}
		} catch (IOException e) {
			throw new ProviderException("There was a problem downloading a wallpaper. Complete URL -> " + completeURL + ". Message: " + e.getMessage());
		} catch (ProviderException pe) {
			throw pe;
		}
	}
		
	private String composeCompleteURL() {
		LOG.info(baseURL + "search" + WDUtilities.QM + "q" + WDUtilities.EQUAL + 
				   activeKeyword + WDUtilities.AND + "res" + WDUtilities.EQUAL + resolution + WDUtilities.AND + "thpp" + WDUtilities.EQUAL + "60" + 
				   WDUtilities.AND + "order_mode" + WDUtilities.EQUAL + "desc" + WDUtilities.AND + "order" + WDUtilities.EQUAL + order);
		return baseURL + "search" + WDUtilities.QM + "q" + WDUtilities.EQUAL + 
			   activeKeyword + WDUtilities.AND + "res" + WDUtilities.EQUAL + resolution + WDUtilities.AND + "thpp" + WDUtilities.EQUAL + "60" + 
			   WDUtilities.AND + "order_mode" + WDUtilities.EQUAL + "desc" + WDUtilities.AND + "order" + WDUtilities.EQUAL + order;
	}

}
