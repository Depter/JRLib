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
import javax.swing.JTextField;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.GeometryEditor.Title",
    mimeType = "jreserve/triangle-claim",
    position = 1000,
    prefferedID = "org.jreserve.dummy.claimtriangle.edtior.GeometryEditor",
    background = "43C443"
)
@Messages({
    "LBL.GeometryEditor.Title=Geometry"
})
public class GeometryEditor extends AbstractExpandableElement {

    private Lookup lookup = Lookups.singleton("Geometry editor");
    
    @Override
    protected JComponent createVisualComponent() {
        JTextField label = new JTextField("Geometry editor");
        label.setBackground(Color.RED);
        label.setOpaque(true);
        return label;
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
