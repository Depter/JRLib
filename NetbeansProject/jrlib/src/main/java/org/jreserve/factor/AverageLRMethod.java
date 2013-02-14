package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageLRMethod extends AbstractLRMethod {

    @Override
    protected double getLinkRatio(Triangle factors, Triangle weights, int accidents, int dev) {
        double sum = 0d;
        double sw = 0d;
    
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            double weight = weights.getValue(a, dev);
            if(!Double.isNaN(factor) && !Double.isNaN(weight)) {
                sum += (weight * factor);
                sw += weight;
            }
        }
        
        return  (sw != 0d)? sum/sw : Double.NaN;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof AverageLRMethod);
    }
    
    @Override
    public int hashCode() {
        return AverageLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "AverageLRMethod";
    }
}
