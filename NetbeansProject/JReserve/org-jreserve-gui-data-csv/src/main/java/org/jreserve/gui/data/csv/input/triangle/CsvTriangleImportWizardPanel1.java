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

package org.jreserve.gui.data.csv.input.triangle;

import java.awt.Component;
import java.io.File;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.inport.ImportDataWizardPanelGeometry;
import org.jreserve.gui.data.csv.input.AbstractCsvReader;
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
    "MSG.CsvTriangleImportWizardPanel1.File.Empty=Input file not set!",
    "# {0} - path",
    "MSG.CsvTriangleImportWizardPanel1.File.NotFound=File ''{0}'' not found!",
    "MSG.CsvTriangleImportWizardPanel1.CellSeparator.Empty=Cell separator not set!",
    "MSG.CsvTriangleImportWizardPanel1.DecimalSeparator.Empty=Decimal separator not set!"
})
public class CsvTriangleImportWizardPanel1 implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private boolean valid;
    private WizardDescriptor wiz;
    private final ChangeSupport cs = new ChangeSupport(this);
    private CsvTriangleImportPanel1 component;
    private volatile AbstractCsvReader.CsvFormatData validationData;
    
    @Override
    public Component getComponent() {
        if(component == null)
            component = new CsvTriangleImportPanel1(this);
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        //TODO store settings
    }

    @Override
    public boolean isValid() {
        return valid;
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
               isDecimalSeparatorValid();
    }
    
    private boolean isFileValid() {
        String path = component.getCsvPath();
        if(path==null || path.length()==0) {
            showError(Bundle.MSG_CsvTriangleImportWizardPanel1_File_Empty());
            return false;
        }
        
        File f = new File(path);
        if(!f.isFile()) {
            showError(Bundle.MSG_CsvTriangleImportWizardPanel1_File_NotFound(path));
            return false;
        }
        return true;
    }
    
    private boolean isCellSeparatorValid() {
        String sep = component.getCellSeparator();
        if(sep==null || sep.length()==0) {
            showError(Bundle.MSG_CsvTriangleImportWizardPanel1_CellSeparator_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isDecimalSeparatorValid() {
        String sep = component.getDecimalSeparator();
        if(sep==null || sep.length()==0) {
            showError(Bundle.MSG_CsvTriangleImportWizardPanel1_DecimalSeparator_Empty());
            return false;
        }
        return true;
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
    public void prepareValidation() {
        validationData = new AbstractCsvReader.CsvFormatData()
            .setCsvFile(new File(component.getCsvPath()))
            .setColumnHeader(component.hasColumnHeader())
            .setRowHeaders(component.hasRowHeader())
            .setCellsQuoted(component.isCellsQuoted())
            .setCellSep(component.getCellSeparator())
            .setDecimalSep(component.getDecimalSeparator().charAt(0));
        component.startProgressBar();
    }

    @Override
    public void validate() throws WizardValidationException {
        try {
            CsvTriangleReader reader = new CsvTriangleReader(validationData);
            double[][] values = reader.read();
            wiz.putProperty(ImportDataWizardPanelGeometry.PROP_TRIANGLE_ARRAY, values);
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
}
