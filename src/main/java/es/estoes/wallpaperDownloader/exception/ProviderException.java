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

package es.estoes.wallpaperDownloader.exception;

import org.apache.log4j.Logger;

public class ProviderException extends RuntimeException {
	
	private static final long serialVersionUID = 6004654670878563660L;
	private static final Logger LOG = Logger.getLogger(ProviderException.class);

    public ProviderException() {
		super();
	}

	public ProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProviderException(String message) {
		super(message);
		LOG.error("Error during Provider operation. Error: " + message);
	}

	public ProviderException(Throwable cause) {
		super(cause);
	}

}

