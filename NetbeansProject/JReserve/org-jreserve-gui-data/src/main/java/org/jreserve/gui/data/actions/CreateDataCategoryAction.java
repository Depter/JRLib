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
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.notifications.DialogUtil;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "File",
    id = "org.jreserve.gui.data.actions.CreateDataCategoryAction"
)
@ActionRegistration(
    iconBase = "org/jreserve/gui/data/icons/folder_db_add.png",
    displayName = "#CTL_CreateDataCategoryAction"
)
@ActionReferences({
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Data", position = 100),
    @ActionReference(path = "Node/DataCategory/Actions", position = 100)
})
@Messages({
    "CTL_CreateDataCategoryAction=New Category",
    "MSG.CreateDataCategoryAction.Create.Error=Unable to create data category."
})
public final class CreateDataCategoryAction implements ActionListener {
    
    private final static Logger logger = Logger.getLogger(CreateDataCategoryAction.class.getName());
    
    private final DataCategory context;

    public CreateDataCategoryAction(DataCategory context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        CreateDataCategoryDialog panel = new CreateDataCategoryDialog(context);
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
                context.getDataManager().createDataCategory(parent, name);
                logger.log(Level.FINE, String.format("DataCategory created: %s/%s", parent.getPath(), name));
            } catch (IOException ex) {
                String msg = "Unable to create data category '%s' under '%s'!";
                logger.log(Level.SEVERE, String.format(msg, name, parent.getPath()), ex);
                BubbleUtil.showException(Bundle.MSG_CreateDataCategoryAction_Create_Error(), ex);
            }
        }
    }
}
