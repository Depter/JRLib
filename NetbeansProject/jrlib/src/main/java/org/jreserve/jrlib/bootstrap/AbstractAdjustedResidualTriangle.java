package org.jreserve.jrlib.bootstrap;

import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * Adjusts the input residuals in order to compensate for
 * the bootstrap bias.
 * 
 * The adjustment factor `a` is calculated with the following formula:
 * <pre>
 *               N
 *     adj^2 = -----
 *             N - p
 * </pre>
 * where:
 * -   `N` is the number of cells, where the residual is not NaN.
 * -   `p` is equals to the number of parameters minus 1.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractAdjustedResidualTriangle<T extends Triangle> 
    extends AbstractTriangleModification<T> {
    
    protected double[][] values;
    protected double adjustment;
    
    /**
     * Creates an instance for the given link-ratios.
     * 
     * @throws NullPointerException if `source` is null.
     */
    protected AbstractAdjustedResidualTriangle(T source) {
        this(source, true);
    }
    
    protected AbstractAdjustedResidualTriangle(T source, boolean isAttached) {
        super(source, isAttached);
        doRecalculate();
    }
    
    public double getAdjustment() {
        return adjustment;
    }

    @Override
    public double getValue(int accident, int development) {
        double v = source.getValue(accident, development);
        return Double.isNaN(v)? Double.NaN : adjustment * v;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        int accidents = source.getAccidentCount();
        int devs = source.getDevelopmentCount();
        
        int[] pA = new int[accidents];
        int[] pD = new int[devs];
        
        int n = 0;
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<devs; d++) {
                if(!Double.isNaN(source.getValue(a, d))) {
                    n++;
                    pA[a] = 1;
                    pD[d] = 1;
                }
            }
        }
        
        double p = sum(pA) + sum(pD) - 1;
        double dN = n;
        adjustment = Math.sqrt(dN / (dN - p));
    }
    
    private int sum(int[] arr) {
        int sum = 0;
        for(int i : arr)
            sum += i;
        return sum;
    }
}
