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

package org.jreserve.gui.misc.utils.dataobject.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "File",
    id = "org.jreserve.gui.misc.utils.dataobject.impl.CreateFolderAction"
)
@ActionRegistration(
    displayName = "#LBL.CreateFolderAction.Name",
    iconBase = "org/jreserve/gui/misc/utils/folder.png"
)
@ActionReference(
    path = "Ribbon/TaskPanes/Edit/Edit/New",
    position = 200
)
@Messages({
    "LBL.CreateFolderAction.Name=Create Folder",
    "LBL.CreateFolderAction.WizardTitle=Create Folder"
})
public class CreateFolderAction implements ActionListener {

    public DataObjectProvider dop;
    
    public CreateFolderAction(DataObjectProvider dop) {
        this.dop = dop;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        CreateDataFolderIterator it = new CreateDataFolderIterator(dop);
        //Initialize
        WizardDescriptor wiz = new WizardDescriptor(it);
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_CreateFolderAction_WizardTitle());
        DialogDisplayer.getDefault().notify(wiz);        
    }

}
