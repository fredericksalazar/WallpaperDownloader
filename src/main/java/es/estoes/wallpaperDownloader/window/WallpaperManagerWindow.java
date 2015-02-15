package es.estoes.wallpaperDownloader.window;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.util.WallpaperListRenderer;

public class WallpaperManagerWindow extends JFrame {

	// Constants
	private static final long serialVersionUID = 8790655483965002934L;

	// Attributes
	private JButton btnRemoveFavoriteWallpaper;
	private JButton btnSetNoFavoriteWallpaper;
	private JButton btnRemoveNoFavoriteWallpaper;
	private JButton btnSetFavoriteWallpaper;
	private JButton btnClose;
	private JScrollPane noFavoriteScrollPanel;
	private JList noFavoriteWallpapersList;
	
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
		
		noFavoriteScrollPanel = new JScrollPane();
		noFavoriteScrollPanel.setBounds(12, 337, 1040, 265);
		getContentPane().add(noFavoriteScrollPanel);
		
		noFavoriteWallpapersList = new JList();
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
		// ---------------------------------------------------------------------
		// Populating favorite wallpapers
		// ---------------------------------------------------------------------
		refreshFavoriteWallpapers();	
		
		// ---------------------------------------------------------------------
		// Populating no favorite wallpapers
		// ---------------------------------------------------------------------
		refreshNoFavoriteWallpapers();
	}
	
	private void initializeListeners() {
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});		
	}

	private void refreshFavoriteWallpapers() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refreshNoFavoriteWallpapers() {
		ImageIcon[] wallpapers = WDUtilities.getLastImageIconWallpapers(20);
		noFavoriteWallpapersList = new JList(wallpapers);
		changePointerJList();
		noFavoriteScrollPanel.setViewportView(noFavoriteWallpapersList);
		// JList single selection
		noFavoriteWallpapersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// JList horizontal orientation
		noFavoriteWallpapersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// Only 1 row to display
		noFavoriteWallpapersList.setVisibleRowCount(4);
		// Using a custom render to render every element within JList
		noFavoriteWallpapersList.setCellRenderer(new WallpaperListRenderer());		
	}
	
	/**
	 * This method changes the pointer when the user moves the mouse over the JList
	 */
	private void changePointerJList() {
		noFavoriteWallpapersList.addMouseMotionListener(new MouseMotionListener() {
    	    public void mouseMoved(MouseEvent e) {
    	        final int x = e.getX();
    	        final int y = e.getY();
    	        // only display a hand if the cursor is over the items
    	        final Rectangle cellBounds = noFavoriteWallpapersList.getCellBounds(0, noFavoriteWallpapersList.getModel().getSize() - 1);
    	        if (cellBounds != null && cellBounds.contains(x, y)) {
    	        	noFavoriteWallpapersList.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	        } else {
    	        	noFavoriteWallpapersList.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    	        }
    	    }

    	    public void mouseDragged(MouseEvent e) {
    	    }
	  });		
	}
}
