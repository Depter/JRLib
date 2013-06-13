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

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.jreserve.gui.misc.expandable.ExpandableContainerHandler;
import org.jreserve.gui.misc.expandable.ExpandableFactory;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
//@MultiViewElement.Registration(
//    displayName = "#LBL.TriangleEditorMultiview.Title",
//    //iconBase = "org/jreserve/dummy/projecttree/resources/triangle.png",
//    mimeType = TriangleEditorMultiview.MIME_TYPE,
//    persistenceType = TopComponent.PERSISTENCE_NEVER,
//    preferredID = "org.jreserve.dummy.claimtriangle.edtior.TriangleEditorMultiview",
//    position = 100
//)
@Messages({
    "LBL.TriangleEditorMultiview.Title=Editor"
})
public class TriangleEditorMultiview extends JPanel implements MultiViewElement {
    final static String MIME_TYPE = "application/jreserve-triangle-claim";

    private JComponent toolBar;
    private ExpandableContainerHandler handler;
    private MultiViewElementCallback callback;
    
    @Override
    public JComponent getVisualRepresentation() {
        if(handler == null) { 
            Lookup lkp = Lookups.singleton(TriangleFactory.createTriangle());
            handler = ExpandableFactory.createScrollPanel(MIME_TYPE, lkp);
        }
        return handler.getComponent();
    }

    @Override
    public JComponent getToolbarRepresentation() {
        if(toolBar == null)
            toolBar = new JPanel();
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return handler==null? Lookup.EMPTY : handler.getLookup();
    }

    @Override
    public void componentOpened() {
        if(handler != null)
            handler.componentOpened();
    }

    @Override
    public void componentClosed() {
        if(handler != null)
            handler.componentClosed();
    }

    @Override
    public void componentShowing() {
        if(handler != null)
            handler.componentShowing();
    }

    @Override
    public void componentHidden() {
        if(handler != null)
            handler.componentHidden();
    }

    @Override
    public void componentActivated() {
        if(handler != null)
            handler.componentActivated();
    }

    @Override
    public void componentDeactivated() {
        if(handler != null)
            handler.componentDeactivated();
    }

    @Override
    public UndoRedo getUndoRedo() {
        return handler==null? 
            UndoRedo.NONE :
            handler.getUndoRedo();
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
        this.callback.getTopComponent().setDisplayName("APC Paid");
    }

    @Override
    public CloseOperationState canCloseElement() {
        return handler==null? 
                CloseOperationState.STATE_OK :
                handler.canCloseElement();
    }
}
