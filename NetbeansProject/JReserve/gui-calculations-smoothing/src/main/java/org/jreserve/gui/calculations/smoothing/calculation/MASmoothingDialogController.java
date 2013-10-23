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
package org.jreserve.gui.calculations.smoothing.calculation;

import java.awt.Component;
import java.util.List;
import org.jreserve.gui.calculations.smoothing.dialog.AbstractSmoothDialogController;
import org.jreserve.gui.calculations.smoothing.dialog.SmoothRecord;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.smoothing.AbstractVectorSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.MASmoothingDialogController.Length.Empty=Length not set!",
    "MSG.MASmoothingDialogController.Length.NotNumeric=Length is not a number!",
    "# {0} - maxLength",
    "MSG.MASmoothingDialogController.Length.Invalid=Length must be wihin [1; {0}]!"
})
public abstract class MASmoothingDialogController<C extends CalculationData> 
    extends AbstractSmoothDialogController<C> {
    
    private List<SmoothRecord> records;
    private int length = 2;
    private boolean isValid = false;
    private double[] original;
    private MAParamPanel panel;
    
    public MASmoothingDialogController(C data, TriangleGeometry geometry, List<Cell> cells, String title) {
        super(title);
        this.records = SmoothRecord.createRecords(data, geometry, cells);
        
        this.original = new double[records.size()];
        for(int i=0; i<original.length; i++)
            original[i] = records.get(i).getOriginal();
    }
    
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public Component getParameterComponent() {
        if(panel == null) {
            updateRecords(records);
            panel = new MAParamPanel(this);
            panel.setBounds(2, original.length);
        }
        return panel;
    }

    @Override
    public List<SmoothRecord> getRecords() {
        return records;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void updateRecords(List<SmoothRecord> records) {
        double[] smoothed = new double[original.length];
        System.arraycopy(original, 0, smoothed, 0, original.length);
        createSmoothing().getMethod().smooth(smoothed);
        
        for(int i=0; i<smoothed.length; i++)
            records.get(i).setSmoothed(smoothed[i]);
    }
    
    protected abstract AbstractVectorSmoothing createSmoothing();
    
    void changed() {
        isValid = isLengthValid();
        if(isValid)
            panel.showError(null);
        fireChange();
    }
    
    private boolean isLengthValid() {
//        String str = panel.getLength();
//        if(str == null || str.length() == 0) {
//            panel.showError(Bundle.MSG_MASmoothingDialogController_Length_Empty());
//            return false;
//        }
//        
//        int value;
//        try {
//            value = Integer.parseInt(str);
//        } catch (Exception ex) {
//            panel.showError(Bundle.MSG_MASmoothingDialogController_Length_NotNumeric());
//            return false;
//        }
//        
//        if(value < 2 || value > original.length) {
//            panel.showError(Bundle.MSG_MASmoothingDialogController_Length_Invalid(original.length));
//            return false;
//        }
        
        this.length = panel.getLength();
        return true;
    }
    
    protected final int getLength() {
        return length;
    }
    
    protected final SmoothingCell[] createCellsArr() {
        List<SmoothingCell> cells = createCells();
        return cells.toArray(new SmoothingCell[cells.size()]);
    }
}
