package org.jrlib.bootstrap.odp.scale;

/**
 * This class simply mirrors the fitted {@link OdpRSMethod OdpRSMethod}. 
 * For development periods, where there is no input link ratio returns 
 * NaN.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultOdpRSMethod implements OdpRSMethod {

    private OdpResidualScale scales;
    
    @Override
    public void fit(OdpResidualScale source) {
        this.scales = source;
    }

    @Override
    public double getValue(int development) {
        return scales==null? Double.NaN : scales.getValue(development);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultOdpRSMethod);
    }
    
    @Override
    public int hashCode() {
        return DefaultOdpRSMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultOdpRSMethod";
    }
}
