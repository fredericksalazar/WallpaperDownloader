package es.estoes.wallpaperDownloader.provider;

import es.estoes.wallpaperDownloader.util.PropertiesManager;

public class WallbaseProvider extends Provider {
	
	public WallbaseProvider () {
		super();
		PropertiesManager pm = PropertiesManager.getInstance();
		keywordsProperty = "provider-wallbase-keywords";
		baseURL = pm.getProperty("provider.wallbase.baseurl");
	}

}
