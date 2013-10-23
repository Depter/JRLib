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
package org.jreserve.jrlib.vector.smoothing;

import java.util.Arrays;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.vector.Vector;

/**
 * Base class for most of the smoothing methods. Extending classes
 * are able to perform smoothing on vector data.
 * 
 * The class will extract the values represented by the input cells. It is
 * not expected that the class is initialized with a sorted array of cells, 
 * but the {@link VectorSmoothingMethod#smooth(double[]) VectorSmoothingMethod.smooth()} 
 * method will be called with sorted values (sorted according to the input cells).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractSmoothing implements VectorSmoothing {
    
    private int cellCount;
    private int[] cells;
    private boolean[] applied;
    private VectorSmoothingMethod method;
    
    /**
     * Creates an instance, which will use the given cells and method.
     * 
     * @throws NullPointerException if 'method' or `cells` or one of 
     * it's element is null.
     */
    public AbstractSmoothing(SmoothingIndex[] cells, VectorSmoothingMethod method) {
        if(cells == null)
            throw new NullPointerException("Cells were null!");
        this.cellCount = cells.length;
        initCells(cells);
        
        if(method == null)
            throw new NullPointerException("Method is null!");
        this.method = method;
    }
    
    private void initCells(SmoothingIndex[] cells) {
        Arrays.sort(cells);
        
        this.cells = new int[cellCount];
        applied = new boolean[cellCount];
        for(int i=0; i<cellCount; i++) {
            this.cells[i] = cells[i].getIndex();
            applied[i] = cells[i].isApplied();
        }
    }
    
    public VectorSmoothingMethod getMethod() {
        return method;
    }
    
    @Override
    public double[] smooth(Vector input) {
        double[] smoothInput = getSmoothedVector(input);
        double[] result = input.toArray();
        smooth(result, smoothInput);
        return result;
    }
    
    private double[] getSmoothedVector(Vector input) {
        double[] values = getValues(input);
        method.smooth(values);
        return values;
    }
    
    private double[] getValues(Vector input) {
        double[] result = new double[cellCount];
        for(int i=0; i<cellCount; i++)
            result[i] = input.getValue(cells[i]);
        return result;
    }
    
    private void smooth(double[] values, double[] smoothInput) {
        for(int i=0; i<cellCount; i++)
            if(isApplied(i, values))
                values[cells[i]] = smoothInput[i];
    }
    
    private boolean isApplied(int i, double[] values) {
        if(!applied[i]) return false;
        int index = cells[i];
        return index >=0 && index < values.length;
    }

    public SmoothingIndex[] getSmoothingCells() {
        SmoothingIndex[] result = new SmoothingIndex[cellCount];
        for(int i=0; i<cellCount; i++)
            result[i] = new SmoothingIndex(cells[i], applied[i]);
        return result;
    }
    
}
