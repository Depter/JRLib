package org.jreserve.factor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageLRMethod extends AbstractLRMethod {

    @Override
    protected double getLinkRatio(DevelopmentFactors factors, int accidents, int dev) {
        double sum = 0d;
        int n = 0;
    
        for(int a=0; a<accidents; a++) {
            double factor = factors.getValue(a, dev);
            if(!Double.isNaN(factor)) {
                sum += factor;
                n++;
            }
        }
        
        return  (n>0)? sum/(double)n : Double.NaN;
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
