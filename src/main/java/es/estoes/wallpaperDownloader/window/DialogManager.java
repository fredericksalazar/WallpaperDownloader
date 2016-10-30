package es.estoes.wallpaperDownloader.window;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class DialogManager {

	// Constants
	private static final String TITLE = "Information";
	
	// Attributes
	private final JOptionPane optionPane;
	private final JDialog dialog;
	private final String message;
	private final int time;
	
	
	// Methods
	/**
	 * Constructor
	 * @param message message to display
	 * @param time time in msec to close the message automatically
	 */
	public DialogManager(String message, int time) {
		super();
		this.message = message;
		this.time = time;
		optionPane = new JOptionPane(this.message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		dialog = new JDialog();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		dialog.setTitle(TITLE);
		dialog.setModal(true);	
		dialog.setContentPane(optionPane);	
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		
		// Centering dialog after pack method
		int x = (screenSize.width - dialog.getWidth()) / 2;
		int y = (screenSize.height - dialog.getHeight()) / 2;
		dialog.setLocation(x, y);
	}

	/**
	 * Opens dialog.
	 */
	public void openDialog() {
		// Create timer to dispose of dialog after n seconds defined in time attribute
		Timer timer = new Timer(this.time, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
		        dialog.dispose();
		    }
		});
		
		// The timer should only go off once
		timer.setRepeats(false);

		// Start timer to close JDialog as dialog modal we must start the timer before its visible
		timer.start();
		dialog.setVisible(true);

	}
	
}
