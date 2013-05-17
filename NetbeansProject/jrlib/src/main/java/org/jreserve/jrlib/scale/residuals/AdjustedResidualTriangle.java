package org.jreserve.jrlib.scale.residuals;

import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * Adjust the residuals for bootstrap-bias. A separate adjustment factor is 
 * calculated for each development period, with the following formula
 * <pre>
 *             n
 *      a^2 = ---, if n > 1
 *            n-1
 *      a = 0, if n <= 1
 * </pre>
 * where 'n' is the number of non NaN values in the development period.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedResidualTriangle<T extends Triangle> 
    extends AbstractTriangleModification<T> {
    
    private double[] adjustments;

    /**
     * Creates an instance with the given source.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public AdjustedResidualTriangle(T source) {
        super(source);
        doRecalculate();
    }
    
    public double getAdjustmentFactor(int development) {
        return 0 <= development && development < getDevelopmentCount()?
                adjustments[development] :
                Double.NaN;
    }
    
    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                source.getValue(accident, development) * adjustments[development] :
                Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        int developments = source.getDevelopmentCount();
        int accidents = source.getAccidentCount();
        adjustments = new double[developments];
        for(int d=0; d<developments; d++)
            recalculateAdjustment(accidents, d);
    }
    
    private void recalculateAdjustment(int accidents, int development) {
        int n = 0;
        for(int a=0; a<accidents; a++)
            if(!Double.isNaN(source.getValue(a, development)))
                n++;
        if(n > 1)
            adjustments[development] = calculateAdjustment(n);
    }
    
    private double calculateAdjustment(double n) {
        return Math.sqrt(n / (n-1));
    }
}
