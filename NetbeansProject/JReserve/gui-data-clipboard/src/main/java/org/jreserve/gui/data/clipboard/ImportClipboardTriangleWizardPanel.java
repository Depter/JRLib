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
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.inport.ImportDataWizardPanelGeometry;
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
    "MSG.ImportClipboardTriangleWizardPanel.NoData=No data is selected!",
    "# {0} - value",
    "# {1} - row",
    "# {2} - column",
    "MSG.ImportClipboardTriangleWizardPanel.ParseError=Unable to parse value ''{0}'' in cell [{1}, {2}]!"
})
class ImportClipboardTriangleWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {
    
    private final TextParser parser = new TextParser();
    private final ChangeSupport cs = new ChangeSupport(this);
    private ImportClipboardTriangleVisualPanel panel;
    private WizardDescriptor wiz;
    private boolean valid;

    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new ImportClipboardTriangleVisualPanel(this);
        }
        return panel;
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
        if(valid) {
            double[][] values = getTriangle();
            settings.putProperty(ImportDataWizardPanelGeometry.PROP_TRIANGLE_ARRAY, values);
        }
    }

    private double[][] getTriangle() {
        String[][] cells = panel.getValues();
        
        List<double[]> rows = new ArrayList<double[]>();
        for(String[] row : cells)
            rows.add(createRow(row));
        
        int size = rows.size();
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = rows.get(i);
        
        return result;
    }
    
    private double[] createRow(String[] row) {
        List<Double> values = new ArrayList<Double>(row.length);
        for(String cell : row)
            values.add(parse(cell));
        
        int index = values.size()-1;
        while(index >= 0 && values.get(index) == null)
            values.remove(index--);
        
        int size = values.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++) {
            Double v = values.get(i);
            result[i] = v==null? Double.NaN : v.doubleValue();
        }
        
        return result;
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
    
    boolean canParse(String value) {
        return parser.canParse(value);
    }
    
    private Double parse(String value) {
        return parser.parse(value);
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
        String[][] values = panel.getValues();
        int rCount = values.length;
        if(rCount == 0) {
            showError(Bundle.MSG_ImportClipboardTriangleWizardPanel_NoData());
            return false;
        }
        
        int cCount = values[0].length;
        for(int r=0; r<rCount; r++) {
            for(int c=0; c<cCount; c++) {
                String cell = values[r][c];
                if(cell !=null && !canParse(cell)) {
                    showError(Bundle.MSG_ImportClipboardTriangleWizardPanel_ParseError(cell, r+1, c+1));
                    return false;
                }
            }
        }
        return true;
    }
}
