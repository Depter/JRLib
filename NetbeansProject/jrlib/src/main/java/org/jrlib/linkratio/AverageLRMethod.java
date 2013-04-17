package org.jrlib.linkratio;

/**
 * Calculates the link-ratio for a development period as the
 * average of the development factors in the given development 
 * period.
 * 
 * The formula for the link ratio for development period `d`:
 *               sum(f(a,d))
 *      lr(d) = -------------
 *              count(f(a,d))
 * where:
 * -   `f(a,d)` is the development factor for accident period 'a' and 
 *     development period 'd'.
 * -   `count((f(a,d))` is the number of non NaN development factors
 *     in development period `d`.
 * -   if `f(a,d)` is NaN, then the cell is ignored.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageLRMethod extends AbstractLRMethod {

    public final static double MACK_ALPHA = 0d;
    
    @Override
    protected double getLinkRatio(int accidents, int dev) {
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