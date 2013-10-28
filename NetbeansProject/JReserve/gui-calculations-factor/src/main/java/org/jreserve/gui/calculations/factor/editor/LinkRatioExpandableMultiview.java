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
package org.jreserve.gui.calculations.factor.editor;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.factor.impl.FactorBundleImpl;
import org.jreserve.gui.calculations.factor.impl.FactorDataObject;
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
    displayName = "#LBL.LinkRatioExpandableMultiview.Title",
    iconBase = "org/jreserve/gui/calculations/factor/factors.png",
    mimeType = FactorDataObject.MIME_TYPE,
    persistenceType = TopComponent.PERSISTENCE_NEVER,
    preferredID = "org.jreserve.gui.calculations.factor.editor.LinkRatioExpandableMultiview",
    position = 100
)
@Messages({
    "LBL.LinkRatioExpandableMultiview.Title=Link Ratio"
})
public class LinkRatioExpandableMultiview extends AbstractExpandableMultiviewElement {
    
    private Lookup context;
    private FactorBundleImpl bundle;
    private JPanel toolBar;
    private MultiViewElementCallback callback;
    private CalculationListener calcListener;
    
    public LinkRatioExpandableMultiview(Lookup lkp) {
        this.context = lkp;
        this.bundle = lkp.lookup(FactorBundleImpl.class);
        calcListener = new CalculationListener();
    }

    @Override
    protected ExpandableContainerHandler createHandler() {
        return ExpandableFactory.createScrollPanel(
                FactorDataObject.MIME_TYPE, 
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
            if(bundle == evt.getCalculationProvider() && callback != null) {
                DataObject obj = context.lookup(DataObject.class);
                callback.getTopComponent().setDisplayName(obj.getName());
            }
        }
    }
}
