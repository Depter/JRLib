package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 * Calculates the development factors according to the suggestion of Mack, 
 * where f(d) = sum(0:a)[c(a,d)*c(a,d+1)] / sum(0:a)[c(a,d)^2].
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class Mack1LRMethod extends AbstractLRMethod {

    @Override
    protected double getLinkRatio(DevelopmentFactors factors, int accidents, int dev) {
        Triangle source = factors.getSource();
        
        double ss = 0d;
        double s = 0d;
        for(int a=0; a<accidents; a++) {
            double c = source.getValue(a, dev);
            double cPlus1 = source.getValue(a, dev+1);
            if(!Double.isNaN(c) && !Double.isNaN(cPlus1)) {
                ss += (c * cPlus1);
                s += (c * c);
            }
        }
        return s==0d? Double.NaN : ss/s;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof Mack1LRMethod);
    }
    
    @Override
    public int hashCode() {
        return Mack1LRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "Mack1LRMethod";
    }

}
