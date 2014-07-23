package es.estoes.wallpaperDownloader.harvest;

public class Harvester {
	
	// Constants
	
	// Attributes
	public static boolean isWorking;
	public static boolean isHaltRequired;
	
	// Methods
	// TODO: Implement singleton?
	public void stop () {
		isHaltRequired = true;
		while (isWorking) {
			// Do nothing. It is necessary to wait until providers have finished the last job
		}
		// TODO: Stops stuff
	}

}
