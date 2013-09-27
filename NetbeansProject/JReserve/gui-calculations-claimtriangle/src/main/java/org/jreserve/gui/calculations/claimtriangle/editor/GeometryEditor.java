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

package org.jreserve.gui.calculations.claimtriangle.editor;

import java.awt.Component;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleDataObject;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.GeometryEditor.Title",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    position = 200,
    prefferedID = "org.jreserve.gui.calculations.triangle.editor.GeometryEditor",
    background = "43C443",
    iconBase = "org/jreserve/gui/calculations/claimtriangle/geometry.png"
)
@Messages({
    "LBL.GeometryEditor.Title=Geometry"
})
public class GeometryEditor extends AbstractExpandableElement {

    private GeometryEditorPanel panel;
    private ClaimTriangleCalculationImpl calculation;
    
    public GeometryEditor() {
        this(Lookup.EMPTY);
    }
    
    public GeometryEditor(Lookup context) {
        calculation = context.lookup(ClaimTriangleCalculationImpl.class);
    }
    
    @Override
    protected Component createVisualComponent() {
        panel = new GeometryEditorPanel();
        panel.setCalculation(calculation);
        return panel;
    }

    @Override
    protected boolean openMaximized() {
        return false;
    }

    @Override
    public void componentClosed() {
        if(panel != null)
            panel.componentClosed();
        super.componentClosed();
    }
}
