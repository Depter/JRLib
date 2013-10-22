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
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.AbstractVectorSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractRegressionDialogController<T extends Triangle> 
    extends AbstractSmoothDialogController<T> {
    
    private List<SmoothRecord> records;
    private boolean hasIntercept = true;
    private boolean isValid = true;
    private double[] original;
    private RegressionParamPanel panel;
    
    public AbstractRegressionDialogController(String title, Triangle triangle, TriangleGeometry geometry, List<Cell> cells) {
        super(title);
        this.records = SmoothRecord.createRecords(triangle, geometry, cells);
        
        this.original = new double[records.size()];
        for(int i=0; i<original.length; i++)
            original[i] = records.get(i).getOriginal();
    }
    
    public void setHasIntercept(boolean hasIntercept) {
        this.hasIntercept = hasIntercept;
    }

    @Override
    public Component getParameterComponent() {
        if(panel == null) {
            updateRecords(records);
            panel = new RegressionParamPanel(this);
            panel.setHasIntercept(hasIntercept);
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
        hasIntercept = panel.hasIntercept();
        fireChange();
    }
    
    protected final boolean hasIntercept() {
        return hasIntercept;
    }
}
