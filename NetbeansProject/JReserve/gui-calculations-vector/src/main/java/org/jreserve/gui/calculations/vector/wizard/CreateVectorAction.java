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
package org.jreserve.gui.calculations.vector.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import org.jreserve.gui.calculations.api.NamedCalculationProvider;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionID(
    category = "File",
    id = "org.jreserve.gui.calculations.vector.wizard.CreateVectorAction"
)
@ActionRegistration(
    displayName = "#LBL.CreateVectorAction.Name",
    iconBase = "org/jreserve/gui/calculations/vector/vector.png"
)
@ActionReference(
    path = "Ribbon/TaskPanes/Edit/Edit/New",
    position = 600
)
@Messages({
    "LBL.CreateVectorAction.Name=Vector",
    "LBL.CreateVectorAction.WizardTitle=Create Vector"
})
public class CreateVectorAction implements ActionListener {
    private NamedCalculationProvider cop;
    
    public CreateVectorAction(NamedCalculationProvider cop) {
        this.cop = cop;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        Project p = FileOwnerQuery.getOwner(cop.getRoot());
        CreateVectorWizardIterator it = new CreateVectorWizardIterator(p);
        WizardDescriptor wiz = new WizardDescriptor(it);
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_CreateVectorAction_WizardTitle());
        DialogDisplayer.getDefault().notify(wiz);        
    }
}
