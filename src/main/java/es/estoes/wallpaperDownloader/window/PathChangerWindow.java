package es.estoes.wallpaperDownloader.window;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import es.estoes.wallpaperDownloader.util.WDUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class PathChangerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField newPath;

	/**
	 * Create the frame.
	 * @param mainWindow 
	 */
	public PathChangerWindow(final WallpaperDownloader mainWindow, String whatToChange) {
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		switch (whatToChange) {
		case WDUtilities.DOWNLOADS_DIRECTORY:
			setTitle("Change Downloads Directory Path");
			break;
		case WDUtilities.CHANGER_DIRECTORY:
			setTitle("Change Changer Directory Path");
		default:
			break;
		}
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
		
		newPath = new JTextField();
		newPath.setBounds(12, 36, 298, 19);
		getContentPane().add(newPath);
		newPath.setColumns(10);
		switch (whatToChange) {
		case WDUtilities.DOWNLOADS_DIRECTORY:
			newPath.setText(WDUtilities.getDownloadsPath());
			break;
		case WDUtilities.CHANGER_DIRECTORY:
			newPath.setText(WDUtilities.getChangerPath());
		default:
			break;
		}
		
		JLabel lblSelectDownloadsPath = null;
		switch (whatToChange) {
		case WDUtilities.DOWNLOADS_DIRECTORY:
			lblSelectDownloadsPath = new JLabel("Please, select the new downloads directory");
			break;
		case WDUtilities.CHANGER_DIRECTORY:
			lblSelectDownloadsPath = new JLabel("Please, select the new changer directory");
		default:
			break;
		}
		lblSelectDownloadsPath.setBounds(12, 12, 312, 15);
		getContentPane().add(lblSelectDownloadsPath);
				
		JButton btnSelectPath = new JButton("Select");
		btnSelectPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Set Locale to English
				JComponent.setDefaultLocale(Locale.ENGLISH);
				final JFileChooser fileChooser = new JFileChooser();
			    fileChooser.setDialogTitle("Choose a directory");
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    // Disable the "All files" option.
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    // Show hiding files
			    fileChooser.setFileHidingEnabled(false);

			    if (fileChooser.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) { 
			    	newPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
			    }
			}
		});
		btnSelectPath.setBounds(317, 33, 117, 25);
		getContentPane().add(btnSelectPath);
		
		JButton btnApplyPathChange = new JButton("Apply");
		btnApplyPathChange.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// All the downloaded wallpapers are moved to the new location
				// Stop harvesting process
				mainWindow.getHarvester().stop();
				WDUtilities.moveDownloadedWallpapers(newPath.getText());
				// Start harvesting process
				mainWindow.getHarvester().start();
				// Refresh new path in main window
				switch (whatToChange) {
				case WDUtilities.DOWNLOADS_DIRECTORY:
					mainWindow.getDownloadsDirectory().setText(newPath.getText());
					break;
				case WDUtilities.CHANGER_DIRECTORY:
					mainWindow.getChangerDirectory().setText(newPath.getText());
				default:
					break;
				}				
				// Close frame
				dispose();
			}
		});
		btnApplyPathChange.setBounds(361, 236, 73, 25);
		getContentPane().add(btnApplyPathChange);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(12, 236, 91, 25);
		getContentPane().add(btnCancel);
	}
}
