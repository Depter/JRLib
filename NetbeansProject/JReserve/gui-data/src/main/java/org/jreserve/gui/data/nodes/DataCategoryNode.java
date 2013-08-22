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
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataEvent;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.DataCategoryNode.Rename.Error=Unable to rename Data Category: {0}"
})
class DataCategoryNode extends AbstractNode {
    
    private final static String ICON = "org/jreserve/gui/data/icons/folder_db.png";
    private final static String ACTION_PATH = "Node/DataCategory/Actions";  //NOI18
    
    private final DataCategory category;
    
    DataCategoryNode(DataCategory category) {
        super(
            Children.create(new DataCategoryChildren(category),true),
            Lookups.fixed(category, new ActionCookies(category))
        );
        this.category = category;
        setDisplayName(category.getName());
        setIconBaseWithExtension(ICON);
        EventBusManager.getDefault().subscribe(this);
    }
    
    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        int size = actions.size();
        return actions.toArray(new Action[size]);
    }    

    @Override
    public boolean canRename() {
        return category.getParent() != null;
    }

    @Override
    public void setName(String s) {
        try {
            category.getDataManager().renameDataItem(category, s);
        } catch (Exception ex) {
            BubbleUtil.showException(Bundle.MSG_DataCategoryNode_Rename_Error(category.getPath()), ex);
        }
    }
    
    @EventBusListener
    public void dataEvent(DataEvent evt) {
        if(shouldUpdateChildren(evt))
            setChildren(Children.create(new DataCategoryChildren(category), true));
        else if(isRenamed(evt))
            setDisplayName(category.getName());
    }
    
    private boolean shouldUpdateChildren(DataEvent evt) {
        return isChildCreated(evt) || 
               isChildDeleted(evt) ||
               isChildMoved(evt);
    }
    
    private boolean isChildCreated(DataEvent evt) {
        return (
                (evt instanceof DataEvent.DataCategoryCreatedEvent) ||
                (evt instanceof DataEvent.DataSourceCreatedEvent)
               ) && 
               this.category == evt.getDataItem().getParent();
    }
    
    private boolean isChildDeleted(DataEvent evt) {
        return (evt instanceof DataEvent.DataItemDeletedEvent) &&
                this.category == ((DataEvent.DataItemDeletedEvent)evt).getParent();
    }
    
    private boolean isChildMoved(DataEvent evt) {
        if(evt instanceof DataEvent.DataItemRenamed) {
            DataEvent.DataItemRenamed e = (DataEvent.DataItemRenamed)evt;
            String path = category.getPath();
            String oldItemPath = getParentPath(e.getOldPath());
            String newItemPath = getParentPath(e.getDataItem().getPath());
            return path.equals(oldItemPath) || path.equals(newItemPath);
        }
        return false;
    }
    
    private String getParentPath(String path) {
        int index = path.lastIndexOf('/');
        if(index < 0)
            return "";
        return path.substring(0, index);
    }
    
    private boolean isRenamed(DataEvent evt) {
        return (category == evt.getDataItem()) &&
               (evt instanceof DataEvent.DataItemRenamed) &&
                !getDisplayName().equals(category.getName());
    }
    
    @Override
    public Transferable drag() {
        return DataItemFlavor.createTransferable(category);
    }

    @Override
    public PasteType getDropType(final Transferable t, int action, int index) {
        return DataItemFlavor.getDropType(category, t);
    }
    
    private static class ActionCookies implements Deletable {

        private final DataCategory category;

        private ActionCookies(DataCategory category) {
            this.category = category;
        }
        
        @Override
        public void delete() throws Exception {
            category.getDataManager().deleteDataItem(category);
        }

        @Override
        public Icon getIcon() {
            return ImageUtilities.loadImageIcon(ICON, false);
        }

        @Override
        public String getDisplayName() {
            return category.getPath();
        }
    }
}
