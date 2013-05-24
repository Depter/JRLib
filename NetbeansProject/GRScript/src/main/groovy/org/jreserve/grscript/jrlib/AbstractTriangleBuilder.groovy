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
package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.triangle.Triangle
/**
 * Class, that allows the use of builder syntax for triangles. The only difference
 * between de methods of this class, and the methods provided by the
 * {@link AbstractTriangleDelegate AbstractTriangleDelegate} class, is
 * this class already has an instance of a {@link org.jreserve.jrlib.Triangle Triangle)
 * instance, thus it does not have to be provided by the user.
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractTriangleBuilder<T extends Triangle> {
    
    protected T triangle
    private AbstractTriangleDelegate<T> delegate

    AbstractTriangleBuilder(T triangle, AbstractTriangleDelegate<T> delegate) {
        this.triangle = triangle
        this.delegate = delegate
    }
    
    void corrigate(Map map) {
        triangle = delegate.corrigate(triangle, map)
    }
    
    void corrigate(int accident, int development, double correction) {
        triangle = delegate.corrigate(triangle, accident, development, correction)
    }
    
    void exclude(Map map) {
        triangle = delegate.exclude(triangle, map)
    }
    
    void exclude(int accident, int development) {
        triangle = delegate.exclude(triangle, accident, development)
    }
    
    void smooth(Closure cl) {
        triangle = delegate.smooth(triangle, cl)
    }
    
    T getTriangle() {
        return triangle
    }	
}

