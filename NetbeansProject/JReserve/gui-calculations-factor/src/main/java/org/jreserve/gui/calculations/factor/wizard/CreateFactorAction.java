/*
 * Copyright (C) 2013, Peter Decsi.
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jreserve.gui.calculations.factor.wizard;

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
    id = "org.jreserve.gui.calculations.factor.wizard.CreateFactorAction"
)
@ActionRegistration(
    displayName = "#LBL.CreateFactorAction.Name",
    iconBase = "org/jreserve/gui/calculations/factor/factors.png"
)
@ActionReference(
    path = "Ribbon/TaskPanes/Edit/Edit/New",
    position = 700
)
@Messages({
    "LBL.CreateFactorAction.Name=Factors",
    "LBL.CreateFactorAction.WizardTitle=Create Factors"
})
public class CreateFactorAction implements ActionListener {
    private NamedCalculationProvider cop;
    
    public CreateFactorAction(NamedCalculationProvider cop) {
        this.cop = cop;
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        Project p = FileOwnerQuery.getOwner(cop.getRoot());
        CreateFactorWizardIterator it = new CreateFactorWizardIterator(p);
        WizardDescriptor wiz = new WizardDescriptor(it);
        wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
        wiz.setTitle(Bundle.LBL_CreateFactorAction_WizardTitle());
        DialogDisplayer.getDefault().notify(wiz);        
    }
}
