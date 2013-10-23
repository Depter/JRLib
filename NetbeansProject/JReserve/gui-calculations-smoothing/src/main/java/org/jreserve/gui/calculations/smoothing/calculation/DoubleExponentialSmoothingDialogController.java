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
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.smoothing.DoubleExponentialSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DoubleExponentialSmoothingDialogController.Title=Double Exponential Smoothing",
    "MSG.DoubleExponentialSmoothingDialogController.Alpha.Empty=Alpha not set!",
    "MSG.DoubleExponentialSmoothingDialogController.Alpha.NotNumeric=Alpha is not a number!",
    "MSG.DoubleExponentialSmoothingDialogController.Alpha.Invalid=Alpha must be within [0; 1]",
    "MSG.DoubleExponentialSmoothingDialogController.Beta.Empty=Beta not set!",
    "MSG.DoubleExponentialSmoothingDialogController.Beta.NotNumeric=Beta is not a number!",
    "MSG.DoubleExponentialSmoothingDialogController.Beta.Invalid=Beta must be within [0; 1]"
})
public abstract class DoubleExponentialSmoothingDialogController<C extends CalculationData> 
    extends AbstractSmoothDialogController<C> {
    
    private List<SmoothRecord> records;
    private double alpha = 0d;
    private double beta = 0d;
    private boolean isValid = false;
    private double[] original;
    private DoubleExponentialParamPanel panel;
    
    public DoubleExponentialSmoothingDialogController(C data, TriangleGeometry geometry, List<Cell> cells) {
        super(Bundle.LBL_DoubleExponentialSmoothingDialogController_Title());
        this.records = SmoothRecord.createRecords(data, geometry, cells);
        
        this.original = new double[records.size()];
        for(int i=0; i<original.length; i++)
            original[i] = records.get(i).getOriginal();
    }
    
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
    
    public void setBeta(double beta) {
        this.beta = beta;
    }

    @Override
    public Component getParameterComponent() {
        if(panel == null) {
            updateRecords(records);
            panel = new DoubleExponentialParamPanel(this);
            panel.setAlpha(LocaleSettings.getExactString(alpha));
            panel.setBeta(LocaleSettings.getExactString(beta));
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
    
    private DoubleExponentialSmoothing createSmoothing() {
        List<SmoothingCell> cells = createCells();
        return new DoubleExponentialSmoothing(cells.toArray(new SmoothingCell[cells.size()]), alpha, beta);
    }
    
    void changed() {
        isValid = isInputValid();
        if(isValid)
            panel.showError(null);
        fireChange();
    }
    
    private boolean isInputValid() {
        return isParamValid(true) && isParamValid(false);
    }
    
    private boolean isParamValid(boolean isAlpha) {
        String str = isAlpha? panel.getAlpha() : panel.getBeta();
        if(str == null || str.length() == 0) {
            String msg = isAlpha? 
                    Bundle.MSG_DoubleExponentialSmoothingDialogController_Alpha_Empty():
                    Bundle.MSG_DoubleExponentialSmoothingDialogController_Beta_Empty();
            panel.showError(msg);
            return false;
        }
        
        Double value = LocaleSettings.toDouble(str);
        if(value == null) {
            String msg = isAlpha?
                    Bundle.MSG_DoubleExponentialSmoothingDialogController_Alpha_NotNumeric() :
                    Bundle.MSG_DoubleExponentialSmoothingDialogController_Beta_NotNumeric();
            panel.showError(msg);
            return false;
        }
        
        double dv = value.doubleValue();
        if(dv < 0d || dv > 1d) {
            String msg = isAlpha?
                    Bundle.MSG_DoubleExponentialSmoothingDialogController_Alpha_Invalid() :
                    Bundle.MSG_DoubleExponentialSmoothingDialogController_Beta_Invalid();
            panel.showError(msg);
            return false;
        }
        
        if(isAlpha) {
            this.alpha = dv;
        } else {
            this.beta = dv;
        }
        return true;
    }
    
    protected final double getAlpha() {
        return alpha;
    }
    
    protected final double getBeta() {
        return beta;
    }
}
