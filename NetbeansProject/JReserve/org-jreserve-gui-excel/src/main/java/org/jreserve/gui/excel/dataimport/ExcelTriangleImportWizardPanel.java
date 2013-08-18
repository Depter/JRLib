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
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.gui.data.api.inport.ImportDataWizardPanelGeometry;
import org.jreserve.gui.poi.read.PoiUtil;
import org.jreserve.gui.poi.read.ReferenceUtil;
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
    "# {0} - addres",
    "MSG.ExcelTriangleImportWizardPanel.Read.NumberError=Unable to read number form cell ''{0}''!",
    "MSG.ExcelTriangleImportWizardPanel.Read.Error=Unable to read file!"
})
public class ExcelTriangleImportWizardPanel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {
    
    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean valid = false;
    private WizardDescriptor wiz;
    private ExcelTriangleImportVisualPanel component;
    
    private final Object inputLock = new Object();
    private ValidationInput input;

    @Override
    public Component getComponent() {
        if(component == null)
            component = new ExcelTriangleImportVisualPanel(this);
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
            double[][] values = null;
            try {
                values = PoiUtil.read(input.file, input.reference, new TriangleTableReader());
            } catch (Exception ex) {
                String msg = "Unable to read data!";
                String lMsg = Bundle.MSG_ExcelTriangleImportWizardPanel_Read_Error();
                throw new WizardValidationException(component, msg, lMsg);
            }
            
            if(values.length == 0) {
                String msg = "No data is imported!";
                String lMsg = Bundle.MSG_ExcelTableImportWizardPanel_Data_Empty();
                throw new WizardValidationException(component, msg, lMsg);
            }
            
            wiz.putProperty(ImportDataWizardPanelGeometry.PROP_TRIANGLE_ARRAY, values);
        }
    }
    
    private class ValidationInput {
        private final File file;
        private final CellReference reference;
    
        private ValidationInput() {
            file = new File(component.getFilePath());
            
            String rString = component.getReference();
            ReferenceUtil util = component.getReferenceUtil();
            reference = util.toCellReference(rString);
        }
    }
}
