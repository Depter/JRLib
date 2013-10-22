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
import org.jreserve.gui.localesettings.LocaleSettings;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.ExponentialSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExponentialSmoothingDialogController.Title=Exponential Smoothing",
    "MSG.ExponentialSmoothingDialogController.Alpha.Empty=Alpha not set!",
    "MSG.ExponentialSmoothingDialogController.Alpha.NotNumeric=Alpha is not a number!",
    "MSG.ExponentialSmoothingDialogController.Alpha.Invalid=Alpha must be within [0; 1]"
})
public abstract class ExponentialSmoothingDialogController<T extends Triangle> 
    extends AbstractSmoothDialogController<T> {
    
    private List<SmoothRecord> records;
    private double alpha = 0d;
    private boolean isValid = false;
    private double[] original;
    private ExponentialParamPanel panel;
    
    public ExponentialSmoothingDialogController(Triangle triangle, TriangleGeometry geometry, List<Cell> cells) {
        super(Bundle.LBL_ExponentialSmoothingDialogController_Title());
        this.records = SmoothRecord.createRecords(triangle, geometry, cells);
        
        this.original = new double[records.size()];
        for(int i=0; i<original.length; i++)
            original[i] = records.get(i).getOriginal();
    }
    
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public Component getParameterComponent() {
        if(panel == null) {
            updateRecords(records);
            panel = new ExponentialParamPanel(this);
            panel.setAlpha(LocaleSettings.getExactString(alpha));
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
    
    private ExponentialSmoothing createSmoothing() {
        List<SmoothingCell> cells = createCells();
        return new ExponentialSmoothing(cells.toArray(new SmoothingCell[cells.size()]), alpha);
    }
    
    void changed() {
        isValid = isAlphaValid();
        if(isValid)
            panel.showError(null);
        fireChange();
    }
    
    private boolean isAlphaValid() {
        String str = panel.getAlpha();
        if(str == null || str.length() == 0) {
            panel.showError(Bundle.MSG_ExponentialSmoothingDialogController_Alpha_Empty());
            return false;
        }
        
        Double value = LocaleSettings.toDouble(str);
        if(value == null) {
            panel.showError(Bundle.MSG_ExponentialSmoothingDialogController_Alpha_NotNumeric());
            return false;
        }
        
        double dv = value.doubleValue();
        if(dv < 0d || dv > 1d) {
            panel.showError(Bundle.MSG_ExponentialSmoothingDialogController_Alpha_Invalid());
            return false;
        }
        
        this.alpha = value.doubleValue();
        return true;
    }
    
    protected final double getAlpha() {
        return alpha;
    }
}
