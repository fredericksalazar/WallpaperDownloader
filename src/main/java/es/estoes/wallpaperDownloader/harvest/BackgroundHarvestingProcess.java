/**
 * Copyright 2016-2017 Eloy García Almadén <eloy.garcia.pca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.estoes.wallpaperDownloader.harvest;

import java.util.LinkedList;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.exception.ProviderException;
import es.estoes.wallpaperDownloader.provider.Provider;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;

/**
* This class implements SwingWorker abstract class. It is used to execute a independent 
* Thread from the main one (Event Dispatch Thread) which renders the GUI, listens to the
* events produced by keyboard and elements of the GUI, etc...
* The Thread created by SwingWorker class is a Thread in which a heavy process can be 
* executed in background and return control to EDT until it finishes
* More information: http://chuwiki.chuidiang.org/index.php?title=Ejemplo_sencillo_con_SwingWorker
 */
public class BackgroundHarvestingProcess extends SwingWorker<Void, Void> {

	// Constants
	private static final Logger LOG = Logger.getLogger(BackgroundHarvestingProcess.class);
	
	// Attributes
	private LinkedList<Provider> providers = null;
	private Long timer;

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
		// Getting timer
		switch (new Integer(prefm.getPreference("application-timer"))) {
		case 0: this.timer = Long.valueOf(60000);
				break;
		case 1: this.timer = Long.valueOf(300000);
				break;
		case 2: this.timer = Long.valueOf(600000);
				break;
		case 3: this.timer = Long.valueOf(1200000);
				break;
		case 4: this.timer = Long.valueOf(1800000);
		break;
		}
		LOG.info("Timer set every " + ((this.timer/1000)/60) + " min");
		// For every Provider
		// 1.- Getting 1 wallpaper per defined keyword
		// 2.- When all the keywords have been used, take provider and put it at the end of 
		// the list
		// 3.- Starting again with the next provider
		while (providers.size()>0) {
			// First, Internet connection is tested
			if (WDUtilities.checkConnectivity()) {
				// If there is connectivity, then the process must go on
				Provider provider = providers.removeFirst(); 
				provider.obtainKeywords();
				try {
					while (!provider.getAreKeywordsDone()) {
						provider.getWallpaper();
						Thread.sleep(timer);
					}				
				} catch (ProviderException pe) {
					// Do nothing
				}
				providers.addLast(provider);					
			} else {
				// If there is no connectivity, wait for 60 seconds and try again
				if(LOG.isInfoEnabled()) {
					LOG.error("No connection to Internet. Harvesting process will try to download more wallpapers in 60 seconds. Please wait...");
				}
				Thread.sleep(Long.valueOf(60000));
				
			}
		}
		return null;
	}


}
