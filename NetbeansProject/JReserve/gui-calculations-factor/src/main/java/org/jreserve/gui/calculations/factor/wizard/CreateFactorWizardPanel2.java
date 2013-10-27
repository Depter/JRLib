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

import org.jreserve.gui.calculations.api.NamedCalculationProvider;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.misc.namedcontent.ProjectContentProvider;
import org.jreserve.gui.misc.utils.wizard.AbstractWizardPanel;
import org.netbeans.api.project.Project;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.CreateFactorWizardPanel2.DOP.NotFound=Project does not contain data storages!",
    "MSG.CreateFactorWizardPanel2.Source.Empty=Field 'Source' is empty!",
    "MSG.CreateFactorWizardPanel2.Source.NotFound=Claim Triangle not found!"
})
class CreateFactorWizardPanel2 extends AbstractWizardPanel<CreateFactorVisualPanel2>{

    @Override
    protected CreateFactorVisualPanel2 createComponent() {
        return new CreateFactorVisualPanel2(this);
    }

    @Override
    protected void initComponent() {
        Object p = wiz.getProperty(CreateFactorWizardIterator.PROP_PROJECT);
        if(p instanceof Project)
            panel.setProject((Project) p);
    }
    
    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty(CreateFactorWizardIterator.PROP_DATA_SOURCE, getSource());
    }
    
    private ClaimTriangleCalculation getSource() {
        ProjectContentProvider pol = panel.getProjectObjectLookup();
        String path = panel.getSourcePath();
        return pol.getContent(path, ClaimTriangleCalculation.class);
    }

    @Override
    protected boolean isInputValid() {
        NamedCalculationProvider dop = panel.getSourceProvider();
        if(dop == null) {
            showError(Bundle.MSG_CreateFactorWizardPanel2_DOP_NotFound());
            return false;
        }
        
        String path = panel.getSourcePath();
        if(path == null || path.length() == 0) {
            showError(Bundle.MSG_CreateFactorWizardPanel2_Source_Empty());
            return false;
        }
        
        ProjectContentProvider pol = panel.getProjectObjectLookup();
        if(pol == null || pol.getContent(path, ClaimTriangleCalculation.class) == null) {
            showError(Bundle.MSG_CreateFactorWizardPanel2_Source_NotFound());
            return false;
        }
        
        return true;
    }
}
