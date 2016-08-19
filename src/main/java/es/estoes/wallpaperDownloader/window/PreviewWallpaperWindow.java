package es.estoes.wallpaperDownloader.window;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class PreviewWallpaperWindow {

	private JFrame frmWallpaperPreview;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreviewWallpaperWindow window = new PreviewWallpaperWindow();
					window.frmWallpaperPreview.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PreviewWallpaperWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWallpaperPreview = new JFrame();
		frmWallpaperPreview.setTitle("Wallpaper Preview");
		frmWallpaperPreview.setBounds(100, 100, 1024, 768);
		frmWallpaperPreview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
