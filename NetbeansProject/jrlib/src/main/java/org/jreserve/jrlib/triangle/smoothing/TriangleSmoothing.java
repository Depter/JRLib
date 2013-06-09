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
package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.triangle.Triangle;

/**
 * A triangle smoothing can smooth some values within a triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleSmoothing {
    
    /**
     * Retuns the smoothed values for the given triangle. The returned
     * array must have the same dimensions as the input triangle.
     * 
     * @throws NullPointerException if `input` is null.
     * @param input the triangle to smooth.
     * @return the smoothed triangle.
     */
    public double[][] smooth(Triangle input);
    
    /**
     * Returns the cells, which are smoothed, or input for the smoothing.
     */
    public SmoothingCell[] getSmoothingCells();
}
