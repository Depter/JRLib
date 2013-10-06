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
import org.jreserve.gui.misc.expandable.ExpandableComponentHandler;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.DataSourceEditor.Title",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    position = 100,
    prefferedID = "org.jreserve.gui.calculations.triangle.editor.DataSourceEditor",
    background = "646464",
    iconBase = "org/jreserve/gui/data/icons/database.png"
)
@Messages({
    "LBL.DataSourceEditor.Title=Input Data"
})
public class DataSourceEditor extends AbstractExpandableElement {

    private DataSourceEditorPanel panel;
    private ClaimTriangleCalculationImpl calculation;
    private final Lookup lkp;
    
    public DataSourceEditor() {
        this(Lookup.EMPTY);
    }
    
    public DataSourceEditor(Lookup context) {
        calculation = context.lookup(ClaimTriangleCalculationImpl.class);
        DataObject obj = context.lookup(DataObject.class);
        Lookup oLkp = obj==null? Lookup.EMPTY : obj.getLookup();
        Lookup cLkp = calculation==null? Lookup.EMPTY : Lookups.singleton(calculation);
        lkp = new ProxyLookup(oLkp, cLkp);
    }

    @Override
    public Lookup getGlobalLookup() {
        return lkp;
    }
    
    @Override
    protected Component createVisualComponent() {
        panel = new DataSourceEditorPanel();
        panel.setCalculation(calculation);
        return panel;
    }

    @Override
    public void setHandler(ExpandableComponentHandler handler) {
        super.setHandler(handler);
        panel.setUndo(handler.getContainer().getUndoRedo());
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
