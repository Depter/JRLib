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
 */
@ExpandableElement.Registration(
    displayName = "#LBL.TriangleEditor.Title",
    mimeType = "jreserve/triangle-claim",
    position = 2000,
    prefferedID = "org.jreserve.dummy.claimtriangle.edtior.TriangleEditor",
    background = "#COLOR.TriangleEditor.Background"
)
@Messages({
    "LBL.TriangleEditor.Title=Triangle",
    "COLOR.TriangleEditor.Background=FF7D30"
})
public class TriangleEditor extends AbstractExpandableElement {
    
    private Lookup lookup = Lookups.singleton("Triangle editor");
    private TriangleEditorPanel editorPanel;
    
    @Override
    protected JComponent createVisualComponent() {
        editorPanel = new TriangleEditorPanel();
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