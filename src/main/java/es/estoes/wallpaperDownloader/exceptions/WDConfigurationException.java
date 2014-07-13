package es.estoes.wallpaperDownloader.exceptions;

import org.apache.log4j.Logger;

public class WDConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -5874393138119291110L;
	private static final Logger LOG = Logger.getLogger(WDConfigurationException.class);

    public WDConfigurationException() {
		super();
	}

	public WDConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public WDConfigurationException(String message) {
		super(message);
		LOG.error("Error configuring the application. Error: " + message);
		LOG.error("Exiting the application...");
		System.exit(0);
	}

	public WDConfigurationException(Throwable cause) {
		super(cause);
	}

}
