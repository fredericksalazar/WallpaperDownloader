package es.estoes.wallpaperDownloader.window;

import java.awt.EventQueue;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import es.estoes.wallpaperDownloader.harvest.Harvester;
import es.estoes.wallpaperDownloader.item.ComboItem;
import es.estoes.wallpaperDownloader.util.FavouriteFileFilter;
import es.estoes.wallpaperDownloader.util.NoFavouriteFileFilter;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.PropertiesManager;
import es.estoes.wallpaperDownloader.util.WDConfigManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.util.WallpaperListRenderer;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import javax.swing.JComboBox;
import java.text.Format;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;

public class WallpaperDownloader {

	// Constants
	protected static final Logger LOG = Logger.getLogger(WallpaperDownloader.class);
	private static WallpaperDownloader window;
	
	// Attributes
	
	// diskSpacePB will be an attribute representing disk space occupied within the downloads directory
	// It is static because it will be able to be accessed from any point within the application's code
	public static JProgressBar diskSpacePB = new JProgressBar();
	public static JLabel lblSpaceWarning;
	public static JScrollPane scroll;
	public static JList<ImageIcon> lastWallpapersList;
	
	private Harvester harvester;
	private JFrame frame;
	private JTextField wallhavenKeywords;
	private JCheckBox wallhavenCheckbox;
	private JButton btnApply;
	private JButton btnCloseExit;
	private JButton btnMinimize;
	private JButton btnOpenDownloadsDirectory;
	private JButton btnClipboard;
	private JComboBox<ComboItem> searchTypeComboBox;
	private JFormattedTextField wallhavenWidthResolution;
	private NumberFormat integerFormat;
	private JLabel lblX;
	private JFormattedTextField wallhavenHeigthResolution;
	private JCheckBox allResolutionsCheckbox;
	private JComboBox<ComboItem> timerComboBox;
	private JFormattedTextField downloadDirectorySize;
	private JPanel miscPanel;
	private JPanel miscPanel_1;
	private JFormattedTextField downloadsDirectory;
	private JButton btnChangeDownloadsDirectory;
	private JButton btnManageNoFavouriteWallpapers;
	private JButton btnManageFavouriteWallpapers;
	private JButton btnRemoveWallpapers;
	private JButton btnRemoveWallpaper;
	private JButton btnSetFavouriteWallpaper;
	
	
	// Getters & Setters
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public Harvester getHarvester() {
		return harvester;
	}

	public void setHarvester(Harvester harvester) {
		this.harvester = harvester;
	}
	
	public JFormattedTextField getDownloadsDirectory() {
		return downloadsDirectory;
	}

	public void setDownloadsDirectory(JFormattedTextField downloadsDirectory) {
		this.downloadsDirectory = downloadsDirectory;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// 1.- Log configuration
				WDConfigManager.configureLog();

				// 2.- Application configuration
				WDConfigManager.checkConfig();
				PropertiesManager pm = PropertiesManager.getInstance();
				window = new WallpaperDownloader();
				window.frame.setVisible(true);
				window.frame.setTitle(pm.getProperty("app.name") + " V" + pm.getProperty("app.version"));
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WallpaperDownloader() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Configuring tooltips
		ToolTipManager.sharedInstance().setInitialDelay(100);
		
		// Configuring frames
		frame = new JFrame();
		frame.setBounds(100, 100, 690, 440);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{116, 386, 73, 96, 0};
		gridBagLayout.rowHeights = new int[]{338, 53, 25, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - frame.getWidth()) / 2;
		int y = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(x, y);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 2;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.gridwidth = 4;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		// Providers (tab)
		JPanel providersPanel = new JPanel();
		tabbedPane.addTab("Providers", null, providersPanel, null);
		providersPanel.setLayout(null);

		wallhavenCheckbox = new JCheckBox("Wallhaven.cc");
		wallhavenCheckbox.setBounds(8, 8, 129, 23);
		providersPanel.add(wallhavenCheckbox);

		JLabel lblKeywords = new JLabel("Keywords");
		lblKeywords.setBounds(12, 39, 70, 15);
		providersPanel.add(lblKeywords);
		
		wallhavenKeywords = new JTextField();
		wallhavenKeywords.setBounds(100, 37, 255, 19);
		providersPanel.add(wallhavenKeywords);
		wallhavenKeywords.setColumns(10);
		
		JLabel lblResolution = new JLabel("Resolution");
		lblResolution.setBounds(12, 70, 94, 15);
		providersPanel.add(lblResolution);
		
		// Only integers will be allowed
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);
		wallhavenWidthResolution = new JFormattedTextField(integerFormat);
		wallhavenWidthResolution.setColumns(4);
		wallhavenWidthResolution.setBounds(100, 68, 49, 19);
		providersPanel.add(wallhavenWidthResolution);
		
		searchTypeComboBox = new JComboBox<ComboItem>();
		searchTypeComboBox.setBounds(528, 68, 129, 19);
		providersPanel.add(searchTypeComboBox);
		
		JLabel lblSearchType = new JLabel("Search Type");
		lblSearchType.setBounds(431, 71, 94, 15);
		providersPanel.add(lblSearchType);
		
		lblX = new JLabel("x");
		lblX.setBounds(151, 72, 12, 15);
		providersPanel.add(lblX);
		
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);
		wallhavenHeigthResolution = new JFormattedTextField(integerFormat);
		wallhavenHeigthResolution.setColumns(4);
		wallhavenHeigthResolution.setBounds(161, 68, 49, 19);
		providersPanel.add(wallhavenHeigthResolution);
		
		allResolutionsCheckbox = new JCheckBox("All Resolutions");
		allResolutionsCheckbox.setBounds(224, 66, 151, 23);
		providersPanel.add(allResolutionsCheckbox);
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			JLabel lblKeywordsHelp = new JLabel(icon);
			lblKeywordsHelp.setToolTipText("Each keyword must be separated by ';'. If it is empty then it will search any wallpaper");
			lblKeywordsHelp.setBounds(358, 35, 30, 23);
			providersPanel.add(lblKeywordsHelp);
		} catch (IOException ex) {
			JLabel lblKeywordsHelp = new JLabel("(separated by ;) (Empty->All wallpapers)");
			lblKeywordsHelp.setBounds(362, 39, 70, 15);
			providersPanel.add(lblKeywordsHelp);
		}
		
		// Application Settings (tab)
		JPanel appSettingsPanel = new JPanel();
		tabbedPane.addTab("Application Settings", null, appSettingsPanel, null);
		appSettingsPanel.setLayout(null);
		
		JLabel lblTimer = new JLabel("Wallpaper Downloader will download a new wallpaper every");
		lblTimer.setBounds(12, 12, 439, 19);
		appSettingsPanel.add(lblTimer);
		
		timerComboBox = new JComboBox<ComboItem>();
		timerComboBox.setBounds(455, 12, 94, 19);
		appSettingsPanel.add(timerComboBox);
		
		JLabel lblDownloadDirectorySize = new JLabel("Maximun size for download directory (MB)");
		lblDownloadDirectorySize.setBounds(12, 36, 304, 19);
		appSettingsPanel.add(lblDownloadDirectorySize);
		
		downloadDirectorySize = new JFormattedTextField(integerFormat);
		downloadDirectorySize.setColumns(4);
		downloadDirectorySize.setBounds(317, 37, 49, 19);
		appSettingsPanel.add(downloadDirectorySize);
		
		// Downloads Directory (tab)
		miscPanel = new JPanel();
		tabbedPane.addTab("Downloads Directory", null, miscPanel, null);
		miscPanel.setLayout(null);
		
		JLabel lblDownloadsDirectory = new JLabel("Downloads Directory:");
		lblDownloadsDirectory.setBounds(12, 16, 160, 19);
		miscPanel.add(lblDownloadsDirectory);
		
		downloadsDirectory = new JFormattedTextField((Format) null);
		downloadsDirectory.setEditable(false);
		downloadsDirectory.setColumns(4);
		downloadsDirectory.setBounds(174, 18, 405, 19);
		miscPanel.add(downloadsDirectory);
		
		btnOpenDownloadsDirectory = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/open_folder_24px_icon.png"));
			btnOpenDownloadsDirectory.setIcon(new ImageIcon(img));
			btnOpenDownloadsDirectory.setToolTipText("Open downloads directory");
			btnOpenDownloadsDirectory.setBounds(588, 8, 34, 33);
		} catch (IOException ex) {
			btnOpenDownloadsDirectory.setText("Open");
			btnOpenDownloadsDirectory.setBounds(589, 11, 72, 25);
		}		
		miscPanel.add(btnOpenDownloadsDirectory);
		
		btnClipboard = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/clipboard_24px_icon.png"));
			btnClipboard.setIcon(new ImageIcon(img));
			btnClipboard.setToolTipText("Copy downloads directory path into the Clipboard");
			btnClipboard.setBounds(630, 8, 34, 33);
		} catch (IOException ex) {
			btnClipboard.setText("Clipboard");
			btnClipboard.setBounds(630, 8, 34, 33);
		}
		miscPanel.add(btnClipboard);
		
		btnChangeDownloadsDirectory = new JButton("Change Downloads Directory");
		btnChangeDownloadsDirectory.setBounds(12, 90, 259, 25);
		miscPanel.add(btnChangeDownloadsDirectory);
		
		//JProgressBar diskSpacePB = new JProgressBar();
		diskSpacePB.setBounds(174, 56, 405, 18);
		miscPanel.add(diskSpacePB);
		
		JLabel lblDiskSpace = new JLabel("Downloads dir space:");
		lblDiskSpace.setBounds(12, 55, 160, 19);
		miscPanel.add(lblDiskSpace);
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/warning_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblSpaceWarning = new JLabel(icon);
			lblSpaceWarning.setToolTipText("Directory full. Wallpapers (execpt favourite ones) will be removed randomly in order to download more.");
			lblSpaceWarning.setBounds(588, 53, 30, 23);
			miscPanel.add(lblSpaceWarning);
			// At first, the label won't be visible
			lblSpaceWarning.setVisible(false);
		} catch (IOException ex) {
			lblSpaceWarning = new JLabel("Directory full. Wallpapers will be removed randomly");
			lblSpaceWarning.setBounds(588, 53, 30, 23);
			miscPanel.add(lblSpaceWarning);
			// At first, the label won't be visible
			lblSpaceWarning.setVisible(false);
		}
		
		// Wallpapers (tab)
		miscPanel_1 = new JPanel();
		tabbedPane.addTab("Wallpapers", null, miscPanel_1, null);
		miscPanel_1.setLayout(null);

		ImageIcon[] wallpapers = {};
		lastWallpapersList = new JList<ImageIcon>(wallpapers);
		
		JLabel lblLastWallpapers = new JLabel("Last 5 wallpapers downloaded");
		lblLastWallpapers.setBounds(12, 12, 238, 15);
		miscPanel_1.add(lblLastWallpapers);
		
        scroll = new JScrollPane();
        scroll.setPreferredSize(new Dimension(300, 400));
		scroll.setBounds(12, 36, 652, 105);
		miscPanel_1.add(scroll);
		
		btnManageNoFavouriteWallpapers = new JButton("Set favourite wallpapers");
		btnManageNoFavouriteWallpapers.setBackground(Color.WHITE);
		btnManageNoFavouriteWallpapers.setBounds(12, 244, 268, 25);
		miscPanel_1.add(btnManageNoFavouriteWallpapers);
		
		btnManageFavouriteWallpapers = new JButton("Set no favourite wallpapers");
		btnManageFavouriteWallpapers.setBackground(Color.WHITE);
		btnManageFavouriteWallpapers.setBounds(12, 274, 268, 25);
		miscPanel_1.add(btnManageFavouriteWallpapers);
		
		btnRemoveWallpapers = new JButton("Remove wallpapers");
		btnRemoveWallpapers.setBackground(Color.WHITE);
		btnRemoveWallpapers.setBounds(12, 303, 268, 25);
		miscPanel_1.add(btnRemoveWallpapers);
		
		btnRemoveWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveWallpaper.setIcon(new ImageIcon(img));
			btnRemoveWallpaper.setToolTipText("Delete selected wallpaper");
			btnRemoveWallpaper.setBounds(12, 149, 34, 33);
		} catch (IOException ex) {
			btnRemoveWallpaper.setText("Delete");
			btnRemoveWallpaper.setBounds(12, 149, 34, 33);
		}
		miscPanel_1.add(btnRemoveWallpaper);
		
		btnSetFavouriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/favourite_24px_icon.png"));
			btnSetFavouriteWallpaper.setIcon(new ImageIcon(img));
			btnSetFavouriteWallpaper.setToolTipText("Set selected wallpaper as favourite");
			btnSetFavouriteWallpaper.setBounds(58, 149, 34, 33);
		} catch (IOException ex) {
			btnSetFavouriteWallpaper.setText("Set as favaourite");
			btnSetFavouriteWallpaper.setBounds(58, 149, 34, 33);
		}
		miscPanel_1.add(btnSetFavouriteWallpaper);
		
		JLabel lblBatchTasks = new JLabel("Wallpaper batch tasks");
		lblBatchTasks.setBounds(12, 221, 238, 15);
		miscPanel_1.add(lblBatchTasks);
		
		// Global buttons
		btnCloseExit = new JButton("Close & Exit");
		GridBagConstraints gbc_btnCloseExit = new GridBagConstraints();
		gbc_btnCloseExit.anchor = GridBagConstraints.WEST;
		gbc_btnCloseExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnCloseExit.gridx = 0;
		gbc_btnCloseExit.gridy = 2;
		frame.getContentPane().add(btnCloseExit, gbc_btnCloseExit);		
		
		btnApply = new JButton("Apply");
		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnApply.insets = new Insets(0, 0, 5, 5);
		gbc_btnApply.gridx = 2;
		gbc_btnApply.gridy = 2;
		frame.getContentPane().add(btnApply, gbc_btnApply);
		
		btnMinimize = new JButton("Minimize");
		btnMinimize.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnMinimize = new GridBagConstraints();
		gbc_btnMinimize.insets = new Insets(0, 0, 5, 0);
		gbc_btnMinimize.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnMinimize.gridx = 3;
		gbc_btnMinimize.gridy = 2;
		frame.getContentPane().add(btnMinimize, gbc_btnMinimize);
		
		// Setting up configuration
		initializeGUI();
		
		// Setting up listeners
		initializeListeners();
		
		// Starting the application
		initializeHarvesting();
	}

	/**
	 * This method configures all the listeners
	 */
	private void initializeListeners() {
		
		final PreferencesManager prefm = PreferencesManager.getInstance();
		
		// Listeners
		/**
		 * wallhavenCheckbox Action Listeners
		 */
		// Clicking event
		wallhavenCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (wallhavenCheckbox.isSelected()) {
					wallhavenKeywords.setEnabled(true);
					wallhavenWidthResolution.setEnabled(true);
					wallhavenHeigthResolution.setEnabled(true);
					searchTypeComboBox.setEnabled(true);
					allResolutionsCheckbox.setEnabled(true);
					allResolutionsCheckbox.setSelected(false);
					String screenResolution = WDUtilities.getResolution();
					String[] resolution = screenResolution.split("x");
			        wallhavenWidthResolution.setValue(new Integer(resolution[0]));
					wallhavenHeigthResolution.setValue(new Integer(resolution[1]));
				} else {
					wallhavenKeywords.setEnabled(false);
					wallhavenWidthResolution.setEnabled(false);
					wallhavenHeigthResolution.setEnabled(false);
					searchTypeComboBox.setEnabled(false);
					allResolutionsCheckbox.setEnabled(false);
				}
			}
		});

		/**
		 * allResolutionsCheckbox Action Listeners
		 */
		// Clicking event
		allResolutionsCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (allResolutionsCheckbox.isSelected()) {
					wallhavenWidthResolution.setValue(new Integer(0));
					wallhavenHeigthResolution.setValue(new Integer(0));
					wallhavenWidthResolution.setEnabled(false);
					wallhavenHeigthResolution.setEnabled(false);
				} else {
					String screenResolution = WDUtilities.getResolution();
					String[] resolution = screenResolution.split("x");
			        wallhavenWidthResolution.setValue(new Integer(resolution[0]));
					wallhavenHeigthResolution.setValue(new Integer(resolution[1]));
					wallhavenWidthResolution.setEnabled(true);
					wallhavenHeigthResolution.setEnabled(true);
				}
			}
		});
		
		/**
		 * btnCloseExit Action Listeners
		 */
		// Clicking event
		btnCloseExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// The application is closed
				System.exit(0);
			}
		});

		/**
		 * btnMinimize Action Listeners
		 */
		// Clicking event
		btnMinimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// The application is minimized within System Tray
		        //Check the SystemTray is supported
		        if (!SystemTray.isSupported()) {
		            LOG.error("SystemTray is not supported");
		            return;
		        } else {
		            final PopupMenu popup = new PopupMenu();
		            URL systemTrayIcon = WallpaperDownloader.class.getResource("/images/icons/wd_systemtray_icon.png");
		            final TrayIcon trayIcon = new TrayIcon(new ImageIcon(systemTrayIcon, "Wallpaper Downloader").getImage(), "Wallpaper Downloader");
		            final SystemTray tray = SystemTray.getSystemTray();
		           
		            // Create a pop-up menu components -- BEGIN
		            MenuItem maximizeItem = new MenuItem("Maximize");
		            maximizeItem.addActionListener(new ActionListener() {
		            	public void actionPerformed(ActionEvent evt) {
		                	int state = window.frame.getExtendedState();  
		                	state = state & ~Frame.ICONIFIED;  
		                	window.frame.setExtendedState(state);  
		                	window.frame.setVisible(true);
		                	
		                	// Removing system tray icon
		                	tray.remove(trayIcon);
		            	}
		            });
		            MenuItem browseItem = new MenuItem("Open downloaded wallpapers");
		            browseItem.addActionListener(new ActionListener() {
		            	public void actionPerformed(ActionEvent evt) {
		            		File downloadsDirectory = new File(WDUtilities.getDownloadsPath());
		            		Desktop desktop = Desktop.getDesktop();
		            		try {
								desktop.open(downloadsDirectory);
							} catch (IOException e) {
								// There was some error trying to open the downloads Directory
								LOG.error("Error trying to open the Downloads directory. Error: " + e.getMessage());
							}
		            	}
		            });
		            MenuItem exitItem = new MenuItem("Exit");
		            exitItem.addActionListener(new ActionListener() {
		            	public void actionPerformed(ActionEvent evt) {
		                	// Removing system tray icon
		                	tray.remove(trayIcon);

		    				// The application is closed
		    				System.exit(0);		                	
		            	}
		            });
		            // Create a pop-up menu components -- END
		           
		            //Add components to pop-up menu
		            popup.add(maximizeItem);
		            popup.add(browseItem);
		            popup.addSeparator();
		            popup.add(exitItem);
		           
		            trayIcon.setPopupMenu(popup);
		            
		            // Adding a new event. When the user clicks the left button the application window is restored again in the same
		            // state
		            MouseAdapter mouseAdapter = new MouseAdapter() {

		                @Override
		                public void mouseClicked(MouseEvent e) {
		                	int state = window.frame.getExtendedState();  
		                	state = state & ~Frame.ICONIFIED;  
		                	window.frame.setExtendedState(state);  
		                	window.frame.setVisible(true);
		                	
		                	// Removing system tray icon
		                	tray.remove(trayIcon);
		                }
		            };
		            trayIcon.addMouseListener(mouseAdapter);
		            trayIcon.setImageAutoSize(true);
		           
		            try {
		                tray.add(trayIcon);
		            } catch (AWTException e) {
		                LOG.error("TrayIcon could not be added.");
		            }
		            
		            // Hiding window
		            window.frame.setVisible(false);
		        }
			}		
		});
		
		/**
		 * btnApply Action Listeners
		 */
		// Clicking event
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Storing user settings
				// ---------------------------------------------------------------------------
				// Providers
				// ---------------------------------------------------------------------------
				// Wallhaven.cc
				if (wallhavenCheckbox.isSelected()) {
					prefm.setPreference("provider-wallhaven", WDUtilities.APP_YES);
					if (!wallhavenKeywords.getText().isEmpty()) {
						prefm.setPreference("provider-wallhaven-keywords", wallhavenKeywords.getText());					
					} else {
						prefm.setPreference("provider-wallhaven-keywords", PreferencesManager.DEFAULT_VALUE);
					}
					if (allResolutionsCheckbox.isSelected()) {
						prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);						
					} else {
						prefm.setPreference("wallpaper-resolution", wallhavenWidthResolution.getValue().toString() + "x" + wallhavenHeigthResolution.getValue().toString());						
					}
					prefm.setPreference("wallpaper-search-type", new Integer(searchTypeComboBox.getSelectedIndex()).toString());

				} else {
					prefm.setPreference("provider-wallhaven", WDUtilities.APP_NO);
					prefm.setPreference("provider-wallhaven-keywords", PreferencesManager.DEFAULT_VALUE);
					prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);
					prefm.setPreference("wallpaper-search-type", "3");
				}
				// ---------------------------------------------------------------------------
				// User settings
				// ---------------------------------------------------------------------------
				prefm.setPreference("application-timer", new Integer(timerComboBox.getSelectedIndex()).toString());
				prefm.setPreference("application-max-download-folder-size", downloadDirectorySize.getValue().toString());
				
				// Stopping and starting harvesting process
				harvester.stop();
				harvester.start();
				
				// Information
				DialogManager info = new DialogManager("Changes applied. Downloading process started.", 2000);
				info.openDialog();
			}
		});	
		
		/**
		 * btnOpenDownloadsDirectory Action Listeners
		 */
		// Clicking event
		btnOpenDownloadsDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// It Opens the downloads directory using the default file manager
        		File downloadsDirectory = new File(WDUtilities.getDownloadsPath());
        		Desktop desktop = Desktop.getDesktop();
        		try {
					desktop.open(downloadsDirectory);
				} catch (IOException e) {
					// There was some error trying to open the downloads Directory
					LOG.error("Error trying to open the Downloads directory. Error: " + e.getMessage());
				}
			}
		});

		/**
		 * btnClipboard Action Listeners
		 */
		// Clicking event
		btnClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Copy downloads directory path into the clipboard
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection data = new StringSelection(downloadsDirectory.getText());
				clipboard.setContents(data, data);
				DialogManager info = new DialogManager("The downloads directory path was copied to the clipboard", 2000);
				info.openDialog();
			}
		});
		
		/**
		 * btnChangeDownloadsDirectory Action Listeners
		 */
		btnChangeDownloadsDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DownloadsPathChangerWindow downloadsPathChangerWindow = new DownloadsPathChangerWindow(window);
				downloadsPathChangerWindow.setVisible(true);
			}
		});
		
		/**
		 * btnManageNoFavouriteWallpapers Action Listeners
		 */
		btnManageNoFavouriteWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Set Locale to English
				JComponent.setDefaultLocale(Locale.ENGLISH);
				// New folder button disabled
				UIManager.put("FileChooser.readOnly", Boolean.TRUE);
				ImagePreviewJFileChooser ipjc = new ImagePreviewJFileChooser();
				// Setting filter for displaying only no favourite wallpapers
				ipjc.setFileFilter(new NoFavouriteFileFilter());
		    	// Setting title
				ipjc.setDialogTitle("Choose a wallpaper to set as favourite");
		    	// Setting approve button text and tooltip
		    	ipjc.setApproveButtonText("Set favourite");
		    	ipjc.setApproveButtonToolTipText("Set the selected wallpapers as favourite");
		    	// Enabling multi-selection
		    	ipjc.setMultiSelectionEnabled(true);
		    	
			    if (ipjc.showOpenDialog(null) == ImagePreviewJFileChooser.APPROVE_OPTION) {
			    	List<File> filesSelected = Arrays.asList(ipjc.getSelectedFiles());
			    	Iterator<File> file = filesSelected.iterator();
			    	
			    	while (file.hasNext()) {
			    		WDUtilities.setFavourite(file.next().getAbsolutePath());
					}			    	
			    }
			    // New folder button enabled
			    UIManager.put("FileChooser.readOnly", Boolean.FALSE);
			}
		});
		
		/**
		 * btnManageFavouriteWallpapers Action Listeners
		 */
		btnManageFavouriteWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Set Locale to English
				JComponent.setDefaultLocale(Locale.ENGLISH);
				// New folder button disabled
				UIManager.put("FileChooser.readOnly", Boolean.TRUE);
				ImagePreviewJFileChooser ipjc = new ImagePreviewJFileChooser();
				// Setting filter for displaying only no favourite wallpapers
				ipjc.setFileFilter(new FavouriteFileFilter());
		    	// Setting title
				ipjc.setDialogTitle("Choose a wallpaper to set as no favourite");
		    	// Setting approve button text and tooltip
		    	ipjc.setApproveButtonText("Set no favourite");
		    	ipjc.setApproveButtonToolTipText("Set the selected wallpapers as no favourite");
		    	// Enabling multi-selection
		    	ipjc.setMultiSelectionEnabled(true);
		    	
			    if (ipjc.showOpenDialog(null) == ImagePreviewJFileChooser.APPROVE_OPTION) {
			    	List<File> filesSelected = Arrays.asList(ipjc.getSelectedFiles());
			    	Iterator<File> file = filesSelected.iterator();
			    	
			    	while (file.hasNext()) {
			    		WDUtilities.setNoFavourite(file.next().getAbsolutePath());
					}			    	
			    }
			    // New folder button enabled
			    UIManager.put("FileChooser.readOnly", Boolean.FALSE);
			}
		});
		
		/**
		 * btnRemoveWallpapers Action Listeners
		 */
		btnRemoveWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Set Locale to English
				JComponent.setDefaultLocale(Locale.ENGLISH);
				// New folder button disabled
				UIManager.put("FileChooser.readOnly", Boolean.TRUE);
				ImagePreviewJFileChooser ipjc = new ImagePreviewJFileChooser();
				// Setting filter for displaying only images
		        FileNameExtensionFilter filter=new FileNameExtensionFilter("Image Files","jpg","jpeg","png","gif");
		        // Set it as current filter
		        ipjc.setFileFilter(filter);
		    	// Setting title
				ipjc.setDialogTitle("Choose a wallpaper to remove");
		    	// Setting approve button text and tooltip
		    	ipjc.setApproveButtonText("Remove");
		    	ipjc.setApproveButtonToolTipText("Remove the wallpapers selected");
		    	// Enabling multi-selection
		    	ipjc.setMultiSelectionEnabled(true);
		    	
			    if (ipjc.showOpenDialog(null) == ImagePreviewJFileChooser.APPROVE_OPTION) {
			    	List<File> filesSelected = Arrays.asList(ipjc.getSelectedFiles());
			    	Iterator<File> file = filesSelected.iterator();
			    	
			    	while (file.hasNext()) {
			    		WDUtilities.removeWallpaper(file.next().getAbsolutePath());
					}			    	
			    }
			    // New folder button enabled
			    UIManager.put("FileChooser.readOnly", Boolean.FALSE);
			}
		});
		
		/**
		 * lastWallpapersList Action Listeners
		 */
		/*
	    ListSelectionListener listSelectionListener = new ListSelectionListener() {
	        @SuppressWarnings("rawtypes")
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
	          System.out.println("First index: " + listSelectionEvent.getFirstIndex());
	          System.out.println(", Last index: " + listSelectionEvent.getLastIndex());
	          boolean adjust = listSelectionEvent.getValueIsAdjusting();
	          System.out.println(", Adjusting? " + adjust);
	          if (!adjust) {
	            JList list = (JList) listSelectionEvent.getSource();
	            int selections[] = list.getSelectedIndices();
	            List selectionValues = list.getSelectedValuesList();
	            for (int i = 0, n = selections.length; i < n; i++) {
	              if (i == 0) {
	                System.out.println(" Selections: ");
	              }
	              System.out.println(selections[i] + "/" + selectionValues.get(i) + " ");
	            }
	          }
	          // Good code bellow
	          
	          JList list = (JList) listSelectionEvent.getSource();
	          int selections[] = list.getSelectedIndices();
	          List selectionValues = list.getSelectedValuesList();
	          for (int i = 0, n = selections.length; i < n; i++) {
	        	  ImageIcon icon = (ImageIcon)selectionValues.get(i);
	        	  DialogManager dm = new DialogManager(icon.getDescription(), 2000);
	        	  dm.openDialog();
		      }
	          
	        }
	        
	      };
	      lastWallpapersList.addListSelectionListener(listSelectionListener);
	      */
		
		  /**
		  * lastWallpapersList Mouse Motion Listeners
		  */	      
	      lastWallpapersList.addMouseMotionListener(new MouseMotionListener() {
    	    public void mouseMoved(MouseEvent e) {
    	        final int x = e.getX();
    	        final int y = e.getY();
    	        // only display a hand if the cursor is over the items
    	        final Rectangle cellBounds = lastWallpapersList.getCellBounds(0, lastWallpapersList.getModel().getSize() - 1);
    	        if (cellBounds != null && cellBounds.contains(x, y)) {
    	        	lastWallpapersList.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	        } else {
    	        	lastWallpapersList.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    	        }
    	    }

    	    public void mouseDragged(MouseEvent e) {
    	    }
	      });	      
	      
		 /**
		  * btnRemoveWallpaper Action Listeners
		  */
	      btnRemoveWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpaper
				ImageIcon wallpaperSelectedIcon = lastWallpapersList.getSelectedValue();
				WDUtilities.removeWallpaper(wallpaperSelectedIcon.getDescription());
			}
	      });
	      
		 /**
		  * btnSetFavouriteWallpaper Action Listeners
		  */
	      btnSetFavouriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpaper
				ImageIcon wallpaperSelectedIcon = lastWallpapersList.getSelectedValue();
				WDUtilities.setFavourite(wallpaperSelectedIcon.getDescription());
			}
		  });	      
	}

	/**
	 * This method configures GUI according to user configuration file preferences
	 */
	private void initializeGUI() {

		final PreferencesManager prefm = PreferencesManager.getInstance();

		// ---------------------------------------------------------------------
		// Checking providers
		// ---------------------------------------------------------------------
		// Wallhaven.cc
		String wallhavenEnable = prefm.getPreference("provider-wallhaven");
		if (wallhavenEnable.equals(WDUtilities.APP_YES)) {
			wallhavenCheckbox.setSelected(true);
			wallhavenKeywords.setEnabled(true);
			if (!prefm.getPreference("provider-wallhaven-keywords").equals(PreferencesManager.DEFAULT_VALUE)) {
				wallhavenKeywords.setText(prefm.getPreference("provider-wallhaven-keywords"));			
			} else {
				wallhavenKeywords.setText("");
			}
			String[] resolution = prefm.getPreference("wallpaper-resolution").split("x");
			if (!prefm.getPreference("wallpaper-resolution").equals(PreferencesManager.DEFAULT_VALUE)) {
		        wallhavenWidthResolution.setValue(new Integer(resolution[0]));
				wallhavenHeigthResolution.setValue(new Integer(resolution[1]));
				allResolutionsCheckbox.setSelected(false);
			} else {
				wallhavenWidthResolution.setValue(new Integer(0));
				wallhavenHeigthResolution.setValue(new Integer(0));
				allResolutionsCheckbox.setSelected(true);
			}
	        wallhavenWidthResolution.setEnabled(true);
	        wallhavenHeigthResolution.setEnabled(true);
			allResolutionsCheckbox.setEnabled(true);
			searchTypeComboBox.setEnabled(true);
		} else {
			wallhavenKeywords.setEnabled(false);
			wallhavenWidthResolution.setEnabled(false);
			wallhavenHeigthResolution.setEnabled(false);
			allResolutionsCheckbox.setEnabled(false);
			wallhavenWidthResolution.setValue(new Integer(0));
			wallhavenHeigthResolution.setValue(new Integer(0));
			allResolutionsCheckbox.setSelected(true);
			searchTypeComboBox.setEnabled(false);
		}
		searchTypeComboBox.addItem(new ComboItem("Relevance", "0")); 
		searchTypeComboBox.addItem(new ComboItem("Date Added", "1")); 
		searchTypeComboBox.addItem(new ComboItem("Views", "2")); 
		searchTypeComboBox.addItem(new ComboItem("Favorites", "3")); 
		searchTypeComboBox.addItem(new ComboItem("Random", "4"));
		searchTypeComboBox.setSelectedIndex(new Integer(prefm.getPreference("wallpaper-search-type")));
		// ---------------------------------------------------------------------
		// Checking user settings
		// ---------------------------------------------------------------------
		timerComboBox.addItem(new ComboItem("1 min", "0"));
		timerComboBox.addItem(new ComboItem("5 min", "1"));
		timerComboBox.addItem(new ComboItem("10 min", "2"));
		timerComboBox.addItem(new ComboItem("20 min", "3"));
		timerComboBox.addItem(new ComboItem("30 min", "4"));
		timerComboBox.setSelectedIndex(new Integer(prefm.getPreference("application-timer")));
		downloadDirectorySize.setValue(new Integer(prefm.getPreference("application-max-download-folder-size")));
		// ---------------------------------------------------------------------
		// Checking Miscelanea
		// ---------------------------------------------------------------------
		downloadsDirectory.setValue(WDUtilities.getDownloadsPath());
		// ---------------------------------------------------------------------
		// Checking Disk Space Progress Bar
		// ---------------------------------------------------------------------
		refreshProgressBar();
		// ---------------------------------------------------------------------
		// Populating JScrollPane with the last 5 wallpapers downloaded
		// ---------------------------------------------------------------------
		refreshJScrollPane();
	}
	
	/**
	 * This method starts the harvesting process
	 */
	private void initializeHarvesting() {
		harvester = Harvester.getInstance();
		harvester.start();
	}
	
	/**
	 * This method refreshes the progress bar representing the space occupied within the downloads directory
	 */
	public static void refreshProgressBar() {
		int percentage = WDUtilities.getPercentageSpaceOccupied(WDUtilities.getDownloadsPath());
		// If percentage is 90% or higher, the warning label and icon will be shown
		if (percentage >= 90) {
			lblSpaceWarning.setVisible(true);
		} else {
			lblSpaceWarning.setVisible(false);
		}
		diskSpacePB.setValue(percentage);
	}
	
	/**
	 * This method refreshes the JScrollPane with the las 5 wallpapers downloaded
	 */
	@SuppressWarnings({ })
	public static void refreshJScrollPane() {
		ImageIcon[] wallpapers = WDUtilities.getLastImageIconWallpapers(5);
		//lastWallpapersList = new JList(wallpapers);
		lastWallpapersList.setListData(wallpapers);
		scroll.setViewportView(lastWallpapersList);
		// JList single selection
		lastWallpapersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// JList horizontal orientation
		lastWallpapersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// Only 1 row to display
		lastWallpapersList.setVisibleRowCount(1);
		// Using a custom render to render every element within JList
		lastWallpapersList.setCellRenderer(new WallpaperListRenderer());
	}
}
