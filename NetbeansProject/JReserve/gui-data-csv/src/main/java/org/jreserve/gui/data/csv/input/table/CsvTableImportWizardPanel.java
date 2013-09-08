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

package org.jreserve.gui.data.csv.input.table;

import java.awt.Component;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.gui.data.csv.input.AbstractCsvReader;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.CsvTableImportWizardPanel.File.Empty=Input file not set!",
    "# {0} - path",
    "MSG.CsvTableImportWizardPanel.File.NotFound=File ''{0}'' not found!",
    "MSG.CsvTableImportWizardPanel.CellSeparator.Empty=Cell separator not set!",
    "MSG.CsvTableImportWizardPanel.DecimalSeparator.Empty=Decimal separator not set!",
    "MSG.CsvTableImportWizardPanel.DateFormat.Empty=Date format not set!",
    "MSG.CsvTableImportWizardPanel.DateFormat.Invalid=Date format is invalid!"
})
public class CsvTableImportWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private CsvTableImportVisualPanel component;
    private boolean valid = false;
    private final ChangeSupport cs = new ChangeSupport(this);
    
    private DataSource ds;
    private WizardDescriptor wiz;
    
    private final Date checkDate = new Date();
    private final SimpleDateFormat sdf = new SimpleDateFormat();
    private volatile ValidationData validationData;
    
    @Override
    public Component getComponent() {
        if(component == null)
            component = new CsvTableImportVisualPanel(this);
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        this.ds = (DataSource) wiz.getProperty(ImportDataProvider.PROP_DATA_SOURCE);
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
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
    
    void panelChanged() {
        valid = isInputValid();
        if(valid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wiz != null)
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean isInputValid() {
        return component != null &&
               isFileValid() &&
               isCellSeparatorValid() &&
               isDecimalSeparatorValid() &&
               isDateFormatValid();
    }
    
    private boolean isFileValid() {
        String path = component.getCsvPath();
        if(path==null || path.length()==0) {
            showError(Bundle.MSG_CsvTableImportWizardPanel_File_Empty());
            return false;
        }
        
        File f = new File(path);
        if(!f.isFile()) {
            showError(Bundle.MSG_CsvTableImportWizardPanel_File_NotFound(path));
            return false;
        }
        return true;
    }
    
    private boolean isCellSeparatorValid() {
        String sep = component.getCellSeparator();
        if(sep==null || sep.length()==0) {
            showError(Bundle.MSG_CsvTableImportWizardPanel_CellSeparator_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isDecimalSeparatorValid() {
        String sep = component.getDecimalSeparator();
        if(sep==null || sep.length()==0) {
            showError(Bundle.MSG_CsvTableImportWizardPanel_DecimalSeparator_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isDateFormatValid() {
        String pattern = component.getDateFormat();
        if(pattern==null || pattern.length()==0) {
            showError(Bundle.MSG_CsvTableImportWizardPanel_DateFormat_Empty());
            return false;
        }
        
        try {
            sdf.applyPattern(pattern);
            sdf.format(checkDate);
        } catch (Exception ex) {
            showError(Bundle.MSG_CsvTableImportWizardPanel_DateFormat_Invalid());
            return false;
        }
        return true;
    }

    @Override
    public void prepareValidation() {
        validationData = new ValidationData();
        component.startProgressBar();
    }

    @Override
    public void validate() throws WizardValidationException {
        try {
            CsvTableReader reader = new CsvTableReader(validationData);
            List<DataEntry> entries = reader.read();
            wiz.putProperty(ImportDataProvider.PROP_IMPORT_DATA, entries);
        } catch (Exception ex) {
            throw new WizardValidationException(component, ex.getMessage(), ex.getLocalizedMessage());
        } finally {
            stopProgressBar();
        }
    }
    
    void stopProgressBar() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                component.stopProgressBar();
            }
        });
    }
    
    class ValidationData extends AbstractCsvReader.CsvFormatData{
        final DataType dt = CsvTableImportWizardPanel.this.ds.getDataType();
        final String dateFormat = component.getDateFormat();

        private ValidationData() {
            super.setCsvFile(new File(component.getCsvPath()));
            super.setColumnHeader(component.hasColumnHeader());
            super.setRowHeaders(component.hasRowHeader());
            super.setCellsQuoted(component.isCellsQuoted());
            super.setCellSep(component.getCellSeparator());
            super.setDecimalSep(component.getDecimalSeparator().charAt(0));
        }
    }
    
}
