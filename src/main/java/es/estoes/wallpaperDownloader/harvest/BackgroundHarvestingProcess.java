package es.estoes.wallpaperDownloader.harvest;

import java.util.LinkedList;

import javax.swing.SwingWorker;

import es.estoes.wallpaperDownloader.provider.Provider;
import es.estoes.wallpaperDownloader.util.PreferencesManager;

/**
* This class implements SwingWorker abstract class. It is used to execute a independent 
* Thread from the main one (Event Dispatch Thread) which renders the GUI, listens to the
* events produced by keyboard and elements of the GUI, etc...
* The Thread created by SwingWorker class is a Thread in which a heavy process can be 
* executed in background and return control to EDT until it finishes
* More information: http://chuwiki.chuidiang.org/index.php?title=Ejemplo_sencillo_con_SwingWorker
 */
public class BackgroundHarvestingProcess extends SwingWorker<Void, Void> {

	// Attributes
	private LinkedList<Provider> providers = null;

	// Getters & Setters
	public void setProviders(LinkedList<Provider> providers) {
		this.providers = providers;
	}

	// Methods
	/**
	 * This method executes the background process and returns control to EDT
	 * @return 
	 */
	@Override
	protected Void doInBackground() throws Exception {
		PreferencesManager prefm = PreferencesManager.getInstance();
		// For every Provider
		// 1.- Getting 1 wallpaper per defined keyword
		// 2.- When all the keywords have been used, take provider and put it at the end of 
		// the list
		// 3.- Starting again with the next provider
		while (providers.size()>0) {
			Provider provider = providers.removeFirst(); 
			provider.obtainKeywords();
			while (!provider.getAreKeywordsDone()) {
				provider.getWallpaper();
				Thread.sleep(Long.valueOf(prefm.getPreference("application-timer")));
			}
			providers.addLast(provider);					
		}
		
		return null;
	}


}