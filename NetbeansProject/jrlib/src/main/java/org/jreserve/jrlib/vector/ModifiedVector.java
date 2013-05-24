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

/**
 * A modified vector represents a vector, which takes
 * an input vector and exposes it's values after some modifications.
 * Examples of modifications can be:
 * -   Exluding cell(s).
 * -   Setting new values to a cell(s).
 * -   Smoothing values within the triangle.
 * 
 * Modified vectors do not change the dimensions of the input, and
 * do not return non <i>NaN</i> values outside the dimensions of the source
 * vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedVector extends Vector {
    
    /**
     * Returns the source vector, which values are being modified by this 
     * instance.
     */
    public Vector getSourceVector();
}
