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

import javax.swing.JComponent;
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
    mimeType = TriangleEditorMultiview.MIME_TYPE,
    position = 1000,
    prefferedID = "org.jreserve.dummy.claimtriangle.edtior.GeometryEditor",
    background = "43C443"
)
@Messages({
    "LBL.GeometryEditor.Title=Geometry"
})
public class GeometryEditor extends AbstractExpandableElement {

    private Lookup lookup = Lookups.singleton("Geometry editor");
    private GeometryEditorPanel editorPanel;
    
    @Override
    protected JComponent createVisualComponent() {
        editorPanel = new GeometryEditorPanel();
        return editorPanel;
    }
    
    @Override
    protected boolean openMaximized() {
        return false;
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
}
