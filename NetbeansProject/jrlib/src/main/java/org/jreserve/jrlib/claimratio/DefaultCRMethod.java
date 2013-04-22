package org.jreserve.jrlib.claimratio;

/**
 * This class simply mirrors the fitted {@link ClaimRatio ClaimRatio}. 
 * For development periods, where there is no input link ratio returns 
 * NaN.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultCRMethod implements ClaimRatioMethod {
    
    private ClaimRatio cr;
    
    @Override
    public void fit(ClaimRatio cr) {
        this.cr = cr;
    }

    @Override
    public double getValue(int development) {
        return cr==null? Double.NaN : cr.getValue(development);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultCRMethod);
    }
    
    @Override
    public int hashCode() {
        return DefaultCRMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultCRMethod";
    }
}
