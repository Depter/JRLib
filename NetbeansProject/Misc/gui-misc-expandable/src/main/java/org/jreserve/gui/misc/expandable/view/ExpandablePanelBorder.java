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
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpandablePanelBorder implements Border {

    final static Color INNER_BORDER = new Color(255, 255, 255, 100);
    final static Color OUTTER_BORDER = new Color(50, 50, 50, 200);
    private final static Insets INSETS = new Insets(2, 2, 2, 2);
    
    private static Border INSTANCE = null;
    
    static Border getInstance() {
        if(INSTANCE == null)
            INSTANCE = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(OUTTER_BORDER, 1), 
                    BorderFactory.createLineBorder(INNER_BORDER, 1));
        return INSTANCE;
    }
    
    private ExpandablePanelBorder() {}
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(INNER_BORDER);
        g.drawRect(x+1, y+1, width-2, height-2);
        
        g.setColor(OUTTER_BORDER);
        g.drawRect(x, y, width, height);
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
