package es.estoes.wallpaperDownloader.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;
import es.estoes.wallpaperDownloader.window.DialogManager;
import es.estoes.wallpaperDownloader.window.WallpaperDownloader;
import es.estoes.wallpaperDownloader.window.WallpaperManagerWindow;
import org.apache.commons.io.comparator.*;

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
	public static final String SORTING_BY_DATE = "sort_by_date";
	public static final String SORTING_NO_SORTING = "no_sorting";
	public static final String WD_ALL = "all";

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
	 * @param wallpapersType Type of the wallpapers wanted to retrieve (favorite, non favorite, all)
	 * @return
	 */
	public static List<File> getAllWallpapers(String wallpapersType) {
		LOG.info("Getting all the wallpapers...");
		File downloadDirectory = new File(WDUtilities.getDownloadsPath());
		List<File> wallpapers = new ArrayList<File>();
		if (wallpapersType.equals(WDUtilities.WD_ALL)) {
			wallpapers = (List<File>) FileUtils.listFiles(downloadDirectory, FileFilterUtils.prefixFileFilter(WDUtilities.WD_FAVOURITE_PREFIX), null);
			wallpapers.addAll((List<File>) FileUtils.listFiles(downloadDirectory, FileFilterUtils.prefixFileFilter(WDUtilities.WD_PREFIX), null));
		} else {
			wallpapers = (List<File>) FileUtils.listFiles(downloadDirectory, FileFilterUtils.prefixFileFilter(wallpapersType), null);
			
			if (wallpapersType.equals(WDUtilities.WD_FAVOURITE_PREFIX)) {
				// Adding total number of favorite wallpapers to WallpaperManagerWindow if it exists
				WallpaperManagerWindow.refreshFavoriteWallpapersTotalNumber(wallpapers.size());				
			} else if (wallpapersType.equals(WDUtilities.WD_PREFIX) && WallpaperManagerWindow.lblTotalNoFavoriteWallpapers != null) {
				// Adding total number of no favorite wallpapers to WallpaperManagerWindow if it exists only if WallpaperManagerWindow has been
				// instantiated
				WallpaperManagerWindow.refreshNoFavoriteWallpapersTotalNumber(wallpapers.size());				
			}

		}
				
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
			List<File> wallpapers = getAllWallpapers(WDUtilities.WD_ALL);

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
	 * Set wallpapers as favorite
	 * @param originalAbsolutePath
	 */
	public static void setFavorite(List<String> originalAbsolutePaths) {
		Iterator<String> wallpaperIterator = originalAbsolutePaths.iterator();
		while (wallpaperIterator.hasNext()) {
			String currentWallpaperPath = wallpaperIterator.next();
			File originalWallpaper = new File(currentWallpaperPath);
			String wallpaperOriginalName = originalWallpaper.getName();
			int index = wallpaperOriginalName.indexOf("-");
			String wallpaperFavouriteName = WD_FAVOURITE_PREFIX + wallpaperOriginalName.substring(index + 1);
			File favouriteWallpaper = new File(WDUtilities.getDownloadsPath() + File.separatorChar + wallpaperFavouriteName);
			originalWallpaper.renameTo(favouriteWallpaper);
			// Information
			DialogManager info = new DialogManager(wallpaperOriginalName + " wallpaper set as favourite.", 2000);
			info.openDialog();
			WallpaperDownloader.refreshJScrollPane();
			LOG.info(currentWallpaperPath + " wallpaper has been set as favourite");
		}		
	}

	/**
	 * Set wallpapers as no favorite
	 * @param originalAbsolutePath
	 */
	public static void setNoFavorite(List<String> originalAbsolutePaths) {
		Iterator<String> wallpaperIterator = originalAbsolutePaths.iterator();
		while (wallpaperIterator.hasNext()) {
			String currentWallpaperPath = wallpaperIterator.next();
			File originalWallpaper = new File(currentWallpaperPath);
			String wallpaperOriginalName = originalWallpaper.getName();
			int index = wallpaperOriginalName.indexOf("-");
			String wallpaperNoFavouriteName = WD_PREFIX + wallpaperOriginalName.substring(index + 1);
			File favouriteWallpaper = new File(WDUtilities.getDownloadsPath() + File.separatorChar + wallpaperNoFavouriteName);
			originalWallpaper.renameTo(favouriteWallpaper);
			// Information
			DialogManager info = new DialogManager(wallpaperOriginalName + " wallpaper set as no favourite.", 2000);
			info.openDialog();
			WallpaperDownloader.refreshJScrollPane();
			LOG.info(currentWallpaperPath + " wallpaper has been set as no favourite");
		}	
	}

	/**
	 * Remove wallpapers
	 * @param originalAbsolutePaths
	 */
	public static void removeWallpaper(List<String> originalAbsolutePaths) {
		Iterator<String> wallpaperIterator = originalAbsolutePaths.iterator();
		while (wallpaperIterator.hasNext()) {
			String currentWallpaperPath = wallpaperIterator.next();
			File originalWallpaper = new File(currentWallpaperPath);
			try {
				FileUtils.forceDelete(originalWallpaper);
				// Information
				DialogManager info = new DialogManager(currentWallpaperPath + " wallpaper removed.", 2000);
				info.openDialog();
				WallpaperDownloader.refreshJScrollPane();
				LOG.info(currentWallpaperPath + " wallpaper has been removed");
			} catch (IOException e) {
				LOG.error("The wallpaper " + currentWallpaperPath + " couldn't be removed. Error: " + e.getMessage());
			}
		}		
	}

	/**
	 * Get all wallpapers downloaded sorted by date
	 * @return
	 */
	public static File[] getAllWallpapersSortedByDate(String wallpapersType) {
		List<File> wallpaperList = getAllWallpapers(wallpapersType);
		File[] wallpapers = new File[wallpaperList.size()];
		wallpapers = wallpaperList.toArray(wallpapers);
		Arrays.sort(wallpapers, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
		return wallpapers;
	}
	
	/**
	 * Get the image icon fo the last numWallpapers wallpapers downloaded
	 */
	public static ImageIcon[] getImageIconWallpapers(int numWallpapers, int from, String sort, String wallpapersType) {
		File[] wallpapers = {};
		int to = from + numWallpapers;
		int j = 0;
		
		if (sort.equals(SORTING_BY_DATE)) {
			wallpapers = getAllWallpapersSortedByDate(wallpapersType);
		} else {
			List<File> wallpapersList = getAllWallpapers(wallpapersType);
			wallpapers = wallpapersList.toArray(wallpapers);
		}
		
		int wallpapersLength = wallpapers.length;
		if (to > wallpapersLength) {
			to = wallpapersLength;
		}
		ImageIcon[] wallpaperIcons = new ImageIcon[numWallpapers];
		for (int i = from; i < to; i++) {
			ImageIcon originalIcon = new ImageIcon(wallpapers[wallpapersLength - (i + 1)].getAbsolutePath());
			Image img = originalIcon.getImage();
			Image newimg = img.getScaledInstance(127, 100,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon resizedIcon = new ImageIcon(newimg);
			// Setting a description (absolute path). Doing this. it will be possible to know this path later
			resizedIcon.setDescription(wallpapers[wallpapersLength - (i + 1)].getAbsolutePath());
			wallpaperIcons[j] = resizedIcon;
			j ++;
		}
		return wallpaperIcons;
	}
	
	/**
	 * It picks a random file from wallpapers download directory
	 * @return
	 */
	public static File pickRandomFile() {
		List<File> files = WDUtilities.getAllWallpapers(WDUtilities.WD_PREFIX);
		if (!files.isEmpty()) {
			Random generator = new Random();
			int index = generator.nextInt(files.size());
			return files.get(index);			
		} else {
			return null;
		}
		
	}
}
