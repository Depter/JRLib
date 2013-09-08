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

package org.jreserve.gui.data.clipboard;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.jrlib.gui.data.MonthDate;
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
    "MSG.ImportClipboardTableWizardPanel.DateFormat.Empty=Date format is not set!",
    "# {0} - pattern",
    "MSG.ImportClipboardTableWizardPanel.DateFormat.Invalid=Date format ''{0}'' is invalid!",
    "MSG.ImportClipboardTableWizardPanel.Table.Empty=No input is pasted!",
    "# {0} - rowIndex",
    "# {1} - cCount",
    "MSG.ImportClipboardTableWizardPanel.Table.EmptyRow=Row ''{0}'' must have at least ''{1}'' columns!",
    "# {0} - str",
    "# {1} - row",
    "# {2} - column",
    "MSG.ImportClipboardTableWizardPanel.Table.Date.Invalid=''{0}'' at [{1}, {2}] can not be parsed as a date!",
    "# {0} - str",
    "# {1} - row",
    "# {2} - column",
    "MSG.ImportClipboardTableWizardPanel.Table.Value.Invalid=''{0}'' at [{1}, {2}]  can not be parsed as a number!",
    "# {0} - row",
    "MSG.ImportClipboardTableWizardPanel.Table.VectorDates=Accident and development date is different in row ''{0}''!",
    "# {0} - row2",
    "# {1} - row1",
    "MSG.ImportClipboardTableWizardPanel.Table.DuplicateRow=Row ''{0}'' is the dupplicate of row ''{1}''!"
})
class ImportClipboardTableWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    
    private final TextParser tp = new TextParser();
    private final ChangeSupport cs = new ChangeSupport(this);
    private final MonthDate.Factory mdf = new MonthDate.Factory();
    
    private boolean valid = false;
    private WizardDescriptor wiz;
    private ImportClipboardTableVisualPanel component;
    private boolean isTriangle;
    private List<DataEntry> entries = new ArrayList<DataEntry>();
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ImportClipboardTableVisualPanel(this);
            changed();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        DataType dt = ((DataSource)wiz.getProperty(ImportDataProvider.PROP_DATA_SOURCE)).getDataType();
        isTriangle = DataType.TRIANGLE == dt;
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        if(valid) {
            settings.putProperty(ImportDataProvider.PROP_IMPORT_DATA, entries);
        }
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
    
    void changed() {
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
        if(component == null)
            return false;
        entries.clear();
        
        SimpleDateFormat sdf = getDateFormat();
        if(sdf == null)
            return false;
        
        String[][] cells = component.getValues();
        int rCount = cells.length;
        if(rCount == 0) {
            showError(Bundle.MSG_ImportClipboardTableWizardPanel_Table_Empty());
            return false;
        }
        
        for(int r=0; r<rCount; r++)
            if(!isValidRow(sdf, cells[r], r))
                return false;
        
        return true;
    }
    
    private SimpleDateFormat getDateFormat() {
        String datePattern = component.getDateFormat();
        if(datePattern == null || datePattern.isEmpty()) {
            showError(Bundle.MSG_ImportClipboardTableWizardPanel_DateFormat_Empty());
            return null;
        }
        
        try {
            return new SimpleDateFormat(datePattern);
        } catch (Exception ex) {
            showError(Bundle.MSG_ImportClipboardTableWizardPanel_DateFormat_Invalid(datePattern));
            return null;
        }
    }
    
    private boolean isValidRow(SimpleDateFormat sdf, String[] row, int rowIndex) {
        int mintWidth = isTriangle? 3 : 2;
        if(row == null || row.length<mintWidth) {
            showError(Bundle.MSG_ImportClipboardTableWizardPanel_Table_EmptyRow(rowIndex+1, mintWidth));
            return false;
        }
        
        int columnIndex = 0;
        Date accident = null;
        Date development;
        double value;
        
        try {
            accident = sdf.parse(row[columnIndex]);
            columnIndex++;
        } catch (Exception ex) {
            showError(Bundle.MSG_ImportClipboardTableWizardPanel_Table_Date_Invalid(row[columnIndex], rowIndex+1, columnIndex+1));
            return false;
        }
        
        try {
            development = sdf.parse(row[columnIndex]);
            columnIndex++;
        } catch (Exception ex) {
            if(isTriangle) {
                showError(Bundle.MSG_ImportClipboardTableWizardPanel_Table_Date_Invalid(row[columnIndex], rowIndex+1, columnIndex+1));
                return false;
            } else {
                development = accident;
            }
        }
        
        try {
            value = tp.parse(row[columnIndex]);
        } catch (Exception ex) {
            showError(Bundle.MSG_ImportClipboardTableWizardPanel_Table_Value_Invalid(row[columnIndex], rowIndex+1, columnIndex+1));
            return false;
        }
        
        MonthDate ad = mdf.toMonthDate(accident);
        MonthDate dd = mdf.toMonthDate(development);
        
        if(!isTriangle) {
            if(!ad.equals(dd)) {
                showError(Bundle.MSG_ImportClipboardTableWizardPanel_Table_VectorDates(rowIndex+1));
                return false;
            }
        }
        
        DataEntry entry = new DataEntry(ad, dd, value);
        int index = entries.indexOf(entry);
        if(index >= 0) {
            showError(Bundle.MSG_ImportClipboardTableWizardPanel_Table_DuplicateRow(index+1, rowIndex+1));
            return false;
        }
        entries.add(entry);
        
        return true;
    }
}
