package org.jrlib.claimratio;

import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangleInput;
import org.jrlib.vector.AbstractVector;

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
public class ClaimRatioCalculator extends AbstractVector<RatioTriangle> implements ClaimRatio {

    private int developments;
    private double[] values;
    
    /**
     * Creates a new instance from the given source.
     * 
     * @see DefaultRatioTriangle#DefaultRatioTriangle(ClaimTriangle, ClaimTriangle)  
     * @throws NullPointerException if one of the parameters is null.
     */
    public ClaimRatioCalculator(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(new DefaultRatioTriangle(numerator, denominator));
    }
    
    /**
     * Creates a new instance from the given source.
     * 
     * @see DefaultRatioTriangle#DefaultRatioTriangle(RatioTriangleInput) 
     * @throws NullPointerException if `source` is null.
     */
    public ClaimRatioCalculator(RatioTriangleInput source) {
        this(new DefaultRatioTriangle(source));
    }
    
    /**
     * Creates a new instance from the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public ClaimRatioCalculator(RatioTriangle source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source;
    }

    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return source.getSourceInput();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
    
    @Override
    public int getLength() {
        return developments;
    }

    public double getValue(int development) {
        return withinBonds(development)? values[development] : Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        ClaimTriangle nik = source.getSourceNumeratorTriangle();
        ClaimTriangle dik = source.getSourceDenominatorTriangle();
        developments = source.getDevelopmentCount();
        values = new double[developments];
        
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
            
            values[dev] = sn / sd;
        }
    }
}