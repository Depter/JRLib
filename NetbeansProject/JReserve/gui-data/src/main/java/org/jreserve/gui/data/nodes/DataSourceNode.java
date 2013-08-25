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
package org.jreserve.gui.data.nodes;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.data.editor.EditorUtil;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.netbeans.api.actions.Editable;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.DataSourceNode.Rename.Error=Unable to rename Data Storage: {0}"
})
class DataSourceNode extends AbstractNode {
    
    @StaticResource private final static String TRIANGLE_IMG = "org/jreserve/gui/data/icons/database_triangle.png";
    @StaticResource private final static String VECTOR_IMG = "org/jreserve/gui/data/icons/database_vector.png";
    private final static String ACTION_PATH = "Node/DataSource/Actions";  //NOI18
    
    private final DataSource source;
    private Action preferredAction;
    
    DataSourceNode(DataSource source) {
        super(Children.LEAF, 
              new ProxyLookup(
                source.getLookup(),
                Lookups.fixed(
                    source.getParent(), source.getDataProvider(), 
                    new ActionsCookie(source)
                )
              )
        );
        this.source = source;
        setDisplayName(source.getName());
        initIconBase();
        EventBusManager.getDefault().subscribe(this);
    }
    
    private void initIconBase() {
        if(DataType.TRIANGLE == source.getDataType())
            setIconBaseWithExtension(TRIANGLE_IMG);
        else
            setIconBaseWithExtension(VECTOR_IMG);
    }
    
    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        int size = actions.size();
        return actions.toArray(new Action[size]);
    }    

    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void setName(String s) {
        try {
            source.getDataManager().renameDataItem(source, s);
        } catch (Exception ex) {
            BubbleUtil.showException(Bundle.MSG_DataSourceNode_Rename_Error(source.getPath()), ex);
        }
    }
    
    @EventBusListener
    public void dataEvent(DataEvent evt) {
        if(isRenamed(evt))
            setDisplayName(source.getName());
    }
    
    private boolean isRenamed(DataEvent evt) {
        return (source == evt.getDataItem()) &&
               (evt instanceof DataEvent.DataItemRenamed) &&
                !getDisplayName().equals(source.getName());
    }
    
    @Override
    public Transferable drag() {
        return DataItemFlavor.createTransferable(source);
    }
    
    @Override
    public Action getPreferredAction() {
        if(preferredAction == null)
            preferredAction = new PreferredAction();
        return preferredAction;
    }
    
    private class PreferredAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorUtil.openEditor(source);
        }
    }
    
    
    private static class ActionsCookie implements Editable, Deletable {
        
        private final DataSource ds;

        private ActionsCookie(DataSource ds) {
            this.ds = ds;
        }
        
        @Override
        public void edit() {
            EditorUtil.openEditor(ds);
        }

        @Override
        public void delete() throws Exception {
            ds.getDataManager().deleteDataItem(ds);
        }

        @Override
        public Icon getIcon() {
            if(DataType.TRIANGLE == ds.getDataType())
                return ImageUtilities.loadImageIcon(TRIANGLE_IMG, false);
            return ImageUtilities.loadImageIcon(VECTOR_IMG, false);
        }

        @Override
        public String getDisplayName() {
            return ds.getPath();
        }
    }
}
