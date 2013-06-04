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
package org.jreserve.gui.misc.expandable.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpandableBorder implements javax.swing.border.Border {

    private final static Insets INSETS = new Insets(3, 3, 3, 3);
    final static Color INNER_BORDER = new Color(255, 255, 255, 100);
    final static Color OUTTER_BORDER = new Color(50, 50, 50, 200);
    private final static int ARCH = 8;
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(OUTTER_BORDER);
        g.drawRoundRect(0, 0, width - 1, height - 1, ARCH, ARCH);
        
        g.setColor(INNER_BORDER);
        g.drawRoundRect(1, 1, width - 3, height - 3, ARCH, ARCH);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return INSETS;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
