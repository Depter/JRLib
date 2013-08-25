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

package org.jreserve.gui.misc.audit.api;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JComponent;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.jreserve.gui.misc.audit.multiview.AuditTableMultiviewPanel;
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
public class AuditableMultiview implements MultiViewElement {
    
    private final static Logger logger = Logger.getLogger(AuditableMultiview.class.getName());
    
    private Auditable auditable;
    private Lookup lkp;
    private MultiViewElementCallback callback;
    private AuditTableMultiviewPanel panel;
    
    public AuditableMultiview(Lookup lkp) {
        this.lkp = lkp;
        auditable = lkp.lookup(Auditable.class);
        if(auditable == null) {
            logger.log(Level.WARNING, "No Auditable instance in the supplied lookup!");
            auditable = new DummyAuditable();
        }
    }
    
    @Override
    public JComponent getVisualRepresentation() {
        return getPanel();
    }

    private AuditTableMultiviewPanel getPanel() {
        if(panel == null)
            panel = new AuditTableMultiviewPanel(auditable);
        return panel;
    }
    
    @Override
    public JComponent getToolbarRepresentation() {
        return getPanel().getToolBar();
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return lkp;
    }

    @Override public void componentOpened() {}
    @Override public void componentClosed() {}
    @Override public void componentShowing() {}
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
    
    private static class DummyAuditable implements Auditable {
        @Override
        public List<AuditRecord> getAuditEvents() {
            return Collections.EMPTY_LIST;
        }
    }
}
