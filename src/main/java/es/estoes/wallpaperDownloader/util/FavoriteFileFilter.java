package es.estoes.wallpaperDownloader.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Class to filter only favorite wallpapers (those which start with fwd-). It is used within JFileChooser
 * @author egarcia
 *
 */
public class FavoriteFileFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		
		boolean accept = false;
		String fileName = file.getName();
		int index = fileName.indexOf("-");
		String prefix = fileName.substring(0,index + 1);
		if (prefix.equals(WDUtilities.WD_FAVORITE_PREFIX)) {
			accept = true;
		}
		return accept;
	}

	@Override
	public String getDescription() {
		return "Favorite wallpapers";
	}

}
