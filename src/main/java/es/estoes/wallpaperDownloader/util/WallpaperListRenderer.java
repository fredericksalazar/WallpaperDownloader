package es.estoes.wallpaperDownloader.util;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class WallpaperListRenderer extends DefaultListCellRenderer {
    @SuppressWarnings("rawtypes")
	@Override
    public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setIcon((Icon) value);
        return label;
    }
}
