package org.jreserve.factor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLRMethod implements LinkRatioMethod {
    
    @Override
    public double[] getLinkRatios(DevelopmentFactors factors) {
        int accidents = factors.getAccidentCount();
        int devs = factors.getDevelopmentCount();
        
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = getLinkRatio(factors, accidents, d);
        
        return result;
    }

    protected abstract double getLinkRatio(DevelopmentFactors factors, int accidents, int dev);
}
