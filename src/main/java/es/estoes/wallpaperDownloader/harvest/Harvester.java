package es.estoes.wallpaperDownloader.harvest;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import es.estoes.wallpaperDownloader.provider.Provider;
import es.estoes.wallpaperDownloader.provider.WallbaseProvider;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

/**
 * This class uses the Singleton principle and design pattern. It can only be instantiated 
 * one time during the execution of the application and it gathers all the methods related
 * to the harvester which will find, download and store the wallpapers. It will be 
 * a Provider aggregator
 * 
 * @author egarcia
 *
 */
public class Harvester {
	
	// Constants
	private static volatile Harvester instance;
	
	// Attributes
	private LinkedList<Provider> providers = null;
	private boolean isProviderWorking;
	private boolean isHaltRequired;
	
	// Methods
	/**
	 * Constructor
	 */
	private Harvester () {
		initializeProviders();
	}
	
	private void initializeProviders() {
		isHaltRequired = false;
		PreferencesManager prefm = PreferencesManager.getInstance();
		providers = new LinkedList<Provider>();
		// Reading user preferences
		// -----------------------------------------------
		// Wallbase.cc
		// -----------------------------------------------
		String wallbaseEnable = prefm.getPreference("provider-wallbase");
		if (wallbaseEnable.equals(WDUtilities.APP_YES)) {
			providers.add(new WallbaseProvider());
		}				
	}

	public static Harvester getInstance() {
		if (instance == null) {
			synchronized (Harvester.class) {
				if (instance == null) {
					instance = new Harvester();
					
				}
			}
		}
		return instance;
	}
	
	/**
	 * This method stops the harvesting process
	 */
	public void stop () {
		isHaltRequired = true;
		while (isProviderWorking) {
			// Do nothing. It is necessary to wait until providers have finished the last job
		}
		// TODO: Stops stuff
		providers = null;
		
	}
	
	/**
	 * This method starts the harvesting process
	 */
	public void start () {		
		Provider provider = null;
		if (providers.equals(null)) {
			initializeProviders();
		}
		
		// For every Provider
		// 1.- Getting 1 wallpaper per defined keyword
		// 2.- When all the keywords have been used, take provider and put it at the end of 
		// the list
		// 3.- Starting again with the next provider
		while (!isHaltRequired) {
			Iterator<Provider> iterator = providers.iterator();
			while (iterator.hasNext()) {
				provider = iterator.next();
				provider.obtainKeywords();
				while (!provider.getAreKeywordsDone()) {
					provider.getWallpaper();
					provider.storeWallpaper();
				}
				providers.addLast(providers.removeFirst());
			}
			isHaltRequired = true;
		}
		
	}

}
