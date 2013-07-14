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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
    category = "File",
    id = "org.jreserve.gui.data.actions.DeleteDataCategoryAction"
)
@ActionRegistration(
    iconBase = "org/jreserve/gui/data/icons/folder_db_delete.png",
    displayName = "#CTL_DeleteDataCategoryAction"
)
@Messages({
    "CTL_DeleteDataCategoryAction=Delete Category"
})
public final class DeleteDataCategoryAction implements ActionListener {

    private final List<DataCategory> context;

    public DeleteDataCategoryAction(List<DataCategory> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (DataCategory dataCategory : context) {
            // TODO use dataCategory
        }
    }
    
    private boolean confirmDelete() {
        //Set<String> entries = getDeletedEntries();
        
        return false;
    }
    

}
