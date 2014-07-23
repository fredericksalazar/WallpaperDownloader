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

