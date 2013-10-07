/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jreserve.gui.trianglewidget;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import org.jreserve.gui.localesettings.LocaleSettings.DecimalFormatter;

/**
 * Default implementation for the {@link TriangleWidgetRenderer TriangleWidgetRenderer}
 * interface.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultTriangleWidgetRenderer extends JLabel implements TriangleWidgetRenderer {
    
    private DecimalFormatter df;
    private final static Border BASE_BORDER = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK), 
                BorderFactory.createEmptyBorder(1, 2, 1, 2));
    
    private Color background;
    private Color foreground;
    private Color selectionBackground;
    private Color selectionForeground;
    private Color highlightColor = new Color(255, 204, 0);
    
    public DefaultTriangleWidgetRenderer() {
        this(null);
    }
    
    public DefaultTriangleWidgetRenderer(DecimalFormatter df) {
        this.df = df;
        initColors();
        setOpaque(true);
        setBorder(BASE_BORDER);
        setHorizontalAlignment(SwingConstants.RIGHT);
    }
    
    private void initColors() {
        JList list = new JList();
        background = list.getBackground();
        foreground = list.getForeground();
        selectionBackground = list.getSelectionBackground();
        selectionForeground = list.getSelectionForeground();
    }
    
    public void setDecimalCount(int decimalcount) {
        df.setDecimalCount(decimalcount);
    }
    
    @Override
    public Component getComponent(TriangleWidget widget, double value, int row, int column, boolean selected) {
        DecimalFormatter wdf = widget.getDecimalFormatter();
        setText(format(df!=null? df : wdf, value));
        
        if(selected) {
            setBackground(selectionBackground);
            setForeground(selectionForeground);
        } else {
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
        }
        
        return this;
    }
    
    private String format(DecimalFormatter format, double value) {
        if(format == null)
            return ""+value;
        try  {
            return format.format(value);
        } catch (Exception ex) {
            return ""+value;
        }
    }
}
