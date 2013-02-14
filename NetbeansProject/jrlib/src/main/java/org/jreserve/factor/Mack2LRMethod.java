package org.jreserve.factor;

import org.jreserve.triangle.Triangle;

/**
 * Calculates the development factors according to the suggestion of Mack, 
 * where: 
 * <pre>
 *        sum(w(i,k) * C(i,k)^2 * f(i,k))
 * f(d) = -------------------------------
 *            sum(w(i,k) * C(i,k)^2)
 * </pre>
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Mack2LRMethod extends AbstractLRMethod {

    @Override
    protected double getLinkRatio(Triangle factors, Triangle weights, int accidents, int dev) {
        Triangle source = factors.getSource();
        
        double ss = 0d;
        double s = 0d;
        for(int a=0; a<accidents; a++) {
            double w = weights.getValue(a, dev);
            double c2 = Math.pow(source.getValue(a, dev), 2d);
            double f = factors.getValue(a, dev);
            if(!Double.isNaN(c2) && !Double.isNaN(f) && !Double.isNaN(w)) {
                ss += (w * c2 * f);
                s += (w * c2);
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
        return "Mack2LRMethod";
    }

}
