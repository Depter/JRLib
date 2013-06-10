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

import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVector<T extends CalculationData> extends AbstractCalculationData<T> implements Vector {
    
    protected int length;
    
    /**
     * Creates a class, with the given source.
     * 
     * @throws NullPointerException when `source` is `null`.
     */
    protected AbstractVector(T source) {
        super(source);
    }
    
    /**
     * Creates an instance, without a source calculation.
     */
    protected AbstractVector() {
    }

    /**
     * Returns `true` if the given index is within the bounds of this vector.
     */
    protected boolean withinBonds(int index) {
        return 0 <= index && index < getLength();
    }
    
    /**
     * Retunrs the length of this vector.
     */
    public int getLength() {
        return length;
    }
    
    @Override
    public double[] toArray() {
        int size = getLength();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = getValue(i);
        return result;
    }

}
