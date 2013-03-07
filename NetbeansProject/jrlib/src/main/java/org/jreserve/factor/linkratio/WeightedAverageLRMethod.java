package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeightedAverageLRMethod extends AbstractLRMethod {

    public final static double MACK_ALPHA = 1d;
    
    @Override
    protected double getLinkRatio(FactorTriangle factors, int accidents, int dev) {
        double sum = 0d;
        double sw = 0d;
        
        Triangle weights = factors.getSourceTriangle();
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            double weight = weights.getValue(a, dev);
            if(!Double.isNaN(factor) && !Double.isNaN(weight)) {
                sum += (factor * weight);
                sw += weight;
            }
        }
        
        return  (sw != 0d)? sum / sw : Double.NaN;
    }

    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
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
