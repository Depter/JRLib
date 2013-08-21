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

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class WidgetCorner extends JComponent {

    private TriangleWidget widget;
    
    WidgetCorner(TriangleWidget widget) {
        this.widget = widget;
        setPreferredSize(calculatePrefferedSize());
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private Dimension calculatePrefferedSize() {
        Dimension xHeader = widget.getHorizontalHeader().getPreferredSize();
        Dimension yHeader = widget.getVerticalHeader().getPreferredSize();
        return new Dimension(yHeader.width, xHeader.height);
    }
    
    void resize() {
        setPreferredSize(calculatePrefferedSize());
        revalidate();
    }
    
}
