/**
 * Copyright 2016-2018 Eloy García Almadén <eloy.garcia.pca@gmail.com>
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
