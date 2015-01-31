package es.estoes.wallpaperDownloader.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.window.DialogManager;

/**
 * This class gathers all the utilities needed for the correct behavior of the application. 
 * It implements a kind of Singleton pattern not pure and only based on static methods. 
 * Actually, there won't be an object of this class ever 
 * @author egarcia
 *
 */
public class WDUtilities {

	// Constants
	protected static final Logger LOG = Logger.getLogger(WDUtilities.class);
	public static final String APP_YES = "yes";
	public static final String APP_NO = "no";
	public static final String PROVIDER_SEPARATOR = ";";
	public static final String QM = "?";
	public static final String EQUAL = "=";
	public static final String AND = "&";
	public static final String URL_SLASH = "/";
	public static final String WD_PREFIX = "wd-";
	public static final String DEFAULT_DOWNLOADS_DIRECTORY = "downloads";
	public static final String UNIT_MB = "MB";
	public static final String WD_FAVOURITE_PREFIX = "fwd-";

	// Attributes
	private static String appPath;
	private static String downloadsPath;
	private static String userConfigurationFilePath;

	// Getters & Setters
	public static String getAppPath() {
		return appPath;
	}

	public static void setAppPath(String appPath) {
		WDUtilities.appPath = appPath;
	}

	public static String getDownloadsPath() {
		return downloadsPath;
	}

	public static void setDownloadsPath(String downloadsPath) {
		WDUtilities.downloadsPath = downloadsPath;
	}

	public static String getUserConfigurationFilePath() {
		return userConfigurationFilePath;
	}

	public static void setUserConfigurationFilePath(
			String userConfigurationFilePath) {
		WDUtilities.userConfigurationFilePath = userConfigurationFilePath;
	}

	// Methods (All the methods are static)

	/**
	 * Constructor
	 */
	private WDUtilities() {

	}

	/**
	 * Get screen resolution
	 * @return
	 */
	public static String getResolution() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();
		return width.intValue() + "x" + height.intValue();
	}

	/**
	 * Get all the wallpapers from the download directory
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<File> getAllWallpapers() {
		LOG.info("Getting all the wallpapers...");
		File downloadDirectory = new File(WDUtilities.getDownloadsPath());
		List<File> wallpapers = (List<File>) FileUtils.listFiles(downloadDirectory, FileFilterUtils.prefixFileFilter(WDUtilities.WD_PREFIX), null);		
		return wallpapers;
	}

	/**
	 * Move all the wallpapers to a new location
	 * @param newPath
	 */
	public static void moveDownloadedWallpapers(String newPath) {
		if (!WDUtilities.getDownloadsPath().equals(newPath)) {
			File destDir = new File(newPath);
			File srcDir = new File(WDUtilities.getDownloadsPath());
			// Get all the wallpapers from the current location
			List<File> wallpapers = getAllWallpapers();

			// Copy every file to the new location
			Iterator<File> wallpaper = wallpapers.iterator();
			while (wallpaper.hasNext()) {
				try {
					FileUtils.copyFileToDirectory((File) wallpaper.next(), destDir, true);
				} catch (IOException e) {
					// Something went wrong
					LOG.error("Error copying file " + wallpaper.toString());
					// Information
					DialogManager info = new DialogManager("Something went wrong. Downloads directory couldn't be changed. Check log for more information.", 2000);
					info.openDialog();
				}
			}

			// Remove old directory
			try {
				FileUtils.deleteDirectory(srcDir);
			} catch (IOException e) {
				// Something went wrong
				LOG.error("The original downloads directory " + WDUtilities.getDownloadsPath() + " couldn't be removed. Please, check also directory " + destDir.getAbsolutePath() + " because all the wallpapers were copyied there");
				// Information
				DialogManager info = new DialogManager("Something went wrong. Downloads directory couldn't be changed. Check log for more information.", 2000);
				info.openDialog();
			}

			// Change Downloads path within application properties and WDUtilities
			PreferencesManager prefm = PreferencesManager.getInstance();
			prefm.setPreference("application-downloads-folder", destDir.getAbsolutePath());
			WDUtilities.setDownloadsPath(destDir.getAbsolutePath());

			// Information
			DialogManager info = new DialogManager("Dowloads directory has been succesfully changed to " + destDir.getAbsolutePath(), 2000);
			info.openDialog();

		}
	}

	/**
	 * Calculates the percentage of the space occupied within the directory
	 * @param directoryPath
	 * @return int
	 */
	public static int getPercentageSpaceOccupied(String directoryPath) {
		PreferencesManager prefm = PreferencesManager.getInstance();
		int downloadsDirectorySize = new Integer(prefm.getPreference("application-max-download-folder-size"));
		long spaceLong = WDUtilities.getDirectorySpaceOccupied(directoryPath, WDUtilities.UNIT_MB);
		// Obtaining percentage
		int percentage = (int) ((spaceLong * 100) / downloadsDirectorySize);
		if (percentage > 100) {
			percentage = 100;
		}
		LOG.info("Downloads directory space occupied: " + percentage + " %");
		return percentage;
	}

	/**
	 * Calculate the space occupied within the directory
	 * @param directoryPath
	 * @param unit
	 * @return
	 */
	public static long getDirectorySpaceOccupied(String directoryPath, String unit) {
		long space = 0;
		File directory = new File(directoryPath);
		if (unit.equals(WDUtilities.UNIT_MB)) {
			// Calculates the space in MBytes
			space = FileUtils.sizeOfDirectory(directory);
			// Turning bytes into Megabytes
			space = (space / 1024) / 1024;
		}
		return space;
	}

	/**
	 * Set a wallpaper as favourite
	 * @param originalAbsolutePath
	 */
	public static void setFavourite(String originalAbsolutePath) {
		File originalWallpaper = new File(originalAbsolutePath);
		String wallpaperOriginalName = originalWallpaper.getName();
		int index = wallpaperOriginalName.indexOf("-");
		String wallpaperFavouriteName = WD_FAVOURITE_PREFIX + wallpaperOriginalName.substring(index + 1);
		File favouriteWallpaper = new File(WDUtilities.getDownloadsPath() + File.separatorChar + wallpaperFavouriteName);
		originalWallpaper.renameTo(favouriteWallpaper);
		// Information
		DialogManager info = new DialogManager(wallpaperOriginalName + " wallpaper set as favourite.", 2000);
		info.openDialog();
		LOG.info(originalAbsolutePath + " wallpaper has been set as favourite");
	}

	/**
	 * Set a wallpaper as no favourite
	 * @param originalAbsolutePath
	 */
	public static void setNoFavourite(String originalAbsolutePath) {
		File originalWallpaper = new File(originalAbsolutePath);
		String wallpaperOriginalName = originalWallpaper.getName();
		int index = wallpaperOriginalName.indexOf("-");
		String wallpaperNoFavouriteName = WD_PREFIX + wallpaperOriginalName.substring(index + 1);
		File favouriteWallpaper = new File(WDUtilities.getDownloadsPath() + File.separatorChar + wallpaperNoFavouriteName);
		originalWallpaper.renameTo(favouriteWallpaper);
		// Information
		DialogManager info = new DialogManager(wallpaperOriginalName + " wallpaper set as no favourite.", 2000);
		info.openDialog();
		LOG.info(originalAbsolutePath + " wallpaper has been set as no favourite");
	}
}
