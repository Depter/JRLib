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

package org.jreserve.gui.data.editor;

import java.awt.Image;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.jrlib.gui.data.DataType;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MultiViewElement.Registration(
    displayName = "#LBL.DataSourceTableMultiviewElement.Title",
    mimeType = DataSource.MIME_TYPE,
    persistenceType = TopComponent.PERSISTENCE_NEVER,
    preferredID = "org.jreserve.gui.data.editor.DataSourceTableMultiviewElement"
)
@Messages({
    "LBL.DataSourceTableMultiviewElement.Title=Data"
})
public class DataSourceTableMultiviewElement implements MultiViewElement {
//GUI
//  TOOLBAR
//      Refresh, copy, Delete, Show/Hide filter, scale
//  PANEL
//      TABLE
//      FILTER
    
    @StaticResource private final static String IMG_TRIANGLE = "org/jreserve/gui/data/icons/database_triangle.png";
    @StaticResource private final static String IMG_VECTOR = "org/jreserve/gui/data/icons/database_vector.png";
    
    private MultiViewElementCallback callBack;
    private DataSource ds;
    private DataSourceListener dsListener;
    private Lookup lkp;
    
    private JPanel panel;
    private JPanel toolBar;
    
    public DataSourceTableMultiviewElement(Lookup ctx) {
        this.ds = ctx.lookup(DataSource.class);
        this.lkp = ctx;
        dsListener = new DataSourceListener();
        EventBusManager.getDefault().subscribe(dsListener);
    }
    
    @Override
    public JComponent getVisualRepresentation() {
        if(panel == null)
            panel = new JPanel();
        return panel;
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
        return lkp;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        EventBusManager.getDefault().unsubscribe(dsListener);
        dsListener = null;
        ds = null;
        lkp = Lookup.EMPTY;
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callBack = callback;
        TopComponent tc = callback.getTopComponent();
        tc.setDisplayName(ds.getName());
        tc.setToolTipText(ds.getPath());
        tc.setIcon(getImage());
    }
    
    private Image getImage() {
        String path = (DataType.TRIANGLE == ds.getDataType())? IMG_TRIANGLE : IMG_VECTOR;
        return ImageUtilities.loadImage(path);
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    private class DataSourceListener {
        
        @EventBusListener
        public void renameEvent(DataEvent.DataItemRenamed evt) {
            if(ds == evt.getDataItem() && callBack != null) {
                TopComponent tc = callBack.getTopComponent();
                tc.setDisplayName(ds.getName());
                tc.setToolTipText(ds.getPath());
            }
        }
    }
}
