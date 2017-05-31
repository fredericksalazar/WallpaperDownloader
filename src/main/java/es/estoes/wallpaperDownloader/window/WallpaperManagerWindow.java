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

import java.awt.Component;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import es.estoes.wallpaperDownloader.changer.LinuxWallpaperChanger;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import es.estoes.wallpaperDownloader.util.WallpaperListRenderer;

public class WallpaperManagerWindow extends JFrame {

	// Constants
	private static final long serialVersionUID = 8790655483965002934L;
	
	public static JLabel lblTotalFavoriteWallpapers;
	
	public static JLabel lblTotalNoFavoriteWallpapers;

	// Attributes
	private JButton btnRemoveFavoriteWallpaper;
	private JButton btnSetNoFavoriteWallpaper;
	private JButton btnPreviewNoFavoriteWallpaper;
	private JButton btnSetDskNoFavoriteWallpaper;
	private JButton btnRemoveNoFavoriteWallpaper;
	private JButton btnSetFavoriteWallpaper;
	private JButton btnPreviewFavoriteWallpaper;
	private JButton btnSetDskFavoriteWallpaper;
	private JButton btnClose;
	private JScrollPane noFavoriteScrollPanel;
	private JList<Object> noFavoriteWallpapersList;
	private JScrollPane favoriteScrollPanel;
	private JList<Object> favoriteWallpapersList;
	private JButton btnBackFavoriteWallpapers;
	private JButton btnForwardFavoriteWallpapers;
	private JLabel lblFirstFavoriteWallpaper;
	private JLabel lblLastFavoriteWallpaper;
	private JButton btnBackNoFavoriteWallpapers;
	private JButton btnForwardNoFavoriteWallpapers;
	private JLabel lblFirstNoFavoriteWallpaper;
	private JLabel lblLastNoFavoriteWallpaper;
	
	// Methods
	/**
	 * Constructor
	 */
	public WallpaperManagerWindow() {
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Wallpapers Manager");
		setBounds(100, 100, 774, 621);
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
		
		lblTotalFavoriteWallpapers = new JLabel("");
		lblTotalFavoriteWallpapers.setBounds(163, 12, 70, 15);
		getContentPane().add(lblTotalFavoriteWallpapers);
		
		favoriteScrollPanel = new JScrollPane();
		favoriteScrollPanel.setBounds(12, 33, 688, 212);
		getContentPane().add(favoriteScrollPanel);
		
		favoriteWallpapersList = new JList<Object>();
		favoriteScrollPanel.setViewportView(favoriteWallpapersList);
		
		JLabel lblNoFavorite = new JLabel("No Favorite Wallpapers");
		lblNoFavorite.setBounds(12, 310, 178, 15);
		getContentPane().add(lblNoFavorite);
		
		noFavoriteScrollPanel = new JScrollPane();
		noFavoriteScrollPanel.setBounds(12, 337, 688, 212);
		getContentPane().add(noFavoriteScrollPanel);
		
		noFavoriteWallpapersList = new JList<Object>();
		noFavoriteWallpapersList.setBounds(12, 337, 688, 212);
		getContentPane().add(noFavoriteWallpapersList);
		
		btnRemoveNoFavoriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveNoFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnRemoveNoFavoriteWallpaper.setToolTipText("Delete selected wallpaper");
			btnRemoveNoFavoriteWallpaper.setBounds(712, 369, 34, 33);
		} catch (IOException ex) {
			btnRemoveNoFavoriteWallpaper.setText("Delete");
			btnRemoveNoFavoriteWallpaper.setBounds(1064, 466, 34, 33);
		}
		getContentPane().add(btnRemoveNoFavoriteWallpaper);
		
		btnSetFavoriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/favourite_24px_icon.png"));
			btnSetFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnSetFavoriteWallpaper.setToolTipText("Set selected wallpaper as favorite");
			btnSetFavoriteWallpaper.setBounds(712, 410, 34, 33);
		} catch (IOException ex) {
			btnSetFavoriteWallpaper.setText("Set as favaourite");
			btnSetFavoriteWallpaper.setBounds(1064, 478, 34, 33);
		}
		getContentPane().add(btnSetFavoriteWallpaper);
		
		btnSetDskFavoriteWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/desktop_24px_icon.png"));
			btnSetDskFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnSetDskFavoriteWallpaper.setToolTipText("Set selected wallpaper");
			btnSetDskFavoriteWallpaper.setBounds(713, 182, 34, 33);
		} catch (IOException ex) {
			btnSetDskFavoriteWallpaper.setText("Set wallpaper");
			btnSetDskFavoriteWallpaper.setBounds(713, 182, 34, 33);
		}
		// This button only will be available for those desktops which support setting wallpapers directly
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			getContentPane().add(btnSetDskFavoriteWallpaper);			

		}
		
		btnPreviewFavoriteWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/view_24px_icon.png"));
			btnPreviewFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnPreviewFavoriteWallpaper.setToolTipText("Preview wallpaper");
			btnPreviewFavoriteWallpaper.setBounds(712, 141, 34, 33);
		} catch (IOException ex) {
			btnPreviewFavoriteWallpaper.setText("Preview wallpaper");
			btnPreviewFavoriteWallpaper.setBounds(712, 161, 34, 33);
		}
		getContentPane().add(btnPreviewFavoriteWallpaper);

		btnRemoveFavoriteWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnRemoveFavoriteWallpaper.setToolTipText("Delete selected wallpaper");
			btnRemoveFavoriteWallpaper.setBounds(712, 60, 34, 33);
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
			btnSetNoFavoriteWallpaper.setBounds(712, 101, 34, 33);
		} catch (IOException ex) {
			btnSetNoFavoriteWallpaper.setText("Set as no favorite");
			btnSetNoFavoriteWallpaper.setBounds(1064, 177, 34, 33);
		}		
		getContentPane().add(btnSetNoFavoriteWallpaper);

		
		btnPreviewNoFavoriteWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/view_24px_icon.png"));
			btnPreviewNoFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnPreviewNoFavoriteWallpaper.setToolTipText("Preview wallpaper");
			btnPreviewNoFavoriteWallpaper.setBounds(712, 450, 34, 33);
		} catch (IOException ex) {
			btnPreviewNoFavoriteWallpaper.setText("Preview wallpaper");
			btnPreviewNoFavoriteWallpaper.setBounds(712, 470, 34, 33);
		}
		getContentPane().add(btnPreviewNoFavoriteWallpaper);
		
		btnSetDskNoFavoriteWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/desktop_24px_icon.png"));
			btnSetDskNoFavoriteWallpaper.setIcon(new ImageIcon(img));
			btnSetDskNoFavoriteWallpaper.setToolTipText("Set selected wallpaper");
			btnSetDskNoFavoriteWallpaper.setBounds(712, 490, 34, 33);
		} catch (IOException ex) {
			btnSetDskNoFavoriteWallpaper.setText("Set wallpaper");
			btnSetDskNoFavoriteWallpaper.setBounds(712, 490, 34, 33);
		}
		// This button only will be available for those desktops which support setting wallpapers directly
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			getContentPane().add(btnSetDskNoFavoriteWallpaper);			

		}
		
		btnClose = new JButton("Close");
		btnClose.setBounds(630, 561, 116, 25);
		getContentPane().add(btnClose);
		
		lblFirstFavoriteWallpaper = new JLabel("1");
		lblFirstFavoriteWallpaper.setBounds(331, 255, 34, 15);
		getContentPane().add(lblFirstFavoriteWallpaper);
		
		JLabel lblBetween = new JLabel("--");
		lblBetween.setBounds(369, 255, 17, 15);
		getContentPane().add(lblBetween);
		
		lblLastFavoriteWallpaper = new JLabel("10");
		lblLastFavoriteWallpaper.setBounds(413, 255, 34, 15);
		getContentPane().add(lblLastFavoriteWallpaper);
		
		btnBackFavoriteWallpapers = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/left_arrow_24px_icon.png"));
			btnBackFavoriteWallpapers.setIcon(new ImageIcon(img));
			btnBackFavoriteWallpapers.setToolTipText("Back");
			btnBackFavoriteWallpapers.setBounds(282, 250, 34, 33);
		} catch (IOException ex) {
			btnBackFavoriteWallpapers.setText("Back");
			btnBackFavoriteWallpapers.setBounds(282, 250, 34, 33);
		}		
		getContentPane().add(btnBackFavoriteWallpapers);
		
		btnForwardFavoriteWallpapers = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/right_arrow_24px_icon.png"));
			btnForwardFavoriteWallpapers.setIcon(new ImageIcon(img));
			btnForwardFavoriteWallpapers.setToolTipText("Forward");
			btnForwardFavoriteWallpapers.setBounds(442, 250, 34, 33);
		} catch (IOException ex) {
			btnForwardFavoriteWallpapers.setText("Forward");
			btnForwardFavoriteWallpapers.setBounds(442, 250, 34, 33);
		}		
		getContentPane().add(btnForwardFavoriteWallpapers);
		
		btnBackNoFavoriteWallpapers = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/left_arrow_24px_icon.png"));
			btnBackNoFavoriteWallpapers.setIcon(new ImageIcon(img));
			btnBackNoFavoriteWallpapers.setToolTipText("Back");
			btnBackNoFavoriteWallpapers.setBounds(282, 553, 34, 33);
		} catch (IOException ex) {
			btnBackNoFavoriteWallpapers.setText("Back");
			btnBackNoFavoriteWallpapers.setBounds(282, 553, 34, 33);
		}
		getContentPane().add(btnBackNoFavoriteWallpapers);
		
		btnForwardNoFavoriteWallpapers = new JButton();
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/right_arrow_24px_icon.png"));
			btnForwardNoFavoriteWallpapers.setIcon(new ImageIcon(img));
			btnForwardNoFavoriteWallpapers.setToolTipText("Forward");
			btnForwardNoFavoriteWallpapers.setBounds(442, 553, 34, 33);
		} catch (IOException ex) {
			btnForwardNoFavoriteWallpapers.setText("Forward");
			btnForwardNoFavoriteWallpapers.setBounds(442, 553, 34, 33);
		}
		getContentPane().add(btnForwardNoFavoriteWallpapers);
		
		lblFirstNoFavoriteWallpaper = new JLabel("1");
		lblFirstNoFavoriteWallpaper.setBounds(331, 561, 34, 15);
		getContentPane().add(lblFirstNoFavoriteWallpaper);
		
		JLabel lblBetweenNoFavorite = new JLabel("--");
		lblBetweenNoFavorite.setBounds(369, 561, 17, 15);
		getContentPane().add(lblBetweenNoFavorite);
		
		lblLastNoFavoriteWallpaper = new JLabel("10");
		lblLastNoFavoriteWallpaper.setBounds(413, 561, 34, 15);
		getContentPane().add(lblLastNoFavoriteWallpaper);
		
		lblTotalNoFavoriteWallpapers = new JLabel("");
		lblTotalNoFavoriteWallpapers.setBounds(183, 310, 70, 15);
		getContentPane().add(lblTotalNoFavoriteWallpapers);
		
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
		/**
		 * btnClose
		 */
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		/**
		 * btnForwardFavoriteWallpapers
		 */
		btnForwardFavoriteWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int firstWallpaperToDisplay = Integer.valueOf(lblLastFavoriteWallpaper.getText()) + 1;
				int totalWallpapers = Integer.valueOf(lblTotalFavoriteWallpapers.getText());
				
				// It will be possible to forward the wallpapers list only if there are more wallpapers to display
				if (totalWallpapers >= firstWallpaperToDisplay) {
					int to = firstWallpaperToDisplay + 9;
					if (to > totalWallpapers) {
						to = totalWallpapers;
					}
					lblFirstFavoriteWallpaper.setText(String.valueOf(firstWallpaperToDisplay));
					lblLastFavoriteWallpaper.setText(String.valueOf(to));
					refreshFavoriteWallpapers();
				}
			}
		});
		
		/**
		 * btnBackFavoriteWallpapers
		 */
		btnBackFavoriteWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int firstWallpaperToDisplay = Integer.valueOf(lblFirstFavoriteWallpaper.getText()) - 10;
				int totalWallpapers = Integer.valueOf(lblTotalFavoriteWallpapers.getText());
				
				// It will be possible to backward the wallpapers list only if there are wallpapers to display
				if (firstWallpaperToDisplay > 0) {
					int from = firstWallpaperToDisplay;
					int to = from + 9;
					if (to > totalWallpapers) {
						to = totalWallpapers;
					}
					lblFirstFavoriteWallpaper.setText(String.valueOf(from));
					lblLastFavoriteWallpaper.setText(String.valueOf(to));
					refreshFavoriteWallpapers();
				}
			}
		});
		
		/**
		 * btnForwardNoFavoriteWallpapers
		 */
		btnForwardNoFavoriteWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int firstWallpaperToDisplay = Integer.valueOf(lblLastNoFavoriteWallpaper.getText()) + 1;
				int totalWallpapers = Integer.valueOf(lblTotalNoFavoriteWallpapers.getText());
				
				// It will be possible to forward the wallpapers list only if there are more wallpapers to display
				if (totalWallpapers >= firstWallpaperToDisplay) {
					int to = firstWallpaperToDisplay + 9;
					if (to > totalWallpapers) {
						to = totalWallpapers;
					}
					lblFirstNoFavoriteWallpaper.setText(String.valueOf(firstWallpaperToDisplay));
					lblLastNoFavoriteWallpaper.setText(String.valueOf(to));
					refreshNoFavoriteWallpapers();
				}
			}
		});
		
		/**
		 * btnBackNoFavoriteWallpapers
		 */
		btnBackNoFavoriteWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int firstWallpaperToDisplay = Integer.valueOf(lblFirstNoFavoriteWallpaper.getText()) - 10;
				int totalWallpapers = Integer.valueOf(lblTotalNoFavoriteWallpapers.getText());
				
				// It will be possible to backward the wallpapers list only if there are wallpapers to display
				if (firstWallpaperToDisplay > 0) {
					int from = firstWallpaperToDisplay;
					int to = from + 9;
					if (to > totalWallpapers) {
						to = totalWallpapers;
					}
					lblFirstNoFavoriteWallpaper.setText(String.valueOf(from));
					lblLastNoFavoriteWallpaper.setText(String.valueOf(to));
					refreshNoFavoriteWallpapers();
				}
			}
		});
		
		/**
		 * btnRemoveFavoriteWallpaper
		 */
		btnRemoveFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = favoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.removeWallpaper(wallpapersSelectedAbsolutePath, Boolean.TRUE);
				refreshFavoriteWallpapers();
			}
		});
		
		/**
		 * btnSetNoFavoriteWallpaper
		 */
		btnSetNoFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the selected wallpaper
				List<Object> wallpapersSelected = favoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.setNoFavorite(wallpapersSelectedAbsolutePath);
				refreshFavoriteWallpapers();
				refreshNoFavoriteWallpapers();
			}
		});
		
		/**
		 * btnPreviewFavoriteWallpaper
		 */
		btnPreviewFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = favoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				
				// Opens the preview window
		        Component component = (Component) actionEvent.getSource();
		        JFrame wallpaperManagerWindow = (JFrame) SwingUtilities.getRoot(component);
				PreviewWallpaperWindow previewWindow = new PreviewWallpaperWindow(wallpapersSelectedAbsolutePath.get(0), (WallpaperManagerWindow) wallpaperManagerWindow);
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
		 * btnRemoveNoFavoriteWallpaper
		 */
		btnRemoveNoFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = noFavoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.removeWallpaper(wallpapersSelectedAbsolutePath, Boolean.TRUE);
				refreshNoFavoriteWallpapers();
			}
		});
		
		/**
		 * btnSetFavoriteWallpaper
		 */
		btnSetFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the selected wallpaper
				List<Object> wallpapersSelected = noFavoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.setFavorite(wallpapersSelectedAbsolutePath);
				refreshFavoriteWallpapers();
				refreshNoFavoriteWallpapers();
			}
		});
		
		/**
		 * btnPreviewNoFavoriteWallpaper
		 */
		btnPreviewNoFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = noFavoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				
				// Opens the preview window
		        Component component = (Component) actionEvent.getSource();
		        JFrame wallpaperManagerWindow = (JFrame) SwingUtilities.getRoot(component);
				PreviewWallpaperWindow previewWindow = new PreviewWallpaperWindow(wallpapersSelectedAbsolutePath.get(0), (WallpaperManagerWindow) wallpaperManagerWindow);
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
		  * btnSeDskFavoritetWallpaper Action Listeners
		  */
	      btnSetDskFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = favoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.getWallpaperChanger().setWallpaper(wallpapersSelectedAbsolutePath.get(0));
			}
		  });	  

		 /**
		  * btnSeDskNoFavoritetWallpaper Action Listeners
		  */
	      btnSetDskNoFavoriteWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = noFavoriteWallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.getWallpaperChanger().setWallpaper(wallpapersSelectedAbsolutePath.get(0));
			}
		  });	  

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void refreshFavoriteWallpapers() {
		ImageIcon[] wallpapers = WDUtilities.getImageIconWallpapers(10, Integer.valueOf(lblFirstFavoriteWallpaper.getText()) - 1, WDUtilities.SORTING_NO_SORTING, WDUtilities.WD_FAVORITE_PREFIX);
		favoriteWallpapersList = new JList(wallpapers);
		changePointerJList();
		favoriteScrollPanel.setViewportView(favoriteWallpapersList);
		// JList single selection
		favoriteWallpapersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// JList horizontal orientation
		favoriteWallpapersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// Only 1 row to display
		favoriteWallpapersList.setVisibleRowCount(2);
		// Using a custom render to render every element within JList
		favoriteWallpapersList.setCellRenderer(new WallpaperListRenderer(WallpaperListRenderer.WITH_TEXT));		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void refreshNoFavoriteWallpapers() {
		ImageIcon[] wallpapers = WDUtilities.getImageIconWallpapers(10, Integer.valueOf(lblFirstNoFavoriteWallpaper.getText()) - 1, WDUtilities.SORTING_NO_SORTING, WDUtilities.WD_PREFIX);
		noFavoriteWallpapersList = new JList(wallpapers);
		changePointerJList();
		noFavoriteScrollPanel.setViewportView(noFavoriteWallpapersList);
		// JList single selection
		noFavoriteWallpapersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// JList horizontal orientation
		noFavoriteWallpapersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// Only 1 row to display
		noFavoriteWallpapersList.setVisibleRowCount(2);
		// Using a custom render to render every element within JList
		noFavoriteWallpapersList.setCellRenderer(new WallpaperListRenderer(WallpaperListRenderer.WITH_TEXT));		
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
		favoriteWallpapersList.addMouseMotionListener(new MouseMotionListener() {
    	    public void mouseMoved(MouseEvent e) {
    	        final int x = e.getX();
    	        final int y = e.getY();
    	        // only display a hand if the cursor is over the items
    	        final Rectangle cellBounds = favoriteWallpapersList.getCellBounds(0, noFavoriteWallpapersList.getModel().getSize() - 1);
    	        if (cellBounds != null && cellBounds.contains(x, y)) {
    	        	favoriteWallpapersList.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	        } else {
    	        	noFavoriteWallpapersList.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    	        }
    	    }

    	    public void mouseDragged(MouseEvent e) {
    	    }
	  });
	}
	
	/**
	 * This methods refreshes the total number of favorite wallpapers
	 * @param total
	 */
	public static void refreshFavoriteWallpapersTotalNumber (int total) {
		if (lblTotalFavoriteWallpapers != null) {
			lblTotalFavoriteWallpapers.setText(String.valueOf(total));
		}
	}
	
	/**
	 * This methods refreshes the total number of no favorite wallpapers
	 * @param total
	 */
	public static void refreshNoFavoriteWallpapersTotalNumber (int total) {
		if (lblTotalNoFavoriteWallpapers != null) {
			lblTotalNoFavoriteWallpapers.setText(String.valueOf(total));
		}
	}
}
