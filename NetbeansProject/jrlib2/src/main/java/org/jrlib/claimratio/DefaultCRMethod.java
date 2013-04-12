package org.jrlib.claimratio;

import org.jrlib.triangle.TriangleUtil;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 * Calculates the ratio as the ratio of column totals in the input triangles.
 * 
 * The formula used to calculate the ratio `r(d)` for development period `d` is:
 *             sum(N(a,d))
 *      r(d) = -----------
 *             sum(D(a,d))
 * where:
 * -   `N(a,d)` is the value from the 
 *     {@link RatioTriangle#getSourceNumeratorTriangle() numerator} triangle for
 *     accident period `a` and development period `d`.
 * -   `N(a,d)` is the value from the 
 *     {@link RatioTriangle#getSourceDenominatorTriangle() denominator} triangle for
 *     accident period `a` and development period `d`.
 * -   If either `N(a,d)`, `D(a,d)` or the value from the 
 *     {@link RatioTriangle#getValue(int, int) RatioTriangle} is null, then
 *     the specified cells are ignored.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultCRMethod implements ClaimRatioMethod {

    private int developments;
    private double[] ratios;
    
    @Override
    public void fit(RatioTriangle source) {
        ClaimTriangle nik = source.getSourceNumeratorTriangle();
        ClaimTriangle dik = source.getSourceDenominatorTriangle();
        developments = source.getDevelopmentCount();
        ratios = new double[developments];
        
        int accidents = source.getAccidentCount();
        for(int dev=0; dev<developments; dev++) {
            double sn = 0d;
            double sd = 0d;
            
            for(int a=0; a<accidents; a++) {
                double n = nik.getValue(a, dev);
                double d = dik.getValue(a, dev);
                double r = source.getValue(a, dev);
                if(!Double.isNaN(n) && !Double.isNaN(d) && !Double.isNaN(r)) {
                    sn += n;
                    sd += d;
                }
            }
            
            ratios[dev] = sn / sd;
        }
    }

    @Override
    public double getValue(int development) {
        return (0 <= development && development < developments)?
                ratios[development] : 
                Double.NaN;
    }

    @Override
    public DefaultCRMethod copy() {
        DefaultCRMethod copy = new DefaultCRMethod();
        copy.developments = developments;
        copy.ratios = TriangleUtil.copy(ratios);
        return copy;
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
