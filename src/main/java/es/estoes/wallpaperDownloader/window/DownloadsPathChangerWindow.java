package es.estoes.wallpaperDownloader.window;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import es.estoes.wallpaperDownloader.util.WDUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DownloadsPathChangerWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField newDownloadsPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DownloadsPathChangerWindow frame = new DownloadsPathChangerWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DownloadsPathChangerWindow() {
		// DISPOSE_ON_CLOSE for closing only this frame instead of the entire application
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Change Downloads Directory Path");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		// Centering window
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
		
		newDownloadsPath = new JTextField();
		newDownloadsPath.setBounds(12, 36, 298, 19);
		getContentPane().add(newDownloadsPath);
		newDownloadsPath.setColumns(10);
		newDownloadsPath.setText(WDUtilities.getDownloadsPath());
		
		JLabel lblSelectDownloadsPath = new JLabel("Please, select the new downloads directory");
		lblSelectDownloadsPath.setBounds(12, 12, 312, 15);
		getContentPane().add(lblSelectDownloadsPath);
		
		JButton btnSelectPath = new JButton("Select");
		btnSelectPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
			    fileChooser.setDialogTitle("Choose a directory");
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    // Disable the "All files" option.
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    // Show hiding files
			    fileChooser.setFileHidingEnabled(false);

			    if (fileChooser.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) { 
			    	newDownloadsPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
			    }
			}
		});
		btnSelectPath.setBounds(317, 33, 117, 25);
		getContentPane().add(btnSelectPath);
		
		JButton btnApplyPathChange = new JButton("Apply");
		btnApplyPathChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// All the downloaded wallpapers are moved to the new location
				// TODO: stop harvesting process
				WDUtilities.moveDownloadedWallpapers(newDownloadsPath.getText());
				// TODO: start harvesting process
				// TODO: refresh path in main window
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
