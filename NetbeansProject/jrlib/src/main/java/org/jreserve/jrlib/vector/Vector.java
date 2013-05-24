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

/**
 * Vectors represent data with one dimension.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Vector extends CalculationData {
    
    /**
     * Retunrs the length of this vector.
     */
    public int getLength();
    
    /**
     * Returns the value from the given index. If the index falls outside
     * the dimension of the vector(`0 > index || index >= length`) then
     * <i>Double.NaN</i> is returned.
     */
    public double getValue(int index);
    
    /**
     * Creates an array from the vector. Modifying the returned array does
     * not affect the inner state of the instance. 
     * 
     * The returned array should have the same dimension as returned
     * from {@link #getLength() getLength}.
     */
    public double[] toArray();
}
