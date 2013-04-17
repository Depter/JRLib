package org.jrlib.bootstrap;

import org.jrlib.triangle.AbstractTriangleModification;
import org.jrlib.triangle.Triangle;

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
        super(source);
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
        double n = recalculateN();
        double p = recalculateP();
        adjustment = Math.sqrt(n / (n - p));
    }
    
    /**
     * Calculates the number of residuals in the
     * input triangle.
     */
    protected int recalculateN() {
        int accidents = source.getAccidentCount();
        int n = 0;
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount();
            for(int d=0; d<devs; d++)
                if(!Double.isNaN(source.getValue(a, d)))
                    n++;
        }
        return n;
    }

    /**
     * Calculates the number of parameters in the model.
     * The default implementation is `p = nA + nD - 1`,
     * where `nA` is the number of accident periods
     * and `nD` is teh number of development periods
     * in the source triangle.
     */
    protected int recalculateP() {
        int a = source.getAccidentCount();
        int d = source.getDevelopmentCount();
        return a + d - 1;
    }
}
