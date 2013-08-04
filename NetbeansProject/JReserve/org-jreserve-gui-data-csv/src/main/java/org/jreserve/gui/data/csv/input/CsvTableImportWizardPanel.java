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

package org.jreserve.gui.data.csv.input;

import java.awt.Component;
import java.io.File;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CsvTableImportWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    private CsvTableImportVisualPanel component;
    private boolean valid = false;
    private final ChangeSupport cs = new ChangeSupport(this);
    private WizardDescriptor wiz;
    
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
        return false;
    }
    
    private boolean isFileValid() {
        String path = component.getCsvPath();
        if(path==null || path.length()==0) {
            showError("empty");
            return false;
        }
        
        File f = new File(path);
        if(!f.isFile()) {
            showError("not found");
            return false;
        }
        return true;
    }
    
    private boolean isCellSeparatorValid() {
        return true;
    }
    
    private boolean isDecimalSeparatorValid() {
        return true;
    }
    
    private boolean isDateFormatValid() {
        return true;
    }
    
}
