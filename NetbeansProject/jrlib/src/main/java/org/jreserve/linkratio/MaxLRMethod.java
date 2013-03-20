package org.jreserve.linkratio;

import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MaxLRMethod extends AbstractLRMethod {
    
    @Override
    protected double getLinkRatio(FactorTriangle factors, int accidents, int dev) {
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
    
    @Override
    public MaxLRMethod copy() {
        return new MaxLRMethod();
    }
}
