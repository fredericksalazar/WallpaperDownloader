package es.estoes.wallpaperDownloader.window;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;

public class WallpaperManagerWindow extends JFrame {

	// Constants
	private static final long serialVersionUID = 8790655483965002934L;

	// Attributes
	private JButton btnRemoveFavoriteWallpaper;
	private JButton btnSetNoFavoriteWallpaper;
	private JButton btnRemoveNoFavoriteWallpaper;
	private JButton btnSetFavoriteWallpaper;
	private JButton btnClose;
	
	// Methods
	/**
	 * Constructor
	 */
	public WallpaperManagerWindow() {
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Wallpapers Manager");
		setBounds(100, 100, 1200, 640);
		getContentPane().setLayout(null);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
		
		JLabel lblFavoriteWallpapers = new JLabel("Favorite Wallpapers");
		lblFavoriteWallpapers.setBounds(12, 12, 151, 15);
		getContentPane().add(lblFavoriteWallpapers);
		
		JScrollPane favoriteScrollPanel = new JScrollPane();
		favoriteScrollPanel.setBounds(12, 33, 1040, 265);
		getContentPane().add(favoriteScrollPanel);
		
		JList favoriteWallpapersList = new JList();
		favoriteScrollPanel.setViewportView(favoriteWallpapersList);
		
		JLabel lblNoFavorite = new JLabel("No Favorite Wallpapers");
		lblNoFavorite.setBounds(12, 310, 178, 15);
		getContentPane().add(lblNoFavorite);
		
		JScrollPane noFavoriteScrollPanel = new JScrollPane();
		noFavoriteScrollPanel.setBounds(12, 337, 1040, 265);
		getContentPane().add(noFavoriteScrollPanel);
		
		JList noFavoriteWallpapersList = new JList();
		noFavoriteWallpapersList.setBounds(12, 337, 1037, 287);
		getContentPane().add(noFavoriteWallpapersList);
		
		btnRemoveNoFavoriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveNoFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnRemoveNoFavoriteWallpaper.setToolTipText("Delete selected wallpaper");
			btnRemoveNoFavoriteWallpaper.setBounds(1064, 421, 34, 33);
		} catch (IOException ex) {
			btnRemoveNoFavoriteWallpaper.setText("Delete");
			btnRemoveNoFavoriteWallpaper.setBounds(1064, 466, 34, 33);
		}
		getContentPane().add(btnRemoveNoFavoriteWallpaper);
		
		
		btnRemoveNoFavoriteWallpaper = new JButton();
		
		btnSetFavoriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/favourite_24px_icon.png"));
			btnSetFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnSetFavoriteWallpaper.setToolTipText("Set selected wallpaper as favorite");
			btnSetFavoriteWallpaper.setBounds(1064, 478, 34, 33);
		} catch (IOException ex) {
			btnSetFavoriteWallpaper.setText("Set as favaourite");
			btnSetFavoriteWallpaper.setBounds(1064, 478, 34, 33);
		}
		getContentPane().add(btnSetFavoriteWallpaper);
		
		btnRemoveFavoriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnRemoveFavoriteWallpaper.setToolTipText("Delete selected wallpaper");
			btnRemoveFavoriteWallpaper.setBounds(1064, 120, 34, 33);
		} catch (IOException ex) {
			btnRemoveFavoriteWallpaper.setText("Delete");
			btnRemoveFavoriteWallpaper.setBounds(1064, 120, 34, 33);
		}
		getContentPane().add(btnRemoveFavoriteWallpaper);
		
		btnSetNoFavoriteWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/no_favorite_24px_icon.png"));
			btnSetNoFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnSetNoFavoriteWallpaper.setToolTipText("Set selected wallpaper as no favorite");
			btnSetNoFavoriteWallpaper.setBounds(1064, 177, 34, 33);
		} catch (IOException ex) {
			btnSetNoFavoriteWallpaper.setText("Set as no favorite");
			btnSetNoFavoriteWallpaper.setBounds(1064, 177, 34, 33);
		}		
		getContentPane().add(btnSetNoFavoriteWallpaper);
		

		
		btnClose = new JButton("Close");
		btnClose.setBounds(1064, 577, 116, 25);
		getContentPane().add(btnClose);
		
		// Setting up configuration
		initializeGUI();
		
		// Setting up listeners
		initializeListeners();
	}

	private void initializeGUI() {
		// TODO Auto-generated method stub
		
	}
	
	private void initializeListeners() {
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});		
	}


}
