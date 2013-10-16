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
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.triangle.smoothing.SplineSmoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.SplineSmoothingDialogController.Title=Spline Smoothing",
    "MSG.SplineSmoothingDialogController.Lambda.Empty=Lambda not set!",
    "MSG.SplineSmoothingDialogController.Lambda.NotNumeric=Alpha is not a number!"
})
public abstract class SplineSmoothingDialogController<T extends Triangle> 
    extends AbstractSmoothDialogController<T> {
    
    private List<SmoothRecord> records;
    private double lambda = 0d;
    private boolean isValid = false;
    private double[] original;
    private SplineParamPanel panel;
    
    public SplineSmoothingDialogController(Triangle triangle, TriangleGeometry geometry, List<Cell> cells) {
        super(Bundle.LBL_SplineSmoothingDialogController_Title());
        this.records = SmoothRecord.createRecords(triangle, geometry, cells);
        
        this.original = new double[records.size()];
        for(int i=0; i<original.length; i++)
            original[i] = records.get(i).getOriginal();
    }

    @Override
    public Component getParameterComponent() {
        if(panel == null) {
            panel = new SplineParamPanel(this);
            panel.setLambda(LocaleSettings.getExactString(lambda));
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
    
    private SplineSmoothing createSmoothing() {
        List<SmoothingCell> cells = createCells();
        return new SplineSmoothing(cells.toArray(new SmoothingCell[cells.size()]), lambda);
    }
    
    void changed() {
        isValid = isLambdaValid();
        if(isValid)
            panel.showError(null);
        fireChange();
    }
    
    private boolean isLambdaValid() {
        String str = panel.getLambda();
        if(str == null || str.length() == 0) {
            panel.showError(Bundle.MSG_SplineSmoothingDialogController_Lambda_Empty());
            return false;
        }
        
        Double value = LocaleSettings.toDouble(str);
        if(value == null) {
            panel.showError(Bundle.MSG_SplineSmoothingDialogController_Lambda_NotNumeric());
            return false;
        }
        
        this.lambda = value.doubleValue();
        return true;
    }
    
    protected final double getLambda() {
        return lambda;
    }
}
