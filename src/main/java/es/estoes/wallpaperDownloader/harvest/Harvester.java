package es.estoes.wallpaperDownloader.harvest;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.provider.Provider;
import es.estoes.wallpaperDownloader.provider.WallbaseProvider;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

/**
 * This class uses the Singleton principle and design pattern. It can only be instantiated 
 * one time during the execution of the application and it gathers all the methods related
 * to the harvester which will find, download and store the wallpapers. It will be 
 * a Provider aggregator. It implements Factory Method design pattern at initializeProviders
 * method. 
 *  
 * @author egarcia
 *
 */
public class Harvester {
	
	// Constants
	private static final Logger LOG = Logger.getLogger(Harvester.class);
	private static volatile Harvester instance;
	
	// Attributes
	private BackgroundHarvestingProcess backgroundHarvestingProcess = null;
	private LinkedList<Provider> providers = null;
	private boolean isProviderWorking;
	private boolean isHaltRequired;
	
	// Getters & Setters
	public void setIsProviderWorking (boolean isProviderWorking) {
		this.isProviderWorking = isProviderWorking;
	}
	
	public boolean getIsHaltRequired() {
		return this.isHaltRequired;
	}
	
	// Methods
	/**
	 * Constructor
	 */
	private Harvester () {
		isProviderWorking = false;
		initializeProviders();
	}
	
	private void initializeProviders() {
		LOG.info("Initializing providers...");
		isHaltRequired = false;
		PreferencesManager prefm = PreferencesManager.getInstance();
		providers = new LinkedList<Provider>();
		// Reading user preferences
		// -----------------------------------------------
		// Wallbase.cc
		// -----------------------------------------------
		String wallbaseEnable = prefm.getPreference("provider-wallbase");
		if (wallbaseEnable.equals(WDUtilities.APP_YES)) {
			providers.add(new WallbaseProvider());	// Factory method
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
		LOG.info("Stoping harvesting process...");
		isHaltRequired = true;
		providers = null;
		if (backgroundHarvestingProcess != null) {
			while (!backgroundHarvestingProcess.isCancelled()) {
				
			}
			backgroundHarvestingProcess = null;
		}
	}
	
	/**
	 * This method starts the harvesting process
	 */
	public void start () {		
		LOG.info("Starting harvesting process...");
		while (isProviderWorking) {
			// Do nothing. It is necessary to wait until providers have finished the last job 
		}
		
		if (providers == null) {
			initializeProviders();
		}
		
		if (providers.size() > 0) {
			LOG.info("Providers found. Starting to download...");
			isProviderWorking = true;
			backgroundHarvestingProcess = new BackgroundHarvestingProcess();
			backgroundHarvestingProcess.setProviders(providers);
			backgroundHarvestingProcess.execute();								
		} else {
			LOG.info("Providers were not found. Nothing to do.");
			isProviderWorking = false;
		}
	}
}
