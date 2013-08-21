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
package org.jreserve.gui.data.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataManager;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(
    category = "File",
    id = "org.jreserve.gui.data.actions.DeleteDataCategoryAction"
)
@ActionRegistration(
    displayName = "#CTL_DeleteDataCategoryAction",
    lazy = false
)
@ActionReferences({
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Data", position = 400),
    @ActionReference(path = "Node/DataCategory/Actions", position = 400),
    @ActionReference(path = "Node/DataSource/Actions", position = 400)
})
@Messages({
    "CTL_DeleteDataCategoryAction=Delete Items",
    "MSG.DeleteDataCategoryAction.ContainsRoot=Can not delete \"Data\" category!"
})
public final class DeleteDataCategoryAction extends AbstractAction 
    implements ContextAwareAction, LookupListener {
    
    @StaticResource private final static String SMALL_ICON = "org/jreserve/gui/data/icons/folder_db_delete.png";   //NOI18
    @StaticResource private final static String LARGE_ICON = "org/jreserve/gui/data/icons/folder_db_delete32.png"; //NOI18
    
    private Lookup context;
    private Lookup.Result<DataItem> lkpInfo;
    private List<DataItem> items = new ArrayList<DataItem>();
    
    public DeleteDataCategoryAction() {
        this(Utilities.actionsGlobalContext());
    }
 
    private DeleteDataCategoryAction(Lookup context) {
        putValue(Action.NAME, Bundle.CTL_DeleteDataCategoryAction());
        super.putValue(Action.LARGE_ICON_KEY, ImageUtilities.loadImageIcon(LARGE_ICON, false));
        super.putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(SMALL_ICON, false));
        this.context = context;
    }
 
    void init() {
        if (lkpInfo != null)
            return;
 
        lkpInfo = context.lookupResult(DataItem.class);
        lkpInfo.addLookupListener(this);
        resultChanged(null);
    }
 
    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        DeleteDataCategoryDialog.showDialog(items);
    }
 
    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(shouldEnable());
    }
    
    private boolean shouldEnable() {
        items.clear();
        items.addAll(lkpInfo.allInstances());
        
        if(items.isEmpty())
            return false;
        
        DataManager m1 = null;
        for(DataItem item : items) {
            if(item.getParent() == null)
                return false;
            if(m1 == null)
                m1 = item.getDataManager();
            
            if(m1 != item.getDataManager())
                return false;
        }
        
        return true;
        
    }
 
    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new DeleteDataCategoryAction(context);
    }
}
