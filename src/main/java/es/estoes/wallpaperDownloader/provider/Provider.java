package es.estoes.wallpaperDownloader.provider;

import java.util.Arrays;
import java.util.List;

import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

public abstract class Provider {
	
	// Constants
	
	// Attributes
	public List<String> keywords;
	public String keywordsProperty;
	
	// Methods
	/**
	 * This method gets the keywords defined by the user for a specific provider
	 * and split them using delimiter 'zero or more whitespace, ; , zero or more whitespace'
	 */
	public void obtainKeywords() {
		PreferencesManager prefm = PreferencesManager.getInstance();
		String keywordsFromPreferences = prefm.getPreference(keywordsProperty);
		keywords = Arrays.asList(keywordsFromPreferences.split("\\s*" + WDUtilities.PROVIDER_SEPARATOR + "\\s*"));
	}
	
	public void getWallpaper() {
		
	}
	
	public void storeWallpaper() {
		
	}

}
