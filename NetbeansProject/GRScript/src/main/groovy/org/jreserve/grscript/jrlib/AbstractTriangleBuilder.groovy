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
 */
class AbstractTriangleBuilder<T extends Triangle> {
    
    private T triangle
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

