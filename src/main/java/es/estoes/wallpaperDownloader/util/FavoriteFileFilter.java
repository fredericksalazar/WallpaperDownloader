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
