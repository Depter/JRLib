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
package org.jreserve.dummy.claimtriangle.edtior;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.GeometryEditor.Title",
    mimeType = "jreserve/editor-claimtriangle",
    position = 1000,
    prefferedID = "org.jreserve.dummy.claimtriangle.edtior.GeometryEditor"
)
@Messages({
    "LBL.GeometryEditor.Title=Geometry"
})
public class GeometryEditor implements ExpandableElement {
    final static Color BACKGROUND = new Color(67, 196, 67);
    final static Color FOREGROUND = Color.WHITE;

    @Override
    public JComponent getVisualComponent() {
        JLabel label = new JLabel("Geometry editor");
        label.setBackground(Color.RED);
        label.setOpaque(true);
        return label;
    }

    @Override
    public JComponent[] getFrameComponents() {
        return new JComponent[0];
    }

    @Override
    public Color getBackground() {
        return BACKGROUND;
    }

    @Override
    public Color getForeground() {
        return FOREGROUND;
    }
    
}
