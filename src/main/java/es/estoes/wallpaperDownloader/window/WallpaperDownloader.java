/**
 * Copyright 2016-2017 Eloy García Almadén <eloy.garcia.pca@gmail.com>
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.JLabel;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
	protected static WallpaperDownloader window;
	private static final PropertiesManager pm = PropertiesManager.getInstance();
	private static ResourceBundle i18nBundle;
	
	// Attributes
	// diskSpacePB will be an attribute representing disk space occupied within the downloads directory
	// It is static because it will be able to be accessed from any point within the application's code
	public static boolean fromSystemTray;
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
	private JComboBox<ComboItem> changerComboBox;
	private JButton btnChangeMoveDirectory;
	private JFormattedTextField moveDirectory;
	private JLabel lblMoveHelp;
	private JCheckBox moveFavoriteCheckBox;
	private JButton btnMoveWallpapers;
	private JButton btnRandomWallpaper;
	private JLabel lblNotifications;
	private JComboBox<ComboItem> notificationsComboBox;
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
	private JCheckBox dualMonitorCheckbox;
	private JLabel lblSearchTypeDualMonitor;
	private JComboBox<ComboItem> searchTypeDualMonitorComboBox;
	private JButton btnApplyResolution;
	private JButton btnResetResolution;
	private JButton btnChangeKeywords;
	private JButton btnApplyKeywords;
	private JLabel lblSystemTrayHelp;
	
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
				// Resource bundle for i18n
				Locale locale = Locale.getDefault();
				i18nBundle = ResourceBundle.getBundle("I18n", locale);
				
				// Log configuration
				WDConfigManager.configureLog();

				// Application configuration
				WDConfigManager.checkConfig();
				window = new WallpaperDownloader();
				
				// 3.- System Look & Feel
                try {
                	String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
                	if (systemLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel") || WDUtilities.isSnapPackage()) {
                		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");                		
                	} else {
                		UIManager.setLookAndFeel(systemLookAndFeel);                		
                	}
                } catch (ClassNotFoundException exception) {
                    exception.printStackTrace();
                } catch (InstantiationException exception) {
                    exception.printStackTrace();
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                } catch (UnsupportedLookAndFeelException exception) {
                    exception.printStackTrace();
                }

                SwingUtilities.updateComponentTreeUI(window.frame);
				
				// 4,. Configuring main frame
				window.frame.setBackground(new Color(255, 255, 255));
				window.frame.setExtendedState(Frame.NORMAL);
				window.frame.setVisible(true);
				window.frame.setTitle(pm.getProperty("app.name") + " V" + pm.getProperty("app.version"));
				
				// Command comes from system tray
				fromSystemTray = false;
				
				// Minimize the application if start minimized feature is enable
				if (startMinimizedCheckBox.isSelected()) {
					try {
						// Sleeps during 3 seconds in order to avoid problems in GNOME 3 minimization
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
				
				// Adding a listener for knowing when the main window changes its state
				window.frame.addWindowStateListener(new WindowStateListener() {
					@Override
					public void windowStateChanged(WindowEvent windowEvent) {
						final PreferencesManager prefm = PreferencesManager.getInstance();
						String systemTrayIconEnable = prefm.getPreference("system-tray-icon");
						if (!isOldSystemTray() && systemTrayIconEnable.equals(WDUtilities.APP_YES)) {
							// Check if commands comes from system tray
							if (fromSystemTray) {
								fromSystemTray = false;
							} else {
								// If command doesn't come from system tray, then it is necessary to capture the
								// minimize order
								if (window.frame.getExtendedState() == Frame.NORMAL){
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
									window.frame.setExtendedState(Frame.NORMAL);
								}
							}
						}
					}					
				});
				
				// Adding a listener to know when the main window loses or gains focus
				window.frame.addWindowFocusListener(new WindowFocusListener() {
					@Override
					public void windowGainedFocus(WindowEvent arg0) {
						// Nothing to do here
					}

					@Override
					public void windowLostFocus(WindowEvent arg0) {
						final PreferencesManager prefm = PreferencesManager.getInstance();
						String systemTrayIconEnable = prefm.getPreference("system-tray-icon");
						if (!isOldSystemTray() && systemTrayIconEnable.equals(WDUtilities.APP_YES)) {
							window.frame.setExtendedState(Frame.NORMAL);							
						}
					}
				});

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
		widthResolution.setBounds(100, 42, 53, 27);
		providersPanel.add(widthResolution);
		
		lblX = new JLabel("x");
		lblX.setBounds(153, 46, 12, 15);
		providersPanel.add(lblX);
		
		heigthResolution = new JFormattedTextField((Format) null);
		heigthResolution.setColumns(4);
		heigthResolution.setBounds(159, 42, 53, 27);
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
		timerComboBox.setBounds(453, 6, 96, 23);
		appSettingsPanel.add(timerComboBox);
		JLabel lblDownloadDirectorySize = new JLabel(i18nBundle.getString("application.settings.maximun.size"));
		lblDownloadDirectorySize.setBounds(12, 34, 304, 19);
		appSettingsPanel.add(lblDownloadDirectorySize);
		
		downloadDirectorySize = new JFormattedTextField(integerFormat);
		downloadDirectorySize.setColumns(4);
		downloadDirectorySize.setBounds(313, 30, 56, 27);
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
			lblMoveHelp.setBounds(244, 72, 30, 23);
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
		lblNotifications.setBounds(12, 143, 304, 19);
		appSettingsPanel.add(lblNotifications);
		
		notificationsComboBox = new JComboBox<ComboItem>();
		notificationsComboBox.setBounds(317, 140, 134, 23);
		appSettingsPanel.add(notificationsComboBox);

		startMinimizedCheckBox = new JCheckBox(i18nBundle.getString("application.settings.start.minimized"));
		startMinimizedCheckBox.setBounds(12, 173, 179, 23);
		appSettingsPanel.add(startMinimizedCheckBox);
		
		JSeparator settingsSeparator3 = new JSeparator();
		settingsSeparator3.setBounds(12, 168, 631, 2);
		appSettingsPanel.add(settingsSeparator3);
		
		JLabel lblTimeToMinimize = new JLabel(i18nBundle.getString("application.settings.time.minimize"));
		lblTimeToMinimize.setBounds(194, 175, 126, 19);
		appSettingsPanel.add(lblTimeToMinimize);
		
		timeToMinimizeComboBox = new JComboBox<ComboItem>();
		timeToMinimizeComboBox.setBounds(317, 173, 95, 24);
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
		
		stIconCheckBox = new JCheckBox(i18nBundle.getString("application.settings.system.tray.icon"));
		stIconCheckBox.setBounds(435, 173, 144, 23);
		appSettingsPanel.add(stIconCheckBox);
		
		lblSystemTrayHelp = new JLabel((Icon) null);
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/help_24px_icon.png"));
			ImageIcon icon = new ImageIcon(img);
			lblSystemTrayHelp = new JLabel(icon);
			lblSystemTrayHelp.setToolTipText(i18nBundle.getString("application.settings.system.tray.icon.help"));
			lblSystemTrayHelp.setBounds(574, 173, 30, 23);
			appSettingsPanel.add(lblSystemTrayHelp);
		} catch (IOException ex) {
			lblSystemTrayHelp = new JLabel(i18nBundle.getString("application.settings.system.tray.icon.help"));
			lblSystemTrayHelp.setBounds(566, 173, 30, 23);
			appSettingsPanel.add(lblSystemTrayHelp);
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
		// Only those desktop environment programmed to be changeable will display this option
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {

			JSeparator settingsSeparator4 = new JSeparator();
			settingsSeparator4.setBounds(12, 199, 631, 2);
			appSettingsPanel.add(settingsSeparator4);		
			JLabel lblChanger = new JLabel(i18nBundle.getString("application.settings.change.every"));
			lblChanger.setBounds(12, 208, 304, 19);
			appSettingsPanel.add(lblChanger);
			
			changerComboBox = new JComboBox<ComboItem>();
			changerComboBox.setBounds(317, 210, 94, 19);
			appSettingsPanel.add(changerComboBox);

			JLabel lblChangerDirectory = new JLabel(i18nBundle.getString("application.settings.change.directory"));
			lblChangerDirectory.setBounds(12, 237, 134, 19);
			appSettingsPanel.add(lblChangerDirectory);
			
			
			btnAddDirectory = new JButton();
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/add_16px_icon.png"));
				btnAddDirectory.setIcon(new ImageIcon(img));
				btnAddDirectory.setToolTipText(i18nBundle.getString("application.settings.change.add.directory"));
				btnAddDirectory.setBounds(561, 266, 34, 33);
			} catch (IOException ex) {
				btnAddDirectory.setToolTipText(i18nBundle.getString("application.settings.change.add.directory"));
				btnAddDirectory.setBounds(561, 274, 34, 33);
			}		
			
			btnRemoveDirectory = new JButton();
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/remove_16px_icon.png"));
				btnRemoveDirectory.setIcon(new ImageIcon(img));
				btnRemoveDirectory.setToolTipText(i18nBundle.getString("application.settings.change.remove.directory"));
				btnRemoveDirectory.setBounds(561, 311, 34, 33);
			} catch (IOException ex) {
				btnRemoveDirectory.setToolTipText(i18nBundle.getString("application.settings.change.remove.directory"));
				btnRemoveDirectory.setBounds(561, 319, 34, 33);
			}		
			
			JScrollPane listDirectoriesScrollPane = new JScrollPane();
			listDirectoriesScrollPane.setBounds(143, 244, 406, 121);
			appSettingsPanel.add(listDirectoriesScrollPane);
			
			listDirectoriesToWatch = new JList<String>();
			listDirectoriesScrollPane.setColumnHeaderView(listDirectoriesToWatch);
			listDirectoriesToWatch.setBackground(UIManager.getColor("Button.background"));
			listDirectoriesToWatch.setToolTipText(i18nBundle.getString("application.settings.change.directory.help"));
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
		
		//JProgressBar diskSpacePB = new JProgressBar();
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

		//  Adding text

		try
		{
			// Version 3.0
		    doc.insertString(0, i18nBundle.getString("about.changelog.features.3.0.title"), keyWord );
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
				harvester.stop();
				harvester.start();
				
				// Repaint pause/resume buttons
				pauseResumeRepaint();
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
				harvester.stop();
				harvester.start();
				
				// Repaint pause/resume buttons
				pauseResumeRepaint();
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
				harvester.stop();
				harvester.start();
				
				// Repaint pause/resume buttons
				pauseResumeRepaint();
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
				harvester.stop();
				harvester.start();
				
				// Repaint pause/resume buttons
				pauseResumeRepaint();
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
				harvester.stop();
				harvester.start();

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
				harvester.stop();
				harvester.start();
				
				// Repaint pause/resume buttons
				pauseResumeRepaint();
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
				harvester.stop();
				harvester.start();
				
				// Repaint pause/resume buttons
				pauseResumeRepaint();

			
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
				// Restarting downloading process
				restartDownloadingProcess();
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
				// Restarting downloading process
				restartDownloadingProcess();
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
				// Restarting downloading process
				restartDownloadingProcess();
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
				
				// Restarting downloading process
				restartDownloadingProcess();
			}
		});

		/**
		 * timerComboBox Action Listener.
		 */
		// Clicking event
		timerComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				prefm.setPreference("application-timer", new Integer(timerComboBox.getSelectedIndex()).toString());
				// Restarting downloading process
				restartDownloadingProcess();
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
		 * changerComboBox Action Listener.
		 */
		// Clicking event
		changerComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
					prefm.setPreference("application-changer", new Integer(changerComboBox.getSelectedIndex()).toString());
				}
				
				//Stoping and starting changer process
				changer.stop();
				changer.start();
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
					DialogManager info = new DialogManager(i18nBundle.getString("messages.downloaded.path.copied"), 2000);
					info.openDialog();
				}
			}
		});
		// TODO:
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
						          process = Runtime.getRuntime().exec("xdg-open " + pm.getProperty("repository.icons"));
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
			btnRandomWallpaper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					WDUtilities.getWallpaperChanger().setRandomWallpaper();
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
	 * Restarts downloading process.
	 */
	public static void restartDownloadingProcess() {
		final PreferencesManager prefm = PreferencesManager.getInstance();
		if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_YES)) {
			pauseDownloadingProcess();
			resumeDownloadingProcess();
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
            window.frame.setExtendedState(Frame.ICONIFIED);
            return;
        } else {
        	if (isOldSystemTray()) {
    			// For OS and DE which have an old system tray, legacy mode will be used
                final PopupMenu popup = new PopupMenu();
                URL systemTrayIcon = WallpaperDownloader.class.getResource("/images/icons/wd_systemtray_icon.png");
                final TrayIcon trayIcon = new TrayIcon(new ImageIcon(systemTrayIcon, "Wallpaper Downloader").getImage(), "Wallpaper Downloader");
                final SystemTray tray = SystemTray.getSystemTray();
               
                // Create a pop-up menu components -- BEGIN
                // Maximize
                java.awt.MenuItem maximizeItem = new java.awt.MenuItem("Maximize");
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
                java.awt.MenuItem browseItem = new java.awt.MenuItem("Open downloaded wallpapers");
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
                java.awt.MenuItem resumeItem = new java.awt.MenuItem("Resume");
                java.awt.MenuItem pauseItem = new java.awt.MenuItem("Pause");

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
                	java.awt.MenuItem changeItem = new java.awt.MenuItem("Change wallpaper randomly");
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
        			java.awt.MenuItem moveItem = new java.awt.MenuItem("Move favorite wallpapers");
    	            moveItem.addActionListener(new ActionListener() {
    	            	public void actionPerformed(ActionEvent evt) {
    	            		moveFavoriteWallpapers();
    	            	}
    	            });
    	            popup.add(moveItem);
                }

        		// Manage downloaded wallpapers
                java.awt.MenuItem manageItem = new java.awt.MenuItem("Manage downloaded wallpapers");
                manageItem.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent evt) {
        				WallpaperManagerWindow wmw = new WallpaperManagerWindow();
        				wmw.setVisible(true);                    	
        			}
                });
                popup.add(manageItem);

        		// Choose wallpaper
                java.awt.MenuItem chooseItem = new java.awt.MenuItem("Choose wallpaper");
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
                java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
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
        	} else {
    			// GTK3 integration is going to be used for Plasma 5 and Gmone Shell
        		// Changing state
        		window.frame.setExtendedState(Frame.ICONIFIED);
    			// Hiding window
    			window.frame.setVisible(false);
    			Display.setAppName("WallpaperDownloader");
    			Display display = new Display();
    			Shell shell = new Shell(display);
    			InputStream iconInputStream = WallpaperDownloader.class.getResourceAsStream("/images/icons/wd_systemtray_icon.png");
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

    	                	// window.frame.setExtendedState(Frame.NORMAL) will be set in the WindowsListener
    	                	window.frame.setVisible(true);
    		            }
    				});
    				
    				// Adding options to the menu
    				// Maximize
    				MenuItem maximize = new MenuItem (menu, SWT.PUSH);
    				maximize.setText ("Maximize");
    				maximize.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
    	                	// Removing system tray icon and all stuff related
    		            	item.dispose();
    		            	icon.dispose();
    		            	menu.dispose();
    	                	tray.dispose();
    	                	shell.dispose();
    	                	display.dispose();
    	                	
    	                	// window.frame.setExtendedState(Frame.NORMAL) will be set in the WindowsListener
    	                	window.frame.setVisible(true);
    		            }
    				});

    				// Open downloaded wallpapers
    				MenuItem open = new MenuItem (menu, SWT.PUSH);
    				open.setText ("Open downloaded wallpapers");
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
    				
    				// Resume/Pause downloading process
    				MenuItem resume = new MenuItem (menu, SWT.PUSH);
    				MenuItem pause = new MenuItem (menu, SWT.PUSH);

    				resume.setText ("Resume");
    				resume.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
                    		resumeDownloadingProcess();
                    		resume.setEnabled(false);
                    		pause.setEnabled(true);
    		            }
    				});

					pause.setText ("Pause");
    				pause.addListener (SWT.Selection, new Listener () {          
    		            public void handleEvent (Event e) {
                    		pauseDownloadingProcess();
                    		pause.setEnabled(false);;
                    		resume.setEnabled(true);
    		            }
    				});
    				
    				if (harvester.getStatus() != Harvester.STATUS_DISABLED) {
            			// Checking downloading process
            			if (prefm.getPreference("downloading-process").equals(WDUtilities.APP_NO)) {
            				pause.setEnabled(false);
            			} else {
            				resume.setEnabled(false);
            			}
            		}

                    // Change wallpaper
                    if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
        				MenuItem change = new MenuItem (menu, SWT.PUSH);
    					change.setText ("Change wallpaper randomly");
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
    					move.setText ("Move favorite wallpapers");
        				move.addListener (SWT.Selection, new Listener () {          
        		            public void handleEvent (Event e) {
        	            		moveFavoriteWallpapers();
        		            }
        				});
                    }

            		// Manage downloaded wallpapers
    				MenuItem manage = new MenuItem (menu, SWT.PUSH);
					manage.setText ("Manage downloaded wallpapers");
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
    	                	window.frame.setExtendedState(Frame.NORMAL); 
    	                	window.frame.setVisible(true);

            				WallpaperManagerWindow wmw = new WallpaperManagerWindow();
            				wmw.setVisible(true);
    		            }
    				});

            		// Choose wallpaper
    				MenuItem choose = new MenuItem (menu, SWT.PUSH);
					choose.setText ("Choose wallpaper");
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
    	                	window.frame.setExtendedState(Frame.NORMAL);
    	                	window.frame.setVisible(true);

            				ChooseWallpaperWindow cww = new ChooseWallpaperWindow();
            				cww.setVisible(true);
    		            }
    				});

                    // Exit
    				MenuItem exit = new MenuItem (menu, SWT.PUSH);
					exit.setText ("Exit");
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
		searchKeywords.setEnabled(false);

		// Resolution
		String[] resolution = prefm.getPreference("wallpaper-resolution").split("x");
        widthResolution.setValue(new Integer(resolution[0]));
        widthResolution.setEnabled(false);
		heigthResolution.setValue(new Integer(resolution[1]));
        heigthResolution.setEnabled(false);

        // Download policy
        downloadPolicyComboBox.addItem(new ComboItem("Any wallpaper and keep the original resolution", "0"));
        downloadPolicyComboBox.addItem(new ComboItem("Any wallpaper and resize it (if bigger) to the resolution defined", "1"));
        downloadPolicyComboBox.addItem(new ComboItem("Only wallpapers with the resolution set by the user", "2"));
        downloadPolicyComboBox.setSelectedIndex(new Integer(prefm.getPreference("download-policy")));
        
		// Wallhaven.cc
		String wallhavenEnable = prefm.getPreference("provider-wallhaven");
		if (wallhavenEnable.equals(WDUtilities.APP_YES)) {
			wallhavenCheckbox.setSelected(true);
			searchTypeWallhavenComboBox.setEnabled(true);
		} else {
			searchTypeWallhavenComboBox.setEnabled(false);
		}
		searchTypeWallhavenComboBox.addItem(new ComboItem("Relevance", "0")); 
		searchTypeWallhavenComboBox.addItem(new ComboItem("Newest", "1")); 
		searchTypeWallhavenComboBox.addItem(new ComboItem("Views", "2")); 
		searchTypeWallhavenComboBox.addItem(new ComboItem("Favorites", "3")); 
		searchTypeWallhavenComboBox.addItem(new ComboItem("Random", "4"));
		searchTypeWallhavenComboBox.setSelectedIndex(new Integer(prefm.getPreference("wallpaper-search-type")));
		
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

		// DualMonitorBackground
		String dualMonitorEnable = prefm.getPreference("provider-dualMonitorBackgrounds");
		if (dualMonitorEnable.equals(WDUtilities.APP_YES)) {
			dualMonitorCheckbox.setSelected(true);
			searchTypeDualMonitorComboBox.setEnabled(true);
		} else {
			searchTypeDualMonitorComboBox.setEnabled(false);
		}
		searchTypeDualMonitorComboBox.addItem(new ComboItem("Date", "0")); 
		searchTypeDualMonitorComboBox.addItem(new ComboItem("Rating", "1")); 
		searchTypeDualMonitorComboBox.addItem(new ComboItem("Popularity", "2")); 
		searchTypeDualMonitorComboBox.addItem(new ComboItem("Random", "3")); 
		searchTypeDualMonitorComboBox.setSelectedIndex(new Integer(prefm.getPreference("provider-dualMonitorBackgrounds-search-type")));

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
			timeToMinimizeComboBox.setEnabled(true);
		} else {
			startMinimizedCheckBox.setSelected(false);
			timeToMinimizeComboBox.setEnabled(false);
		}
		
		// Time to minimize
		timeToMinimizeComboBox.addItem(new ComboItem("1s", "1"));
		timeToMinimizeComboBox.addItem(new ComboItem("2s", "2"));
		timeToMinimizeComboBox.addItem(new ComboItem("3s", "3"));
		timeToMinimizeComboBox.addItem(new ComboItem("4s", "4"));
		timeToMinimizeComboBox.addItem(new ComboItem("5s", "5"));
		timeToMinimizeComboBox.addItem(new ComboItem("6s", "6"));
		timeToMinimizeComboBox.addItem(new ComboItem("7s", "7"));
		timeToMinimizeComboBox.addItem(new ComboItem("8s", "8"));
		timeToMinimizeComboBox.addItem(new ComboItem("9s", "9"));
		timeToMinimizeComboBox.addItem(new ComboItem("10s", "10"));
		timeToMinimizeComboBox.setSelectedIndex((new Integer(prefm.getPreference("time-to-minimize")) - 1));

		// System tray icon
		String systemTrayIconEnable = prefm.getPreference("system-tray-icon");
		if (systemTrayIconEnable.equals(WDUtilities.APP_YES)) {
			stIconCheckBox.setSelected(true);
		} else {
			stIconCheckBox.setSelected(false);
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
        		(WDUtilities.getWallpaperChanger() instanceof LinuxWallpaperChanger && !((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_GNOME3) && !((LinuxWallpaperChanger)WDUtilities.getWallpaperChanger()).getDesktopEnvironment().equals(WDUtilities.DE_KDE))) {

			oldSystemTray = true;
		}
		return oldSystemTray;
	}
	
	private void pauseResumeRepaint() {
		PreferencesManager prefm = PreferencesManager.getInstance();		
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
	}
}
