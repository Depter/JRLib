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
package org.jreserve.jrlib.vector;

import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;

/**
 * An InputVector simply wrapes an array, to be able to use
 * it's values in further calculations.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class InputVector extends AbstractVector<CalculationData> {

    private double[] values;
    
    /**
     * Creates an instance for the given values. If `values` is
     * `null` then an empty vector with length of 0 is created.
     */
    public InputVector(double[] values) {
        initData(values);
    }
    
    private void initData(double[] values) {
        if(values == null || values.length == 0) {
            fillEmptyData();
        } else {
            fillData(values);
        }
    }
    
    private void fillEmptyData() {
        length = 0;
    }
    
    private void fillData(double[] values) {
        length = values.length;
        this.values = new double[length];
        System.arraycopy(values, 0, this.values, 0, length);
    }
    
    /**
     * Allows extending classes to be able to reset their values after
     * initialisation. If `values` is `null` then an empty vector with 
     * length of 0 is created.
     * 
     * Calling this method fires a change event.
     */
    protected void setData(double[] data) {
        setState(CalculationState.INVALID);
        initData(data);
        setState(CalculationState.VALID);
    }

    @Override
    public double getValue(int index) {
        return withinBonds(index)? values[index] : Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
    }
}
