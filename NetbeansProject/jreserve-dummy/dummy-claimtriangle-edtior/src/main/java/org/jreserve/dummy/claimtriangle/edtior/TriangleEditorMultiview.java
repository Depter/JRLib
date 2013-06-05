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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleEditorMultiview extends JPanel implements MultiViewElement {
    final static String MIME_TYPE = "jreserve/triangle-claim";

    private JToolBar toolBar;
    private ExpandableContainerHandler handler;
    private MultiViewElementCallback callback;
    
    @Override
    public JComponent getVisualRepresentation() {
        if(handler == null) 
            handler = ExpandableFactory.createScrollPanel(MIME_TYPE, null);
        return handler.getComponent();
    }

    @Override
    public JComponent getToolbarRepresentation() {
        if(toolBar == null)
            toolBar = new JToolBar();
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
        handler.componentOpened();
    }

    @Override
    public void componentClosed() {
        handler.componentClosed();
    }

    @Override
    public void componentShowing() {
        handler.componentShowing();
    }

    @Override
    public void componentHidden() {
        handler.componentHidden();
    }

    @Override
    public void componentActivated() {
        handler.componentActivated();
    }

    @Override
    public void componentDeactivated() {
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
    }

    @Override
    public CloseOperationState canCloseElement() {
        return handler==null? 
                CloseOperationState.STATE_OK :
                handler.canCloseElement();
    }
}
