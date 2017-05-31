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
import java.io.FilenameFilter;

/**
 * Class to filter only images.
 * @author Eloy García Almadén
 *
 */
public class ImageFilenameFilter implements FilenameFilter {

	static final String[] EXTENSIONS = new String[]{"jpg", "JPG", "jpeg", "JPEG", "png", "PNG"};
	
	@Override
	public boolean accept(File dir, String name) {
		boolean accept = false;
		for (final String ext : EXTENSIONS) {
            if (name.endsWith("." + ext)) {
                accept = true;
            }
        }
        return accept;		
	}

}
