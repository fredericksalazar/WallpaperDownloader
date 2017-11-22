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

import java.awt.Color;
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

public class ChooseWallpaperWindow extends JFrame {

	// Constants
	private static final long serialVersionUID = 8790655483965002934L;
	
	public static JLabel lblTotaleWallpapers;

	// Attributes
	private JButton btnRemoveWallpaper;
	private JButton btnPreviewWallpaper;
	private JButton btnSetDskWallpaper;
	private JScrollPane wallpapersScrollPanel;
	private JList<Object> wallpapersList;
	private JButton btnBackWallpapers;
	private JButton btnForwardWallpapers;
	private JLabel lblFirstWallpaper;
	private JLabel lblLastWallpaper;
	
	// Methods
	/**
	 * Constructor
	 */
	public ChooseWallpaperWindow() {
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Choose wallpaper");
		setBackground(new Color(255, 255, 255));
		setBounds(100, 100, 774, 546);
		getContentPane().setLayout(null);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
		
		JLabel lblWallpapers = new JLabel("Wallpapers");
		lblWallpapers.setBounds(12, 12, 151, 15);
		getContentPane().add(lblWallpapers);
		
		lblTotaleWallpapers = new JLabel("");
		lblTotaleWallpapers.setBounds(163, 12, 70, 15);
		getContentPane().add(lblTotaleWallpapers);
		
		wallpapersScrollPanel = new JScrollPane();
		wallpapersScrollPanel.setBounds(12, 33, 688, 420);
		getContentPane().add(wallpapersScrollPanel);
		
		wallpapersList = new JList<Object>();
		wallpapersScrollPanel.setViewportView(wallpapersList);

		btnRemoveWallpaper = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/delete_24px_icon.png"));
			btnRemoveWallpaper.setIcon(new ImageIcon(img));
			btnRemoveWallpaper.setToolTipText("Delete selected wallpaper");
			btnRemoveWallpaper.setBounds(713, 182, 34, 33);
		} catch (IOException ex) {
			btnRemoveWallpaper.setText("Delete");
			btnRemoveWallpaper.setBounds(713, 182, 34, 33);

		}
		getContentPane().add(btnRemoveWallpaper);

		btnPreviewWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/view_24px_icon.png"));
			btnPreviewWallpaper.setIcon(new ImageIcon(img));
			btnPreviewWallpaper.setToolTipText("Preview wallpaper");
			btnPreviewWallpaper.setBounds(712, 222, 34, 33);
		} catch (IOException ex) {
			btnPreviewWallpaper.setText("Preview wallpaper");
			btnPreviewWallpaper.setBounds(1064, 120, 34, 33);
		}
		getContentPane().add(btnPreviewWallpaper);

		btnSetDskWallpaper = new JButton();

		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/desktop_24px_icon.png"));
			btnSetDskWallpaper.setIcon(new ImageIcon(img));
			btnSetDskWallpaper.setToolTipText("Set selected wallpaper");
			btnSetDskWallpaper.setBounds(712, 262, 34, 33);
		} catch (IOException ex) {
			btnSetDskWallpaper.setText("Set wallpaper");
			btnSetDskWallpaper.setBounds(712, 161, 34, 33);
		}
		// This button only will be available for those desktops which support setting wallpapers directly
		if (WDUtilities.getWallpaperChanger().isWallpaperChangeable()) {
			getContentPane().add(btnSetDskWallpaper);			
		}
		
		lblFirstWallpaper = new JLabel("1");
		lblFirstWallpaper.setBounds(331, 470, 34, 15);
		getContentPane().add(lblFirstWallpaper);
		
		JLabel lblBetween = new JLabel("--");
		lblBetween.setBounds(369, 470, 17, 15);
		getContentPane().add(lblBetween);
		
		lblLastWallpaper = new JLabel("20");
		lblLastWallpaper.setBounds(413, 470, 34, 15);
		getContentPane().add(lblLastWallpaper);
		
		btnBackWallpapers = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/left_arrow_24px_icon.png"));
			btnBackWallpapers.setIcon(new ImageIcon(img));
			btnBackWallpapers.setToolTipText("Back");
			btnBackWallpapers.setBounds(282, 465, 34, 33);
		} catch (IOException ex) {
			btnBackWallpapers.setText("Back");
			btnBackWallpapers.setBounds(282, 250, 34, 33);
		}		
		getContentPane().add(btnBackWallpapers);
		
		btnForwardWallpapers = new JButton();
		
		try {
			Image img = ImageIO.read(getClass().getResource("/images/icons/right_arrow_24px_icon.png"));
			btnForwardWallpapers.setIcon(new ImageIcon(img));
			btnForwardWallpapers.setToolTipText("Forward");
			btnForwardWallpapers.setBounds(442, 465, 34, 33);
		} catch (IOException ex) {
			btnForwardWallpapers.setText("Forward");
			btnForwardWallpapers.setBounds(442, 250, 34, 33);
		}		
		getContentPane().add(btnForwardWallpapers);
		
		// Setting up configuration
		initializeGUI();
		
		// Setting up listeners
		initializeListeners();
	}

	private void initializeGUI() {
		// ---------------------------------------------------------------------
		// Populating favorite wallpapers
		// ---------------------------------------------------------------------
		refreshWallpapers();	
	}
	
	private void initializeListeners() {
		
		/**
		 * btnForwardFavoriteWallpapers
		 */
		btnForwardWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int firstWallpaperToDisplay = Integer.valueOf(lblLastWallpaper.getText()) + 1;
				int totalWallpapers = Integer.valueOf(lblTotaleWallpapers.getText());
				
				// It will be possible to forward the wallpapers list only if there are more wallpapers to display
				if (totalWallpapers >= firstWallpaperToDisplay) {
					int to = firstWallpaperToDisplay + 19;
					if (to > totalWallpapers) {
						to = totalWallpapers;
					}
					lblFirstWallpaper.setText(String.valueOf(firstWallpaperToDisplay));
					lblLastWallpaper.setText(String.valueOf(to));
					refreshWallpapers();
				}
			}
		});
		
		/**
		 * btnBackFavoriteWallpapers
		 */
		btnBackWallpapers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int firstWallpaperToDisplay = Integer.valueOf(lblFirstWallpaper.getText()) - 20;
				int totalWallpapers = Integer.valueOf(lblTotaleWallpapers.getText());
				
				// It will be possible to backward the wallpapers list only if there are wallpapers to display
				if (firstWallpaperToDisplay > 0) {
					int from = firstWallpaperToDisplay;
					int to = from + 19;
					if (to > totalWallpapers) {
						to = totalWallpapers;
					}
					lblFirstWallpaper.setText(String.valueOf(from));
					lblLastWallpaper.setText(String.valueOf(to));
					refreshWallpapers();
				}
			}
		});
		
		/**
		 * btnRemoveFavoriteWallpaper
		 */
		btnRemoveWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = wallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				WDUtilities.removeWallpaper(wallpapersSelectedAbsolutePath, Boolean.TRUE);
				refreshWallpapers();
			}
		});
		
		/**
		 * btnPreviewFavoriteWallpaper
		 */
		btnPreviewWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = wallpapersList.getSelectedValuesList();
				List<String> wallpapersSelectedAbsolutePath = new ArrayList<String>();
				Iterator<Object> wallpapersSelectedIterator = wallpapersSelected.iterator();
				while (wallpapersSelectedIterator.hasNext()) {
					ImageIcon wallpaperSelectedIcon = (ImageIcon) wallpapersSelectedIterator.next();
					wallpapersSelectedAbsolutePath.add(wallpaperSelectedIcon.getDescription());
				}
				
				// Opens the preview window
		        Component component = (Component) actionEvent.getSource();
		        JFrame wallpaperManagerWindow = (JFrame) SwingUtilities.getRoot(component);
				PreviewWallpaperWindow previewWindow = new PreviewWallpaperWindow(wallpapersSelectedAbsolutePath.get(0), (ChooseWallpaperWindow) wallpaperManagerWindow);
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
	      btnSetDskWallpaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected wallpapers
				List<Object> wallpapersSelected = wallpapersList.getSelectedValuesList();
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
	public void refreshWallpapers() {
		ImageIcon[] wallpapers = WDUtilities.getImageIconWallpapers(20, Integer.valueOf(lblFirstWallpaper.getText()) - 1, WDUtilities.SORTING_MULTIPLE_DIR, WDUtilities.WD_ALL);
		wallpapersList = new JList(wallpapers);
		changePointerJList();
		wallpapersScrollPanel.setViewportView(wallpapersList);
		// JList single selection
		wallpapersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// JList horizontal orientation
		wallpapersList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// Only 4 row to display
		wallpapersList.setVisibleRowCount(4);
		// Using a custom render to render every element within JList
		wallpapersList.setCellRenderer(new WallpaperListRenderer(WallpaperListRenderer.WITH_TEXT));		
	}
	
	/**
	 * This method changes the pointer when the user moves the mouse over the JList
	 */
	private void changePointerJList() {
		wallpapersList.addMouseMotionListener(new MouseMotionListener() {
    	    public void mouseMoved(MouseEvent e) {
    	        final int x = e.getX();
    	        final int y = e.getY();
    	        // only display a hand if the cursor is over the items
    	        final Rectangle cellBounds = wallpapersList.getCellBounds(0, wallpapersList.getModel().getSize() - 1);
    	        if (cellBounds != null && cellBounds.contains(x, y)) {
    	        	wallpapersList.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
	public static void refreshWallpapersTotalNumber (int total) {
		if (lblTotaleWallpapers != null) {
			lblTotaleWallpapers.setText(String.valueOf(total));
		}
	}
	
}
