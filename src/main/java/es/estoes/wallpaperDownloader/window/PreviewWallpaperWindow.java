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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreviewWallpaperWindow extends JFrame {

	
	/**
	 * Constants.
	 */
	private static final long serialVersionUID = 3193070568124083581L;
	
	/**
	 * Attributes.
	 */
	private String wallpaperToPreview;
	private JButton btnRemoveWallpaper;
	private JButton btnSetNoFavoriteWallpaper;
	private JButton btnSetFavoriteWallpaper;
	private JButton btnSetWallpaper;
	
	/**
	 * Getters & Setters.
	 */
	public String getWallpaperToPreview() {
		return wallpaperToPreview;
	}

	public void setWallpaperToPreview(String wallpaperToRemove) {
		this.wallpaperToPreview = wallpaperToRemove;
	}

	/**
	 * Constructor
	 * @param wallpaperSelectedAbsolutePath 
	 */
	public PreviewWallpaperWindow(String wallpaperSelectedAbsolutePath, WallpaperManagerWindow wallpaperManagerWindow) {
		// Setting wallpaperToRemove
		this.setWallpaperToPreview(wallpaperSelectedAbsolutePath);
		
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Wallpaper Preview");
		setBounds(100, 100, 1024, 768);
		getContentPane().setLayout(null);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
		
		JPanel previewPanel = new JPanel(new BorderLayout());
		previewPanel.setBounds(12, 0, 992, 693);
		getContentPane().add(previewPanel);

		// Resizing image
		ImageIcon imageIcon = new ImageIcon(wallpaperSelectedAbsolutePath);
		
		Image scaledImage = WDUtilities.getScaledImage(imageIcon.getImage(), 992, 693);
		ImageIcon icon = new ImageIcon(scaledImage);
		JLabel previewLabel = new JLabel("", icon, JLabel.CENTER);
		previewPanel.add(previewLabel, BorderLayout.CENTER);		

		btnRemoveWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveWallpaper.setIcon(new ImageIcon(img));
			btnRemoveWallpaper.setToolTipText("Delete selected wallpaper");
			btnRemoveWallpaper.setBounds(12, 698, 34, 33);
		} catch (IOException ex) {
			btnRemoveWallpaper.setText("Delete");
			btnRemoveWallpaper.setBounds(12, 698, 34, 33);
		}
		getContentPane().add(btnRemoveWallpaper);
		
		/**
		  * btnRemoveWallpaper Action Listeners
		  */
	     btnRemoveWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpaper and remove it
				List<String> wallpaperToRemoveList = new ArrayList<String>();
				wallpaperToRemoveList.add(wallpaperToPreview);
				WDUtilities.removeWallpaper(wallpaperToRemoveList, Boolean.TRUE);
				if (wallpaperManagerWindow != null) {
					wallpaperManagerWindow.refreshFavoriteWallpapers();
					wallpaperManagerWindow.refreshNoFavoriteWallpapers();
				}
				dispose();
			}
	     });
			
	     if (WDUtilities.isWallpaperFavorite(this.getWallpaperToPreview())) {

			btnSetNoFavoriteWallpaper = new JButton();

			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/no_favorite_24px_icon.png"));
				btnSetNoFavoriteWallpaper.setIcon(new ImageIcon(img));
				btnSetNoFavoriteWallpaper.setToolTipText("Set selected wallpaper as no favorite");
				btnSetNoFavoriteWallpaper.setBounds(51, 698, 34, 33);
			} catch (IOException ex) {
				btnSetNoFavoriteWallpaper.setText("Set as no favorite");
				btnSetNoFavoriteWallpaper.setBounds(51, 698, 34, 33);
			}		
			getContentPane().add(btnSetNoFavoriteWallpaper);

			btnSetNoFavoriteWallpaper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<String> favoriteWallpaperList = new ArrayList<String>();
					favoriteWallpaperList.add(wallpaperToPreview);
					WDUtilities.setNoFavorite(favoriteWallpaperList);
					if (wallpaperManagerWindow != null) {
						wallpaperManagerWindow.refreshFavoriteWallpapers();
						wallpaperManagerWindow.refreshNoFavoriteWallpapers();
					}
					dispose();
				}
			});			
		} else {
			
			btnSetFavoriteWallpaper = new JButton();
			
			try {
				Image img = ImageIO.read(getClass().getResource("/images/icons/favourite_24px_icon.png"));
				btnSetFavoriteWallpaper.setIcon(new ImageIcon(img));
				btnSetFavoriteWallpaper.setToolTipText("Set selected wallpaper as favorite");
				btnSetFavoriteWallpaper.setBounds(51, 698, 34, 33);
			} catch (IOException ex) {
				btnSetFavoriteWallpaper.setText("Set as favaourite");
				btnSetFavoriteWallpaper.setBounds(51, 698, 34, 33);
			}
			getContentPane().add(btnSetFavoriteWallpaper);

			btnSetFavoriteWallpaper.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<String> noFavoriteWallpaperList = new ArrayList<String>();
					noFavoriteWallpaperList.add(wallpaperToPreview);
					WDUtilities.setFavorite(noFavoriteWallpaperList);
					if (wallpaperManagerWindow != null) {
						wallpaperManagerWindow.refreshFavoriteWallpapers();
						wallpaperManagerWindow.refreshNoFavoriteWallpapers();
					}
					dispose();
				}
			});	
		}
	     
	    btnSetWallpaper = new JButton();
	     
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/desktop_24px_icon.png"));
			btnSetWallpaper.setIcon(new ImageIcon(img));
			btnSetWallpaper.setToolTipText("Set selected wallpaper");
			btnSetWallpaper.setBounds(90, 698, 34, 33);
		} catch (IOException ex) {
			btnSetWallpaper.setText("Set wallpaper");
			btnSetWallpaper.setBounds(90, 698, 34, 33);
		}

		// This button only will be available for those desktops which support setting wallpapers directly
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
		getContentPane().add(btnSetWallpaper);			

		 /**
		  * btnSetWallpaper Action Listeners
		  */
	      btnSetWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpaper and remove it
				WDUtilities.getWallpaperChanger().setWallpaper(wallpaperToPreview);
			}
		  });	  
		}
		
	}	
}
