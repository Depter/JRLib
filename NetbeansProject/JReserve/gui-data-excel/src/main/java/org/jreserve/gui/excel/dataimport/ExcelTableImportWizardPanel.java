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

package org.jreserve.gui.excel.dataimport;

import java.awt.Component;
import java.io.File;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
import org.jreserve.gui.poi.read.PoiUtil;
import org.jreserve.gui.poi.read.ReferenceUtil;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataType;
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
    "MSG.ExcelTableImportWizardPanel.Workbook.Empty=Workbook not selected!",
    "MSG.ExcelTableImportWizardPanel.Reference.Empty=Reference is not set!",
    "# {0} - reference",
    "MSG.ExcelTableImportWizardPanel.Reference.Invalid=Reference ''{0}'' is invalid!",
    "MSG.ExcelTableImportWizardPanel.Data.Empty=The specified range is empty!",
    "# {0} - row",
    "# {1} - column",
    "MSG.ExcelTableImportWizardPanel.Data.WrongCell=Illegal cell value in [{0}, {1}]!",
    "# {0} - addres",
    "MSG.ExcelTableImportWizardPanel.Read.DateError=Unable to read date from cell ''{0}''!",
    "# {0} - addres",
    "MSG.ExcelTableImportWizardPanel.Read.NumberError=Unable to read number from cell ''{0}''!",
    "# {0} - r1",
    "# {1} - r0",
    "MSG.ExcelTableImportWizardPanel.Read.Duplicate=Entry in row ''{0}'' is duplicate of row ''{1}''!",
    "MSG.ExcelTableImportWizardPanel.Read.Error=Unable to read file!"
})
class ExcelTableImportWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean valid = false;
    private WizardDescriptor wiz;
    private ExcelTableImportVisualPanel component;
    
    private final Object inputLock = new Object();
    private ValidationInput input;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ExcelTableImportVisualPanel(this);
            if(wiz != null)
                initPanel();
        }
        return component;
    }
    
    private void initPanel() {
        DataSource ds = (DataSource) wiz.getProperty(ImportDataProvider.PROP_DATA_SOURCE);
        component.setVector(DataType.VECTOR == ds.getDataType());
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        if(component != null)
            initPanel();
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
        return component != null &&
               isWorkbookSelected() &&
               isValidReference();
    }
    
    private boolean isWorkbookSelected() {
        if(component.getReferenceUtil() == null) {
            showError(Bundle.MSG_ExcelTableImportWizardPanel_Workbook_Empty());
            return false;
        }
        return true;
    }
    
    private boolean isValidReference() {
        String ref = component.getReference();
        if(ref == null || ref.length()==0) {
            showError(Bundle.MSG_ExcelTableImportWizardPanel_Reference_Empty());
            return false;
        }
        
        ReferenceUtil refUtil = component.getReferenceUtil();
        if(!refUtil.isReferenceValid(ref)) {
            showError(Bundle.MSG_ExcelTableImportWizardPanel_Reference_Invalid(ref));
            return false;
        }
        
        return true;
    }

    @Override
    public void prepareValidation() {
        synchronized(inputLock) {
            input = new ValidationInput();
        }
    }

    @Override
    public void validate() throws WizardValidationException {
        synchronized(inputLock) {
            List<DataEntry> entries;
            try {
                entries = PoiUtil.read(input.file, input.reference, 
                        input.isVector? new VectorDataEntryTableReader() : new TriangleDataEntryTableReader());
            } catch (Exception ex) {
                String msg = "Unable to read data!";
                String lMsg = Bundle.MSG_ExcelTableImportWizardPanel_Read_Error();
                throw new WizardValidationException(component, msg, lMsg);
            }
            
            if(entries.isEmpty()) {
                String msg = "No data is imported!";
                String lMsg = Bundle.MSG_ExcelTableImportWizardPanel_Data_Empty();
                throw new WizardValidationException(component, msg, lMsg);
            }
            
            wiz.putProperty(ImportDataProvider.PROP_IMPORT_DATA, entries);
        }
    }
    
    private class ValidationInput {
        private final File file;
        private final CellReference reference;
        private final boolean isVector;
        
        private ValidationInput() {
            isVector = component.isVector();
            file = new File(component.getFilePath());
            
            String rString = component.getReference();
            ReferenceUtil util = component.getReferenceUtil();
            reference = util.toCellReference(rString);
        }
    }
}
