package es.estoes.wallpaperDownloader.window;

import java.awt.EventQueue;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import es.estoes.wallpaperDownloader.changer.ChangerDaemon;
import es.estoes.wallpaperDownloader.changer.LinuxWallpaperChanger;
import es.estoes.wallpaperDownloader.harvest.Harvester;
import es.estoes.wallpaperDownloader.item.ComboItem;
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
import java.net.URI;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import javax.swing.JComboBox;
import java.text.Format;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JTextPane;

public class WallpaperDownloader {

	// Constants
	protected static final Logger LOG = Logger.getLogger(WallpaperDownloader.class);
	protected static WallpaperDownloader window;
	private static final PropertiesManager pm = PropertiesManager.getInstance();
	
	// Attributes
	
	// diskSpacePB will be an attribute representing disk space occupied within the downloads directory
	// It is static because it will be able to be accessed from any point within the application's code
	public static JProgressBar diskSpacePB = new JProgressBar();
	public static JLabel lblSpaceWarning;
	public static JScrollPane scroll;
	public static JList<ImageIcon> lastWallpapersList;
	private static Harvester harvester;
	private ChangerDaemon changer;
	private JFrame frame;
	private JTextField searchKeywords;
	private JCheckBox wallhavenCheckbox;
	private JCheckBox devianartCheckbox;
	private JCheckBox bingCheckbox;
	private JCheckBox socialWallpaperingCheckbox;
	private JCheckBox socialWallpaperingIgnoreKeywordsCheckbox;
	private JCheckBox wallpaperFusionCheckbox;
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
	private JComboBox<ComboItem> devianartSearchTypeComboBox;
	private JComboBox<ComboItem> timerComboBox;
	private JFormattedTextField downloadDirectorySize;
	private JPanel miscPanel;
	private JPanel wallpapersPanel;
	private JFormattedTextField downloadsDirectory;
	private JButton btnChangeDownloadsDirectory;
	private JButton btnManageWallpapers;
	private JButton btnRemoveWallpaper;
	private JButton btnSetFavoriteWallpaper;
	private JButton btnSetWallpaper;
	private JButton btnPreviewWallpaper;
	private JPanel aboutPanel;
	private JTextField version;
	private JSeparator aboutSeparator1;
	private JLabel lblDeveloper;
	private JTextField developer;
	private JLabel lblSourceCode;
	private JButton btnRepository;
	private JSeparator aboutSeparator2;
	private JTextField icons;
	private JButton btnIcons;
	private JComboBox<ComboItem> changerComboBox;
	private JButton btnChangeMoveDirectory;
	private JFormattedTextField moveDirectory;
	private JLabel lblMoveHelp;
	private JCheckBox moveFavoriteCheckBox;
	private JButton btnMoveWallpapers;
	private JLabel lblNotifications;
	private JComboBox<ComboItem> notificationsComboBox;
	private static JCheckBox startMinimizedCheckBox;
	private static JButton btnPause;
	private static JButton btnPlay;
	private static JPanel providersPanel;
	private static JLabel lblGreenSpot;
	private static JLabel lblRedSpot;
	private JList<String> listDirectoriesToWatch;
	private DefaultListModel<String> listDirectoriesModel;
	private JButton btnAddDirectory;
	private JButton btnRemoveDirectory;
	private JPanel appSettingsPanel;
	
	// Getters & Setters
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public Harvester getHarvester() {
		return WallpaperDownloader.harvester;
	}

	public void setHarvester(Harvester harvester) {
		WallpaperDownloader.harvester = harvester;
	}
	
	public JFormattedTextField getDownloadsDirectory() {
		return downloadsDirectory;
	}

	public void setDownloadsDirectory(JFormattedTextField downloadsDirectory) {
		this.downloadsDirectory = downloadsDirectory;
	}

	public JFormattedTextField getMoveDirectory() {
		return moveDirectory;
	}

	public void setMoveDirectory(JFormattedTextField moveDirectory) {
		this.moveDirectory = moveDirectory;
	}

	public DefaultListModel<String> getListDirectoriesModel() {
		return listDirectoriesModel;
	}

	public void setListDirectoriesModel(DefaultListModel<String> listDirectoriesModel) {
		this.listDirectoriesModel = listDirectoriesModel;
	}

	public JButton getBtnRemoveDirectory() {
		return btnRemoveDirectory;
	}

	public void setBtnRemoveDirectory(JButton btnRemoveDirectory) {
		this.btnRemoveDirectory = btnRemoveDirectory;
	}

	public JPanel getAppSettingsPanel() {
		return appSettingsPanel;
	}

	public void setAppSettingsPanel(JPanel appSettingsPanel) {
		this.appSettingsPanel = appSettingsPanel;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// 1.- Log configuration
				WDConfigManager.configureLog();

				// 2.- Application configuratiobtnn
				WDConfigManager.checkConfig();
				window = new WallpaperDownloader();
				window.frame.setVisible(true);
				window.frame.setTitle(pm.getProperty("app.name") + " V" + pm.getProperty("app.version"));
				// Minimize the application if start minimized feature is enable
				if (startMinimizedCheckBox.isSelected()) {
					minimizeApplication();
				}

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
	@SuppressWarnings("serial")
	private void initialize() {
		// Configuring tooltips
		ToolTipManager.sharedInstance().setInitialDelay(100);
		
		// Configuring frames
		frame = new JFrame();
		frame.setBounds(100, 100, 694, 496);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{116, 386, 73, 96, 0};
		gridBagLayout.rowHeights = new int[]{338, 53, 25, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - frame.getWidth()) / 2;
		int y = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(x, y);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 3;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.gridwidth = 4;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		// Providers (tab)
		providersPanel = new JPanel();
		tabbedPane.addTab("Providers", null, providersPanel, null);
		providersPanel.setLayout(null);

		wallhavenCheckbox = new JCheckBox("Wallhaven.cc");
		wallhavenCheckbox.setBounds(8, 48, 129, 23);
		providersPanel.add(wallhavenCheckbox);

		JLabel lblKeywords = new JLabel("Keywords");
		lblKeywords.setBounds(12, 14, 70, 15);
		providersPanel.add(lblKeywords);
		
		searchKeywords = new JTextField();
		searchKeywords.setBounds(100, 13, 255, 19);
		providersPanel.add(searchKeywords);
		searchKeywords.setColumns(10);

		JSeparator separator1 = new JSeparator();
		separator1.setBounds(12, 45, 531, 2);
		providersPanel.add(separator1);
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			JLabel lblKeywordsHelp = new JLabel(icon);
			lblKeywordsHelp.setToolTipText("Each keyword must be separated by ';'. If it is empty then it will search any wallpaper");
			lblKeywordsHelp.setBounds(366, 13, 30, 23);
			providersPanel.add(lblKeywordsHelp);
		} catch (IOException ex) {
			JLabel lblKeywordsHelp = new JLabel("(separated by ;) (Empty->All wallpapers)");
			lblKeywordsHelp.setBounds(362, 39, 70, 15);
			providersPanel.add(lblKeywordsHelp);
		}

		JLabel lblResolution = new JLabel("Resolution");
		lblResolution.setBounds(12, 75, 94, 15);
		providersPanel.add(lblResolution);
		
		// Only integers will be allowed
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);
		wallhavenWidthResolution = new JFormattedTextField(integerFormat);
		wallhavenWidthResolution.setColumns(4);
		wallhavenWidthResolution.setBounds(100, 73, 49, 19);
		providersPanel.add(wallhavenWidthResolution);
		
		searchTypeComboBox = new JComboBox<ComboItem>();
		searchTypeComboBox.setBounds(528, 73, 129, 19);
		providersPanel.add(searchTypeComboBox);
		
		JLabel lblSearchType = new JLabel("Search Type");
		lblSearchType.setBounds(431, 76, 94, 15);
		providersPanel.add(lblSearchType);
		
		lblX = new JLabel("x");
		lblX.setBounds(151, 77, 12, 15);
		providersPanel.add(lblX);
		
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);
		wallhavenHeigthResolution = new JFormattedTextField(integerFormat);
		wallhavenHeigthResolution.setColumns(4);
		wallhavenHeigthResolution.setBounds(161, 73, 49, 19);
		providersPanel.add(wallhavenHeigthResolution);
		
		allResolutionsCheckbox = new JCheckBox("All Resolutions");
		allResolutionsCheckbox.setBounds(224, 71, 151, 23);
		providersPanel.add(allResolutionsCheckbox);

		JSeparator separator2 = new JSeparator();
		separator2.setBounds(8, 103, 531, 2);
		providersPanel.add(separator2);
		
		devianartCheckbox = new JCheckBox("Devianart");
		devianartCheckbox.setBounds(8, 107, 129, 23);
		providersPanel.add(devianartCheckbox);
		
		JLabel lblDevianartSearchType = new JLabel("Search Type");
		lblDevianartSearchType.setBounds(12, 135, 94, 15);
		providersPanel.add(lblDevianartSearchType);

		devianartSearchTypeComboBox = new JComboBox<ComboItem>();
		devianartSearchTypeComboBox.setBounds(107, 133, 150, 19);
		providersPanel.add(devianartSearchTypeComboBox);
		
		JSeparator separator3 = new JSeparator();
		separator3.setBounds(7, 162, 531, 2);
		providersPanel.add(separator3);
		
		bingCheckbox = new JCheckBox("Bing daily wallpaper");
		bingCheckbox.setBounds(8, 172, 249, 23);
		providersPanel.add(bingCheckbox);
		
		JSeparator separator4 = new JSeparator();
		separator4.setBounds(8, 203, 531, 2);
		providersPanel.add(separator4);
		
		socialWallpaperingCheckbox = new JCheckBox("Social Wallpapering");
		socialWallpaperingCheckbox.setBounds(8, 213, 210, 23);
		providersPanel.add(socialWallpaperingCheckbox);
		
		socialWallpaperingIgnoreKeywordsCheckbox = new JCheckBox("Ignore keywords");
		socialWallpaperingIgnoreKeywordsCheckbox.setBounds(222, 213, 143, 23);
		providersPanel.add(socialWallpaperingIgnoreKeywordsCheckbox);
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			JLabel lblIgnoreKeywordsSocialWallpaperingHelp = new JLabel(icon);
			lblIgnoreKeywordsSocialWallpaperingHelp.setToolTipText("Social Wallpapering only allows the download of reviewed wallpapers. Those found using keywords and not reviewed won't be downloaded");
			lblIgnoreKeywordsSocialWallpaperingHelp.setBounds(366, 212, 30, 23);
			providersPanel.add(lblIgnoreKeywordsSocialWallpaperingHelp);
		} catch (IOException ex) {
			JLabel lblIgnoreKeywordsSocialWallpaperingHelp = new JLabel("Ignore keywords");
			lblIgnoreKeywordsSocialWallpaperingHelp.setBounds(366, 212, 30, 23);
			providersPanel.add(lblIgnoreKeywordsSocialWallpaperingHelp);
		}

		JSeparator separator5 = new JSeparator();
		separator5.setBounds(8, 247, 531, 2);
		providersPanel.add(separator5);
		
		wallpaperFusionCheckbox = new JCheckBox("WallpaperFusion");
		wallpaperFusionCheckbox.setBounds(8, 257, 210, 23);
		providersPanel.add(wallpaperFusionCheckbox);
		
		btnPause = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/pause_16px_icon.png"));
			btnPause.setIcon(new ImageIcon(img));
			btnPause.setToolTipText("Pause downloading process");
			btnPause.setBounds(431, 6, 34, 33);
		} catch (IOException ex) {
			btnPause.setToolTipText("Pause downloading process");
			btnPause.setBounds(431, 6, 34, 33);
		}

		btnPlay = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/play_16px_icon.png"));
			btnPlay.setIcon(new ImageIcon(img));
			btnPlay.setToolTipText("Resume downloading process");
			btnPlay.setBounds(431, 6, 34, 33);
		} catch (IOException ex) {
			btnPlay.setToolTipText("Resume downloading process");
			btnPlay.setBounds(431, 6, 34, 33);
		}

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/green_spot_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblGreenSpot = new JLabel(icon);			
			lblGreenSpot.setToolTipText("Downloading process is enabled");
			lblGreenSpot.setBounds(523, 15, 20, 18);
		} catch (IOException ex) {
			lblGreenSpot = new JLabel("Downloading process is enabled");
			lblGreenSpot.setBounds(637, 11, 30, 23);
		}

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/red_spot_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblRedSpot = new JLabel(icon);			
			lblRedSpot.setToolTipText("Downloading process is disabled");
			lblRedSpot.setBounds(523, 15, 20, 18);
		} catch (IOException ex) {
			lblRedSpot = new JLabel("Downloading process is disabled");
			lblRedSpot.setBounds(637, 11, 30, 23);
		}

		// Application Settings (tab)
		appSettingsPanel = new JPanel();
		tabbedPane.addTab("Application Settings", null, appSettingsPanel, null);
		appSettingsPanel.setLayout(null);
		
		JLabel lblTimer = new JLabel("WallpaperDownloader will download a new wallpaper every");
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
		
		JSeparator settingsSeparator1 = new JSeparator();
		settingsSeparator1.setBounds(12, 62, 531, 2);
		appSettingsPanel.add(settingsSeparator1);
		
		moveFavoriteCheckBox = new JCheckBox("Move favorite wallpapers");
		moveFavoriteCheckBox.setBounds(12, 72, 226, 23);
		appSettingsPanel.add(moveFavoriteCheckBox);

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblMoveHelp = new JLabel(icon);
			lblMoveHelp.setToolTipText("Enable this option to have an extra button within Wallpapers tab to move all your favorite wallpapers to another location");
			lblMoveHelp.setBounds(244, 72, 30, 23);
			appSettingsPanel.add(lblMoveHelp);
		} catch (IOException ex) {
			JLabel lblMoveHelp = new JLabel("Move favorite wallpapers to another directory");
			lblMoveHelp.setBounds(213, 85, 30, 23);
			appSettingsPanel.add(lblMoveHelp);
		}
		
		JLabel lblMoveFavoriteDirectory = new JLabel("Directory ");
		lblMoveFavoriteDirectory.setBounds(12, 103, 134, 19);
		appSettingsPanel.add(lblMoveFavoriteDirectory);
		
		moveDirectory = new JFormattedTextField((Format) null);
		moveDirectory.setEditable(false);
		moveDirectory.setColumns(4);
		moveDirectory.setBounds(144, 103, 405, 19);
		appSettingsPanel.add(moveDirectory);
		
		btnChangeMoveDirectory = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/change_folder_24px_icon.png"));
			btnChangeMoveDirectory.setIcon(new ImageIcon(img));
			btnChangeMoveDirectory.setToolTipText("Change directory where you want to move your favorite wallpapers");
			btnChangeMoveDirectory.setBounds(561, 95, 34, 33);
		} catch (IOException ex) {
			btnChangeMoveDirectory.setText("Change directory where you want to move your favorite wallpapers");
			btnChangeMoveDirectory.setBounds(561, 107, 34, 33);
		}	
		appSettingsPanel.add(btnChangeMoveDirectory);

		JSeparator settingsSeparator2 = new JSeparator();
		settingsSeparator2.setBounds(12, 134, 531, 2);
		appSettingsPanel.add(settingsSeparator2);
		
		lblNotifications = new JLabel("Please, select the level of notifications");
		lblNotifications.setBounds(12, 143, 304, 19);
		appSettingsPanel.add(lblNotifications);
		
		notificationsComboBox = new JComboBox<ComboItem>();
		notificationsComboBox.setBounds(317, 143, 134, 19);
		appSettingsPanel.add(notificationsComboBox);

		startMinimizedCheckBox = new JCheckBox("Start minimized");
		startMinimizedCheckBox.setBounds(12, 173, 249, 23);
		appSettingsPanel.add(startMinimizedCheckBox);
		
		JSeparator settingsSeparator3 = new JSeparator();
		settingsSeparator3.setBounds(12, 168, 531, 2);
		appSettingsPanel.add(settingsSeparator3);
		
		// Only those desktop environment programmed to be changeable will display this option
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {

			JSeparator settingsSeparator4 = new JSeparator();
			settingsSeparator4.setBounds(12, 199, 531, 2);
			appSettingsPanel.add(settingsSeparator4);		
			JLabel lblChanger = new JLabel("Change wallpaper automatically every");
			lblChanger.setBounds(12, 208, 304, 19);
			appSettingsPanel.add(lblChanger);
			
			changerComboBox = new JComboBox<ComboItem>();
			changerComboBox.setBounds(317, 210, 94, 19);
			appSettingsPanel.add(changerComboBox);
			
			JLabel lblChangerDirectory = new JLabel("Changer directory");
			lblChangerDirectory.setBounds(12, 237, 134, 19);
			appSettingsPanel.add(lblChangerDirectory);
			
			
			btnAddDirectory = new JButton();
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/add_16px_icon.png"));
				btnAddDirectory.setIcon(new ImageIcon(img));
				btnAddDirectory.setToolTipText("Add directory");
				btnAddDirectory.setBounds(561, 266, 34, 33);
			} catch (IOException ex) {
				btnAddDirectory.setToolTipText("Add directory");
				btnAddDirectory.setBounds(561, 274, 34, 33);
			}		
			
			btnRemoveDirectory = new JButton();
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/remove_16px_icon.png"));
				btnRemoveDirectory.setIcon(new ImageIcon(img));
				btnRemoveDirectory.setToolTipText("Remove directory");
				btnRemoveDirectory.setBounds(561, 311, 34, 33);
			} catch (IOException ex) {
				btnRemoveDirectory.setToolTipText("Remove directory");
				btnRemoveDirectory.setBounds(561, 319, 34, 33);
			}		
			
			JScrollPane listDirectoriesScrollPane = new JScrollPane();
			listDirectoriesScrollPane.setBounds(143, 244, 406, 121);
			appSettingsPanel.add(listDirectoriesScrollPane);
			
			listDirectoriesToWatch = new JList<String>();
			listDirectoriesScrollPane.setColumnHeaderView(listDirectoriesToWatch);
			listDirectoriesToWatch.setBackground(UIManager.getColor("Button.background"));
			listDirectoriesToWatch.setToolTipText("Add all the directories that changer will take into account");
			//
			listDirectoriesToWatch.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			listDirectoriesToWatch.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			listDirectoriesToWatch.setVisibleRowCount(-1);
			
			listDirectoriesScrollPane.setViewportView(listDirectoriesToWatch);
												
			/**
			 * btnAddDirectory Action Listener.
			 */
			btnAddDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PathChangerWindow pathChangerWindow = new PathChangerWindow(window, WDUtilities.CHANGER_DIRECTORY);
					pathChangerWindow.setVisible(true);
	  				// There is a bug with KDE (version 5.9) and the preview window is not painted properly
	  				// It is necessary to reshape this window in order to paint all its components
	  				if (WDUtilities.getWallpaperChanger() instanceof LinuxWallpaperChanger) {
	  					if (((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment() == WDUtilities.DE_KDE) {
	  						pathChangerWindow.setSize(449, 299);  						
	  					}
	  				}
				}
			});

			/**
			 * btnAddDirectory Action Listener.
			 */
			btnRemoveDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PreferencesManager prefm = PreferencesManager.getInstance();
					String directoryToRemove = listDirectoriesModel.getElementAt(listDirectoriesToWatch.getSelectedIndex());
					listDirectoriesModel.remove(listDirectoriesToWatch.getSelectedIndex());
					String changerFoldersProperty = prefm.getPreference("application-changer-folder");
					String modifiedChangerFoldersProperty = changerFoldersProperty.replace(";" + directoryToRemove, "");
					prefm.setPreference("application-changer-folder", modifiedChangerFoldersProperty);
					if (listDirectoriesModel.size() <= 1) {
						appSettingsPanel.remove(btnRemoveDirectory);
						appSettingsPanel.repaint();
					}
				}
			});

		}

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
			lblSpaceWarning.setToolTipText("Directory full. Wallpapers (execpt favorite ones) will be removed randomly in order to download more.");
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
		wallpapersPanel = new JPanel();
		tabbedPane.addTab("Wallpapers", null, wallpapersPanel, null);
		wallpapersPanel.setLayout(null);

		JLabel lblLastWallpapers = new JLabel("Last 5 wallpapers downloaded");
		lblLastWallpapers.setBounds(12, 12, 238, 15);
		wallpapersPanel.add(lblLastWallpapers);
		
        scroll = new JScrollPane();
        scroll.setPreferredSize(new Dimension(300, 400));
		scroll.setBounds(12, 36, 652, 105);
		wallpapersPanel.add(scroll);
		
		btnManageWallpapers = new JButton("Manage All Wallpapers");
		btnManageWallpapers.setBackground(Color.WHITE);
		btnManageWallpapers.setBounds(12, 239, 197, 25);
		wallpapersPanel.add(btnManageWallpapers);
		
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
		wallpapersPanel.add(btnRemoveWallpaper);
		
		btnSetFavoriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/favourite_24px_icon.png"));
			btnSetFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnSetFavoriteWallpaper.setToolTipText("Set selected wallpaper as favorite");
			btnSetFavoriteWallpaper.setBounds(53, 149, 34, 33);
		} catch (IOException ex) {
			btnSetFavoriteWallpaper.setText("Set as favaourite");
			btnSetFavoriteWallpaper.setBounds(58, 149, 34, 33);
		}
		wallpapersPanel.add(btnSetFavoriteWallpaper);
		
		btnPreviewWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/view_24px_icon.png"));
			btnPreviewWallpaper.setIcon(new ImageIcon(img));
			btnPreviewWallpaper.setToolTipText("Preview wallpaper");
			btnPreviewWallpaper.setBounds(95, 149, 34, 33);
		} catch (IOException ex) {
			btnPreviewWallpaper.setText("Preview wallpaper");
			btnPreviewWallpaper.setBounds(95, 149, 34, 33);
		}
		wallpapersPanel.add(btnPreviewWallpaper);
		
		btnMoveWallpapers = new JButton("Move Fav. Wallpapers");
		btnMoveWallpapers.setBackground(Color.WHITE);
		btnMoveWallpapers.setBounds(12, 276, 197, 25);
		wallpapersPanel.add(btnMoveWallpapers);

		btnSetWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/desktop_24px_icon.png"));
			btnSetWallpaper.setIcon(new ImageIcon(img));
			btnSetWallpaper.setToolTipText("Set selected wallpaper");
			btnSetWallpaper.setBounds(137, 149, 34, 33);
		} catch (IOException ex) {
			btnSetWallpaper.setText("Set wallpaper");
			btnSetWallpaper.setBounds(99, 149, 34, 33);
		}
		// This button only will be available for those desktops which support setting wallpapers directly
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			wallpapersPanel.add(btnSetWallpaper);			

		}

		// About (tab)
		aboutPanel = new JPanel();
		tabbedPane.addTab("About", null, aboutPanel, null);
		aboutPanel.setLayout(null);
		
		JLabel lblVersion = new JLabel("Version");
		lblVersion.setBounds(12, 16, 70, 15);
		aboutPanel.add(lblVersion);
		
		version = new JTextField() {
			// JTextField without border
			@Override public void setBorder(Border border) {
				// No borders!
			}
		};
		version.setEditable(false);
		version.setBounds(73, 15, 30, 19);
		aboutPanel.add(version);
		version.setColumns(10);
		
		aboutSeparator1 = new JSeparator();
		aboutSeparator1.setBounds(11, 43, 531, 2);
		aboutPanel.add(aboutSeparator1);
		
		lblDeveloper = new JLabel("Developer");
		lblDeveloper.setBounds(12, 57, 95, 15);
		aboutPanel.add(lblDeveloper);
		
		developer = new JTextField() {
			// JTextField without border
			@Override public void setBorder(Border border) {
				// No borders!
			}
		};
		developer.setEditable(false);
		developer.setBounds(108, 56, 405, 19);
		aboutPanel.add(developer);
		developer.setColumns(10);
		
		lblSourceCode = new JLabel("Source code");
		lblSourceCode.setBounds(12, 84, 95, 15);
		aboutPanel.add(lblSourceCode);
		
		btnRepository = new JButton("New button");
		btnRepository.setBounds(92, 80, 456, 25);
		btnRepository.setText("<HTML><FONT color=\"#000099\"><U>" + pm.getProperty("repository.code") + "</U></FONT></HTML>");
		btnRepository.setHorizontalAlignment(SwingConstants.LEFT);
		btnRepository.setBorderPainted(false);
		btnRepository.setOpaque(false);
		btnRepository.setBackground(Color.WHITE);
		aboutPanel.add(btnRepository);
		
		JTextArea txtInfo = new JTextArea();
		txtInfo.setBackground(UIManager.getColor("Button.background"));
		txtInfo.setText("Please, if you want to open any issue beause you find a bug, you can do it in the official code repository (link above). if you have any suggestions you can send them there too. Thanks and enjoy!");
		txtInfo.setEditable(false);
		txtInfo.setBounds(12, 318, 527, 55);
		txtInfo.setLineWrap(true);
		txtInfo.setWrapStyleWord(true);
		aboutPanel.add(txtInfo);
		
		aboutSeparator2 = new JSeparator();
		aboutSeparator2.setBounds(12, 143, 531, 2);
		aboutPanel.add(aboutSeparator2);
		
		JLabel lblIcons = new JLabel("Icons");
		lblIcons.setBounds(12, 111, 95, 15);
		aboutPanel.add(lblIcons);
		
		icons = new JTextField() {
			public void setBorder(Border border) {
			}
		};
		icons.setText("Dave Gandy from");
		icons.setEditable(false);
		icons.setColumns(10);
		icons.setBounds(108, 109, 114, 19);
		aboutPanel.add(icons);
		
		btnIcons = new JButton("<HTML><FONT color=\"#000099\"><U>http://www.flaticon.com/</U></FONT></HTML>");
		btnIcons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnIcons.setOpaque(false);
		btnIcons.setHorizontalAlignment(SwingConstants.LEFT);
		btnIcons.setBorderPainted(false);
		btnIcons.setBackground(Color.WHITE);
		btnIcons.setBounds(223, 106, 456, 25);
		aboutPanel.add(btnIcons);
		
		JLabel lblChangelog = new JLabel("Changelog");
		lblChangelog.setBounds(12, 153, 91, 15);
		aboutPanel.add(lblChangelog);
		
		JSeparator aboutSeparator3 = new JSeparator();
		aboutSeparator3.setBounds(11, 308, 531, 2);
		aboutPanel.add(aboutSeparator3);
		
		JScrollPane changelogScrollPane = new JScrollPane();
		changelogScrollPane.setBounds(12, 175, 527, 123);
		aboutPanel.add(changelogScrollPane);
		
		JTextPane changelogTextPane = new JTextPane();
		changelogScrollPane.setViewportView(changelogTextPane);
		
		StyledDocument doc = changelogTextPane.getStyledDocument();

		//  Define a keyword attribute
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.BLUE);
		StyleConstants.setBold(keyWord, true);

		//  Adding text

		try
		{
		    doc.insertString(0, "New features (Version 2.7)\n\n", keyWord );
		    doc.insertString(doc.getLength(), "- KDE support added.\n", null );
		    doc.insertString(doc.getLength(), "- Now, user can define several different directories for the automated changer.\n", null );
		    doc.insertString(doc.getLength(), "- Pause/resume functionality to download wallpapers.\n", null );
		    doc.insertString(doc.getLength(), "- New option to start the application minimized.\n", null );
		    doc.insertString(doc.getLength(), "- Changelog added.\n\n", null );
		    doc.insertString(doc.getLength(), "Bugs fixed (Version 2.7).\n\n", keyWord );
		    doc.insertString(doc.getLength(), "Social Wallpapering provider now stores the image files with the correct suffix.\n", null );
		}
		catch(Exception exception) { 
			if (LOG.isInfoEnabled()) {
				LOG.error("Error rendering jTextPane. Error: " + exception.getMessage());
			}
		}
		// Text to the beginning
		changelogTextPane.setCaretPosition(0);
		
		// Global buttons
		btnCloseExit = new JButton("Close & Exit");
		GridBagConstraints gbc_btnCloseExit = new GridBagConstraints();
		gbc_btnCloseExit.insets = new Insets(0, 0, 0, 5);
		gbc_btnCloseExit.gridx = 0;
		gbc_btnCloseExit.gridy = 3;
		frame.getContentPane().add(btnCloseExit, gbc_btnCloseExit);
		
		btnApply = new JButton("Apply");
		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.insets = new Insets(0, 0, 0, 5);
		gbc_btnApply.gridx = 2;
		gbc_btnApply.gridy = 3;
		frame.getContentPane().add(btnApply, gbc_btnApply);
		
		btnMinimize = new JButton("Minimize");
		btnMinimize.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnMinimize = new GridBagConstraints();
		gbc_btnMinimize.gridx = 3;
		gbc_btnMinimize.gridy = 3;
		frame.getContentPane().add(btnMinimize, gbc_btnMinimize);
		
		// Setting up configuration
		initializeGUI();
		
		// Setting up listeners
		initializeListeners();
		
		// Starting harvesting process
		initializeHarvesting();
		
		// Starting pause / resume feature
		if (harvester.getStatus() != Harvester.STATUS_DISABLED) {
			// Checking downloading process
			PreferencesManager prefm = PreferencesManager.getInstance();
			if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
				providersPanel.add(btnPlay);
				providersPanel.add(lblRedSpot);
			} else {
				providersPanel.add(btnPause);
				providersPanel.add(lblGreenSpot);
			}
		} else {
			providersPanel.add(lblRedSpot);
		}
		
		// Starting automated changer process
		initializeChanger();
		
	}

	/**
	 * This method configures all the listeners.
	 */
	private void initializeListeners() {
		
		final PreferencesManager prefm = PreferencesManager.getInstance();
		
		// Listeners
		/**
		 * wallhavenCheckbox Action Listener.
		 */
		// Clicking event
		wallhavenCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (wallhavenCheckbox.isSelected()) {
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
					wallhavenWidthResolution.setEnabled(false);
					wallhavenHeigthResolution.setEnabled(false);
					searchTypeComboBox.setEnabled(false);
					allResolutionsCheckbox.setEnabled(false);
				}
			}
		});

		/**
		 * devianartCheckbox Action Listener.
		 */
		// Clicking event
		devianartCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (devianartCheckbox.isSelected()) {
					devianartSearchTypeComboBox.setEnabled(true);
				} else {
					devianartSearchTypeComboBox.setEnabled(false);
				}
			}
		});

		/**
		 * socialWallpaperingCheckbox Action Listener.
		 */
		// Clicking event
		socialWallpaperingCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (socialWallpaperingCheckbox.isSelected()) {
					socialWallpaperingIgnoreKeywordsCheckbox.setEnabled(true);
				} else {
					socialWallpaperingIgnoreKeywordsCheckbox.setEnabled(false);
				}
			}
		});

		/**
		 * allResolutionsCheckbox Action Listener.
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
		 * btnCloseExit Action Listener.
		 */
		// Clicking event
		btnCloseExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// The application is closed
				System.exit(0);
			}
		});

		/**
		 * btnMinimize Action Listener.
		 */
		// Clicking event
		btnMinimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				minimizeApplication();
			}
		
		});
		
		/**
		 * btnApply Action Listener.
		 */
		// Clicking event
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Storing user settings
				// ---------------------------------------------------------------------------
				// Providers
				// ---------------------------------------------------------------------------
				// Search keywords
				if (!searchKeywords.getText().isEmpty()) {
					prefm.setPreference("provider-wallhaven-keywords", searchKeywords.getText());					
				} else {
					prefm.setPreference("provider-wallhaven-keywords", PreferencesManager.DEFAULT_VALUE);
				}
				
				// Wallhaven.cc
				if (wallhavenCheckbox.isSelected()) {
					prefm.setPreference("provider-wallhaven", WDUtilities.APP_YES);
					if (allResolutionsCheckbox.isSelected()) {
						prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);						
					} else {
						prefm.setPreference("wallpaper-resolution", wallhavenWidthResolution.getValue().toString() + "x" + wallhavenHeigthResolution.getValue().toString());						
					}
					prefm.setPreference("wallpaper-search-type", new Integer(searchTypeComboBox.getSelectedIndex()).toString());

				} else {
					prefm.setPreference("provider-wallhaven", WDUtilities.APP_NO);
					prefm.setPreference("wallpaper-resolution", PreferencesManager.DEFAULT_VALUE);
					prefm.setPreference("wallpaper-search-type", "3");
				}

				// Devianart
				if (devianartCheckbox.isSelected()) {
					prefm.setPreference("provider-devianart", WDUtilities.APP_YES);
					prefm.setPreference("wallpaper-devianart-search-type", new Integer(devianartSearchTypeComboBox.getSelectedIndex()).toString());

				} else {
					prefm.setPreference("provider-devianart", WDUtilities.APP_NO);
					prefm.setPreference("wallpaper-devianart-search-type", "0");
				}

				// Bing
				if (bingCheckbox.isSelected()) {
					prefm.setPreference("provider-bing", WDUtilities.APP_YES);

				} else {
					prefm.setPreference("provider-bing", WDUtilities.APP_NO);
				}

				// Social Wallpapering
				if (socialWallpaperingCheckbox.isSelected()) {
					prefm.setPreference("provider-socialWallpapering", WDUtilities.APP_YES);
				} else {
					prefm.setPreference("provider-socialWallpapering", WDUtilities.APP_NO);
				}
				if (socialWallpaperingIgnoreKeywordsCheckbox.isSelected()) {
					prefm.setPreference("provider-socialWallpapering-ignore-keywords", WDUtilities.APP_YES);
				} else {
					prefm.setPreference("provider-socialWallpapering-ignore-keywords", WDUtilities.APP_NO);
				}
				
				// WallpaperFusion
				if (wallpaperFusionCheckbox.isSelected()) {
					prefm.setPreference("provider-wallpaperFusion", WDUtilities.APP_YES);

				} else {
					prefm.setPreference("provider-wallpaperFusion", WDUtilities.APP_NO);
				}

				// ---------------------------------------------------------------------------
				// User settings
				// ---------------------------------------------------------------------------
				prefm.setPreference("application-timer", new Integer(timerComboBox.getSelectedIndex()).toString());
				prefm.setPreference("application-max-download-folder-size", downloadDirectorySize.getValue().toString());
				if (moveFavoriteCheckBox.isSelected()) {
					prefm.setPreference("move-favorite", WDUtilities.APP_YES);
					prefm.setPreference("move-favorite-folder", moveDirectory.getText());
				} else {
					prefm.setPreference("move-favorite", WDUtilities.APP_NO);
					prefm.setPreference("move-favorite-folder", PreferencesManager.DEFAULT_VALUE);
				}
				prefm.setPreference("application-notifications", new Integer(notificationsComboBox.getSelectedIndex()).toString());
				if (startMinimizedCheckBox.isSelected()) {
					prefm.setPreference("start-minimized", WDUtilities.APP_YES);
				} else {
					prefm.setPreference("start-minimized", WDUtilities.APP_NO);
				}
				if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
					prefm.setPreference("application-changer", new Integer(changerComboBox.getSelectedIndex()).toString());
				}

				// Stopping and starting harvesting process
				harvester.stop();
				harvester.start();
				
				//Stoping and starting changer process
				changer.stop();
				changer.start();
				
				// Resume / pause feature
				if (harvester.getStatus() != Harvester.STATUS_DISABLED) {
					// Checking downloading process
					if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
						providersPanel.add(btnPlay);
						providersPanel.add(lblRedSpot);
						providersPanel.remove(lblGreenSpot);
					} else {
						providersPanel.add(btnPause);
						providersPanel.add(lblGreenSpot);
						providersPanel.remove(lblRedSpot);
					}
				} else {
					providersPanel.remove(btnPause);
					providersPanel.remove(btnPlay);
					providersPanel.add(lblRedSpot);
					providersPanel.remove(lblGreenSpot);
				}
				providersPanel.repaint();
				
				// Information
				DialogManager info = new DialogManager("Changes applied.", 2000);
				info.openDialog();
			}
		});	
		
		/**
		 * btnOpenDownloadsDirectory Action Listener.
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
		 * btnClipboard Action Listener.
		 */
		// Clicking event
		btnClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Copy downloads directory path into the clipboard
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection data = new StringSelection(downloadsDirectory.getText());
				clipboard.setContents(data, data);
				// Information
				if (WDUtilities.getLevelOfNotifications() > 1) {
					DialogManager info = new DialogManager("The downloads directory path was copied to the clipboard", 2000);
					info.openDialog();
				}
			}
		});
		
		/**
		 * btnChangeDownloadsDirectory Action Listener.
		 */
		btnChangeDownloadsDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PathChangerWindow pathChangerWindow = new PathChangerWindow(window, WDUtilities.DOWNLOADS_DIRECTORY);
				pathChangerWindow.setVisible(true);
  				// There is a bug with KDE (version 5.9) and the preview window is not painted properly
  				// It is necessary to reshape this window in order to paint all its components
  				if (WDUtilities.getWallpaperChanger() instanceof LinuxWallpaperChanger) {
  					if (((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment() == WDUtilities.DE_KDE) {
  						pathChangerWindow.setSize(449, 299);  						
  					}
  				}
			}
		});
		
		/**
		 * btnManageWallpapers Action Listener.
		 */
		btnManageWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WallpaperManagerWindow wmw = new WallpaperManagerWindow();
				wmw.setVisible(true);
			}
		});
		
		  /**
		  * lastWallpapersList Mouse Motion Listener.
		  */
		  changePointerJList();
	      
		 /**
		  * btnRemoveWallpaper Action Listener.
		  */
	      btnRemoveWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<ImageIcon> wallpapersSelected = lastWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<ImageIcon> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.removeWallpaper(wallpapersSelectedAbsolutePath, Boolean.TRUE);
			}
	      });
	      
		 /**
		  * btnSetFavoriteWallpaper Action Listener.
		  */
	      btnSetFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<ImageIcon> wallpapersSelected = lastWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<ImageIcon> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.setFavorite(wallpapersSelectedAbsolutePath);
			}
		  });

		 /**
		  * btnSetWallpaper Action Listener.
		  */
	      btnSetWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<ImageIcon> wallpapersSelected = lastWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<ImageIcon> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.getWallpaperChanger().setWallpaper(wallpapersSelectedAbsolutePath.get(0));
			}
		  });	      

	      /**
		  * btnRepository Action Listener.
		  */
	      btnRepository.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					switch (WDUtilities.getOperatingSystem()) {
					case WDUtilities.OS_LINUX:
						Process process;
					      try {
					    	  if (WDUtilities.isSnapPackage()) {
						          process = Runtime.getRuntime().exec("/usr/local/bin/xdg-open " + pm.getProperty("repository.code"));
					    	  } else {
						          process = Runtime.getRuntime().exec("xdg-open " + pm.getProperty("repository.code"));
					    	  }
					          process.waitFor();
					          process.destroy();
					      } catch (Exception exception) {
					    	  if (LOG.isInfoEnabled()) {
					    		LOG.error("Browser couldn't be opened. Error: " + exception.getMessage());  
					    	  }
					      }						
					      break;
					default:
					    if (Desktop.isDesktopSupported()) {
				        try {
				          Desktop.getDesktop().browse(new URI(pm.getProperty("repository.code")));
				        } catch (Exception exception) { 
				        	LOG.error(exception.getMessage()); 
				        }
				     }
						break;
					}
					
				}
	      });

		 /**
		  * btnIcons Action Listener.
		  */
	      btnIcons.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					switch (WDUtilities.getOperatingSystem()) {
					case WDUtilities.OS_LINUX:
						Process process;
					      try {
					    	  if (WDUtilities.isSnapPackage()) {
						          process = Runtime.getRuntime().exec("/usr/local/bin/xdg-open " + pm.getProperty("repository.icons"));
					    	  } else {
						          process = Runtime.getRuntime().exec("xdg-open " + pm.getProperty("repository.code"));
					    	  }
					          process.waitFor();
					          process.destroy();
					      } catch (Exception exception) {
					    	  if (LOG.isInfoEnabled()) {
					    		LOG.error("Browser couldn't be opened. Error: " + exception.getMessage());  
					    	  }
					      }						
					      break;
					default:
					    if (Desktop.isDesktopSupported()) {
				        try {
				          Desktop.getDesktop().browse(new URI(pm.getProperty("repository.icons")));
				        } catch (Exception exception) { 
				        	LOG.error(exception.getMessage()); 
				        }
				     }
						break;
					}
					
				}
	      });
		      
	      /**
	       * btnPreviewWallpaper Action Listener.
	       */
	      btnPreviewWallpaper.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent arg0) {
  				// Get the selected wallpaper
  				ImageIcon wallpaperSelected = lastWallpapersList.getSelectedValue();
  				String wallpaperSelectedAbsolutePath = wallpaperSelected.getDescription();
  				
  				// Opens the preview window
  				PreviewWallpaperWindow previewWindow = new PreviewWallpaperWindow(wallpaperSelectedAbsolutePath, null);
  				previewWindow.setVisible(true);
  				// There is a bug with KDE (version 5.9) and the preview window is not painted properly
  				// It is necessary to reshape this window in order to paint all its components
  				if (WDUtilities.getWallpaperChanger() instanceof LinuxWallpaperChanger) {
  					if (((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment() == WDUtilities.DE_KDE) {
  						previewWindow.setSize(1023, 767);  						
  					}
  				}
 	    	  }
	      });

	      /**
	       * moveFavoriteCheckBox Action Listener.
	       */
	      // Clicking event
	      moveFavoriteCheckBox.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent evt) {
				if (moveFavoriteCheckBox.isSelected()) {
					moveFavoriteCheckBox.setSelected(true);
					moveDirectory.setEnabled(true);
					moveDirectory.setText(WDUtilities.getDownloadsPath());
					btnChangeMoveDirectory.setEnabled(true);
					btnMoveWallpapers.setEnabled(true);
				} else {
					moveFavoriteCheckBox.setSelected(false);
					moveDirectory.setEnabled(false);
					moveDirectory.setText("");
					btnChangeMoveDirectory.setEnabled(false);
					btnMoveWallpapers.setEnabled(false);
				}
			}
	      });
	      
	      /**
	       * btnChangeMoveDirectory Action Listener.
	       */
	      btnChangeMoveDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PathChangerWindow pathChangerWindow = new PathChangerWindow(window, WDUtilities.MOVE_DIRECTORY);
					pathChangerWindow.setVisible(true);
	  				// There is a bug with KDE (version 5.9) and the preview window is not painted properly
	  				// It is necessary to reshape this window in order to paint all its components
	  				if (WDUtilities.getWallpaperChanger() instanceof LinuxWallpaperChanger) {
	  					if (((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment() == WDUtilities.DE_KDE) {
	  						pathChangerWindow.setSize(449, 299);  						
	  					}
	  				}
				}
	      });
	      
	      /**
	       * btnMoveWallpapers Action Listener.
	       */
	      btnMoveWallpapers.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					moveFavoriteWallpapers();
				}
	      });

	      /**
	       * btnPause Action Listener.
	       */
	      btnPause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					pauseDownloadingProcess();
				}
	      });

	      /**
	       * btnPlay Action Listener.
	       */
	      btnPlay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					resumeDownloadingProcess();
				}
	      });

	}

	/**
	 * Pauses downloading process.
	 */
	public static void pauseDownloadingProcess() {
		final PreferencesManager prefm = PreferencesManager.getInstance();
		
		// Downloading process is paused
		prefm.setPreference("downloading-process", WDUtilities.APP_NO);
		
		// Stops harvesting process
		harvester.stop();
		
		// Repainting buttons
		providersPanel.add(btnPlay);
		providersPanel.remove(btnPause);
		providersPanel.add(lblRedSpot);
		providersPanel.remove(lblGreenSpot);
		providersPanel.repaint();
		
		// Information
		if (WDUtilities.getLevelOfNotifications() > 0) {
			DialogManager info = new DialogManager("Downloading process has been paused", 2000);
			info.openDialog();
		}
	}

	/**
	 * Resumes downloading process.
	 */
	public static void resumeDownloadingProcess() {
		final PreferencesManager prefm = PreferencesManager.getInstance();
		
		// Downloading process is resumed
		prefm.setPreference("downloading-process", WDUtilities.APP_YES);
		
		// Starts harvesting process
		harvester.start();

		// Repainting buttons
		providersPanel.add(btnPause);
		providersPanel.remove(btnPlay);
		providersPanel.add(lblGreenSpot);
		providersPanel.remove(lblRedSpot);
		providersPanel.repaint();

		// Information
		if (WDUtilities.getLevelOfNotifications() > 0) {
			DialogManager info = new DialogManager("Downloading process has been resumed", 2000);
			info.openDialog();
		}
	}

	/**
	 * Minimizes application.
	 */
	public static void minimizeApplication() {

		final PreferencesManager prefm = PreferencesManager.getInstance();
		
		// The application is minimized within System Tray
        //Check the SystemTray is supported
        if (!SystemTray.isSupported() || !WDUtilities.isMinimizable()) {
            LOG.error("SystemTray is not supported. Frame is traditionally minimized");
            // Frame is traditionally minimized
            //window.frame.setState(Frame.ICONIFIED);
            window.frame.setState(Frame.ICONIFIED);

            return;
        } else {
            final PopupMenu popup = new PopupMenu();
            URL systemTrayIcon = WallpaperDownloader.class.getResource("/images/icons/wd_systemtray_icon.png");
            final TrayIcon trayIcon = new TrayIcon(new ImageIcon(systemTrayIcon, "Wallpaper Downloader").getImage(), "Wallpaper Downloader");
            final SystemTray tray = SystemTray.getSystemTray();
           
            // Create a pop-up menu components -- BEGIN
            // Maximize
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
            // Open downloads directory
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

            popup.add(maximizeItem);
            popup.add(browseItem);

    		// Pause / Resume
            MenuItem resumeItem = new MenuItem("Resume");
            MenuItem pauseItem = new MenuItem("Pause");

            resumeItem.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent evt) {
            		resumeDownloadingProcess();
            		popup.remove(resumeItem);
            		popup.insert(pauseItem, 2);
            	}
            });

            pauseItem.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent evt) {
            		pauseDownloadingProcess();
            		popup.remove(pauseItem);
            		popup.insert(resumeItem, 2);
            	}
            });

            if (harvester.getStatus() != Harvester.STATUS_DISABLED) {
    			// Checking downloading process
    			if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
    	            popup.add(resumeItem);
    			} else {
    	            popup.add(pauseItem);
    			}
    		}

            // Change wallpaper
            if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
	            MenuItem changeItem = new MenuItem("Change wallpaper randomly");
	            changeItem.addActionListener(new ActionListener() {
	            	public void actionPerformed(ActionEvent evt) {
	            		WDUtilities.getWallpaperChanger().setRandomWallpaper();
	            	}
	            });
	            popup.add(changeItem);
            }

            // Move favorite wallpapers
    		String moveFavoriteEnable = prefm.getPreference("move-favorite");
    		if (moveFavoriteEnable.equals(WDUtilities.APP_YES)) {
	            MenuItem moveItem = new MenuItem("Move favorite wallpapers");
	            moveItem.addActionListener(new ActionListener() {
	            	public void actionPerformed(ActionEvent evt) {
	            		moveFavoriteWallpapers();
	            	}
	            });
	            popup.add(moveItem);
            }
    		
            popup.addSeparator();

            // Exit
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent evt) {
                	// Removing system tray icon
                	tray.remove(trayIcon);

    				// The application is closed
    				System.exit(0);		                	
            	}
            });

            popup.add(exitItem);

            // Create a pop-up menu components -- END

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
	
	private static void moveFavoriteWallpapers() {

		final PreferencesManager prefm = PreferencesManager.getInstance();
		
		// Dialog for please wait. It has to be executed on a SwingWorker (another Thread) in 
		// order to not block the entire execution of the application
		final DialogManager pleaseWaitDialog = new DialogManager("Please wait...");

	    SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
	        @Override
	        protected String doInBackground() throws InterruptedException  {
				WDUtilities.moveFavoriteWallpapers(prefm.getPreference("move-favorite-folder"));
				refreshProgressBar();
	        	return null;
	        }
	        @Override
	        protected void done() {
	            pleaseWaitDialog.closeDialog();
	        }
	    };
	    worker.execute();
	    pleaseWaitDialog.openDialog();
	    try {
	        worker.get();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
		
		// Information
		if (WDUtilities.getLevelOfNotifications() > 1) {
			DialogManager info = new DialogManager("All you favorite wallpapers have been successfully moved", 2000);
			info.openDialog();
		}
		
	}

	/**
	 * This method configures GUI according to user configuration file preferences
	 */
	private void initializeGUI() {

		final PreferencesManager prefm = PreferencesManager.getInstance();

		// ---------------------------------------------------------------------
		// Checking providers
		// ---------------------------------------------------------------------
		// Search keywords
		if (!prefm.getPreference("provider-wallhaven-keywords").equals(PreferencesManager.DEFAULT_VALUE)) {
			searchKeywords.setText(prefm.getPreference("provider-wallhaven-keywords"));			
		} else {
			searchKeywords.setText("");
		}

		// Wallhaven.cc
		String wallhavenEnable = prefm.getPreference("provider-wallhaven");
		if (wallhavenEnable.equals(WDUtilities.APP_YES)) {
			wallhavenCheckbox.setSelected(true);
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
			wallhavenWidthResolution.setEnabled(false);
			wallhavenHeigthResolution.setEnabled(false);
			allResolutionsCheckbox.setEnabled(false);
			wallhavenWidthResolution.setValue(new Integer(0));
			wallhavenHeigthResolution.setValue(new Integer(0));
			allResolutionsCheckbox.setSelected(true);
			searchTypeComboBox.setEnabled(false);
		}
		searchTypeComboBox.addItem(new ComboItem("Relevance", "0")); 
		searchTypeComboBox.addItem(new ComboItem("Newest", "1")); 
		searchTypeComboBox.addItem(new ComboItem("Views", "2")); 
		searchTypeComboBox.addItem(new ComboItem("Favorites", "3")); 
		searchTypeComboBox.addItem(new ComboItem("Random", "4"));
		searchTypeComboBox.setSelectedIndex(new Integer(prefm.getPreference("wallpaper-search-type")));
		
		// Devianart
		String devianartEnable = prefm.getPreference("provider-devianart");
		if (devianartEnable.equals(WDUtilities.APP_YES)) {
			devianartCheckbox.setSelected(true);
			devianartSearchTypeComboBox.setEnabled(true);
		} else {
			devianartSearchTypeComboBox.setEnabled(false);
		}
		devianartSearchTypeComboBox.addItem(new ComboItem("Newest", "0")); 
		devianartSearchTypeComboBox.addItem(new ComboItem("What's hot", "1")); 
		devianartSearchTypeComboBox.addItem(new ComboItem("Popular", "2")); 
		devianartSearchTypeComboBox.setSelectedIndex(new Integer(prefm.getPreference("wallpaper-devianart-search-type")));

		// Bing
		String bingEnable = prefm.getPreference("provider-bing");
		if (bingEnable.equals(WDUtilities.APP_YES)) {
			bingCheckbox.setSelected(true);
		}

		// Social Wallpapering
		String socialWallpaperingEnable = prefm.getPreference("provider-socialWallpapering");
		if (socialWallpaperingEnable.equals(WDUtilities.APP_YES)) {
			socialWallpaperingCheckbox.setSelected(true);
			socialWallpaperingIgnoreKeywordsCheckbox.setEnabled(true);
		} else {
			socialWallpaperingIgnoreKeywordsCheckbox.setEnabled(false);
		}
		if (prefm.getPreference("provider-socialWallpapering-ignore-keywords").equals(WDUtilities.APP_YES)) {
			socialWallpaperingIgnoreKeywordsCheckbox.setSelected(true);
		} else {
			socialWallpaperingIgnoreKeywordsCheckbox.setSelected(false);
		}
		
		// WallpaperFusion
		String wallpaperFusionEnable = prefm.getPreference("provider-wallpaperFusion");
		if (wallpaperFusionEnable.equals(WDUtilities.APP_YES)) {
			wallpaperFusionCheckbox.setSelected(true);
		}

		// ---------------------------------------------------------------------
		// Checking user settings
		// ---------------------------------------------------------------------
		timerComboBox.addItem(new ComboItem("1 min", "0"));
		timerComboBox.addItem(new ComboItem("5 min", "1"));
		timerComboBox.addItem(new ComboItem("10 min", "2"));
		timerComboBox.addItem(new ComboItem("20 min", "3"));
		timerComboBox.addItem(new ComboItem("30 min", "4"));
		timerComboBox.setSelectedIndex(new Integer(prefm.getPreference("application-timer")));

		String moveFavoriteEnable = prefm.getPreference("move-favorite");
		if (moveFavoriteEnable.equals(WDUtilities.APP_YES)) {
			moveFavoriteCheckBox.setSelected(true);
			moveDirectory.setEnabled(true);
			moveDirectory.setText(prefm.getPreference("move-favorite-folder"));
			btnChangeMoveDirectory.setEnabled(true);
			btnMoveWallpapers.setEnabled(true);
		} else {
			moveFavoriteCheckBox.setSelected(false);
			moveDirectory.setEnabled(false);
			btnChangeMoveDirectory.setEnabled(false);
			btnMoveWallpapers.setEnabled(false);
		}

		// Move favorite feature
		if (prefm.getPreference("move-favorite").equals(PreferencesManager.DEFAULT_VALUE)) {
			prefm.setPreference("move-favorite", WDUtilities.APP_NO);
			prefm.setPreference("move-favorite-folder", PreferencesManager.DEFAULT_VALUE);
		}
		
		// Notifications
		notificationsComboBox.addItem(new ComboItem("None", "0"));
		notificationsComboBox.addItem(new ComboItem("Only important", "1"));
		notificationsComboBox.addItem(new ComboItem("All", "2"));
		notificationsComboBox.setSelectedIndex(new Integer(prefm.getPreference("application-notifications")));

		// Start minimized feature
		String startMinimizedEnable = prefm.getPreference("start-minimized");
		if (startMinimizedEnable.equals(WDUtilities.APP_YES)) {
			startMinimizedCheckBox.setSelected(true);
		} else {
			startMinimizedCheckBox.setSelected(false);
		}

		// Changer
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			changerComboBox.addItem(new ComboItem("Off", "0"));
			changerComboBox.addItem(new ComboItem("1 min", "1"));
			changerComboBox.addItem(new ComboItem("5 min", "2"));
			changerComboBox.addItem(new ComboItem("10 min", "3"));
			changerComboBox.addItem(new ComboItem("30 min", "4"));
			changerComboBox.addItem(new ComboItem("60 min", "5"));
			changerComboBox.setSelectedIndex(new Integer(prefm.getPreference("application-changer")));
			
			listDirectoriesModel = new DefaultListModel<String>();
			String changerFoldersProperty = prefm.getPreference("application-changer-folder");
			String[] changerFolders = changerFoldersProperty.split(";");
			for (int i = 0; i < changerFolders.length; i ++) {
				listDirectoriesModel.addElement(changerFolders[i]);
			}
			listDirectoriesToWatch.setModel(listDirectoriesModel);
			appSettingsPanel.add(btnAddDirectory);
			if (listDirectoriesModel.size() > 1) {
				appSettingsPanel.add(btnRemoveDirectory);
			}
		}

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
		// ---------------------------------------------------------------------
		// Checking About tab
		// ---------------------------------------------------------------------
		version.setText(pm.getProperty("app.version"));
		developer.setText("Eloy Garcia Almaden (eloy.garcia.pca@gmail.com)");
	}
	
	/**
	 * This method starts the harvesting process.
	 */
	private void initializeHarvesting() {
		harvester = Harvester.getInstance();
		harvester.start();
	}

	/**
	 * Starts automated wallpaper changing process.
	 */
	private void initializeChanger() {
		changer = ChangerDaemon.getInstance();
		changer.start();
	}

	/**
	 * This method refreshes the progress bar representing the space occupied within the downloads directory.
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
	 * This method refreshes the JScrollPane with the last 5 wallpapers downloaded
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void refreshJScrollPane() {
		ImageIcon[] wallpapers = WDUtilities.getImageIconWallpapers(5, 0, WDUtilities.SORTING_BY_DATE, WDUtilities.WD_PREFIX);
		lastWallpapersList = new JList(wallpapers);
		changePointerJList();
		scroll.setViewportView(lastWallpapersList);
		// JList single selection
		lastWallpapersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// JList horizontal orientation
		lastWallpapersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// Only 1 row to display
		lastWallpapersList.setVisibleRowCount(1);
		// Using a custom render to render every element within JList
		lastWallpapersList.setCellRenderer(new WallpaperListRenderer(WallpaperListRenderer.WITHOUT_TEXT));
	}
	
	/**
	 * This method changes the pointer when the user moves the mouse over the JList
	 */
	private static void changePointerJList() {
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
	}
}
