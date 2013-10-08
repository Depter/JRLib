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
package org.jreserve.gui.calculations.claimtriangle.modifications.smoothing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.gui.calculations.api.smoothing.AbstractSmoothDialogController;
import org.jreserve.gui.calculations.api.smoothing.SmoothRecord;
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
    "LBL.ExponentialSmoothingDialogController.Title=Exponential Smoothing"
})
class ExponentialSmoothingDialogController extends AbstractSmoothDialogController<ExponentialModifier>{
    
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

    @Override
    public Component getParameterComponent() {
        if(panel == null) {
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
    public ExponentialModifier createModifier() {
        return new ExponentialModifier(createCells(), alpha);
    }
    
    private List<SmoothingCell> createCells() {
        List<SmoothingCell> cells = new ArrayList<SmoothingCell>(records.size());
        for(SmoothRecord record : records)
            cells.add(createCell(record));
        return cells;
    }
    
    private SmoothingCell createCell(SmoothRecord record) {
        int accident = record.getAccident();
        int development = record.getDevelopment();
        boolean applied = record.isUsed();
        return new SmoothingCell(accident, development, applied);
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
            panel.showError("Alpha not set!");
            return false;
        }
        
        Double value = LocaleSettings.toDouble(str);
        if(value == null) {
            panel.showError("Alpha is not a number!");
            return false;
        }
        
        double dv = value.doubleValue();
        if(dv < 0d || dv > 1d) {
            panel.showError("Alpha must be within [0; 1]");
            return false;
        }
        
        this.alpha = value.doubleValue();
        return true;
    }
    
}
