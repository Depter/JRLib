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
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataManager;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.notifications.DialogUtil;
import org.jreserve.gui.misc.utils.widgets.AbstractContextAwareAction;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(
    category = "File",
    id = "org.jreserve.gui.data.actions.CreateDataCategoryAction"
)
@ActionRegistration(
    displayName = "#CTL_CreateDataCategoryAction",
    lazy = false
)
@ActionReferences({
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Data", position = 100),
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Edit/New", position = 100),
    @ActionReference(path = "Node/DataCategory/Actions", position = 100)
})
@Messages({
    "CTL_CreateDataCategoryAction=New Category",
    "MSG.CreateDataCategoryAction.Create.Error=Unable to create data category."
})
public final class CreateDataCategoryAction extends AbstractContextAwareAction { 
    
    @StaticResource private final static String SMALL_ICON = "org/jreserve/gui/data/icons/folder_db_add.png";   //NOI18
    @StaticResource private final static String LARGE_ICON = "org/jreserve/gui/data/icons/folder_db_add32.png"; //NOI18
    private final static Logger logger = Logger.getLogger(CreateDataCategoryAction.class.getName());
    
    private DataCategory dataCategory;

    public CreateDataCategoryAction() {
        this(Utilities.actionsGlobalContext());
    }
 
    private CreateDataCategoryAction(Lookup context) {
        super(context);
        putValue(Action.NAME, Bundle.CTL_CreateDataCategoryAction());
        super.putValue(Action.LARGE_ICON_KEY, ImageUtilities.loadImageIcon(LARGE_ICON, false));
        super.putValue(Action.SMALL_ICON, ImageUtilities.loadImageIcon(SMALL_ICON, false));
    }
    
    @Override
    protected boolean shouldEnable(Lookup context) {
        dataCategory = context.lookup(DataCategory.class);
        if(dataCategory != null)
            return true;
        
        Project project = context.lookup(Project.class);
        if(project == null)
            return false;
        
        DataManager dm = project.getLookup().lookup(DataManager.class);
        if(dm == null)
            return false;
        dataCategory = dm.getCategory(null);
        return dataCategory != null;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new CreateDataCategoryAction(lkp);
    }

    @Override
    protected void performAction(ActionEvent evt) {
        CreateDataCategoryDialog panel = new CreateDataCategoryDialog(dataCategory);
        DialogListener listener = new DialogListener(panel);
        DialogUtil.showDialog(panel, listener);
    }
    
    private class DialogListener implements ActionListener {
        private CreateDataCategoryDialog panel;
        
        private DialogListener(CreateDataCategoryDialog dialog) {
            this.panel = dialog;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = panel.getCategoryName();
            DataCategory parent = panel.getParentCategory();
            try {
                parent.getDataManager().createDataCategory(parent, name);
                logger.log(Level.FINE, String.format("DataCategory created: %s/%s", parent.getPath(), name));
            } catch (IOException ex) {
                String msg = "Unable to create data category '%s' under '%s'!";
                logger.log(Level.SEVERE, String.format(msg, name, parent.getPath()), ex);
                BubbleUtil.showException(Bundle.MSG_CreateDataCategoryAction_Create_Error(), ex);
            }
        }
    }
}
