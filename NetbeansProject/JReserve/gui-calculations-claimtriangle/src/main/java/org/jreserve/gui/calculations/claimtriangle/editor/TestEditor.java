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
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleDataObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
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
    displayName = "#LBL.TestEditor.Title",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    position = 600,
    prefferedID = "org.jreserve.gui.calculations.claimtriangle.editor.TestEditor",
    background = "#COLOR.TestEditor.Background",
    iconBase = "org/jreserve/gui/calculations/claimtriangle/shield.png"
)
@Messages({
    "LBL.TestEditor.Title=Tests",
    "COLOR.TestEditor.Background=3333FF"
})
public class TestEditor extends AbstractExpandableElement {
    
    private TestEditorPanel panel;
    private ClaimTriangleCalculationImpl calculation;
    
    public TestEditor() {
        this(Lookup.EMPTY);
    }
    
    public TestEditor(Lookup context) {
        calculation = context.lookup(ClaimTriangleCalculationImpl.class);
        EventBusManager.getDefault().subscribe(this);
    }

    @Override
    protected boolean openMaximized() {
        return false;
    }
    
    @Override
    protected Component createVisualComponent() {
        panel = new TestEditorPanel();
        if(calculation != null)
            panel.setTriangle(calculation.getCalculation());
        return panel;
    }
    
    @EventBusListener(forceEDT = true)
    public void calculationChanged(CalculationEvent.Change evt) {
        if(panel!=null && calculation == evt.getCalculationProvider())
            panel.setTriangle(calculation.getCalculation());
    }

    @Override
    public void componentClosed() {
        EventBusManager.getDefault().unsubscribe(this);
        super.componentClosed();
    }
}

