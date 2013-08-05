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
package org.jreserve.gui.data.inport;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.settings.ImportSettings;
import org.jreserve.gui.data.spi.ImportDataProvider;
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
    "MSG.ImportDataWizardPanel1.DataSource.NotSelected=Storage not selected!",
    "# {0} - path",
    "MSG.ImportDataWizardPanel1.DataSource.InvalidPath=''{0}'' is not a valid storage name!",
    "MSG.ImportDataWizardPanel1.DataProvider.NotSelected=Import data source not selected!"
})
class ImportDataWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {
    
    final static String PROP_IMPORT_DATA_PROVIDER_ADAPTER = "import.data.provider.adapter";
    final static String PROP_DATA_SOURCE_PATH = "data.source.path";
    
    private ImportDataWizardVisualPanel1 component;
    private boolean isValid = false;
    private ChangeSupport cs = new ChangeSupport(this);
    private WizardDescriptor wizard;
    
    private DataSource dataSource;
    private ImportDataProvider dataProvider;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ImportDataWizardVisualPanel1();
            component.addPropertyChangeListener(new PanelListener());
            if(wizard != null)
                initComponent();
        }
        return component;
    }
    
    private void initComponent() {
        component.setDataItem(getInitDataItem());
        ImportDataProviderAdapter a = (ImportDataProviderAdapter) wizard.getProperty(PROP_IMPORT_DATA_PROVIDER_ADAPTER);
        if(a == null) {
            a = ImportSettings.getImportDataProvider();
            if(a != null) {
                wizard.putProperty(PROP_IMPORT_DATA_PROVIDER_ADAPTER, a);
                wizard.putProperty(ImportDataProvider.PROP_IMPORT_WIZARD, a.getImportDataProvider());
            }
        }
        component.setImportProvider(a);
    }
    
    private DataItem getInitDataItem() {
        DataItem item = (DataItem) wizard.getProperty(ImportDataProvider.PROP_DATA_SOURCE);
        if(item != null)
            return item;
        return (DataItem) wizard.getProperty(ImportDataWizardIterator.PROP_INIT_DATA_ITEM);
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wizard = settings;
        if(component != null)
            initComponent();
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
    }

    @Override
    public boolean isValid() {
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
    
    private void validateInput() {
        dataSource = (DataSource) component.getClientProperty(ImportDataProvider.PROP_DATA_SOURCE);
        dataProvider = (ImportDataProvider) component.getClientProperty(ImportDataProvider.PROP_IMPORT_WIZARD);
        isValid = checkValid();
        if(isValid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wizard != null)
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean checkValid() {
        return sourceSelected() && providerSelected();
    }
    
    private boolean sourceSelected() {
        if(dataSource == null) {
            String path = (String) component.getClientProperty(PROP_DATA_SOURCE_PATH);
            if(path == null || path.length()==0) {
                showError(Bundle.MSG_ImportDataWizardPanel1_DataSource_NotSelected());
            } else {
                showError(Bundle.MSG_ImportDataWizardPanel1_DataSource_InvalidPath(path));
            }
            return false;
        }
        return true;
    }
    
    private boolean providerSelected() {
        if(dataProvider == null) {
            showError(Bundle.MSG_ImportDataWizardPanel1_DataProvider_NotSelected());
            return false;
        }
        return true;
    }
    
    private class PanelListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propName = evt.getPropertyName();
            if(ImportDataProvider.PROP_DATA_SOURCE.equals(propName) ||
               ImportDataProvider.PROP_IMPORT_WIZARD.equals(propName)) {
                wizard.putProperty(propName, evt.getNewValue());
                validateInput();
            }
        }
    }
}
