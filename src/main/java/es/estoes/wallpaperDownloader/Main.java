/**
 * Copyright 2016-2018 Eloy García Almadén <eloy.garcia.pca@gmail.com>
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

package es.estoes.wallpaperDownloader;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.util.WDConfigManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.window.WallpaperDownloader;

public class Main 
{
	protected static final Logger LOG = Logger.getLogger(Main.class);
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			private WallpaperDownloader window;

			public void run() {
				
				// Setting the system look & feel for the main frame
				String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
				
				try {
		        	if (systemLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel") || WDUtilities.isSnapPackage()) {
		        		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");                		
		        	} else {
		        		UIManager.setLookAndFeel(systemLookAndFeel);                		
		        	}
		        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException exception) {
		            exception.printStackTrace();
		            if (LOG.isInfoEnabled()) {
		            	LOG.error("Error in system look and feel definition: Message: " + exception.getMessage());
		            }
		            try {
						UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException exception2) {
						exception2.printStackTrace();
			            if (LOG.isInfoEnabled()) {
			            	LOG.error("Error in traditional system look and feel definition: Message: " + exception.getMessage());
			            }
					}
		        }
				
				WDConfigManager.configureLog();
				WDConfigManager.checkConfig();				
				WDUtilities.getDimensionScreen();
				
				window = new WallpaperDownloader();
				
			}
		});
	}
}
