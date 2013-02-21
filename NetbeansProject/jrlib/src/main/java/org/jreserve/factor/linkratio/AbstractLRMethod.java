package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLRMethod implements LinkRatioMethod {
    
    public final static double DEFAULT_MACK_ALPHA = 1d;
    
    @Override
    public double[] getLinkRatios(FactorTriangle factors) {
        int accidents = factors.getAccidentCount();
        int devs = factors.getDevelopmentCount();
        
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = getLinkRatio(factors, accidents, d);
        
        return result;
    }

    protected abstract double getLinkRatio(FactorTriangle factors, int accidents, int dev);
    
    @Override
    public double getMackAlpha() {
        return DEFAULT_MACK_ALPHA;
    }
}
