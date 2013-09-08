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
package org.jreserve.gui.data.spi.inport;

import java.awt.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.inport.ImportDataWizardVisualPanelLast;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
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
    "MSG.ImportDataWizardPanelLast.NoEntries=There is no new data to save!",
    "# {0} - accident",
    "# {1} - development",
    "MSG.ImportDataWizardPanelLast.DuplicateEntries=Input contains duplicate entries for {0}/{1}!"
})
public class ImportDataWizardPanelLast implements WizardDescriptor.Panel<WizardDescriptor> {
    
    private WizardDescriptor wiz;
    private ImportDataWizardVisualPanelLast component;
    private boolean valid = false;
    private ChangeSupport cs = new ChangeSupport(this);
    private DataEntry duplicateEntry = null;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ImportDataWizardVisualPanelLast(this);
            component.addChangeListener(new ComponentListener());
            if(wiz != null)
                initComponent();
        }
        return component;
    }
    
    private void initComponent() {
        component.setTriangleGeometry((TriangleGeometry) wiz.getProperty(ImportDataWizardPanelGeometry.PROP_TRIANGLE_GEOMETRY));
        component.setDataSource((DataSource) wiz.getProperty(ImportDataProvider.PROP_DATA_SOURCE));
        component.setEntries((List<DataEntry>) wiz.getProperty(ImportDataProvider.PROP_IMPORT_DATA));
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        duplicateEntry = calculateHasDuplicates((List<DataEntry>) wiz.getProperty(ImportDataProvider.PROP_IMPORT_DATA));
        if(component != null)
            initComponent();
    }
    
    private DataEntry calculateHasDuplicates(List<DataEntry> entries) {
        if(entries == null)
            return null;
        Set<DataEntry> singles = new HashSet<DataEntry>();
        for(DataEntry entry : entries)
            if(!singles.add(entry))
                return entry;
        return null;    
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(ImportDataProvider.PROP_SAVE_TYPE, component.getSaveType());
        settings.putProperty(ImportDataProvider.PROP_IMPORT_DATA_CUMMULATED, component.isInputCummulated());
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }
    
    private void changed() {
        valid = isInputValid();
        if(valid)
            showError(null);
        cs.fireChange();
    }
    
    private boolean isInputValid() {
        if(!component.hasEntriesToSave()) {
            showError(Bundle.MSG_ImportDataWizardPanelLast_NoEntries());
            return false;
        }
        
        if(duplicateEntry != null) {
            String accident = duplicateEntry.getAccidentDate().toString();
            String development = duplicateEntry.getDevelopmentDate().toString();
            showError(Bundle.MSG_ImportDataWizardPanelLast_DuplicateEntries(accident, development));
            return false;
        }
        return true;
    }
    
    private void showError(String msg) {
        if(wiz != null) 
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private class ComponentListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            changed();
        }
    }
}
