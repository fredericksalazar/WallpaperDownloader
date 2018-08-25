/**
 * Copyright 2016-2018 Eloy García Almadén <eloy.garcia.pca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
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
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import javax.swing.JComboBox;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTextPane;
import javax.swing.Icon;
import java.awt.Font;

public class WallpaperDownloader {

	// Constants
	protected static final Logger LOG = Logger.getLogger(WallpaperDownloader.class);
	private static final PropertiesManager pm = PropertiesManager.getInstance();
	
	// Attributes
	protected static WallpaperDownloader window;
	private static JFrame frame;
	private static ResourceBundle i18nBundle;
	public static boolean fromSystemTray;
	// diskSpacePB will be an attribute representing disk space occupied within the downloads directory
	// It is static because it will be able to be accessed from any point within the application's code
	public static JProgressBar diskSpacePB = new JProgressBar();
	public static JLabel lblSpaceWarning;
	public static JScrollPane scroll;
	public static JList<ImageIcon> lastWallpapersList;
	public static Harvester harvester;
	private ChangerDaemon changer;
	private JTextField searchKeywords;
	private static JCheckBox wallhavenCheckbox;
	private static JCheckBox devianartCheckbox;
	private static JCheckBox bingCheckbox;
	private static JCheckBox socialWallpaperingCheckbox;
	private JCheckBox socialWallpaperingIgnoreKeywordsCheckbox;
	private static JCheckBox wallpaperFusionCheckbox;
	private JButton btnChangeResolution;
	private JButton btnMinimize;
	private JButton btnOpenDownloadsDirectory;
	private JButton btnClipboard;
	private JComboBox<ComboItem> downloadPolicyComboBox;
	private JComboBox<ComboItem> searchTypeWallhavenComboBox;
	private JFormattedTextField widthResolution;
	private JLabel lblX;
	private JFormattedTextField heigthResolution;
	private NumberFormat integerFormat;
	private JComboBox<ComboItem> devianartSearchTypeComboBox;
	private JComboBox<ComboItem> timerComboBox;
	private JButton btnChangeSize;
	private JButton btnApplySize;
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
	private JPanel helpPanel;
	private JComboBox<ComboItem> changerComboBox;
	private JCheckBox multiMonitorCheckBox;
	private JButton btnChangeMoveDirectory;
	private JFormattedTextField moveDirectory;
	private JLabel lblMoveHelp;
	private JCheckBox moveFavoriteCheckBox;
	private JButton btnMoveWallpapers;
	private JButton btnRandomWallpaper;
	private JLabel lblNotifications;
	private JComboBox<ComboItem> notificationsComboBox;
	private JComboBox<ComboItem> i18nComboBox;
	private static JCheckBox startMinimizedCheckBox;
	private JComboBox<ComboItem> timeToMinimizeComboBox;
	private JCheckBox stIconCheckBox;
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
	private JButton btnChooseWallpaper;
	private static JCheckBox dualMonitorCheckbox;
	private JLabel lblSearchTypeDualMonitor;
	private JComboBox<ComboItem> searchTypeDualMonitorComboBox;
	private JButton btnApplyResolution;
	private JButton btnResetResolution;
	private JButton btnChangeKeywords;
	private JButton btnApplyKeywords;
	private JLabel lblSystemTrayHelp;
	private JPanel changerPanel;
	private JSeparator settingsSeparator4;
	private JSeparator settingsSeparator5;
	private JCheckBox initOnBootCheckBox;
	
	// Getters & Setters
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
				// Log configuration
				WDConfigManager.configureLog();

				// Application configuration
				WDConfigManager.checkConfig();
				
				window = new WallpaperDownloader();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WallpaperDownloader() {
		// Resource bundle for i18n
		i18nBundle = WDUtilities.getBundle();

		// Creating the main frame
		frame = new JFrame();
		
		// Setting the system look & feel for the main frame
		String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
        	if (systemLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel") || WDUtilities.isSnapPackage()) {
        		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");                		
        	} else {
        		UIManager.setLookAndFeel(systemLookAndFeel);                		
        	}
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
            if (LOG.isInfoEnabled()) {
            	LOG.error("Error in system look and feel definition: Message: " + exception.getMessage());
            }
            try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException exception2) {
				exception2.printStackTrace();
	            if (LOG.isInfoEnabled()) {
	            	LOG.error("Error in traditional system look and feel definition: Message: " + exception.getMessage());
	            }
			}
        }
        SwingUtilities.updateComponentTreeUI(frame);
		
        // Initializing the main frame
        initialize(frame);
		
		// Configuring main frame after initialization
		frame.setBackground(new Color(255, 255, 255));
		frame.setExtendedState(Frame.NORMAL);
		frame.setVisible(true);
		frame.setTitle(pm.getProperty("app.name") + " V" + pm.getProperty("app.version"));

		// Minimizing the application if start minimized feature is enable
		if (startMinimizedCheckBox.isSelected()) {
			try {
				// Sleeps during X seconds in order to avoid problems in GNOME 3 minimization
				if (WDUtilities.getWallpaperChanger() instanceof LinuxWallpaperChanger) {
					if (((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_GNOME3)
						|| ((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_KDE)) {
						PreferencesManager prefm = PreferencesManager.getInstance(); 
						TimeUnit.SECONDS.sleep(new Long(prefm.getPreference("time-to-minimize")));								
					}
				}
			} catch (InterruptedException exception) {
				if (LOG.isInfoEnabled()) {
					LOG.error("Error sleeping for 3 seconds. Message: " + exception.getMessage());
				}
			}
			minimizeApplication();
		}

		// Setting some listeners for the main frame
		// Command comes from system tray
		fromSystemTray = false;
		
		// Adding a listener for knowing when the main window changes its state
		frame.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent windowEvent) {
				final PreferencesManager prefm = PreferencesManager.getInstance();
				String systemTrayIconEnable = prefm.getPreference("system-tray-icon");
				if (SystemTray.isSupported() && 
					!isOldSystemTray() && 
					systemTrayIconEnable.equals(WDUtilities.APP_YES)) {
					// Check if commands comes from system tray
					if (fromSystemTray) {
						fromSystemTray = false;
					} else {
						// If command doesn't come from system tray, then it is necessary to capture the
						// minimize order
						if (frame.getExtendedState() == Frame.NORMAL){
							// The user has minimized the window
							try {
								TimeUnit.MILLISECONDS.sleep(100);
							} catch (InterruptedException exception) {
								if (LOG.isInfoEnabled()) {
									LOG.error("Error going to sleep: " + exception.getMessage());
								}
							}								
							minimizeApplication();
						} else {
							frame.setExtendedState(Frame.NORMAL);
						}
					}
				}
			}					
		});
		
		// Adding a listener to know when the main window loses or gains focus
		frame.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				// Nothing to do here
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
				final PreferencesManager prefm = PreferencesManager.getInstance();
				String systemTrayIconEnable = prefm.getPreference("system-tray-icon");
				if (SystemTray.isSupported() && 
					!isOldSystemTray() && 
					systemTrayIconEnable.equals(WDUtilities.APP_YES)) {
					frame.setExtendedState(Frame.NORMAL);							
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize(JFrame frame) {
		// Configuring tooltips
		ToolTipManager.sharedInstance().setInitialDelay(100);
		
		frame.setBounds(100, 100, 694, 445);
		// If the system tray is old, then windows must be bigger in order to paint Minimize button
		if (isOldSystemTray()) {
			frame.setBounds(100, 100, 694, 484);
		}
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
		tabbedPane.setBorder(null);
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
		providersPanel.setBorder(null);
		tabbedPane.addTab(i18nBundle.getString("providers.title"), null, providersPanel, null);
		providersPanel.setLayout(null);

		wallhavenCheckbox = new JCheckBox(i18nBundle.getString("providers.wallhaven.title"));
		wallhavenCheckbox.setBounds(13, 109, 129, 23);
		providersPanel.add(wallhavenCheckbox);

		JLabel lblKeywords = new JLabel(i18nBundle.getString("providers.keywords"));
		lblKeywords.setBounds(12, 14, 70, 15);
		providersPanel.add(lblKeywords);
		
		searchKeywords = new JTextField();
		searchKeywords.setFont(new Font("Dialog", Font.PLAIN, 11));
		searchKeywords.setBounds(100, 8, 295, 27);
		providersPanel.add(searchKeywords);
		searchKeywords.setColumns(10);

		JSeparator separator1 = new JSeparator();
		separator1.setBounds(12, 104, 610, 2);
		providersPanel.add(separator1);
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			JLabel lblKeywordsHelp = new JLabel(icon);
			lblKeywordsHelp.setToolTipText(i18nBundle.getString("providers.keywords.help"));
			lblKeywordsHelp.setBounds(398, 12, 30, 23);
			providersPanel.add(lblKeywordsHelp);
		} catch (IOException ex) {
			JLabel lblKeywordsHelp = new JLabel("(separated by ;) (Empty->All wallpapers)");
			lblKeywordsHelp.setBounds(362, 39, 70, 15);
			providersPanel.add(lblKeywordsHelp);
		}
		
		// Only integers will be allowed
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);
		
		searchTypeWallhavenComboBox = new JComboBox<ComboItem>();
		searchTypeWallhavenComboBox.setBounds(321, 111, 149, 19);
		providersPanel.add(searchTypeWallhavenComboBox);
		JLabel lblSearchTypeWallhaven = new JLabel(i18nBundle.getString("providers.wallhaven.search.type"));
		lblSearchTypeWallhaven.setBounds(224, 114, 94, 15);
		providersPanel.add(lblSearchTypeWallhaven);
		
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setParseIntegerOnly(true);

		JSeparator separator2 = new JSeparator();
		separator2.setBounds(13, 135, 610, 2);
		providersPanel.add(separator2);
		
		devianartCheckbox = new JCheckBox(i18nBundle.getString("providers.devianart.title"));
		devianartCheckbox.setBounds(13, 138, 129, 23);
		providersPanel.add(devianartCheckbox);
		
		JLabel lblDevianartSearchType = new JLabel(i18nBundle.getString("providers.wallhaven.search.type"));
		lblDevianartSearchType.setBounds(224, 142, 94, 15);
		providersPanel.add(lblDevianartSearchType);

		devianartSearchTypeComboBox = new JComboBox<ComboItem>();
		devianartSearchTypeComboBox.setBounds(321, 140, 150, 19);
		providersPanel.add(devianartSearchTypeComboBox);
		
		JSeparator separator3 = new JSeparator();
		separator3.setBounds(12, 164, 610, 2);
		providersPanel.add(separator3);
		
		bingCheckbox = new JCheckBox(i18nBundle.getString("providers.bing.title"));
		bingCheckbox.setBounds(13, 167, 249, 23);
		providersPanel.add(bingCheckbox);
		
		JSeparator separator4 = new JSeparator();
		separator4.setBounds(13, 193, 610, 2);
		providersPanel.add(separator4);
		
		socialWallpaperingCheckbox = new JCheckBox(i18nBundle.getString("providers.social.wallpapering.title"));
		socialWallpaperingCheckbox.setBounds(13, 196, 194, 23);
		providersPanel.add(socialWallpaperingCheckbox);
		
		socialWallpaperingIgnoreKeywordsCheckbox = new JCheckBox(i18nBundle.getString("providers.social.wallpapering.ignore"));
		socialWallpaperingIgnoreKeywordsCheckbox.setBounds(221, 196, 143, 23);
		providersPanel.add(socialWallpaperingIgnoreKeywordsCheckbox);
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			JLabel lblIgnoreKeywordsSocialWallpaperingHelp = new JLabel(icon);
			lblIgnoreKeywordsSocialWallpaperingHelp.setToolTipText(i18nBundle.getString("providers.social.wallpapering.help"));
			lblIgnoreKeywordsSocialWallpaperingHelp.setBounds(365, 196, 30, 23);
			providersPanel.add(lblIgnoreKeywordsSocialWallpaperingHelp);
		} catch (IOException ex) {
			JLabel lblIgnoreKeywordsSocialWallpaperingHelp = new JLabel(i18nBundle.getString("providers.social.wallpapering.ignore"));
			lblIgnoreKeywordsSocialWallpaperingHelp.setBounds(366, 212, 30, 23);
			providersPanel.add(lblIgnoreKeywordsSocialWallpaperingHelp);
		}

		JSeparator separator5 = new JSeparator();
		separator5.setBounds(14, 221, 610, 2);
		providersPanel.add(separator5);
		
		wallpaperFusionCheckbox = new JCheckBox(i18nBundle.getString("providers.wallpaperfusion.title"));
		wallpaperFusionCheckbox.setBounds(13, 226, 210, 23);
		providersPanel.add(wallpaperFusionCheckbox);
		
		JSeparator separator6 = new JSeparator();
		separator6.setBounds(14, 252, 610, 2);
		providersPanel.add(separator6);
		
		dualMonitorCheckbox = new JCheckBox(i18nBundle.getString("providers.dual.monitor.backgrounds.title"));
		dualMonitorCheckbox.setBounds(14, 257, 201, 23);
		providersPanel.add(dualMonitorCheckbox);
		
		lblSearchTypeDualMonitor = new JLabel(i18nBundle.getString("providers.wallhaven.search.type"));
		lblSearchTypeDualMonitor.setBounds(227, 261, 94, 15);
		providersPanel.add(lblSearchTypeDualMonitor);

		searchTypeDualMonitorComboBox = new JComboBox<ComboItem>();
		searchTypeDualMonitorComboBox.setBounds(324, 259, 149, 19);
		providersPanel.add(searchTypeDualMonitorComboBox);
		
		JLabel lblResolution = new JLabel(i18nBundle.getString("providers.resolution"));
		lblResolution.setBounds(13, 44, 94, 15);
		providersPanel.add(lblResolution);
		
		widthResolution = new JFormattedTextField((Format) null);
		widthResolution.setColumns(4);
		widthResolution.setBounds(100, 41, 53, 28);
		providersPanel.add(widthResolution);
		
		lblX = new JLabel("x");
		lblX.setBounds(153, 46, 12, 15);
		providersPanel.add(lblX);
		
		heigthResolution = new JFormattedTextField((Format) null);
		heigthResolution.setColumns(4);
		heigthResolution.setBounds(159, 41, 53, 28);
		providersPanel.add(heigthResolution);
		
		btnChangeResolution = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/edit_16px_icon.png"));
			btnChangeResolution.setIcon(new ImageIcon(img));
			btnChangeResolution.setToolTipText(i18nBundle.getString("providers.change.resolution"));
			btnChangeResolution.setBounds(214, 38, 34, 33);
		} catch (IOException ex) {
			btnChangeResolution.setText("Change Res.");
			btnChangeResolution.setBounds(224, 38, 131, 25);
		}
		providersPanel.add(btnChangeResolution);
		
		JLabel lblDownloadPolicy = new JLabel(i18nBundle.getString("providers.download.policy"));
		lblDownloadPolicy.setBounds(13, 76, 149, 15);
		providersPanel.add(lblDownloadPolicy);
		
		downloadPolicyComboBox = new JComboBox<ComboItem>();
		downloadPolicyComboBox.setBounds(159, 72, 463, 24);
		providersPanel.add(downloadPolicyComboBox);
		
		btnResetResolution = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/reset_16px_icon.png"));
			btnResetResolution.setIcon(new ImageIcon(img));
			btnResetResolution.setToolTipText(i18nBundle.getString("providers.reset.resolution"));
			btnResetResolution.setBounds(250, 38, 34, 33);
		} catch (IOException ex) {
			btnResetResolution.setText("Reset");
			btnResetResolution.setBounds(357, 38, 72, 25);
		}

		btnApplyResolution = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/accept_16px_icon.png"));
			btnApplyResolution.setIcon(new ImageIcon(img));
			btnApplyResolution.setToolTipText(i18nBundle.getString("providers.save.changes"));
			btnApplyResolution.setBounds(214, 38, 34, 33);
		} catch (IOException ex) {
			btnApplyResolution.setText("Apply");
			btnApplyResolution.setBounds(224, 38, 131, 25);
		}
				
		btnPause = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/pause_16px_icon.png"));
			btnPause.setIcon(new ImageIcon(img));
			btnPause.setToolTipText(i18nBundle.getString("providers.pause"));
			btnPause.setBounds(552, 6, 34, 33);
		} catch (IOException ex) {
			btnPause.setToolTipText(i18nBundle.getString("providers.pause"));
			btnPause.setBounds(431, 6, 34, 33);
		}

		btnPlay = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/play_16px_icon.png"));
			btnPlay.setIcon(new ImageIcon(img));
			btnPlay.setToolTipText(i18nBundle.getString("providers.resume"));
			btnPlay.setBounds(552, 6, 34, 33);
		} catch (IOException ex) {
			btnPlay.setToolTipText(i18nBundle.getString("providers.resume"));
			btnPlay.setBounds(431, 6, 34, 33);
		}
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/green_spot_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblGreenSpot = new JLabel(icon);			
			lblGreenSpot.setToolTipText(i18nBundle.getString("providers.downloading.process.enabled"));
			lblGreenSpot.setBounds(599, 16, 20, 18);
		} catch (IOException ex) {
			lblGreenSpot = new JLabel(i18nBundle.getString("providers.downloading.process.enabled"));
			lblGreenSpot.setBounds(599, 16, 30, 23);
		}

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/red_spot_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblRedSpot = new JLabel(icon);			
			lblRedSpot.setToolTipText(i18nBundle.getString("providers.downloading.process.disabled"));
			lblRedSpot.setBounds(599, 16, 20, 18);
		} catch (IOException ex) {
			lblRedSpot = new JLabel(i18nBundle.getString("providers.downloading.process.disabled"));
			lblRedSpot.setBounds(599, 16, 30, 23);
		}

		btnChangeKeywords = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/edit_16px_icon.png"));
			btnChangeKeywords.setIcon(new ImageIcon(img));
			btnChangeKeywords.setToolTipText(i18nBundle.getString("providers.change.keywords"));
			btnChangeKeywords.setBounds(436, 6, 34, 33);
		} catch (IOException ex) {
			btnChangeKeywords.setText(i18nBundle.getString("providers.change.keywords"));
			btnChangeKeywords.setBounds(436, 6, 131, 25);
		}
		providersPanel.add(btnChangeKeywords);

		btnApplyKeywords = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/accept_16px_icon.png"));
			btnApplyKeywords.setIcon(new ImageIcon(img));
			btnApplyKeywords.setToolTipText(i18nBundle.getString("providers.save.changes"));
			btnApplyKeywords.setBounds(436, 6, 34, 33);
		} catch (IOException ex) {
			btnApplyKeywords.setText(i18nBundle.getString("providers.save.changes"));
			btnApplyKeywords.setBounds(436, 6, 131, 25);
		}

		// Application Settings (tab)
		appSettingsPanel = new JPanel();
		appSettingsPanel.setBorder(null);
		tabbedPane.addTab(i18nBundle.getString("application.settings.title"), null, appSettingsPanel, null);
		appSettingsPanel.setLayout(null);
		
		JLabel lblTimer = new JLabel(i18nBundle.getString("application.settings.downloading.time"));
		lblTimer.setBounds(12, 7, 439, 19);
		appSettingsPanel.add(lblTimer);
		
		timerComboBox = new JComboBox<ComboItem>();
		timerComboBox.setBounds(435, 6, 96, 23);
		appSettingsPanel.add(timerComboBox);
		JLabel lblDownloadDirectorySize = new JLabel(i18nBundle.getString("application.settings.maximun.size"));
		lblDownloadDirectorySize.setBounds(12, 34, 304, 19);
		appSettingsPanel.add(lblDownloadDirectorySize);
		
		downloadDirectorySize = new JFormattedTextField(integerFormat);
		downloadDirectorySize.setColumns(4);
		downloadDirectorySize.setBounds(313, 28, 56, 28);
		appSettingsPanel.add(downloadDirectorySize);
		
		JSeparator settingsSeparator1 = new JSeparator();
		settingsSeparator1.setBounds(12, 62, 631, 2);
		appSettingsPanel.add(settingsSeparator1);
		
		moveFavoriteCheckBox = new JCheckBox(i18nBundle.getString("application.settings.move"));
		moveFavoriteCheckBox.setBounds(12, 72, 226, 23);
		appSettingsPanel.add(moveFavoriteCheckBox);

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblMoveHelp = new JLabel(icon);
			lblMoveHelp.setToolTipText(i18nBundle.getString("application.settings.move.help"));
			lblMoveHelp.setBounds(251, 72, 30, 23);
			appSettingsPanel.add(lblMoveHelp);
		} catch (IOException ex) {
			JLabel lblMoveHelp = new JLabel(i18nBundle.getString("application.settings.move.help"));
			lblMoveHelp.setBounds(213, 85, 30, 23);
			appSettingsPanel.add(lblMoveHelp);
		}
		
		JLabel lblMoveFavoriteDirectory = new JLabel(i18nBundle.getString("application.settings.move.directory"));
		lblMoveFavoriteDirectory.setBounds(12, 103, 134, 19);
		appSettingsPanel.add(lblMoveFavoriteDirectory);
		moveDirectory = new JFormattedTextField((Format) null);
		moveDirectory.setEditable(false);
		moveDirectory.setColumns(4);
		moveDirectory.setBounds(144, 96, 405, 31);
		appSettingsPanel.add(moveDirectory);
		
		btnChangeMoveDirectory = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/change_folder_24px_icon.png"));
			btnChangeMoveDirectory.setIcon(new ImageIcon(img));
			btnChangeMoveDirectory.setToolTipText(i18nBundle.getString("application.settings.move.button.help"));
			btnChangeMoveDirectory.setBounds(561, 95, 34, 33);
		} catch (IOException ex) {
			btnChangeMoveDirectory.setText(i18nBundle.getString("application.settings.move.button.help"));
			btnChangeMoveDirectory.setBounds(561, 107, 34, 33);
		}	
		appSettingsPanel.add(btnChangeMoveDirectory);

		JSeparator settingsSeparator2 = new JSeparator();
		settingsSeparator2.setBounds(12, 134, 631, 2);
		appSettingsPanel.add(settingsSeparator2);
		
		lblNotifications = new JLabel(i18nBundle.getString("application.settings.notifications"));
		lblNotifications.setBounds(12, 143, 126, 19);
		appSettingsPanel.add(lblNotifications);
		
		notificationsComboBox = new JComboBox<ComboItem>();
		notificationsComboBox.setBounds(143, 140, 134, 23);
		appSettingsPanel.add(notificationsComboBox);

		JLabel lblI18n = new JLabel(i18nBundle.getString("application.settings.i18n"));
		lblI18n.setBounds(12, 235, 104, 19);
		appSettingsPanel.add(lblI18n);

		i18nComboBox = new JComboBox<ComboItem>();
		i18nComboBox.setBounds(144, 233, 134, 23);
		appSettingsPanel.add(i18nComboBox);

		startMinimizedCheckBox = new JCheckBox(i18nBundle.getString("application.settings.start.minimized"));
		startMinimizedCheckBox.setBounds(12, 173, 179, 23);
		appSettingsPanel.add(startMinimizedCheckBox);
		
		JSeparator settingsSeparator3 = new JSeparator();
		settingsSeparator3.setBounds(12, 168, 631, 2);
		appSettingsPanel.add(settingsSeparator3);
		
		JLabel lblTimeToMinimize = new JLabel(i18nBundle.getString("application.settings.time.minimize"));
		lblTimeToMinimize.setBounds(12, 201, 126, 19);
		appSettingsPanel.add(lblTimeToMinimize);
		
		timeToMinimizeComboBox = new JComboBox<ComboItem>();
		timeToMinimizeComboBox.setBounds(144, 199, 134, 24);
		appSettingsPanel.add(timeToMinimizeComboBox);
		
		btnChangeSize = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/edit_16px_icon.png"));
			btnChangeSize.setIcon(new ImageIcon(img));
			btnChangeSize.setToolTipText(i18nBundle.getString("application.settings.change.size"));
			btnChangeSize.setBounds(376, 26, 34, 33);
		} catch (IOException ex) {
			btnChangeSize.setText(i18nBundle.getString("application.settings.change.size"));
			btnChangeSize.setBounds(376, 26, 131, 25);
		}
		appSettingsPanel.add(btnChangeSize);
		
		settingsSeparator4 = new JSeparator();
		settingsSeparator4.setBounds(12, 228, 631, 2);
		appSettingsPanel.add(settingsSeparator4);
		
		settingsSeparator5 = new JSeparator();
		settingsSeparator5.setBounds(12, 260, 631, 2);
		appSettingsPanel.add(settingsSeparator5);
		
		// Only if system tray is supported, user will be able to minimize the application into the
		// system tray
		if (SystemTray.isSupported()) {
			JSeparator settingsSeparator6 = new JSeparator();
			settingsSeparator6.setBounds(12, 293, 631, 2);
			appSettingsPanel.add(settingsSeparator6);

		    stIconCheckBox = new JCheckBox(i18nBundle.getString("application.settings.system.tray.icon"));
			stIconCheckBox.setBounds(13, 298, 225, 26);
			appSettingsPanel.add(stIconCheckBox);
			
			lblSystemTrayHelp = new JLabel((Icon) null);
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
				ImageIcon icon = new ImageIcon(img);
				lblSystemTrayHelp = new JLabel(icon);
				lblSystemTrayHelp.setToolTipText(i18nBundle.getString("application.settings.system.tray.icon.help"));
				lblSystemTrayHelp.setBounds(252, 298, 30, 23);
				appSettingsPanel.add(lblSystemTrayHelp);
				
			} catch (IOException ex) {
				lblSystemTrayHelp = new JLabel(i18nBundle.getString("application.settings.system.tray.icon.help"));
				lblSystemTrayHelp.setBounds(566, 173, 30, 23);
				appSettingsPanel.add(lblSystemTrayHelp);
			}
		}
		
		btnApplySize = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/accept_16px_icon.png"));
			btnApplySize.setIcon(new ImageIcon(img));
			btnApplySize.setToolTipText(i18nBundle.getString("providers.save.changes"));
			btnApplySize.setBounds(376, 26, 34, 33);
		} catch (IOException ex) {
			btnApplySize.setText(i18nBundle.getString("providers.save.changes"));
			btnApplySize.setBounds(376, 26, 131, 25);
		}
		
		initOnBootCheckBox = new JCheckBox(i18nBundle.getString("application.settings.starts.on.boot"));
		initOnBootCheckBox.setBounds(12, 265, 631, 23);
		appSettingsPanel.add(initOnBootCheckBox);

		// Automatic changer (tab)
		// Only visible for DE which allows automated changer
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			changerPanel = new JPanel();
			changerPanel.setBorder(null);
			tabbedPane.addTab(i18nBundle.getString("changer.title"), null, changerPanel, null);
			changerPanel.setLayout(null);

			JLabel lblChanger = new JLabel(i18nBundle.getString("changer.change.every"));
			lblChanger.setBounds(12, 4, 179, 19);
			changerPanel.add(lblChanger);
			
			changerComboBox = new JComboBox<ComboItem>();
			changerComboBox.setBounds(192, 6, 94, 19);
			changerPanel.add(changerComboBox);

			JLabel lblChangerDirectory = new JLabel(i18nBundle.getString("changer.change.directory"));
			lblChangerDirectory.setBounds(12, 35, 537, 19);
			changerPanel.add(lblChangerDirectory);

			if (WDUtilities.isGnomeish()) {
				JSeparator changerSeparator1 = new JSeparator();
				changerSeparator1.setBounds(12, 312, 631, 2);
				changerPanel.add(changerSeparator1);		

				multiMonitorCheckBox = new JCheckBox(i18nBundle.getString("changer.change.multimonitor"));
				multiMonitorCheckBox.setBounds(11, 315, 281, 23);
				changerPanel.add(multiMonitorCheckBox);
				
				/**
				 * multiMonitorCheckBox Action Listener.
				 */
				// Clicking event
				multiMonitorCheckBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						PreferencesManager prefm = PreferencesManager.getInstance();
						
						if (multiMonitorCheckBox.isSelected()) {
							prefm.setPreference("application-changer-multimonitor", WDUtilities.APP_YES);
							WDUtilities.changeMultiMonitorModeGnomish("spanned");
						} else {
							prefm.setPreference("application-changer-multimonitor", WDUtilities.APP_NO);
							WDUtilities.changeMultiMonitorModeGnomish("stretched");
						}
					}
				});
				
			}

			btnAddDirectory = new JButton();
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/add_16px_icon.png"));
				btnAddDirectory.setIcon(new ImageIcon(img));
				btnAddDirectory.setToolTipText(i18nBundle.getString("changer.change.add.directory"));
				btnAddDirectory.setBounds(561, 136, 34, 33);
			} catch (IOException ex) {
				btnAddDirectory.setToolTipText(i18nBundle.getString("changer.change.add.directory"));
				btnAddDirectory.setBounds(561, 274, 34, 33);
			}		

			btnRemoveDirectory = new JButton();
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/remove_16px_icon.png"));
				btnRemoveDirectory.setIcon(new ImageIcon(img));
				btnRemoveDirectory.setToolTipText(i18nBundle.getString("changer.change.remove.directory"));
				btnRemoveDirectory.setBounds(561, 181, 34, 33);
			} catch (IOException ex) {
				btnRemoveDirectory.setToolTipText(i18nBundle.getString("changer.change.remove.directory"));
				btnRemoveDirectory.setBounds(561, 319, 34, 33);
			}		

			JScrollPane listDirectoriesScrollPane = new JScrollPane();
			listDirectoriesScrollPane.setBounds(143, 55, 406, 250);
			changerPanel.add(listDirectoriesScrollPane);
			
			listDirectoriesToWatch = new JList<String>();
			listDirectoriesScrollPane.setColumnHeaderView(listDirectoriesToWatch);
			listDirectoriesToWatch.setBackground(UIManager.getColor("Button.background"));
			listDirectoriesToWatch.setToolTipText(i18nBundle.getString("changer.change.directory.help"));
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
					String[] changerFolders = changerFoldersProperty.split(";");
					ArrayList<String> changerFoldersList = new ArrayList<String>(Arrays.asList(changerFolders));
					Iterator<String> iterator = changerFoldersList.iterator();
					while (iterator.hasNext()) {
						String directory = iterator.next();
						if (directory.equals(directoryToRemove)) {
							iterator.remove();
						}
					}
					String modifiedChangerFoldersProperty = "";
					iterator = changerFoldersList.iterator();
					while (iterator.hasNext()) {
						String directory = iterator.next();
						modifiedChangerFoldersProperty = modifiedChangerFoldersProperty + directory;
						if (iterator.hasNext()) {
							modifiedChangerFoldersProperty = modifiedChangerFoldersProperty + ";";
						}
					}
					prefm.setPreference("application-changer-folder", modifiedChangerFoldersProperty);
					if (listDirectoriesModel.size() <= 1) {
						changerPanel.remove(btnRemoveDirectory);
						changerPanel.repaint();
					}
				}
			});
		}

		// Downloads Directory (tab)
		miscPanel = new JPanel();
		miscPanel.setBorder(null);
		tabbedPane.addTab(i18nBundle.getString("downloads.directory.title"), null, miscPanel, null);
		miscPanel.setLayout(null);
		
		JLabel lblDownloadsDirectory = new JLabel(i18nBundle.getString("downloads.directory.title"));
		lblDownloadsDirectory.setBounds(12, 16, 160, 19);
		miscPanel.add(lblDownloadsDirectory);
		
		downloadsDirectory = new JFormattedTextField((Format) null);
		downloadsDirectory.setEditable(false);
		downloadsDirectory.setColumns(4);
		downloadsDirectory.setBounds(174, 11, 405, 31);
		miscPanel.add(downloadsDirectory);
		
		btnOpenDownloadsDirectory = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/open_folder_24px_icon.png"));
			btnOpenDownloadsDirectory.setIcon(new ImageIcon(img));
			btnOpenDownloadsDirectory.setToolTipText(i18nBundle.getString("downloads.directory.open"));
			btnOpenDownloadsDirectory.setBounds(12, 39, 34, 33);
		} catch (IOException ex) {
			btnOpenDownloadsDirectory.setText("Open");
			btnOpenDownloadsDirectory.setBounds(589, 11, 72, 25);
		}		
		if (WDUtilities.isSnapPackage()) {
			btnOpenDownloadsDirectory.setEnabled(false);
		}
		miscPanel.add(btnOpenDownloadsDirectory);
		
		btnClipboard = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/clipboard_24px_icon.png"));
			btnClipboard.setIcon(new ImageIcon(img));
			btnClipboard.setToolTipText(i18nBundle.getString("downloads.directory.copy.clipboard"));
			btnClipboard.setBounds(54, 39, 34, 33);
		} catch (IOException ex) {
			btnClipboard.setText("Clipboard");
			btnClipboard.setBounds(630, 8, 34, 33);
		}
		miscPanel.add(btnClipboard);
		
		btnChangeDownloadsDirectory = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/change_folder_24px_icon.png"));
			btnChangeDownloadsDirectory.setIcon(new ImageIcon(img));
			btnChangeDownloadsDirectory.setToolTipText(i18nBundle.getString("downloads.directory.change"));
			btnChangeDownloadsDirectory.setBounds(588, 10, 34, 33);
		} catch (IOException ex) {
			btnChangeDownloadsDirectory.setText("Change Downloads Directory");
			btnChangeDownloadsDirectory.setBounds(12, 186, 259, 25);
		}	
		miscPanel.add(btnChangeDownloadsDirectory);
		
		diskSpacePB.setBounds(174, 101, 405, 18);
		miscPanel.add(diskSpacePB);
		
		JLabel lblDiskSpace = new JLabel(i18nBundle.getString("downloads.directory.space"));
		lblDiskSpace.setBounds(12, 100, 160, 19);
		miscPanel.add(lblDiskSpace);
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/warning_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblSpaceWarning = new JLabel(icon);
			lblSpaceWarning.setToolTipText(i18nBundle.getString("downloads.directory.full"));
			lblSpaceWarning.setBounds(588, 98, 30, 23);
			miscPanel.add(lblSpaceWarning);
			
			JSeparator separator7 = new JSeparator();
			separator7.setBounds(12, 83, 610, 2);
			miscPanel.add(separator7);
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
		wallpapersPanel.setBorder(null);
		tabbedPane.addTab(i18nBundle.getString("wallpapers.title"), null, wallpapersPanel, null);
		wallpapersPanel.setLayout(null);

		JLabel lblLastWallpapers = new JLabel(i18nBundle.getString("wallpapers.last.five"));
		lblLastWallpapers.setBounds(12, 12, 238, 15);
		wallpapersPanel.add(lblLastWallpapers);
		
        scroll = new JScrollPane();
        scroll.setPreferredSize(new Dimension(300, 400));
		scroll.setBounds(12, 36, 652, 105);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		wallpapersPanel.add(scroll);
		
		btnManageWallpapers = new JButton(i18nBundle.getString("wallpapers.downloaded"));
		btnManageWallpapers.setBackground(Color.WHITE);
		btnManageWallpapers.setBounds(12, 211, 214, 25);
		wallpapersPanel.add(btnManageWallpapers);
		
		btnRemoveWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveWallpaper.setIcon(new ImageIcon(img));
			btnRemoveWallpaper.setToolTipText(i18nBundle.getString("wallpapers.delete"));
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
			btnSetFavoriteWallpaper.setToolTipText(i18nBundle.getString("wallpapers.favorite"));
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
			btnPreviewWallpaper.setToolTipText(i18nBundle.getString("wallpapers.preview"));
			btnPreviewWallpaper.setBounds(95, 149, 34, 33);
		} catch (IOException ex) {
			btnPreviewWallpaper.setText("Preview wallpaper");
			btnPreviewWallpaper.setBounds(95, 149, 34, 33);
		}
		wallpapersPanel.add(btnPreviewWallpaper);
		
		btnMoveWallpapers = new JButton(i18nBundle.getString("wallpapers.move"));
		btnMoveWallpapers.setBackground(Color.WHITE);
		btnMoveWallpapers.setBounds(12, 248, 214, 25);
		wallpapersPanel.add(btnMoveWallpapers);
		
		btnChooseWallpaper = new JButton(i18nBundle.getString("wallpapers.choose"));
		btnChooseWallpaper.setBackground(Color.WHITE);
		btnChooseWallpaper.setBounds(12, 285, 214, 25);
		wallpapersPanel.add(btnChooseWallpaper);
		
        if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
    		btnRandomWallpaper = new JButton(i18nBundle.getString("wallpapers.random"));
    		btnRandomWallpaper.setBackground(Color.WHITE);
    		btnRandomWallpaper.setBounds(12, 323, 214, 25);
    		wallpapersPanel.add(btnRandomWallpaper);
        }

		btnSetWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/desktop_24px_icon.png"));
			btnSetWallpaper.setIcon(new ImageIcon(img));
			btnSetWallpaper.setToolTipText(i18nBundle.getString("wallpapers.set"));
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
		aboutPanel.setBorder(null);
		tabbedPane.addTab(i18nBundle.getString("about.title"), null, aboutPanel, null);
		aboutPanel.setLayout(null);
		
		JLabel lblVersion = new JLabel(i18nBundle.getString("about.version"));
		lblVersion.setBounds(12, 16, 70, 15);
		aboutPanel.add(lblVersion);
		
		version = new JTextField() {
			// JTextField without border
			@Override public void setBorder(Border border) {
				// No borders!
			}
		};
		version.setHorizontalAlignment(SwingConstants.CENTER);
		version.setEditable(false);
		version.setBounds(73, 15, 35, 19);
		version.setColumns(10);
		version.setOpaque(false);
		version.setBackground(new Color(0, 0, 0, 0));
		aboutPanel.add(version);
		
		aboutSeparator1 = new JSeparator();
		aboutSeparator1.setBounds(11, 43, 610, 2);
		aboutPanel.add(aboutSeparator1);
		
		lblDeveloper = new JLabel(i18nBundle.getString("about.developer"));
		lblDeveloper.setBounds(12, 57, 95, 15);
		aboutPanel.add(lblDeveloper);
		
		developer = new JTextField() {
			// JTextField without border
			@Override public void setBorder(Border border) {
				// No borders!
			}
		};
		developer.setEditable(false);
		developer.setBounds(145, 56, 405, 19);
		developer.setColumns(10);
		developer.setOpaque(false);
		developer.setBackground(new Color(0, 0, 0, 0));
		aboutPanel.add(developer);
		
		lblSourceCode = new JLabel(i18nBundle.getString("about.source.code"));
		lblSourceCode.setBounds(12, 338, 108, 15);
		aboutPanel.add(lblSourceCode);
		
		btnRepository = new JButton("New button");
		btnRepository.setBounds(134, 334, 483, 25);
		btnRepository.setText("<HTML><FONT color=\"#000099\"><U>" + pm.getProperty("repository.code") + "</U></FONT></HTML>");
		btnRepository.setHorizontalAlignment(SwingConstants.LEFT);
		btnRepository.setOpaque(false);
		btnRepository.setContentAreaFilled(false);
		btnRepository.setBorderPainted(false);
		aboutPanel.add(btnRepository);
		
		aboutSeparator2 = new JSeparator();
		aboutSeparator2.setBounds(12, 108, 610, 7);
		aboutPanel.add(aboutSeparator2);
		
		JLabel lblIcons = new JLabel(i18nBundle.getString("about.icons"));
		lblIcons.setBounds(12, 81, 95, 15);
		aboutPanel.add(lblIcons);
		
		icons = new JTextField() {
			public void setBorder(Border border) {
			}
		};
		icons.setHorizontalAlignment(SwingConstants.LEFT);
		icons.setText(" Jaime Álvarez; Dave Gandy");
		icons.setEditable(false);
		icons.setColumns(10);
		icons.setBounds(145, 79, 219, 19);
		icons.setOpaque(false);
		icons.setBackground(new Color(0, 0, 0, 0));
		aboutPanel.add(icons);
		
		btnIcons = new JButton("<HTML><FONT color=\"#000099\"><U>http://www.flaticon.com/</U></FONT></HTML>");
		btnIcons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnIcons.setHorizontalAlignment(SwingConstants.LEFT);
		btnIcons.setOpaque(false);
		btnIcons.setContentAreaFilled(false);
		btnIcons.setBorderPainted(false);
		btnIcons.setBounds(381, 76, 229, 25);
		aboutPanel.add(btnIcons);
		
		JLabel lblChangelog = new JLabel(i18nBundle.getString("about.changelog"));
		lblChangelog.setBounds(12, 117, 91, 20);
		aboutPanel.add(lblChangelog);
		
		JScrollPane changelogScrollPane = new JScrollPane();
		changelogScrollPane.setBounds(11, 144, 610, 183);
		aboutPanel.add(changelogScrollPane);
		
		JTextPane changelogTextPane = new JTextPane();
		changelogScrollPane.setViewportView(changelogTextPane);
		
		StyledDocument doc = changelogTextPane.getStyledDocument();

		//  Define a keyword attribute
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.BLUE);
		StyleConstants.setBold(keyWord, true);

		// Changelog
		try
		{
			// Version 3.2
		    doc.insertString(0, i18nBundle.getString("about.changelog.features.3.2.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.3.2.text"), null );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.3.2.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.3.2.text"), null );

			// Version 3.1
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.3.1.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.3.1.text"), null );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.3.1.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.3.1.text"), null );

		    // Version 3.0
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.3.0.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.3.0.text"), null );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.3.0.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.3.0.text"), null );
			
			// Version 2.9
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.2.9.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.2.9.text"), null );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.2.9.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.2.9.text"), null );
			
			// Version 2.8
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.2.8.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.2.8.text"), null );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.2.8.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.2.8.text"), null );

		    // Version 2.7
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.2.7.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.features.2.7.text"), null );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.2.7.title"), keyWord );
		    doc.insertString(doc.getLength(), i18nBundle.getString("about.changelog.bugs.2.7.text"), null );
		}
		catch(Exception exception) { 
			if (LOG.isInfoEnabled()) {
				LOG.error("Error rendering jTextPane. Error: " + exception.getMessage());
			}
		}
		// Text to the beginning
		changelogTextPane.setCaretPosition(0);

		// Help (tab)
		helpPanel = new JPanel();
		helpPanel.setBorder(null);
		tabbedPane.addTab(i18nBundle.getString("help.title"), null, helpPanel, null);
		helpPanel.setLayout(null);
		
		JScrollPane helpScrollPane = new JScrollPane();
		helpScrollPane.setBounds(12, 12, 657, 359);
		helpPanel.add(helpScrollPane);
		
		JTextPane helpTextPane = new JTextPane();
		helpTextPane.setCaretPosition(0);

		// Set content as HTML
		helpTextPane.setContentType("text/html");
		helpTextPane.setText(i18nBundle.getString("help.tips"));
		helpTextPane.setEditable(false);//so its not editable
		helpTextPane.setOpaque(false);//so we dont see whit background

		helpTextPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                	try {
						WDUtilities.openLinkOnBrowser(hle.getURL().toURI().toString());
					} catch (URISyntaxException exception) {
						if (LOG.isInfoEnabled()) {
							LOG.error("Error opening a link. Message: " + exception.getMessage());
						}
					}
                }
            }
        });

		// Text to the beginning
		helpTextPane.setCaretPosition(0);

		helpScrollPane.setViewportView(helpTextPane);
		
		// Minimize button will only be available on OS with an
		// old system tray
		btnMinimize = new JButton(i18nBundle.getString("global.minimize"));
		btnMinimize.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnMinimize = new GridBagConstraints();
		gbc_btnMinimize.insets = new Insets(0, 0, 0, 5);
		gbc_btnMinimize.anchor = GridBagConstraints.NORTH;
		gbc_btnMinimize.gridx = 0;
		gbc_btnMinimize.gridy = 3;
		if (isOldSystemTray()) {
			frame.getContentPane().add(btnMinimize, gbc_btnMinimize);
		}
		
		// Setting up configuration
		initializeGUI();
		
		// Setting up listeners
		initializeListeners();
				
		// Starting automated changer process
		initializeChanger();

		// Starting harvesting process
		initializeHarvesting();
		
		// Starting pause / resume feature
		pauseResumeRepaint();
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
					searchTypeWallhavenComboBox.setEnabled(true);
					prefm.setPreference("provider-wallhaven", WDUtilities.APP_YES);
				} else {
					searchTypeWallhavenComboBox.setEnabled(false);
					prefm.setPreference("provider-wallhaven", WDUtilities.APP_NO);
				}

				// Restarting harvesting process if it is needed
				restartDownloadingProcess();
			}
		});

		/**
		 * searchTypeWallhavenComboBox Action Listener.
		 */
		// Clicking event
		searchTypeWallhavenComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("wallpaper-search-type", new Integer(searchTypeWallhavenComboBox.getSelectedIndex()).toString());
				// Restarting downloading process
				restartDownloadingProcess();
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
					prefm.setPreference("provider-devianart", WDUtilities.APP_YES);
				} else {
					devianartSearchTypeComboBox.setEnabled(false);
					prefm.setPreference("provider-devianart", WDUtilities.APP_NO);
				}

				// Restarting harvesting process if it is needed
				restartDownloadingProcess();
			}
		});

		/**
		 * devianartSearchTypeComboBox Action Listener.
		 */
		// Clicking event
		devianartSearchTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("wallpaper-devianart-search-type", new Integer(devianartSearchTypeComboBox.getSelectedIndex()).toString());
				// Restarting downloading process
				restartDownloadingProcess();
			}
		});

		/**
		 * bingCheckbox Action Listener.
		 */
		// Clicking event
		bingCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (bingCheckbox.isSelected()) {
					prefm.setPreference("provider-bing", WDUtilities.APP_YES);
				} else {
					prefm.setPreference("provider-bing", WDUtilities.APP_NO);
				}

				// Restarting harvesting process if it is needed
				restartDownloadingProcess();
			}
		});

		/**
		 * socialWallpaperingCheckbox Action Listener.
		 */
		// Clicking event
		socialWallpaperingCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (socialWallpaperingCheckbox.isSelected()) {
					prefm.setPreference("provider-socialWallpapering", WDUtilities.APP_YES);
					socialWallpaperingIgnoreKeywordsCheckbox.setEnabled(true);
				} else {
					prefm.setPreference("provider-socialWallpapering", WDUtilities.APP_NO);
					socialWallpaperingIgnoreKeywordsCheckbox.setEnabled(false);
				}

				// Restarting harvesting process if it is needed
				restartDownloadingProcess();
			}
		});

		/**
		 * socialWallpaperingIgnoreKeywordsCheckbox Action Listener.
		 */
		// Clicking event
		socialWallpaperingIgnoreKeywordsCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (socialWallpaperingIgnoreKeywordsCheckbox.isSelected()) {
					prefm.setPreference("provider-socialWallpapering-ignore-keywords", WDUtilities.APP_YES);
				} else {
					prefm.setPreference("provider-socialWallpapering-ignore-keywords", WDUtilities.APP_NO);
				}

				// Restarting harvesting process if it is needed
				restartDownloadingProcess();
			}
		});

		/**
		 * wallpaperFusionCheckbox Action Listener.
		 */
		// Clicking event
		wallpaperFusionCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (wallpaperFusionCheckbox.isSelected()) {
					prefm.setPreference("provider-wallpaperFusion", WDUtilities.APP_YES);
				} else {
					prefm.setPreference("provider-wallpaperFusion", WDUtilities.APP_NO);
				}

				// Restarting harvesting process if it is needed
				restartDownloadingProcess();
			}
		});
		
		/**
		 * dualMonitorCheckbox Action Listener.
		 */
		// Clicking event
		dualMonitorCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (dualMonitorCheckbox.isSelected()) {
					prefm.setPreference("provider-dualMonitorBackgrounds", WDUtilities.APP_YES);
					searchTypeDualMonitorComboBox.setEnabled(true);
				} else {
					prefm.setPreference("provider-dualMonitorBackgrounds", WDUtilities.APP_NO);
					searchTypeDualMonitorComboBox.setEnabled(false);
				}
				
				// Restarting harvesting process if it is needed
				restartDownloadingProcess();
			}
		});

		/**
		 * searchTypeDualMonitorComboBox Action Listener.
		 */
		// Clicking event
		searchTypeDualMonitorComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("provider-dualMonitorBackgrounds-search-type", new Integer(searchTypeDualMonitorComboBox.getSelectedIndex()).toString());
				// Restarting downloading process
				restartDownloadingProcess();
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
		 * btnChangeResolution Action Listener.
		 */
		// Clicking event
		btnChangeResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				widthResolution.setEnabled(true);
				heigthResolution.setEnabled(true);
				providersPanel.remove(btnChangeResolution);
				providersPanel.add(btnApplyResolution);
				providersPanel.add(btnResetResolution);
				providersPanel.repaint();
			}
		});

		/**
		 * btnApplyResolution Action Listener.
		 */
		// Clicking event
		btnApplyResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String width = widthResolution.getValue().toString();
				String heigth = heigthResolution.getValue().toString();

				// Sanitizing width and heigth
				width = width.replace(".", "");
				width = width.replace(",", "");
				heigth = heigth.replace(".", "");
				heigth = heigth.replace(",", "");

				if (!width.isEmpty() && !heigth.isEmpty()) {
					prefm.setPreference("wallpaper-resolution", width + "x" + heigth);
				}
				
				widthResolution.setEnabled(false);
				heigthResolution.setEnabled(false);
				providersPanel.remove(btnApplyResolution);
				providersPanel.remove(btnResetResolution);
				providersPanel.add(btnChangeResolution);
				providersPanel.repaint();
				// Restarting downloading process if it is needed
				if (areProvidersChecked()) {
					restartDownloadingProcess();
				}
			}
		});

		/**
		 * btnResetResolution Action Listener.
		 */
		// Clicking event
		btnResetResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String screenResolution = WDUtilities.getResolution();
				String[] resolution = screenResolution.split("x");
		        widthResolution.setValue(new Integer(resolution[0]));
				heigthResolution.setValue(new Integer(resolution[1]));
			}
		});
		
		/**
		 * downloadPolicyComboBox Action Listener.
		 */
		// Clicking event
		downloadPolicyComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("download-policy", new Integer(downloadPolicyComboBox.getSelectedIndex()).toString());
				// Restarting downloading process if it is needed
				if (areProvidersChecked()) {
					restartDownloadingProcess();
				}
			}
		});

		/**
		 * btnChangeKeywords Action Listener.
		 */
		// Clicking event
		btnChangeKeywords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				searchKeywords.setEnabled(true);
				providersPanel.remove(btnChangeKeywords);
				providersPanel.add(btnApplyKeywords);
				providersPanel.repaint();
			}
		});

		/**
		 * btnApplyKeywords Action Listener.
		 */
		// Clicking event
		btnApplyKeywords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!searchKeywords.getText().isEmpty()) {
					prefm.setPreference("provider-wallhaven-keywords", searchKeywords.getText());					
				} else {
					prefm.setPreference("provider-wallhaven-keywords", PreferencesManager.DEFAULT_VALUE);
				}
				
				searchKeywords.setEnabled(false);
				providersPanel.remove(btnApplyKeywords);
				providersPanel.add(btnChangeKeywords);
				providersPanel.repaint();
				// Restarting downloading process if it is needed
				if (areProvidersChecked()) {
					restartDownloadingProcess();
				}
			}
		});

		/**
		 * btnChangeSize Action Listener.
		 */
		// Clicking event
		btnChangeSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				downloadDirectorySize.setEnabled(true);
				appSettingsPanel.remove(btnChangeSize);
				appSettingsPanel.add(btnApplySize);
				appSettingsPanel.repaint();
			}
		});

		/**
		 * btnApplySize Action Listener.
		 */
		// Clicking event
		btnApplySize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("application-max-download-folder-size", downloadDirectorySize.getValue().toString());
				downloadDirectorySize.setEnabled(false);
				appSettingsPanel.remove(btnApplySize);
				appSettingsPanel.add(btnChangeSize);
				appSettingsPanel.repaint();

				// Refreshing Disk Space Progress Bar
				refreshProgressBar();
				
				// Restarting downloading process if it is needed
				if (areProvidersChecked()) {
					restartDownloadingProcess();
				}
			}
		});

		/**
		 * timerComboBox Action Listener.
		 */
		// Clicking event
		timerComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("application-timer", new Integer(timerComboBox.getSelectedIndex()).toString());
				// Restarting downloading process if it is needed
				if (areProvidersChecked()) {
					restartDownloadingProcess();
				}
			}
		});

		/**
		 * startMinimizedCheckBox Action Listener.
		 */
		// Clicking event
		startMinimizedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (startMinimizedCheckBox.isSelected()) {
					prefm.setPreference("start-minimized", WDUtilities.APP_YES);
					timeToMinimizeComboBox.setEnabled(true);
				} else {
					prefm.setPreference("start-minimized", WDUtilities.APP_NO);
					timeToMinimizeComboBox.setEnabled(false);
				}
				prefm.setPreference("download-policy", new Integer(downloadPolicyComboBox.getSelectedIndex()).toString());
			}
		});

		/**
		 * timeToMinimizeComboBox Action Listener.
		 */
		// Clicking event
		timeToMinimizeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Integer timeToMinimize = new Integer(timeToMinimizeComboBox.getSelectedIndex()) + 1;
				prefm.setPreference("time-to-minimize", timeToMinimize.toString());
			}
		});

		if (SystemTray.isSupported()) {
			/**
			 * stIconCheckBox Action Listener.
			 */
			// Clicking event
			stIconCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (stIconCheckBox.isSelected()) {
						prefm.setPreference("system-tray-icon", WDUtilities.APP_YES);
					} else {
						prefm.setPreference("system-tray-icon", WDUtilities.APP_NO);
					}
				}
			});
		}

		/**
		 * moveFavoriteCheckBox Action Listener.
		 */
		// Clicking event
		moveFavoriteCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (moveFavoriteCheckBox.isSelected()) {
					prefm.setPreference("move-favorite", WDUtilities.APP_YES);
					prefm.setPreference("move-favorite-folder", moveDirectory.getText());
				} else {
					prefm.setPreference("move-favorite", WDUtilities.APP_NO);
					prefm.setPreference("move-favorite-folder", PreferencesManager.DEFAULT_VALUE);
				}
			}
		});

		/**
		 * notificationsComboBox Action Listener.
		 */
		// Clicking event
		notificationsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("application-notifications", new Integer(notificationsComboBox.getSelectedIndex()).toString());
			}
		});

		/**
		 * i18nComboBox Action Listener.
		 */
		// Clicking event
		i18nComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("application-i18n", new Integer(i18nComboBox.getSelectedIndex()).toString());
				pauseDownloadingProcess();
				frame.dispose();
				frame.setVisible(false);
				window = null;
				window = new WallpaperDownloader();
			}
		});

		/**
		 * changerComboBox Action Listener.
		 */
		// Clicking event
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			changerComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					prefm.setPreference("application-changer", new Integer(changerComboBox.getSelectedIndex()).toString());
					
					//Stoping and starting changer process
					changer.stop();
					changer.start();
				}
			});
		}
		
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
					DialogManager info = new DialogManager(i18nBundle.getString("messages.downloaded.path.copied"), 2000);
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
					WDUtilities.openLinkOnBrowser(pm.getProperty("repository.code"));
				}
	      });

		 /**
		  * btnIcons Action Listener.
		  */
	      btnIcons.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					WDUtilities.openLinkOnBrowser(pm.getProperty("repository.icons"));
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

			/**
			 * btnChooseWallpaper Action Listener.
			 */
			btnChooseWallpaper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ChooseWallpaperWindow wmw = new ChooseWallpaperWindow();
					wmw.setVisible(true);
				}
			});
			
  	        /**
		     * btnRandomWallpaper Action Listener.
		     */
		    // Clicking event
			if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
				btnRandomWallpaper.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						WDUtilities.getWallpaperChanger().setRandomWallpaper();
					}
				});
			}

			/**
			 * initOnBootCheckBox Action Listener.
			 */
			// Clicking event
			initOnBootCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (initOnBootCheckBox.isSelected()) {
						// It is necessary to copy wallpaperdownloader.desktop file to ~/.config/autostart
						File srcFile = new File(WDUtilities.getAppPath() + File.separator + WDUtilities.WD_DESKTOP_FILE);
						File destFile = new File(WDUtilities.getAutostartFilePath() + File.separator + WDUtilities.WD_DESKTOP_FILE);
						try {
							FileUtils.copyFile(srcFile, destFile);
							DialogManager info = new DialogManager(i18nBundle.getString("messages.autostart.ok"), 2000);
							info.openDialog();
						} catch (IOException exception) {
							initOnBootCheckBox.setSelected(false);
							if (LOG.isInfoEnabled()) {
								LOG.error("wallpaperdownloader.sh couldn't be copied. Error: " + exception.getMessage());
							}
							DialogManager info = new DialogManager(i18nBundle.getString("messages.autostart.ko"), 2000);
							info.openDialog();
						}
					} else {
						// It is necessary to remove wallpaperdownloader.desktop file from ~/.config/autostart
						File destFile = new File(WDUtilities.getAutostartFilePath() + File.separator + WDUtilities.WD_DESKTOP_FILE);
						try {
							FileUtils.forceDelete(destFile);
							DialogManager info = new DialogManager(i18nBundle.getString("messages.autostart.removed.ok"), 2000);
							info.openDialog();
						} catch (IOException exception) {
							initOnBootCheckBox.setSelected(true);
							if (LOG.isInfoEnabled()) {
								LOG.error("wallpaperdownloader.sh couldn't be copied. Error: " + exception.getMessage());
							}
							DialogManager info = new DialogManager(i18nBundle.getString("messages.autostart.ko"), 2000);
							info.openDialog();
						}
					}
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
			DialogManager info = new DialogManager(i18nBundle.getString("messages.downloading.process.paused"), 2000);
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
			DialogManager info = new DialogManager(i18nBundle.getString("messages.downloading.process.resumed"), 2000);
			info.openDialog();
		}
	}
	
	/**
	 * Restarts downloading process.
	 */
	public static void restartDownloadingProcess() {
		final PreferencesManager prefm = PreferencesManager.getInstance();
		if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_YES)) {
			// The recalibration dialog will be performed within a Swing Timer for avoiding to
			// freeze the entire UI. The event will wait only 1 millisecond in order to display
			// the message. The dialog is important in order to block the entire UI and avoid the
			// user modifies anything else until the harvesting process is stopped and started
			// again
			// one time (setRepeats is false)
			Timer dialogTimer = new Timer(1, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
					DialogManager info = new DialogManager(i18nBundle.getString("messages.harvesting.process.recalibrating"), 5000);
					info.openDialog();
			    }    
			});
			dialogTimer.setRepeats(false);
			dialogTimer.start();
			harvester.stop();
			// The start of the harvester will be performed within a Swing Timer for avoiding to
			// freeze the entire UI. The event will wait 5000 milliseconds in order to let the
			// stopping process ends successfully and only will be done
			// one time (setRepeats is false)
			Timer timer = new Timer(5000, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	harvester.start();
					// Repaint pause/resume buttons
					pauseResumeRepaint();
			    }    
			});
			timer.setRepeats(false);
			timer.start();
		} else {
			// Repaint pause/resume buttons
			pauseResumeRepaint();
		}

	}

	/**
	 * Minimizes application.
	 */
	public static void minimizeApplication() {
		final PreferencesManager prefm = PreferencesManager.getInstance();
		// The application is minimized within System Tray
        // Check the SystemTray is supported or user has selected no system tray icon
		String systemTrayIconEnable = prefm.getPreference("system-tray-icon");
        if (!SystemTray.isSupported() || !WDUtilities.isMinimizable() || systemTrayIconEnable.equals(WDUtilities.APP_NO)) {
            LOG.error("SystemTray is not supported. Frame is traditionally minimized");
            // Frame is traditionally minimized
            frame.setExtendedState(Frame.ICONIFIED);
            return;
        } else {
        	if (isOldSystemTray()) {
    			// For OS and DE which have an old system tray, legacy mode will be used
                final PopupMenu popup = new PopupMenu();
                // TODO:
                URL systemTrayIcon = null;
                if (!prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
                	systemTrayIcon = WallpaperDownloader.class.getResource("/images/icons/wd_systemtray_icon_green.png");
                } else {
                	systemTrayIcon = WallpaperDownloader.class.getResource("/images/icons/wd_systemtray_icon_red.png");
                }
                final TrayIcon trayIcon = new TrayIcon(new ImageIcon(systemTrayIcon, "Wallpaper Downloader").getImage(), "Wallpaper Downloader");
                final SystemTray tray = SystemTray.getSystemTray();
               
                // Create a pop-up menu components -- BEGIN
                // Maximize
                java.awt.MenuItem maximizeItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.maximize"));
                maximizeItem.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent evt) {
                    	int state = frame.getExtendedState();  
                    	state = state & ~Frame.ICONIFIED;  
                    	frame.setExtendedState(state);  
                    	frame.setVisible(true);
                    	
                    	// Removing system tray icon
                    	tray.remove(trayIcon);
                	}
                });
                popup.add(maximizeItem);
                
                // Open downloads directory
                if (!WDUtilities.isSnapPackage()) {
                    java.awt.MenuItem browseItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.open"));
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
                    popup.add(browseItem);
                }

        		// Pause / Resume
                java.awt.MenuItem resumeItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.resume"));
                java.awt.MenuItem pauseItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.pause"));

                resumeItem.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent evt) {
                		resumeDownloadingProcess();
                		popup.remove(resumeItem);
                		popup.insert(pauseItem, 2);
                		
                    	// Removing system tray icon
                    	tray.remove(trayIcon);

                    	minimizeApplication();
                	}
                });

                pauseItem.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent evt) {
                		pauseDownloadingProcess();
                		popup.remove(pauseItem);
                		popup.insert(resumeItem, 2);

                    	// Removing system tray icon
                    	tray.remove(trayIcon);

                    	minimizeApplication();
                	}
                });

                if (harvester.getStatus() != Harvester.STATUS_DISABLED) {
        			// Checking downloading process
        			if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
        	            popup.add(resumeItem);
        	            popup.remove(pauseItem);
        			} else {
        	            popup.add(pauseItem);
        	            popup.remove(resumeItem);
        			}
        		}

                // Change wallpaper
                if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
                	java.awt.MenuItem changeItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.change"));
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
        			java.awt.MenuItem moveItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.move"));
    	            moveItem.addActionListener(new ActionListener() {
    	            	public void actionPerformed(ActionEvent evt) {
    	            		moveFavoriteWallpapers();
    	            	}
    	            });
    	            popup.add(moveItem);
                }

        		// Manage downloaded wallpapers
                java.awt.MenuItem manageItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.manage"));
                manageItem.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent evt) {
        				WallpaperManagerWindow wmw = new WallpaperManagerWindow();
        				wmw.setVisible(true);                    	
        			}
                });
                popup.add(manageItem);

        		// Choose wallpaper
                java.awt.MenuItem chooseItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.choose"));
                chooseItem.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent evt) {
        				ChooseWallpaperWindow cww = new ChooseWallpaperWindow();
        				cww.setVisible(true);
        			}
                });
                popup.add(chooseItem);

                // Separator
                popup.addSeparator();
                
                // Exit
                java.awt.MenuItem exitItem = new java.awt.MenuItem(i18nBundle.getString("system.tray.exit"));
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
                trayIcon.setImageAutoSize(true);
               
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    LOG.error("TrayIcon could not be added.");
                }
                
                // Hiding window
                frame.setVisible(false);        		
        	} else {
    			// GTK3 integration is going to be used for Plasma 5 and Gmone Shell
        		// Changing state
        		frame.setExtendedState(Frame.ICONIFIED);
    			// Hiding window
    			frame.setVisible(false);
    			Display.setAppName("WallpaperDownloader");
    			Display display = new Display();
    			Shell shell = new Shell(display);
    			InputStream iconInputStream = null;
    			if (!prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
                	iconInputStream = WallpaperDownloader.class.getResourceAsStream("/images/icons/wd_systemtray_icon_green.png");
                } else {
                	iconInputStream = WallpaperDownloader.class.getResourceAsStream("/images/icons/wd_systemtray_icon_red.png");
                }
    			org.eclipse.swt.graphics.Image icon = new org.eclipse.swt.graphics.Image(display, iconInputStream);
    			final Tray tray = display.getSystemTray();
    			if (tray == null) {
    				System.out.println ("The system tray is not available");
    			} else {
    				// Creating the pop up menu
    				final Menu menu = new Menu (shell, SWT.POP_UP);
    				
    				final org.eclipse.swt.widgets.TrayItem item = new org.eclipse.swt.widgets.TrayItem (tray, SWT.NONE);
    				item.setToolTipText("WallpaperDownloader");
    				item.addListener (SWT.DefaultSelection, new Listener () {          
    		            public void handleEvent (Event e) {        	                	
    	                	// Removing system tray icon and all stuff related
    		            	item.dispose();
    		            	icon.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();

    	                	// frame.setExtendedState(Frame.NORMAL) will be set in the WindowsListener
    	                	frame.setVisible(true);
    		            }
    				});
    				
    				// Adding options to the menu
    				// Maximize
    				MenuItem maximize = new MenuItem (menu, SWT.PUSH);
    				maximize.setText (i18nBundle.getString("system.tray.maximize"));
    				maximize.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
    	                	// Removing system tray icon and all stuff related
    		            	item.dispose();
    		            	icon.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();
    	                	
    	                	// frame.setExtendedState(Frame.NORMAL) will be set in the WindowsListener
    	                	frame.setVisible(true);
    		            }
    				});

    				// Open downloaded wallpapers
    				if (!WDUtilities.isSnapPackage()) {
        				MenuItem open = new MenuItem (menu, SWT.PUSH);
        				open.setText (i18nBundle.getString("system.tray.open"));
        				open.addListener (SWT.Selection, new Listener () {          
        		            public void handleEvent (Event e) {
                        		File downloadsDirectory = new File(WDUtilities.getDownloadsPath());
                        		Desktop desktop = Desktop.getDesktop();
                        		try {
            						desktop.open(downloadsDirectory);
            					} catch (IOException exception) {
            						// There was some error trying to open the downloads Directory
            						LOG.error("Error trying to open the Downloads directory. Error: " + exception.getMessage());
            					}
        		            }
        				});
    				}
    				
    				// Resume/Pause downloading process
    				MenuItem resume = new MenuItem (menu, SWT.PUSH);
    				MenuItem pause = new MenuItem (menu, SWT.PUSH);

    				resume.setText (i18nBundle.getString("system.tray.resume"));
    				resume.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
                    		resumeDownloadingProcess();
                    		resume.setEnabled(false);
                    		pause.setEnabled(true);
                    		
    	                	// Removing system tray icon and all stuff related
    		            	item.dispose();
    		            	icon.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();

    	                	// Minimizing again the application to refresh the icon
    	                	// in the system tray
    	                	minimizeApplication();
    		            }
    				});

					pause.setText (i18nBundle.getString("system.tray.pause")); 
					pause.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
                    		pauseDownloadingProcess();
                    		pause.setEnabled(false);;
                    		resume.setEnabled(true);

    	                	// Removing system tray icon and all stuff related
    		            	item.dispose();
    		            	icon.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();

    	                	// Minimizing again the application to refresh the icon
    	                	// in the system tray
    	                	minimizeApplication();
    		            }
    				});
    				
//    				if (harvester.getStatus() != Harvester.STATUS_DISABLED) {
            			// Checking downloading process
            			if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
            				pause.setEnabled(false);
            				resume.setEnabled(true);
            			} else {
            				resume.setEnabled(false);
            				pause.setEnabled(true);
            			}
//            		}

                    // Change wallpaper
                    if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
        				MenuItem change = new MenuItem (menu, SWT.PUSH);
    					change.setText (i18nBundle.getString("system.tray.change"));
        				change.addListener (SWT.Selection, new Listener () {          
        		            public void handleEvent (Event e) {
        	            		WDUtilities.getWallpaperChanger().setRandomWallpaper();
        		            }
        				});
                    }

                    // Move favorite wallpapers
    				String moveFavoriteEnable = prefm.getPreference("move-favorite");
            		if (moveFavoriteEnable.equals(WDUtilities.APP_YES)) {
        				MenuItem move = new MenuItem (menu, SWT.PUSH);
    					move.setText (i18nBundle.getString("system.tray.move"));
        				move.addListener (SWT.Selection, new Listener () {          
        		            public void handleEvent (Event e) {
        	            		moveFavoriteWallpapers();
        		            }
        				});
                    }

            		// Manage downloaded wallpapers
    				MenuItem manage = new MenuItem (menu, SWT.PUSH);
					manage.setText (i18nBundle.getString("system.tray.manage"));
    				manage.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
    	                	// Removing system tray icon and all stuff related
    		            	item.dispose();
    		            	icon.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();
    	                	
    	                	fromSystemTray = true;
    	                	frame.setExtendedState(Frame.NORMAL); 
    	                	frame.setVisible(true);

            				WallpaperManagerWindow wmw = new WallpaperManagerWindow();
            				wmw.setVisible(true);
    		            }
    				});

            		// Choose wallpaper
    				MenuItem choose = new MenuItem (menu, SWT.PUSH);
					choose.setText (i18nBundle.getString("system.tray.choose"));
    				choose.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
    	                	// Removing system tray icon and all stuff related
    		            	item.dispose();
    		            	icon.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();
    	                	
    	                	fromSystemTray = true;
    	                	frame.setExtendedState(Frame.NORMAL);
    	                	frame.setVisible(true);

            				ChooseWallpaperWindow cww = new ChooseWallpaperWindow();
            				cww.setVisible(true);
    		            }
    				});

                    // Exit
    				MenuItem exit = new MenuItem (menu, SWT.PUSH);
					exit.setText (i18nBundle.getString("system.tray.exit"));
    				exit.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
    	                	// Removing system tray icon and all stuff related
    		            	icon.dispose();
    		            	item.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();

            				// The application is closed
            				System.exit(0);		                	
    		            }
    				});

    			    item.addListener(SWT.MenuDetect, new Listener() {
    			    	public void handleEvent(Event event) {
    			    		menu.setVisible(true);
    			        }
    			    });
    			    
    				item.setImage(icon);
    			}
    			
    			while (!shell.isDisposed ()) {
    				if (!display.readAndDispatch ()) display.sleep ();
    			}
    			
    			icon.dispose();
    			display.dispose ();
        	}
        }
	}
	
	private static void moveFavoriteWallpapers() {

		final PreferencesManager prefm = PreferencesManager.getInstance();
		
		// Dialog for please wait. It has to be executed on a SwingWorker (another Thread) in 
		// order to not block the entire execution of the application
		final DialogManager pleaseWaitDialog = new DialogManager(i18nBundle.getString("messages.wait"));

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
			DialogManager info = new DialogManager(i18nBundle.getString("messages.moved"), 2000);
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
		searchKeywords.setEnabled(false);

		// Resolution
		String[] resolution = prefm.getPreference("wallpaper-resolution").split("x");
        widthResolution.setValue(new Integer(resolution[0]));
        widthResolution.setEnabled(false);
		heigthResolution.setValue(new Integer(resolution[1]));
        heigthResolution.setEnabled(false);

        // Download policy
        downloadPolicyComboBox.addItem(new ComboItem(i18nBundle.getString("providers.download.policy.0"), "0"));
        downloadPolicyComboBox.addItem(new ComboItem(i18nBundle.getString("providers.download.policy.1"), "1"));
        downloadPolicyComboBox.addItem(new ComboItem(i18nBundle.getString("providers.download.policy.2"), "2"));
        downloadPolicyComboBox.setSelectedIndex(new Integer(prefm.getPreference("download-policy")));
        
		// Wallhaven.cc
		String wallhavenEnable = prefm.getPreference("provider-wallhaven");
		if (wallhavenEnable.equals(WDUtilities.APP_YES)) {
			wallhavenCheckbox.setSelected(true);
			searchTypeWallhavenComboBox.setEnabled(true);
		} else {
			searchTypeWallhavenComboBox.setEnabled(false);
		}
		searchTypeWallhavenComboBox.addItem(new ComboItem(i18nBundle.getString("providers.wallhaven.policy.0"), "0"));
		searchTypeWallhavenComboBox.addItem(new ComboItem(i18nBundle.getString("providers.wallhaven.policy.1"), "1")); 
		searchTypeWallhavenComboBox.addItem(new ComboItem(i18nBundle.getString("providers.wallhaven.policy.2"), "2")); 
		searchTypeWallhavenComboBox.addItem(new ComboItem(i18nBundle.getString("providers.wallhaven.policy.3"), "3")); 
		searchTypeWallhavenComboBox.addItem(new ComboItem(i18nBundle.getString("providers.wallhaven.policy.4"), "4"));
		searchTypeWallhavenComboBox.setSelectedIndex(new Integer(prefm.getPreference("wallpaper-search-type")));
		
		// Devianart
		String devianartEnable = prefm.getPreference("provider-devianart");
		if (devianartEnable.equals(WDUtilities.APP_YES)) {
			devianartCheckbox.setSelected(true);
			devianartSearchTypeComboBox.setEnabled(true);
		} else {
			devianartSearchTypeComboBox.setEnabled(false);
		}
		devianartSearchTypeComboBox.addItem(new ComboItem(i18nBundle.getString("providers.devianart.policy.0"), "0")); 
		devianartSearchTypeComboBox.addItem(new ComboItem(i18nBundle.getString("providers.devianart.policy.1"), "1")); 
		devianartSearchTypeComboBox.addItem(new ComboItem(i18nBundle.getString("providers.devianart.policy.2"), "2")); 
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

		// DualMonitorBackground
		String dualMonitorEnable = prefm.getPreference("provider-dualMonitorBackgrounds");
		if (dualMonitorEnable.equals(WDUtilities.APP_YES)) {
			dualMonitorCheckbox.setSelected(true);
			searchTypeDualMonitorComboBox.setEnabled(true);
		} else {
			searchTypeDualMonitorComboBox.setEnabled(false);
		}
		searchTypeDualMonitorComboBox.addItem(new ComboItem(i18nBundle.getString("providers.dual.monitor.policy.0"), "0")); 
		searchTypeDualMonitorComboBox.addItem(new ComboItem(i18nBundle.getString("providers.dual.monitor.policy.1"), "1")); 
		searchTypeDualMonitorComboBox.addItem(new ComboItem(i18nBundle.getString("providers.dual.monitor.policy.2"), "2")); 
		searchTypeDualMonitorComboBox.addItem(new ComboItem(i18nBundle.getString("providers.dual.monitor.policy.3"), "3")); 
		searchTypeDualMonitorComboBox.setSelectedIndex(new Integer(prefm.getPreference("provider-dualMonitorBackgrounds-search-type")));

		// ---------------------------------------------------------------------
		// Checking user settings
		// ---------------------------------------------------------------------
		timerComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.downloading.time.0"), "0"));
		timerComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.downloading.time.1"), "1"));
		timerComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.downloading.time.2"), "2"));
		timerComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.downloading.time.3"), "3"));
		timerComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.downloading.time.4"), "4"));
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
		notificationsComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.notifications.0"), "0"));
		notificationsComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.notifications.1"), "1"));
		notificationsComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.notifications.2"), "2"));
		notificationsComboBox.setSelectedIndex(new Integer(prefm.getPreference("application-notifications")));

		// i18n
		i18nComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.i18n.0"), "0"));
		i18nComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.i18n.1"), "1"));
		i18nComboBox.setSelectedIndex(new Integer(prefm.getPreference("application-i18n")));

		// Start minimized feature
		String startMinimizedEnable = prefm.getPreference("start-minimized");
		if (startMinimizedEnable.equals(WDUtilities.APP_YES)) {
			startMinimizedCheckBox.setSelected(true);
			timeToMinimizeComboBox.setEnabled(true);
		} else {
			startMinimizedCheckBox.setSelected(false);
			timeToMinimizeComboBox.setEnabled(false);
		}
		
		// Time to minimize
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.0"), "1"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.1"), "2"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.2"), "3"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.3"), "4"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.4"), "5"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.5"), "6"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.6"), "7"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.7"), "8"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.8"), "9"));
		timeToMinimizeComboBox.addItem(new ComboItem(i18nBundle.getString("application.settings.time.minimize.9"), "10"));
		timeToMinimizeComboBox.setSelectedIndex((new Integer(prefm.getPreference("time-to-minimize")) - 1));

		// System tray icon
		if (SystemTray.isSupported()) {
			String systemTrayIconEnable = prefm.getPreference("system-tray-icon");
			if (systemTrayIconEnable.equals(WDUtilities.APP_YES)) {
				stIconCheckBox.setSelected(true);
			} else {
				stIconCheckBox.setSelected(false);
			}
		}
		
		// Starts on boot
		if (WDUtilities.getOperatingSystem().equals(WDUtilities.OS_WINDOWS) || 
			WDUtilities.getOperatingSystem().equals(WDUtilities.OS_WINDOWS_7) || 
			WDUtilities.getOperatingSystem().equals(WDUtilities.OS_WINDOWS_10) || 
			WDUtilities.isSnapPackage()) {
			// Windows users won't be able to start the application when the system is booted checking
			// the GUI option
			// This option is not supported within snap package either
			initOnBootCheckBox.setEnabled(false);
		} else {
			String autostartFilePath = WDUtilities.getAutostartFilePath();
			File autostartFile = new File(autostartFilePath + WDUtilities.WD_DESKTOP_FILE);
			if (autostartFile.exists()) {
				initOnBootCheckBox.setSelected(true);
			} else {
				initOnBootCheckBox.setSelected(false);
			}
		}
		
		// Changer
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			changerComboBox.addItem(new ComboItem(i18nBundle.getString("changer.change.every.0"), "0"));
			changerComboBox.addItem(new ComboItem(i18nBundle.getString("changer.change.every.1"), "1"));
			changerComboBox.addItem(new ComboItem(i18nBundle.getString("changer.change.every.2"), "2"));
			changerComboBox.addItem(new ComboItem(i18nBundle.getString("changer.change.every.3"), "3"));
			changerComboBox.addItem(new ComboItem(i18nBundle.getString("changer.change.every.4"), "4"));
			changerComboBox.addItem(new ComboItem(i18nBundle.getString("changer.change.every.5"), "5"));
			changerComboBox.setSelectedIndex(new Integer(prefm.getPreference("application-changer")));
			
			// Multi monitor support
			if (WDUtilities.isGnomeish()) {
				String multiMonitorSupport = prefm.getPreference("application-changer-multimonitor");
				if (multiMonitorSupport.equals(WDUtilities.APP_YES)) {
					multiMonitorCheckBox.setSelected(true);
				} else {
					multiMonitorCheckBox.setSelected(false);
				}
			}
			
			listDirectoriesModel = new DefaultListModel<String>();
			String changerFoldersProperty = prefm.getPreference("application-changer-folder");
			String[] changerFolders = changerFoldersProperty.split(";");
			for (int i = 0; i < changerFolders.length; i ++) {
				listDirectoriesModel.addElement(changerFolders[i]);
			}
			listDirectoriesToWatch.setModel(listDirectoriesModel);
			changerPanel.add(btnAddDirectory);
			if (listDirectoriesModel.size() > 1) {
				changerPanel.add(btnRemoveDirectory);
			}
		}

		// Directory size
		downloadDirectorySize.setValue(new Integer(prefm.getPreference("application-max-download-folder-size")));
		downloadDirectorySize.setEnabled(false);
		
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
		developer.setText(" Eloy Garcia Almaden (eloy.garcia.pca@gmail.com)");
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
		lastWallpapersList.setFixedCellHeight(100);
		lastWallpapersList.setFixedCellWidth(128);
		lastWallpapersList.setSelectionBackground(Color.cyan);

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
	
	/**
	 * Is an old system tray?.
	 * If OS is Windows or Desktop Environment is MATE, XFCE or Unity, then it is
	 * considered an old system tray
	 * @return boolean
	 */
	private static boolean isOldSystemTray() {
		boolean oldSystemTray = false;
		if (WDUtilities.getOperatingSystem().equals(WDUtilities.OS_WINDOWS) || 
        		WDUtilities.getOperatingSystem().equals(WDUtilities.OS_WINDOWS_7) ||	
        		WDUtilities.getOperatingSystem().equals(WDUtilities.OS_WINDOWS_10) ||	
        		(WDUtilities.getWallpaperChanger() instanceof LinuxWallpaperChanger 
        				&& !((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_GNOME3) 
        				&& !((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_KDE) 
        				&& !((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_CINNAMON)
						&& !((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_PANTHEON))
        		) {

			oldSystemTray = true;
		}
		return oldSystemTray;
	}
	
	/**
	 * Pauses and resumes the harvesting process and repaints the display if it is needed.
	 */
	private static void pauseResumeRepaint() {
		PreferencesManager prefm = PreferencesManager.getInstance();		
		if (harvester.getStatus() == Harvester.STATUS_ENABLED) {
			// Harvesting process is enabled
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
			// Harvesting process is disabled
			if (areProvidersChecked()) {
				// There are providers checked
				providersPanel.remove(btnPause);
				providersPanel.remove(lblGreenSpot);
				providersPanel.add(btnPlay);
				providersPanel.add(lblRedSpot);
			} else {
				// There aren't providers checked
				providersPanel.remove(btnPause);
				providersPanel.remove(btnPlay);
				providersPanel.add(lblRedSpot);
				providersPanel.remove(lblGreenSpot);
			}
		}
		providersPanel.repaint();
	}

	/**
	 * Checks if there are providers scheduled.
	 * @return
	 */
	private static boolean areProvidersChecked() {
		boolean areProvidersChecked = false;
		if (wallhavenCheckbox.isSelected() || 
			devianartCheckbox.isSelected() || 
			bingCheckbox.isSelected() || 
			socialWallpaperingCheckbox.isSelected() || 
			dualMonitorCheckbox.isSelected() || 
			wallpaperFusionCheckbox.isSelected()) {
			areProvidersChecked = true;
		}
		return areProvidersChecked;
	}
}
