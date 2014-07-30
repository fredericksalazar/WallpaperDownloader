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

