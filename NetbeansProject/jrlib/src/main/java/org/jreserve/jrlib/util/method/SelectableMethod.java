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

/**
 * A SelectableMethod can be used in situations, when there are more 
 * possible ways to calculate a value. Such situation can be the
 * calculation and smoothing of link-ratios, extrapolating Mack's 
 * scale factors, etc.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface SelectableMethod<T> {
    
    /**
     * Fits the method to the source data. Behaviour, when <i>source</i> is
     * <i>null</i> is undefined.
     */
    public void fit(T source);
    
    /**
     * Returns the calculated value for the given index. This method should
     * not throw any exceptions. If it is not yet initialised, or
     * <i>index</i> falls outside of some bounds, then Double.NaN should
     * be returned.
     */
    public double getValue(int index);
}
