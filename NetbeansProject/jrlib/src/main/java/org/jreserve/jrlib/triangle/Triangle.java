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
package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.CalculationData;

/**
 * A triangle represents triangular data.
 * 
 * Each implementation should force the following constraints regarding
 * it's dimensions:
 * 1.  The difference between the length of accident period `i` and
 *     `i+1` should be constant.
 * 2.  Accident period `i` has to be longer than accident period `i+1`.
 * 3.  Evry accident period must contain at least one development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Triangle extends CalculationData {
    
    /**
     * Returns the number of accident periods in the triangle.
     */
    public int getAccidentCount();
    
    /**
     * Retunrs the number of development periods in the triangle. This
     * method should return the same value as <i>getDevelopmentcount(0)</i>.
     */
    public int getDevelopmentCount();
    
    /**
     * Retunrs the number of development periods for the given accident 
     * periods. For accident periods outside of the triangle, this value
     * is 0.
     */
    public int getDevelopmentCount(int accident);
    
    /**
     * Retunrs the value from the given cell. If the input represents a cell
     * outside of this triangle (both lover and upper bounds) then 
     * <i>Double.NaN</i> is returned.
     * 
     * @throws NullPointerException if <i>cell</i> is null.
     */
    public double getValue(Cell cell);
    
    /**
     * Retunrs the value from the given location. If the input represents a 
     * cell outside of this triangle (both lover and upper bounds) then 
     * <i>Double.NaN</i> is returned.
     */
    public double getValue(int accident, int development);
    
    /**
     * Creates an array from the triangle. Modifying the returned array does
     * not affect the inner state of the instance. 
     * 
     * The returned array should have the same dimensions as returned
     * from the {@link #getAccidentCount() getAccidentCount} and
     * {@link #getDevelopmentCount(int) getDevelopmentCount} methods.
     */
    public double[][] toArray();
}