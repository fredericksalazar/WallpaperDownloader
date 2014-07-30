package es.estoes.wallpaperDownloader.provider;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

public abstract class Provider {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(Provider.class);
	
	// Attributes
	private List<String> keywords;
	private boolean areKeywordsDone;
	private String activeKeyword;
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

	private void obtainActiveKeyword() {
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
		obtainActiveKeyword();
		LOG.info("Downloading wallpaper with keyword -> " + activeKeyword);
	}
	
	public void storeWallpaper() {
		LOG.info("Storing wallpaper...");		
	}

}
