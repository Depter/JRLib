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
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.gui.data.spi.DataSourceWizard;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.CreateDataSourceWizardPanel2.DataType.Empty=Data type not selected!",
    "MSG.CreateDataSourceWizardPanel2.StorageType.Empty=Storage type not selected!"
})
class CreateDataSourceWizardPanel2 implements WizardDescriptor.FinishablePanel<WizardDescriptor> {
    
    private boolean isValid = false;
    private ChangeSupport cs = new ChangeSupport(this);
    private CreateDataSourceWizardVisualPanel2 component;
    private WizardDescriptor wiz;
    
    private DataType dataType;
    private DataSourceWizard storage;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new CreateDataSourceWizardVisualPanel2();
            component.addPropertyChangeListener(new PanelListener());
            if(wiz != null)
                initComponentValues();
        }
        return component;
    }

    private void initComponentValues() {
        component.setDataType((DataType) wiz.getProperty(CreateDataSourceWizardIterator.PROP_DATA_TYPE));
        component.setStorageType((DataSourceWizardAdapter) wiz.getProperty(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD));
    }
    
    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        if(component != null)
            initComponentValues();
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(CreateDataSourceWizardIterator.PROP_DATA_TYPE, dataType);
        settings.putProperty(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD, storage);
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public boolean isFinishPanel() {
        if(storage == null)
            return false;
        return storage.getPanels().isEmpty();
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }
    
    private void validateInput() {
        dataType = (DataType) component.getClientProperty(CreateDataSourceWizardIterator.PROP_DATA_TYPE);
        storage = (DataSourceWizard) component.getClientProperty(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD);
        isValid = isInputValid();
        if(isValid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wiz != null)
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean isInputValid() {
        if(dataType == null) {
            showError(Bundle.MSG_CreateDataSourceWizardPanel2_DataType_Empty());
            return false;
        }
        
        if(storage == null) {
            showError(Bundle.MSG_CreateDataSourceWizardPanel2_StorageType_Empty());
            return false;
        }
        return true;
    }
    
    private class PanelListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propName = evt.getPropertyName();
            if(CreateDataSourceWizardIterator.PROP_DATA_TYPE.equals(propName))
                validateInput();
            else if(CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD.equals(propName)) {
                wiz.putProperty(propName, evt.getNewValue());
                validateInput();
            }
        }
    }
}
