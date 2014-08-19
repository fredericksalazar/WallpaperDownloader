package es.estoes.wallpaperDownloader.window;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextField;
import es.estoes.wallpaperDownloader.harvest.Harvester;
import es.estoes.wallpaperDownloader.item.ComboItem;
import es.estoes.wallpaperDownloader.util.PreferencesManager;
import es.estoes.wallpaperDownloader.util.PropertiesManager;
import es.estoes.wallpaperDownloader.util.WDConfigManager;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import javax.swing.JComboBox;

public class WallpaperDownloader {

	// Constants
	protected static final Logger LOG = Logger.getLogger(WallpaperDownloader.class);
	private static WallpaperDownloader window;
	
	// Attributes
	private Harvester harvester;
	private JFrame frame;
	private JTextField wallbaseKeywords;
	private JCheckBox wallbaseCheckbox;
	private JButton btnApply;
	private JButton btnCloseExit;
	private JButton btnMinimize;
	private JComboBox<ComboItem> searchTypeComboBox;
	private JFormattedTextField wallbaseWidthResolution;
	private NumberFormat integerFormat;
	private JLabel lblX;
	private JFormattedTextField wallbaseHeigthResolution;
	private JCheckBox allResolutionsCheckbox;
	private JComboBox<ComboItem> timerComboBox;
	private JFormattedTextField downloadDirectorySize;

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
		frame = new JFrame();
		frame.setBounds(100, 100, 690, 440);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{116, 386, 73, 96, 0};
		gridBagLayout.rowHeights = new int[]{338, 53, 25, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 2;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.gridwidth = 4;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		JPanel providersPanel = new JPanel();
		tabbedPane.addTab("Providers", null, providersPanel, null);
		providersPanel.setLayout(null);

		wallbaseCheckbox = new JCheckBox("Wallbase.cc");
		wallbaseCheckbox.setBounds(8, 8, 129, 23);
		providersPanel.add(wallbaseCheckbox);

		JLabel lblKeywords = new JLabel("Keywords");
		lblKeywords.setBounds(12, 39, 70, 15);
		providersPanel.add(lblKeywords);
		
		wallbaseKeywords = new JTextField();
		wallbaseKeywords.setBounds(100, 37, 522, 19);
		providersPanel.add(wallbaseKeywords);
		wallbaseKeywords.setColumns(10);
		
		JLabel lblResolution = new JLabel("Resolution");
		lblResolution.setBounds(12, 70, 94, 15);
		providersPanel.add(lblResolution);
		
		// Only integers will be allowed
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);
		wallbaseWidthResolution = new JFormattedTextField(integerFormat);
		wallbaseWidthResolution.setColumns(4);
		wallbaseWidthResolution.setBounds(100, 68, 49, 19);
		providersPanel.add(wallbaseWidthResolution);
		
		searchTypeComboBox = new JComboBox<ComboItem>();
		searchTypeComboBox.setBounds(528, 68, 94, 19);
		providersPanel.add(searchTypeComboBox);
		
		JLabel lblSearchType = new JLabel("Search Type");
		lblSearchType.setBounds(431, 71, 94, 15);
		providersPanel.add(lblSearchType);
		
		lblX = new JLabel("x");
		lblX.setBounds(151, 72, 12, 15);
		providersPanel.add(lblX);
		
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);
		wallbaseHeigthResolution = new JFormattedTextField(integerFormat);
		wallbaseHeigthResolution.setColumns(4);
		wallbaseHeigthResolution.setBounds(161, 68, 49, 19);
		providersPanel.add(wallbaseHeigthResolution);
		
		allResolutionsCheckbox = new JCheckBox("All Resolutions");
		allResolutionsCheckbox.setBounds(224, 66, 151, 23);
		providersPanel.add(allResolutionsCheckbox);
		
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
		 * wallbaseCheckbox Action Listeners
		 */
		// Clicking event
		wallbaseCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (wallbaseCheckbox.isSelected()) {
					wallbaseKeywords.setEnabled(true);
					wallbaseWidthResolution.setEnabled(true);
					wallbaseHeigthResolution.setEnabled(true);
					searchTypeComboBox.setEnabled(true);
					allResolutionsCheckbox.setEnabled(true);
					allResolutionsCheckbox.setSelected(false);
					String screenResolution = WDUtilities.getResolution();
					String[] resolution = screenResolution.split("x");
			        wallbaseWidthResolution.setValue(new Integer(resolution[0]));
					wallbaseHeigthResolution.setValue(new Integer(resolution[1]));
				} else {
					wallbaseKeywords.setEnabled(false);
					wallbaseWidthResolution.setEnabled(false);
					wallbaseHeigthResolution.setEnabled(false);
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
					wallbaseWidthResolution.setValue(new Integer(0));
					wallbaseHeigthResolution.setValue(new Integer(0));
					wallbaseWidthResolution.setEnabled(false);
					wallbaseHeigthResolution.setEnabled(false);
				} else {
					String screenResolution = WDUtilities.getResolution();
					String[] resolution = screenResolution.split("x");
			        wallbaseWidthResolution.setValue(new Integer(resolution[0]));
					wallbaseHeigthResolution.setValue(new Integer(resolution[1]));
					wallbaseWidthResolution.setEnabled(true);
					wallbaseHeigthResolution.setEnabled(true);
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
		            final TrayIcon trayIcon = new TrayIcon(new ImageIcon("src/main/resources/images/icons/bulb.gif", "Wallpaper Downloader").getImage(), "Wallpaper Downloader");
		                    
		            final SystemTray tray = SystemTray.getSystemTray();
		           
		            // Create a pop-up menu components
		            MenuItem aboutItem = new MenuItem("About");
		            CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
		            CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
		            Menu displayMenu = new Menu("Display");
		            MenuItem errorItem = new MenuItem("Error");
		            MenuItem warningItem = new MenuItem("Warning");
		            MenuItem infoItem = new MenuItem("Info");
		            MenuItem noneItem = new MenuItem("None");
		            MenuItem exitItem = new MenuItem("Exit");
		           
		            //Add components to pop-up menu
		            popup.add(aboutItem);
		            popup.addSeparator();
		            popup.add(cb1);
		            popup.add(cb2);
		            popup.addSeparator();
		            popup.add(displayMenu);
		            displayMenu.add(errorItem);
		            displayMenu.add(warningItem);
		            displayMenu.add(infoItem);
		            displayMenu.add(noneItem);
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
				// Wallbase.cc
				if (wallbaseCheckbox.isSelected()) {
					prefm.setPreference("provider-wallbase", WDUtilities.APP_YES);
					if (!wallbaseKeywords.getText().isEmpty()) {
						prefm.setPreference("provider-wallbase-keywords", wallbaseKeywords.getText());					
					}
					if (allResolutionsCheckbox.isSelected()) {
						prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);						
					} else {
						prefm.setPreference("wallpaper-resolution", wallbaseWidthResolution.getValue().toString() + "x" + wallbaseHeigthResolution.getValue().toString());						
					}
					prefm.setPreference("wallpaper-search-type", new Integer(searchTypeComboBox.getSelectedIndex()).toString());

				} else {
					prefm.setPreference("provider-wallbase", WDUtilities.APP_NO);
					prefm.setPreference("provider-wallbase-keywords", PreferencesManager.DEFAULT_VALUE);
					prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);
					prefm.setPreference("wallpaper-search-type", "3");
				}
				// ---------------------------------------------------------------------------
				// User settings
				// ---------------------------------------------------------------------------
				prefm.setPreference("application-timer", new Integer(timerComboBox.getSelectedIndex()).toString());
				prefm.setPreference("application-max-download-folder-size", downloadDirectorySize.getValue().toString());
				// Stopping and starting process
				harvester.stop();
				harvester.start();
			}
		});		
	}

	/**
	 * This methods configures GUI according to user configuration file preferences
	 */
	private void initializeGUI() {

		final PreferencesManager prefm = PreferencesManager.getInstance();

		// ---------------------------------------------------------------------
		// Checking providers
		// ---------------------------------------------------------------------
		// Wallbase.cc
		String wallbaseEnable = prefm.getPreference("provider-wallbase");
		if (wallbaseEnable.equals(WDUtilities.APP_YES)) {
			wallbaseCheckbox.setSelected(true);
			wallbaseKeywords.setEnabled(true);
			if (!prefm.getPreference("provider-wallbase-keywords").equals(PreferencesManager.DEFAULT_VALUE)) {
				wallbaseKeywords.setText(prefm.getPreference("provider-wallbase-keywords"));			
			}
			String[] resolution = prefm.getPreference("wallpaper-resolution").split("x");
			if (!prefm.getPreference("wallpaper-resolution").equals(PreferencesManager.DEFAULT_VALUE)) {
		        wallbaseWidthResolution.setValue(new Integer(resolution[0]));
				wallbaseHeigthResolution.setValue(new Integer(resolution[1]));
				allResolutionsCheckbox.setSelected(false);
			} else {
				wallbaseWidthResolution.setValue(new Integer(0));
				wallbaseHeigthResolution.setValue(new Integer(0));
				allResolutionsCheckbox.setSelected(true);
			}
	        wallbaseWidthResolution.setEnabled(true);
	        wallbaseHeigthResolution.setEnabled(true);
			allResolutionsCheckbox.setEnabled(true);
			searchTypeComboBox.setEnabled(true);
		} else {
			wallbaseKeywords.setEnabled(false);
			wallbaseWidthResolution.setEnabled(false);
			wallbaseHeigthResolution.setEnabled(false);
			allResolutionsCheckbox.setEnabled(false);
			wallbaseWidthResolution.setValue(new Integer(0));
			wallbaseHeigthResolution.setValue(new Integer(0));
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
	}
	
	/**
	 * This method starts the harvesting process
	 */
	private void initializeHarvesting() {
		harvester = Harvester.getInstance();
		harvester.start();
	}
}
