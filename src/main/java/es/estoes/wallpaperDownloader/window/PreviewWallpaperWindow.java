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
import java.awt.image.BufferedImage;
import java.io.File;
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
	private JButton btnRemoveWallpaper;
	private String wallpaperToRemove;

	
	/**
	 * Getters & Setters.
	 */
	public String getWallpaperToRemove() {
		return wallpaperToRemove;
	}

	public void setWallpaperToRemove(String wallpaperToRemove) {
		this.wallpaperToRemove = wallpaperToRemove;
	}

	/**
	 * Constructor
	 * @param wallpaperSelectedAbsolutePath 
	 */
	public PreviewWallpaperWindow(String wallpaperSelectedAbsolutePath) {
		// Setting wallpaperToRemove
		this.setWallpaperToRemove(wallpaperSelectedAbsolutePath);
		
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

		// Loading image to preview it
		BufferedImage bufferedImage = null;
		
		try {
		    bufferedImage = ImageIO.read(new File(this.getWallpaperToRemove()));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		// Resizing buffered image
		Image resizedImage = bufferedImage.getScaledInstance(992, 693, Image.SCALE_SMOOTH);
		ImageIcon previewImage = new ImageIcon(resizedImage);		
		
		JLabel previewLabel = new JLabel("", previewImage, JLabel.CENTER);
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
				wallpaperToRemoveList.add(wallpaperToRemove);
				WDUtilities.removeWallpaper(wallpaperToRemoveList);
				dispose();
			}
	     });
	}
}
