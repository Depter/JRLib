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
import java.text.MessageFormat;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.inport.ImportDataWizardIterator;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "File",
    id = "org.jreserve.gui.data.actions.ImportDataAction"
)
@ActionRegistration(
    displayName = "#CTL.ImportDataAction",
    iconBase = "org/jreserve/gui/data/icons/import_data.png"
)
@ActionReferences({
    @ActionReference(path = "Ribbon/TaskPanes/Edit/Data", position = 300),
    @ActionReference(path = "Node/DataCategory/Actions", position = 300),
    @ActionReference(path = "Node/DataSource/Actions", position = 300)
})
@Messages({
    "CTL.ImportDataAction=Import Data",
    "LBL.ImportDataAction.WizardTitle=Import Data"
})
public class ImportDataAction implements ActionListener {

    private DataItem context;
    
    public ImportDataAction(DataItem context) {
        this.context = context;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        WizardDescriptor wiz = new WizardDescriptor(new ImportDataWizardIterator(context));
        // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
        // {1} will be replaced by WizardDescriptor.Iterator.name()
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_CreateDataSourceAction_Wizard_Title());
        DialogDisplayer.getDefault().notify(wiz);
    }
    
}
