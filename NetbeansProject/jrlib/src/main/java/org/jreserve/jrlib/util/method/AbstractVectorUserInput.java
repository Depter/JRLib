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
package org.jreserve.jrlib.util.method;

import java.util.Arrays;
import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 * This class is the superclass for all implementations of the
 * {@link SelectableMethod SelectableMethod} interface, which 
 * purpose is to enable manual input at a given stage of a 
 * calculation.
 */
public abstract class AbstractVectorUserInput<T> 
    implements FixedValueMethod<T> {
    
    protected double[] values;
    
    /**
     * Creates an empty instance.
     */
    protected AbstractVectorUserInput() {
        this.values = new double[0];
    }
    
    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    protected AbstractVectorUserInput(int index, double value) {
        if(index < 0)
            throw new IllegalArgumentException("Index is less then 0! "+index);
        this.values = new double[0];
        ensureIndexExists(index);
        values[index] = value;
    }
    
    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
    protected AbstractVectorUserInput(double[] values) {
        this.values = TriangleUtil.copy(values);
    }
    
    /**
     * Sets the value for the given index. If there
     * are indices before the given index, for which
     * the value has not been set, then they have
     * the value of `NaN`.
     */
    public void setValue(int index, double value) {
        ensureIndexExists(index);
        values[index] = value;
    }
    
    private void ensureIndexExists(int development) {
        int length = values.length;
        if(length <= development)
            redimValues(development+1);
    }
    
    private void redimValues(int length) {
        int oldLength = values.length;
        double[] redim = new double[length];
        System.arraycopy(values, 0, redim, 0, oldLength);
        values = redim;
        Arrays.fill(values, oldLength, length-1, Double.NaN);
    }
    
    /**
     * Does nothing.
     */
    @Override
    public void fit(T source) {
    }
    
    /**
     * Returns the value for the given index or `NaN` if
     * no value has been set yet.
     */
    @Override
    public double getValue(int index) {
        if(index < 0 || index >= values.length)
            return Double.NaN;
        return values[index];
    }
}
