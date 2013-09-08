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
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.inport.ImportSettings;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
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
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ImportDataWizardVisualPanel1(this);
            if(wizard != null)
                initComponent();
        }
        return component;
    }
    
    private void initComponent() {
        component.setDataObjectProvider(getSourceProvider());
        component.setDataSource(getInitDataSource());
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
    
    private DataObjectProvider getSourceProvider() {
        return (DataObjectProvider) wizard.getProperty(ImportDataProvider.PROP_SOURCE_PROVIDER);
    }
    
    private DataSource getInitDataSource() {
        DataSource source = (DataSource) wizard.getProperty(ImportDataProvider.PROP_DATA_SOURCE);
        if(source != null)
            return source;
        return (DataSource) wizard.getProperty(ImportDataProvider.PROP_INIT_DATA_SOURCE);
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
        ImportDataProviderAdapter adapter = component.getImportDataProvider();
        settings.putProperty(ImportDataProvider.PROP_IMPORT_WIZARD, adapter.getImportDataProvider());
        if(adapter.isDSRequired()) {
            DataSource ds = component.getSelectedDataSource();
            settings.putProperty(ImportDataProvider.PROP_DATA_SOURCE, ds);
        }
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
    
    void changed() {
        isValid = isInputValid();
        if(isValid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wizard != null)
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean isInputValid() {
        return providerSelected() && sourceSelected();
    }
    
    private boolean providerSelected() {
        ImportDataProviderAdapter adapter = component.getImportDataProvider();
        ImportDataProvider provider = adapter==null? null : adapter.getImportDataProvider();
        wizard.putProperty(ImportDataProvider.PROP_IMPORT_WIZARD, provider);
        
        if(adapter == null) {
            showError(Bundle.MSG_ImportDataWizardPanel1_DataProvider_NotSelected());
            return false;
        }
        return true;
    }
    
    private boolean sourceSelected() {
        ImportDataProviderAdapter dataProvider = component.getImportDataProvider();
        if(!dataProvider.isDSRequired())
            return true;
        
        String path = component.getDataSourcePath();
        if(path == null || path.length() == 0) {
            showError(Bundle.MSG_ImportDataWizardPanel1_DataSource_NotSelected());
            return false;
        }
        
        
        if(component.getSelectedDataSource() == null) {
            showError(Bundle.MSG_ImportDataWizardPanel1_DataSource_InvalidPath(path));
            return false;
        }

        return true;
    }
}
