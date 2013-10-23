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
package org.jreserve.gui.calculations.vector.editor;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.vector.VectorCalculation;
import org.jreserve.gui.calculations.vector.impl.VectorDataObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.expandable.AbstractExpandableMultiviewElement;
import org.jreserve.gui.misc.expandable.ExpandableContainerHandler;
import org.jreserve.gui.misc.expandable.ExpandableFactory;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MultiViewElement.Registration(
    displayName = "#LBL.VectorExpandableMultiview.Title",
    iconBase = "org/jreserve/gui/calculations/vector/vector.png",
    mimeType = VectorDataObject.MIME_TYPE,
    persistenceType = TopComponent.PERSISTENCE_NEVER,
    preferredID = "org.jreserve.gui.calculations.vector.VectorExpandableMultiview",
    position = 100
)
@Messages({
    "LBL.VectorExpandableMultiview.Title=Editor"
})
public class VectorExpandableMultiview extends AbstractExpandableMultiviewElement {
    
    private Lookup context;
    private JPanel toolBar;
    private MultiViewElementCallback callback;
    private VectorCalculation calculation;
    private CalculationListener calcListener;
    
    public VectorExpandableMultiview(Lookup lkp) {
        this.context = lkp;
        this.calculation = lkp.lookup(VectorCalculation.class);
        calcListener = new CalculationListener();
    }

    @Override
    protected ExpandableContainerHandler createHandler() {
        return ExpandableFactory.createScrollPanel(
                VectorDataObject.MIME_TYPE, 
                context);
    }

    @Override
    public JComponent getToolbarRepresentation() {
        if(toolBar == null)
            toolBar = new JPanel();
        return toolBar;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
        if(context != null) {
            DataObject obj = context.lookup(DataObject.class);
            callback.getTopComponent().setDisplayName(obj.getName());
        }
    }

    @Override
    public void componentOpened() {
        EventBusManager.getDefault().subscribe(calcListener);
        super.componentOpened();
    }

    @Override
    public void componentClosed() {
        EventBusManager.getDefault().unsubscribe(calcListener);
        super.componentClosed();
    }
    
    private class CalculationListener {
        
        @EventBusListener(forceEDT = true)
        public void renamed(CalculationEvent.Renamed evt) {
            if(calculation == evt.getCalculationProvider() && callback != null) {
                DataObject obj = context.lookup(DataObject.class);
                callback.getTopComponent().setDisplayName(obj.getName());
            }
        }
    }
}
