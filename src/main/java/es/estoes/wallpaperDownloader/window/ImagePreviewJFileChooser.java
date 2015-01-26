package es.estoes.wallpaperDownloader.window;

import javax.swing.*;
import java.awt.*;
import java.beans.*;
//import javax.swing.filechooser.*;
import java.awt.image.*;
import javax.imageio.*;
import es.estoes.wallpaperDownloader.util.WDUtilities;
import java.io.*;
import java.util.concurrent.*;

public class ImagePreviewJFileChooser extends JFileChooser {
	
	private static final long serialVersionUID = -793293475662593357L;
	JLabel img;
	JButton open;
	
    public ImagePreviewJFileChooser()
    {
        createAndShowGUI();
    }
    
    private void createAndShowGUI()
    {
    	// Set Downloads Directory
    	this.setCurrentDirectory(new File(WDUtilities.getDownloadsPath()));
    	
    	// Set title
    	this.setDialogTitle("Choose a wallpaper to set as favourite");
    	
    	// Set approve button text
    	this.setApproveButtonText("Set favourite");
    	
        // Create label
        img=new JLabel();
        
        // Let label come fatty!!
        img.setPreferredSize(new Dimension(175,175));
        
        // Set label as accessory
        this.setAccessory(img);
        
        //this.setAccessory(btnPrueba);
        
        // Accept only image files
        this.setAcceptAllFileFilterUsed(false);
        
        // Create filter for image files
        //FileNameExtensionFilter filter=new FileNameExtensionFilter("Image Files","jpg","jpeg","png","gif");
        
        // Set it as current filter
        //this.setFileFilter(filter);

        // Add property change listener
        this.addPropertyChangeListener(new PropertyChangeListener(){
        
            // When any JFileChooser property changes, this handler
            // is executed
            public void propertyChange(final PropertyChangeEvent pe)
            {
                // Create SwingWorker for smooth experience
                SwingWorker<Image,Void> worker = new SwingWorker<Image,Void>(){
                
                    // The image processing method
                    protected Image doInBackground()
                    {
                        // If selected file changes..
                        if(pe.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
                        {
                        // Get selected file
                        	File f = new File(pe.getNewValue().toString());
                        
                            try
                            {
                            // Create FileInputStream for file
                            FileInputStream fin=new FileInputStream(f);
                            
                            // Read image from fin
                            BufferedImage bim=ImageIO.read(fin);
                            
                            // Return the scaled version of image
                            return bim.getScaledInstance(178,170,BufferedImage.SCALE_FAST);
                            
                            }catch(Exception e){
                                // If there is a problem reading image,
                                // it might not be a valid image or unable
                                // to read
                                img.setText(" Not valid image/Unable to read");
                            }
                            
                        }
                    
                    return null;
                    }
                    
                    protected void done()
                    {
                        try
                        {
                        // Get the image
                        Image i=get(1L,TimeUnit.NANOSECONDS);
                        
                        // If i is null, go back!
                        if(i==null) return;
                        
                        // Set icon otherwise
                        img.setIcon(new ImageIcon(i));
                        }catch(Exception e){
                            // Print error occured
                            img.setText(" Error occured.");
                        }
                    }
                };
                
                // Start worker thread
                worker.execute();
            }
        });
    }
}
