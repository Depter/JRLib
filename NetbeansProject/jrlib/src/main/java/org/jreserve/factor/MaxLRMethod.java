package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MaxLRMethod extends AbstractLRMethod {
    
    @Override
    protected double getLinkRatio(Triangle factors, Triangle weights, int accidents, int dev) {
        double max = Double.NaN;
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            double weight = weights.getValue(a, dev);
            if(Double.isNaN(max) || (validValue(factor, weight) && factor > max))
                max = factor;
        }
        return max;
    }
    
    private boolean validValue(double factor, double weight) {
        return !Double.isNaN(factor) &&
               !Double.isNaN(weight) &&
               weight != 0d;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof MaxLRMethod);
    }
    
    @Override
    public int hashCode() {
        return MaxLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "MaxLRMethod";
    }
}
