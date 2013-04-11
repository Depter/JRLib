package org.jrlib.linkratio;

/**
 * Calculates the link-ratio for development period `d` as
 * simply takin the smallest non NaN development factor from
 * the development period. This method has a Mack-alpha
 * parameter of 1.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MinLRMethod extends AbstractLRMethod {
    
    @Override
    protected double getLinkRatio(int accidents, int dev) {
        double min = Double.NaN;
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            if(Double.isNaN(min) || (!Double.isNaN(factor) && factor < min))
                min = factor;
        }
        return min;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof MinLRMethod);
    }
    
    @Override
    public int hashCode() {
        return MinLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "MinLRMethod";
    }
    
    @Override
    public MinLRMethod copy() {
        return new MinLRMethod();
    }
}