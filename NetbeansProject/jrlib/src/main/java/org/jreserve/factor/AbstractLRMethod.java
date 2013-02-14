package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLRMethod implements LinkRatioMethod {
    
    @Override
    public double[] getLinkRatios(Triangle factors, Triangle weights) {
        int accidents = factors.getAccidentCount();
        int devs = factors.getDevelopmentCount();
        
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = getLinkRatio(factors, weights, accidents, d);
        
        return result;
    }

    protected abstract double getLinkRatio(Triangle factors, Triangle weights, int accidents, int dev);
}
