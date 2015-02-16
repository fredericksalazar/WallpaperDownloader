package es.estoes.wallpaperDownloader.util;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class WallpaperListRenderer extends DefaultListCellRenderer {
	// Constants
	public static final String WITH_TEXT = "with_text";
	public static final String WITHOUT_TEXT = "without_text";
	
	// Attributes
	private String text;
	
    public WallpaperListRenderer(String text) {
    	if (text.equals(WallpaperListRenderer.WITH_TEXT)) {
    		this.text = WallpaperListRenderer.WITH_TEXT;
    	} else {
    		this.text = WallpaperListRenderer.WITHOUT_TEXT;
    	}
	}
	@SuppressWarnings("rawtypes")
	@Override
    public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setIcon((Icon) value);
        if (text.equals(WallpaperListRenderer.WITH_TEXT)) {
            label.setText(" ");
            label.setHorizontalTextPosition(JLabel.RIGHT);        	
        }
        return label;
    }
}
