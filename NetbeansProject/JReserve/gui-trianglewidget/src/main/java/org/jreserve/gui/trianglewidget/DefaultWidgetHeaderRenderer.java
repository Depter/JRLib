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

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Default implementation for the {@link WidgetHeaderRenderer WidgetHeaderRenderer}
 * interface.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultWidgetHeaderRenderer extends JLabel implements WidgetHeaderRenderer {
    
    public DefaultWidgetHeaderRenderer() {
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(createBorder());
    }
    
    private Border createBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(), 
                BorderFactory.createEmptyBorder(0, 2, 0, 2));
    }
    
    @Override
    public Component getComponent(TriangleWidget widget, Object title, int index, boolean selected) {
        setText(title==null? null : title.toString());
        return this;
    }
}
