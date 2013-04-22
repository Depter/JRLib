package org.jreserve.jrlib.linkratio;

/**
 * Calculates the link-ratio for development period `d` as
 * simply takin the largest non NaN development factor from
 * the development period. This method has a Mack-alpha
 * parameter of 1.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MaxLRMethod extends AbstractLRMethod {
    
    @Override
    protected double getLinkRatio(int accidents, int dev) {
        double max = Double.NaN;
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            if(Double.isNaN(max) || (!Double.isNaN(factor) && factor > max))
                max = factor;
        }
        return max;
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
