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
package org.jreserve.gui.data.actions.createsourcewizard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.openide.NotificationLineSupport;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

public class CreateDataSourceWizardPanel1 implements WizardDescriptor.FinishablePanel<WizardDescriptor> {
    
    private WizardDescriptor wiz;
    private DataCategory category;
    private CreateDataSourceWizardVisualPanel1 component;
    private ChangeSupport cs = new ChangeSupport(this);
    
    private boolean firstCall = true;
    private boolean isValid;
    private DataCategory parent;
    private String name;
    
    @Override
    public Component getComponent() {
        if (component == null) {
            component = new CreateDataSourceWizardVisualPanel1();
            component.setDataCategory(category);
            component.addPropertyChangeListener(new PanelListener());
            validateInput();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        if(firstCall) {
            String msg = wiz==null? null : (String) wiz.getProperty(WizardDescriptor.PROP_ERROR_MESSAGE);
            if(msg != null) {
                wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, null);
                wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
            }
                
            firstCall = false;
        }
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        this.wiz = wiz;
        category = (DataCategory) wiz.getProperty(CreateDataSourceWizardIterator.PROP_DATA_CATEGORY);
        if(component != null) {
            component.setDataCategory(category);
            validateInput();
        }
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        // use wiz.putProperty to remember current panel state
    }

    @Override
    public boolean isFinishPanel() {
        if(!isValid)
            return false;
        DataSourceWizard sw = (DataSourceWizard) component.getClientProperty(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD);
        return sw==null || sw.getPanels().isEmpty();
    }
    
    private void validateInput() {
        parent = (DataCategory) component.getClientProperty(CreateDataSourceWizardVisualPanel1.PROP_PARENT);
        name = (String) component.getClientProperty(CreateDataSourceWizardVisualPanel1.PROP_NAME);
        isValid = checkValid();
        if(isValid)
            showError(null);
        cs.fireChange();
    }
    
    private boolean checkValid() {
        return isParentValid() && 
               nameNotEmpty() && 
               nameNotExis() &&
               sourceWizardSelected();
    }
    
    private boolean isParentValid() {
        if(parent == null) {
            showError(Bundle.MSG_CreateDataSourceWizardVisualPanel1_Parent_Empty());
            return false;
        }
        return true;
    }
    
    private void showError(String msg) {
        if(wiz != null)
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean nameNotEmpty() {
        if(name == null || name.trim().length() == 0) {
            showError(Bundle.MSG_CreateDataSourceWizardVisualPanel1_Name_Empty());
            return false;
        }
        return true;
    }
    
    private boolean nameNotExis() {
        for(DataSource child : parent.getDataSources()) {
            if(name.equalsIgnoreCase(child.getName())) {
                showError(Bundle.MSG_CreateDataSourceWizardVisualPanel1_Name_Exists(name));
                return false;
            }
        }
        return true;
    }
    
    private boolean sourceWizardSelected() {
        if(component.getClientProperty(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD) == null) {
            showError(Bundle.MSG_CreateDataSourceWizardVisualPanel1_SourceWizard_Empty());
            return false;
        }
        return true;
    }
    
    private class PanelListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propName = evt.getPropertyName();
            if(CreateDataSourceWizardVisualPanel1.PROP_NAME.equals(propName) ||
               CreateDataSourceWizardVisualPanel1.PROP_PARENT.equals(propName)) {
                validateInput();
            } else if(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD.equals(propName)) {
                wiz.putProperty(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD, evt.getNewValue());
                cs.fireChange();
            }
        }
        
    }
}
