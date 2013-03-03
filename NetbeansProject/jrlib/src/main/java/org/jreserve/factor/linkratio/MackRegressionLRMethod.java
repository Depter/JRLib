package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;
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
public class MackRegressionLRMethod extends AbstractLRMethod {

    public final static double MACK_ALPHA = 2d;
    
    @Override
    protected double getLinkRatio(FactorTriangle factors, int accidents, int dev) {
        Triangle weights = factors.getInputTriangle();
        
        double ss = 0d;
        double s = 0d;
        for(int a=0; a<accidents; a++) {
            double c2 = Math.pow(weights.getValue(a, dev), 2d);
            double f = factors.getValue(a, dev);
            if(!Double.isNaN(c2) && !Double.isNaN(f)) {
                ss += (c2 * f);
                s += c2;
            }
        }
        return s==0d? Double.NaN : ss/s;
    }

    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof MackRegressionLRMethod);
    }
    
    @Override
    public int hashCode() {
        return MackRegressionLRMethod.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "Mack2LRMethod";
    }

}
