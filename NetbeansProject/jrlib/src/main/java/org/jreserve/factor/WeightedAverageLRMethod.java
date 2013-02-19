package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeightedAverageLRMethod extends AbstractLRMethod {

    @Override
    protected double getLinkRatio(Triangle factors, int accidents, int dev) {
        double sum = 0d;
        double sw = 0d;
        
        Triangle source = factors.getSource();
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            double weight = source.getValue(a, dev);
            if(!Double.isNaN(factor) && !Double.isNaN(weight)) {
                sum += (factor * weight);
                sw += weight;
            }
        }
        
        return  (sw != 0d)? sum / sw : Double.NaN;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof WeightedAverageLRMethod);
    }
    
    @Override
    public int hashCode() {
        return WeightedAverageLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "WeightedAverageLRMethod";
    }
}
