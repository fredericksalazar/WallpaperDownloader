package es.estoes.wallpaperDownloader.window;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
	 */
	public PreviewWallpaperWindow() {
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Wallpaper Preview");
		setBounds(100, 100, 1024, 768);
		getContentPane().setLayout(null);
		
		JPanel previewPanel = new JPanel();
		previewPanel.setBounds(12, 0, 992, 693);
		getContentPane().add(previewPanel);
		
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
