package es.estoes.wallpaperDownloader.window;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class WallpaperManagerWindow extends JFrame {

	// Constants
	private static final long serialVersionUID = 8790655483965002934L;

	// Attributes
	
	// Methods
	/**
	 * Constructor
	 */
	public WallpaperManagerWindow() {
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Wallpapers Manager");
		setBounds(100, 100, 1200, 768);
		getContentPane().setLayout(null);
		
		JLabel lblFavoriteWallpapers = new JLabel("Favorite Wallpapers");
		lblFavoriteWallpapers.setBounds(12, 12, 151, 15);
		getContentPane().add(lblFavoriteWallpapers);
		
		JScrollPane favoriteScrollPanel = new JScrollPane();
		favoriteScrollPanel.setBounds(12, 33, 1040, 290);
		getContentPane().add(favoriteScrollPanel);
		
		JScrollPane noFavoriteScrollPanel = new JScrollPane();
		noFavoriteScrollPanel.setBounds(12, 372, 1040, 290);
		getContentPane().add(noFavoriteScrollPanel);
		
		JLabel lblNoFavorite = new JLabel("No Favorite Wallpapers");
		lblNoFavorite.setBounds(12, 349, 178, 15);
		getContentPane().add(lblNoFavorite);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(12, 677, 116, 25);
		getContentPane().add(btnClose);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
	}
}
