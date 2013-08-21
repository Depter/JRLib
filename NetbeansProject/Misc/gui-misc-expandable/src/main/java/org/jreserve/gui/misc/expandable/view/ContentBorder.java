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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ContentBorder implements Border {

    private final static Insets INSETS = new Insets(0, 2, 2, 2);
    private final static int ARCH = 8;
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(c.getBackground());
        g.fillRoundRect(0, -2, width, height+2, ARCH, ARCH);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return INSETS;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
