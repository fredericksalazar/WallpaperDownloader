package es.estoes.wallpaperDownloader.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;

import es.estoes.wallpaperDownloader.changer.WallpaperChanger;
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
	public static final String WD_FAVORITE_PREFIX = "fwd-";
	public static final String SORTING_BY_DATE = "sort_by_date";
	public static final String SORTING_NO_SORTING = "no_sorting";
	public static final String WD_ALL = "all";
	public static final int STATUS_CODE_200 = 200;
	public static final CharSequence SNAP_KEY = "snap";
	public static final String OS_LINUX = "Linux";
	public static final String OS_WINDOWS = "Windows";
	public static final String OS_WINDOWS_7 = "Windows 7";
	public static final String OS_UNKNOWN = "UNKNOWN";
	public static final String DE_UNITY = "Unity";
	public static final String DE_GNOME = "GNOME";
	public static final String DE_KDE = "KDE";
	public static final String DE_MATE = "MATE";
	public static final String DE_UNKNOWN = "UNKNOWN";
	public static final Object GDM_SESSION_GNOME = "gnome";
	public static final String GDM_SESSION_GNOME_SHELL = "gnome-shell";
	public static final String GDM_SESSION_GNOME_CLASSIC = "gnome-classic";
	public static final String GDM_SESSION_GNOME_FALLBACK = "gnome-fallback";
	public static final String DE_GNOME3 = "GNOME3";
	public static final String DE_GNOME2 = "GNOME2";

	// Attributes
	private static String appPath;
	private static String downloadsPath;
	private static String userConfigurationFilePath;
	private static String operatingSystem;
	private static WallpaperChanger wallpaperChanger;

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
	
	public static String getBlacklistDirectoryPath() {
		PropertiesManager pm = PropertiesManager.getInstance();
		Path blacklistPath = Paths.get(appPath.toString());
		blacklistPath = blacklistPath.resolve(pm.getProperty("app.blacklist.path"));
		return blacklistPath.toString();
	}

	public static String getOperatingSystem() {
		return operatingSystem;
	}

	public static void setOperatingSystem(String operatingSystem) {
		WDUtilities.operatingSystem = operatingSystem;
	}

	public static WallpaperChanger getWallpaperChanger() {
		return wallpaperChanger;
	}

	public static void setWallpaperChanger(WallpaperChanger wallpaperChanger) {
		WDUtilities.wallpaperChanger = wallpaperChanger;
	}
	
	// Methods (All the methods are static)

	/**
	 * Constructor
	 */
	private WDUtilities() {

	}

	/**
	 * Get screen resolution.
	 * @return
	 */
	public static String getResolution() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();
		return width.intValue() + "x" + height.intValue();
	}

	/**
	 * Get all the wallpapers from the download directory.
	 * @param wallpapersType Type of the wallpapers wanted to retrieve (favorite, non favorite, all)
	 * @return
	 */
	public static List<File> getAllWallpapers(String wallpapersType) {
		LOG.info("Getting all the wallpapers...");
		File downloadDirectory = new File(WDUtilities.getDownloadsPath());
		List<File> wallpapers = new ArrayList<File>();
		if (wallpapersType.equals(WDUtilities.WD_ALL)) {
			wallpapers = (List<File>) FileUtils.listFiles(downloadDirectory, FileFilterUtils.prefixFileFilter(WDUtilities.WD_FAVORITE_PREFIX), null);
			wallpapers.addAll((List<File>) FileUtils.listFiles(downloadDirectory, FileFilterUtils.prefixFileFilter(WDUtilities.WD_PREFIX), null));
		} else {
			wallpapers = (List<File>) FileUtils.listFiles(downloadDirectory, FileFilterUtils.prefixFileFilter(wallpapersType), null);
			
			if (wallpapersType.equals(WDUtilities.WD_FAVORITE_PREFIX)) {
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
	 * Move all the wallpapers to a new location.
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
	 * Calculates the percentage of the space occupied within the directory.
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
	 * Calculate the space occupied within the directory.
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
	 * Set wallpapers as favorite.
	 * @param originalAbsolutePath
	 */
	public static void setFavorite(List<String> originalAbsolutePaths) {
		Iterator<String> wallpaperIterator = originalAbsolutePaths.iterator();
		while (wallpaperIterator.hasNext()) {
			String currentWallpaperPath = wallpaperIterator.next();
			File originalWallpaper = new File(currentWallpaperPath);
			String wallpaperOriginalName = originalWallpaper.getName();
			int index = wallpaperOriginalName.indexOf("-");
			String wallpaperFavoriteName = WD_FAVORITE_PREFIX + wallpaperOriginalName.substring(index + 1);
			File favoriteWallpaper = new File(WDUtilities.getDownloadsPath() + File.separatorChar + wallpaperFavoriteName);
			originalWallpaper.renameTo(favoriteWallpaper);
			// Information
			DialogManager info = new DialogManager(wallpaperOriginalName + " wallpaper set as favorite.", 2000);
			info.openDialog();
			WallpaperDownloader.refreshJScrollPane();
			LOG.info(currentWallpaperPath + " wallpaper has been set as favorite");
		}		
	}

	/**
	 * Set wallpapers as no favorite.
	 * @param originalAbsolutePath
	 */
	public static void setNoFavorite(List<String> originalAbsolutePaths) {
		Iterator<String> wallpaperIterator = originalAbsolutePaths.iterator();
		while (wallpaperIterator.hasNext()) {
			String currentWallpaperPath = wallpaperIterator.next();
			File originalWallpaper = new File(currentWallpaperPath);
			String wallpaperOriginalName = originalWallpaper.getName();
			int index = wallpaperOriginalName.indexOf("-");
			String wallpaperNoFavoriteName = WD_PREFIX + wallpaperOriginalName.substring(index + 1);
			File favoriteWallpaper = new File(WDUtilities.getDownloadsPath() + File.separatorChar + wallpaperNoFavoriteName);
			originalWallpaper.renameTo(favoriteWallpaper);
			// Information
			DialogManager info = new DialogManager(wallpaperOriginalName + " wallpaper set as no favorite.", 2000);
			info.openDialog();
			WallpaperDownloader.refreshJScrollPane();
			LOG.info(currentWallpaperPath + " wallpaper has been set as no favorite");
		}	
	}

	/**
	 * Remove wallpapers.
	 * @param originalAbsolutePaths
	 */
	public static void removeWallpaper(List<String> originalAbsolutePaths) {
		Iterator<String> wallpaperIterator = originalAbsolutePaths.iterator();
		while (wallpaperIterator.hasNext()) {
			String currentWallpaperPath = wallpaperIterator.next();
			File originalWallpaper = new File(currentWallpaperPath);
			try {
				// 1.- File is deleted
				FileUtils.forceDelete(originalWallpaper);
				// 2.- File is added to the blacklist
				WDUtilities.addToBlacklist(originalWallpaper.getName());
				// 3.- Information about deleting is displayed
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
	 * Adds a file to the blacklist directory.
	 * @param fileName the name of the file
	 */
	public static void addToBlacklist(String fileName) {
		File blacklistedFile = new File(WDUtilities.getBlacklistDirectoryPath() + File.separator + fileName);
		try {
			FileUtils.touch(blacklistedFile);
			if (LOG.isInfoEnabled()) {
				LOG.info(fileName + " added to the blacklist");	
			}
		} catch (IOException exception) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Error adding " + fileName + " to the blacklist. Error: " + exception.getMessage());	
			}
		}
	}

	/**
	 * Get all wallpapers downloaded sorted by date.
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
 * Get the image icon fo the last numWallpapers wallpapers downloaded.
 * @param numWallpapers number of wallpapers
 * @param from first wallpaper to get
 * @param sort sorting
 * @param wallpapersType type of the wallpapers to get (favorite, no favorite)
 * @return
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
	 * It picks a random file from wallpapers download directory.
	 * @param includeFavoriteWallpapers if it is set to TRUE, this method will pick favorite and not favorite wallpapers
	 * @return
	 */
	public static File pickRandomFile(Boolean includeFavoriteWallpapers) {
		String wallpapersType = WDUtilities.WD_ALL;
		if (!includeFavoriteWallpapers) {
			wallpapersType = WDUtilities.WD_PREFIX;
		}
		List<File> files = WDUtilities.getAllWallpapers(wallpapersType);
		if (!files.isEmpty()) {
			Random generator = new Random();
			int index = generator.nextInt(files.size());
			return files.get(index);			
		} else {
			return null;
		}
		
	}

	/**
	 * Checks if a wallpaper is blacklisted.
	 * @param wallpaperName wallpaper name
	 * @return boolean
	 */
	public static boolean isWallpaperBlacklisted(String wallpaperName) {
		File blacklistedFile = new File(WDUtilities.getBlacklistDirectoryPath() + File.separator + wallpaperName);
		boolean result = blacklistedFile.exists();
		if (LOG.isInfoEnabled()) {
			LOG.info("Is " + wallpaperName + " blacklisted?: " + result + ".PS: Only wallpapers not blacklisted will be stored");
		}
		return result;
	}
	
	/**
	 * Checks if a wallpaper is favorite.
	 * @param wallpaperAbsolutePath wallpaper absolute path
	 * @return boolean
	 */
	public static boolean isWallpaperFavorite(String wallpaperAbsolutePath) {
		if (wallpaperAbsolutePath.contains(WD_FAVORITE_PREFIX)) {
			return true;
		} else {
			return false;
		}
	}

}
