package org.jreserve.smoothing;

import org.jreserve.triangle.TriangularCalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageSmoothing implements TriangleSmoothing {

    private final int[][] cells;
    private final boolean[] applied;

    AverageSmoothing(int[][] cells, boolean[] applied) {
        this.cells = cells;
        this.applied = applied;
    }
    
    public double[][] smooth(TriangularCalculationData input) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    

}
