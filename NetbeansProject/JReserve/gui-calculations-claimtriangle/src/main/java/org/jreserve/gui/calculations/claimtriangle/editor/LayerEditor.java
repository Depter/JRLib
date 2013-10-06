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
import org.jreserve.gui.calculations.api.edit.UndoUtil;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleDataObject;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableComponentHandler;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.trianglewidget.TriangleWidgetPanel;
import org.jreserve.gui.trianglewidget.model.TriangleSelection;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionEvent;
import org.jreserve.gui.trianglewidget.model.TriangleSelectionListener;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.LayerEditor.Title",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    position = 300,
    prefferedID = "org.jreserve.gui.calculations.claimtriangle.editor.LayerEditor",
    background = "#COLOR.LayerEditor.Background",
    iconBase = "org/jreserve/gui/calculations/claimtriangle/triangle.png"
)
@Messages({
    "LBL.LayerEditor.Title=Triangle",
    "COLOR.LayerEditor.Background=43C443"
})
public class LayerEditor extends AbstractExpandableElement {
    
    private LayerEditorPanel panel;
    private ClaimTriangleCalculationImpl calculation;
    private final Lookup lkp;
    private final InstanceContent ic = new InstanceContent();
    
    public LayerEditor() {
        this(Lookup.EMPTY);
    }
    
    public LayerEditor(Lookup context) {
        calculation = context.lookup(ClaimTriangleCalculationImpl.class);
        lkp = new ProxyLookup(new AbstractLookup(ic));
    }

    @Override
    public void setHandler(ExpandableComponentHandler handler) {
        super.setHandler(handler);
        UndoRedo.Manager manager = handler.getContainer().getUndoRedo();
        UndoUtil<ClaimTriangle> undo = new UndoUtil<ClaimTriangle>(manager, calculation);
        panel.setUndoUtil(undo);
    }
    
    @Override
    public Lookup getLookup() {
        return lkp;
    }
    
    @Override
    protected Component createVisualComponent() {
        panel = new LayerEditorPanel(calculation);
        
        TriangleWidgetPanel wPanel = panel.getWidgetPanel();
        wPanel.getTriangleSelectionModel().addTriangleSelectionListener(new SelectionListener());
        ic.add(wPanel.createCopiable());
        return panel;
    }
    
    @Override
    public void componentClosed() {
        if(panel != null)
            panel.componentClosed();
        super.componentClosed();
    }
    
    private class SelectionListener implements TriangleSelectionListener {
        @Override
        public void selectionChanged(TriangleSelectionEvent event) {
            TriangleSelection ts = lkp.lookup(TriangleSelection.class);
            if(ts != null)
                ic.remove(ts);
            ts = panel.getWidgetPanel().getTriangleSelectionModel().createSelection();
            ic.add(ts);
        }
    }
 }
