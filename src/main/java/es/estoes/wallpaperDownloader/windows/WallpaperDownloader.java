package es.estoes.wallpaperDownloader.windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import es.estoes.wallpaperDownloader.utils.PropertiesManager;
import es.estoes.wallpaperDownloader.utils.WDConfigManager;

public class WallpaperDownloader {

	private JFrame frame;
	private JTextField txtHola;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
			        // First, it is necessary run a configuration process to set up the application
			    	WDConfigManager.checkConfig();
					PropertiesManager pm = PropertiesManager.getInstance();
					WallpaperDownloader window = new WallpaperDownloader();
					window.frame.setVisible(true);
					window.frame.setTitle(pm.getProperty("app.name") + " V" + pm.getProperty("app.version"));
				} catch (Exception e) {
					e.printStackTrace();
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
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		txtHola = new JTextField();
		txtHola.setText("Hola");
		txtHola.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(txtHola, GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(txtHola, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(221, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}

}
