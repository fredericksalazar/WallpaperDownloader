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

package es.estoes.wallpaperDownloader.exception;

import org.apache.log4j.Logger;

public class WDPropertyException extends RuntimeException {
	
	private static final long serialVersionUID = 8308720515867593002L;
	private static final Logger LOG = Logger.getLogger(WDPropertyException.class);

    public WDPropertyException() {
		super();
	}

	public WDPropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public WDPropertyException(String message) {
		super(message);
		LOG.error("Error managing a property from a property file. Error: " + message);
		LOG.error("Exiting the application...");
		System.exit(0);
	}

	public WDPropertyException(Throwable cause) {
		super(cause);
	}

}

