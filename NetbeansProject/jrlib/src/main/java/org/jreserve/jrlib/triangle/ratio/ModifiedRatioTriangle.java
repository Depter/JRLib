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
package org.jreserve.jrlib.triangle.ratio;

/**
 * A modified claim-ratio triangle represents a triangle, which takes
 * an input {@link RatioTriangle RatioTriangle} and exposes 
 * it's values after some modifications. Examples of modifications can be:
 * -   Exluding cell(s).
 * -   Setting new values to a cell(s).
 * -   Smoothing values within the triangle.
 * 
 * Modified triangles do not change the dimensions of the source triangle, and
 * do not return non `NaN` values outside the dimensions of the source
 * triangle.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedRatioTriangle extends RatioTriangle {
    
    /**
     * Returns the ratio triangle, being modified by this instance.
     */
    public RatioTriangle getSourceRatioTriangle();
}
