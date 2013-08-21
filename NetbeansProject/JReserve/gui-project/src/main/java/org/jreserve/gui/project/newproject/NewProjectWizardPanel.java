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
package org.jreserve.gui.project.newproject;

import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class NewProjectWizardPanel implements WizardDescriptor.ValidatingPanel<WizardDescriptor>, WizardDescriptor.FinishablePanel<WizardDescriptor>{

    private WizardDescriptor wizard;
    private NewProjectWizardPanelVisual component;
    private ChangeSupport cs = new ChangeSupport(this);
    
    @Override
    public NewProjectWizardPanelVisual getComponent() {
        if(component == null) {
            component = new NewProjectWizardPanelVisual(this);
            component.setName(Bundle.LBL_CreateProjectStep());
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor data) {
        this.wizard = data;
        getComponent().read(wizard);
    }

    @Override
    public void storeSettings(WizardDescriptor data) {
        getComponent().store(data);
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        cs.addChangeListener(cl);
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        cs.removeChangeListener(cl);
    }

    void fireChange() {
        cs.fireChange();
    }
    
    @Override
    public boolean isFinishPanel() {
        return true;
    }

    @Override
    public boolean isValid() {
        return getComponent().isValide(wizard);
    }

    @Override
    public void validate() throws WizardValidationException {
        getComponent().validate(wizard);
    }
}
