package es.estoes.wallpaperDownloader.harvest;

import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.SwingWorker;
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
	private static volatile Harvester instance;
	
	// Attributes
	private BackgroundHarvestingProcess backgroundHarvestingProcess = null;
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
		if (providers.equals(null)) {
			initializeProviders();
		}
		
		while (!isHaltRequired) {
			backgroundHarvestingProcess = new BackgroundHarvestingProcess();
			backgroundHarvestingProcess.setProviders(providers);
			backgroundHarvestingProcess.execute();
			
			//isHaltRequired = true;
		}	
	}
}
