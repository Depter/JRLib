package org.jreserve.jrlib.bootstrap;

import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.Triangle;

/**
 * This class centers a residual triangl in such a way, that the
 * mean of the residuals will be 0.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractCenteredResidualTriangle<T extends Triangle> extends AbstractTriangleModification<T> {

    private double mean;
    
    public AbstractCenteredResidualTriangle(T source) {
        super(source);
        doRecalculate();
    }
    
    public double getMean() {
        return mean;
    }
    
    @Override
    public double getValue(int accident, int development) {
        return source.getValue(accident, development) - mean;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        int accidents = source.getAccidentCount();
        mean = 0d;
        int n = 0;
        
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double v = source.getValue(a, d);
                if(!Double.isNaN(v)) {
                    n++;
                    mean += v;
                }
            }
        }
        
        mean = n==0? 0d : mean / (double)n;
    }
}
