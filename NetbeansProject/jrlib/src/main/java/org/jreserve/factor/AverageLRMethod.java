package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageLRMethod extends AbstractLRMethod {

    public final static double MACK_ALPHA = 0d;
    
    @Override
    protected double getLinkRatio(Triangle factors, int accidents, int dev) {
        double sum = 0d;
        int n = 0;
    
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            if(!Double.isNaN(factor)) {
                sum += factor;
                n++;
            }
        }
        
        return  (n != 0)? sum/(double)n : Double.NaN;
    }

    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
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
