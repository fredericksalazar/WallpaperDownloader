package es.estoes.wallpaperDownloader.windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import es.estoes.wallpaperDownloader.utils.PreferencesManager;
import es.estoes.wallpaperDownloader.utils.PropertiesManager;
import es.estoes.wallpaperDownloader.utils.WDConfigManager;
import es.estoes.wallpaperDownloader.utils.WDUtilities;

import javax.swing.JTabbedPane;
import javax.swing.JButton;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

public class WallpaperDownloader {

	protected static final Logger LOG = Logger.getLogger(WallpaperDownloader.class);
	private JFrame frame;
	private JTextField wallbaseKeywords;

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
				WallpaperDownloader window = new WallpaperDownloader();
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
		final PreferencesManager prefm = PreferencesManager.getInstance();
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
		
		final JCheckBox wallbaseCheckbox = new JCheckBox("Wallbase.cc");
		wallbaseCheckbox.setBounds(8, 8, 129, 23);
		providersPanel.add(wallbaseCheckbox);
		// Checking provider
		// Wallbase.cc
		String wallbaseEnable = prefm.getPreference("provider.wallbase");
		if (wallbaseEnable.equals(WDUtilities.APP_YES)) {
			wallbaseCheckbox.setSelected(true);
		}

		JLabel lblKeywords = new JLabel("Keywords");
		lblKeywords.setBounds(12, 39, 70, 15);
		providersPanel.add(lblKeywords);
		
		wallbaseKeywords = new JTextField();
		wallbaseKeywords.setBounds(100, 37, 522, 19);
		providersPanel.add(wallbaseKeywords);
		wallbaseKeywords.setColumns(10);
		
		JPanel appSettingsPanel = new JPanel();
		tabbedPane.addTab("Application Settings", null, appSettingsPanel, null);
		
		JButton btnCloseExit = new JButton("Close & Exit");
		GridBagConstraints gbc_btnCloseExit = new GridBagConstraints();
		gbc_btnCloseExit.anchor = GridBagConstraints.WEST;
		gbc_btnCloseExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnCloseExit.gridx = 0;
		gbc_btnCloseExit.gridy = 2;
		frame.getContentPane().add(btnCloseExit, gbc_btnCloseExit);
		
		/**
		 * btnCloseExit Action Listeteners
		 */
		// Clicking event
		btnCloseExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// The application is closed
				System.exit(0);
			}
		});
		
		
		JButton btnApply = new JButton("Apply");
		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnApply.insets = new Insets(0, 0, 5, 5);
		gbc_btnApply.gridx = 2;
		gbc_btnApply.gridy = 2;
		frame.getContentPane().add(btnApply, gbc_btnApply);

		/**
		 * btnApply Action Listeteners
		 */
		// Clicking event
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Storing all the application settings
				// Providers //////////////////////////////////////
				// Wallbase.cc
				LOG.info("provider.wallbase is set to " + prefm.getPreference("provider.wallbase"));
				///////////////////////////////////////
				if (wallbaseCheckbox.isSelected()) {
					prefm.setPreference("provider.wallbase", WDUtilities.APP_YES);
				} else {
					prefm.setPreference("provider.wallbase", WDUtilities.APP_NO);					
				}
				///////////////////////////////////////
				LOG.info("Now, provider.wallbase is set to " + prefm.getPreference("provider.wallbase"));
			}
		});

		
		JButton btnMinimize = new JButton("Minimize");
		btnMinimize.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnMinimize = new GridBagConstraints();
		gbc_btnMinimize.insets = new Insets(0, 0, 5, 0);
		gbc_btnMinimize.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnMinimize.gridx = 3;
		gbc_btnMinimize.gridy = 2;
		frame.getContentPane().add(btnMinimize, gbc_btnMinimize);
	}
}
