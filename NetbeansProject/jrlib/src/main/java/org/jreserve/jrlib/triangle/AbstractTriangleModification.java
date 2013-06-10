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

/**
 * Base class for all classes that want to modify a triangle. A modified 
 * triangle has the same dimensions as it's source triangle, but
 * the extending classes can return a modified value for some
 * cells.
 * 
 * A modified triangle should never return a non NaN value outside
 * the bounds of it's source triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleModification<T extends Triangle> extends AbstractTriangle<T> {
    
    /**
     * Creates an instance with the given triangle as source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    protected AbstractTriangleModification(T source) {
        super(source);
    }

    /**
     * Call simply delegated to the source triangle.
     */
    @Override
    public int getAccidentCount() {
        return source.getAccidentCount();
    }

    /**
     * Call simply delegated to the source triangle.
     */
    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    /**
     * Call simply delegated to the source triangle.
     */
    @Override
    public int getDevelopmentCount(int accident) {
        return source.getDevelopmentCount(accident);
    }
    
    /**
     * Call simply delegated to the source triangle.
     */
    @Override
    public double getValue(int accident, int development) {
        return source.getValue(accident, development);
    }
}
