/*
 * Copyright (C) 2013, Peter Decsi.
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jreserve.jrlib.util.method;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FixedValueMethod<T> extends SelectableMethod<T> {
    
    /**
     * Sets the value for the given index. If there
     * are indices before the given index, for which
     * the value has not been set, then they have
     * the value of `NaN`.
     * 
     * @param index the index of the value.
     * @param value the value to be used.
     */
    public void setValue(int index, double value);
    
    /**
     * Returns the value for the given index or `NaN` if
     * no value has been set yet.
     * 
     * @param index the index of the value.
     * @return the value at the given index.
     */
    public double getValue(int index);
}
