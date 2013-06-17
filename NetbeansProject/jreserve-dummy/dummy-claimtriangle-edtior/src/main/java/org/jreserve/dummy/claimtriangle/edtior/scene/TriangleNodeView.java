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

package org.jreserve.dummy.claimtriangle.edtior.scene;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.explorer.propertysheet.PropertySheetView;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.TriangleNodeView.Title=Nodes"
})
public class TriangleNodeView implements MultiViewElement {

    private Scene component;
    private JComponent scene;
    private JComponent sattelite;
    private PropertySheetView properties;
    private JComponent panel;
    private JSplitPane mainSplit;
    private JSplitPane rightSplit;
    
    private JComponent toolBar = new javax.swing.JPanel();
    private Action[] actions = new Action[0];
    private MultiViewElementCallback callback;
    
    @Override
    public JComponent getVisualRepresentation() {
        if(panel == null)
            panel = createJRLibPanel();
        return panel;
    }
    
    private JComponent createVmdPanel() {
        properties = new PropertySheetView();
        properties.setDescriptionAreaVisible(false);
        component = new TriangleVMDScene(properties);
        scene = component.createView();
        sattelite = component.createSatelliteView();
        mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
        rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
        
        mainSplit.setDividerLocation(0.8);
        mainSplit.setLeftComponent(new JScrollPane(scene));
        mainSplit.setRightComponent(rightSplit);
        
        rightSplit.setDividerLocation(0.5);
        rightSplit.setBottomComponent(new JScrollPane(sattelite));
        
        properties.setBorder(new JScrollPane().getBorder());
        rightSplit.setTopComponent(properties);
        
        return mainSplit;
    }
    
    private JComponent createJRLibPanel() {
        component = new TriangleScene();
        scene = component.createView();
        sattelite = component.createSatelliteView();
        
        mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
        mainSplit.setDividerLocation(0.8);
        mainSplit.setLeftComponent(new JScrollPane(scene));
        mainSplit.setRightComponent(new JScrollPane(sattelite));
        return mainSplit;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        return actions;
    }

    @Override
    public Lookup getLookup() {
        return Lookup.EMPTY;
    }

    @Override public void componentOpened() {}
    @Override public void componentClosed() {}
    @Override 
    public void componentShowing() {
        mainSplit.setDividerLocation(0.8);
        if(rightSplit != null)
            rightSplit.setDividerLocation(0.5);
    }
    @Override public void componentHidden() {}
    @Override public void componentActivated() {}
    @Override public void componentDeactivated() {}

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
}
