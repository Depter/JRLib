package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLRMethod implements LinkRatioMethod {
    
    public final static double DEFAULT_MACK_ALPHA = 1d;
    
    protected int developments;
    protected double[] values;
    
    @Override
    public void fit(FactorTriangle factors) {
        int accidents = factors.getAccidentCount();
        developments = factors.getDevelopmentCount();
        
        values = new double[developments];
        for(int d=0; d<developments; d++)
            values[d] = getLinkRatio(factors, accidents, d);
    }

    protected abstract double getLinkRatio(FactorTriangle factors, int accidents, int dev);
    
    @Override
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return values[development];
    }
    
    @Override
    public double getMackAlpha() {
        return DEFAULT_MACK_ALPHA;
    }
}
