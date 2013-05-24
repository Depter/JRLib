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
package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.CalculationData;

/**
 * This interface represents the needed imput for the 
 * {@link Scale Scale} calculations. 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ScaleInput extends CalculationData {

    /**
     * Returns the number of accidents period for the ratios.
     * 
     * @see Scale r(a,d).
     */
    public int getAccidentCount();
    
    /**
     * Returns the number of development periods for the ratios.
     * 
     * @see Scale r(a,d).
     */
    public int getDevelopmentCount();
    
    /**
     * Returns the number of development periods for the given
     * accident period from the ratios.
     * 
     * @see Scale r(a,d).
     */
    public int getDevelopmentCount(int accident);
    
    /**
     * Returns the estimated ratio for the given development period. If
     * the index is out of bounds, then `Double.NaN` is returned.
     * 
     * @see Scale r(d).
     */
    public double getRatio(int development);

    /**
     * Returns the estimated ratio for the given accident and development 
     * period. If the indices are out of bounds, then `Double.NaN` is 
     * returned.
     * 
     * @see Scale r(a,d).
     */
    public double getRatio(int accident, int development);
    
    /**
     * Returns the estimated ratio for the given accident and development 
     * period. If the indices are out of bounds, then `Double.NaN` is 
     * returned.
     * 
     * @see Scale w(a,d).
     */
    public double getWeight(int accident, int development);
}
