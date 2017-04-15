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
