/**
 * Copyright 2016-2017 Eloy García Almadén <eloy.garcia.pca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.estoes.wallpaperDownloader.changer;

public class UnknownWallpaperChanger extends WallpaperChanger {
	
	// Constants

	// Attributes

	// Getters & Setters
	
	// Methods
	/**
	 * Constructor
	 */
	public UnknownWallpaperChanger () {
		super();
	}

	@Override
	public void setWallpaper(String wallpaperPath) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isWallpaperChangeable() {
		return false;
	}
}
