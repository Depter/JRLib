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

import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing
import org.jreserve.jrlib.triangle.Triangle
import org.jreserve.grscript.util.MapUtil
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractTriangleDelegate<T extends Triangle> extends AbstractDelegate {
    
    private MapUtil mapUtil = MapUtil.getInstance()
    
    T corrigate(T triangle, Map map) {
        int accident = mapUtil.getAccident(map) 
        int development = mapUtil.getDevelopment(map) 
        double correction = mapUtil.getDouble(map, "correction", "value") 
        return corrigate(triangle, accident, development, correction)
    }
    
    abstract T corrigate(T triangle, int accident, int development, double correction);
    
    T exclude(T triangle, Map map) {
        int accident = mapUtil.getAccident(map) 
        int development = mapUtil.getDevelopment(map) 
        return exclude(triangle, accident, development)
    }
    
    abstract T exclude(T triangle, int accident, int development);
    
    T smooth(T triangle, Closure cl) {
        TriangleSmoothingDelegate delegate = new TriangleSmoothingDelegate()
        cl.delegate = delegate
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return smooth(triangle, delegate.getTriangleSmoothing())
    }
    
    abstract T smooth(T triangle, TriangleSmoothing smoothing);
	
}