package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.triangle.Triangle
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing
import org.jreserve.grscript.util.MapUtil

/**
 *
 * @author Peter Decsi
 */
abstract class AbstractTriangleDelegate<T extends Triangle>  {
    
    T corrigate(T triangle, Map map) {
        int accident = MapUtil.getAccident(map) 
        int development = MapUtil.getDevelopment(map) 
        int correction = MapUtil.getDouble(map, "correction", "value") 
        return corrigate(triangle, accident, development, correction)
    }
    
    abstract T corrigate(T triangle, int accident, int development, double correction);
    
    T exclude(T triangle, Map map) {
        int accident = MapUtil.getAccident(map) 
        int development = MapUtil.getDevelopment(map) 
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

