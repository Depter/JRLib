package org.jrlib.triangle.ratio;

import org.jrlib.triangle.AbstractTriangle;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * DefaultRatioTriangle is the basic implementation of the 
 * {@link RatioTriangle RatioTriangle} interface. The class
 * calculates the ratios 'r(a,d)' for acccident period `a`
 * and development period `d` with the formula:
 *               n(a,d)
 *      r(a,d) = ------
 *               d(a,d)
 * where `n(a,d)` and `d(a,d)` are the values form the claim
 * triangles used as numerator and denominator.
 * 
 * If the input triangles have differnet geometries, then the
 * smaller values (numeber of accident periods, number of 
 * development periods) are used.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultRatioTriangle extends AbstractTriangle<RatioTriangleInput> implements RatioTriangle {
//TODO write test
    
    private ClaimTriangle numerator;
    private ClaimTriangle denominator;
    
    private int accidents;
    private int developments;
    private double[][] values;
    
    /**
     * Creates an instance with the given input.
     * 
     * @throws NullPointerException if one of the triangles is null.
     */
    public DefaultRatioTriangle(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(new RatioTriangleInput(numerator, denominator));
    }
    
    /**
     * Creates an instance with the given input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultRatioTriangle(RatioTriangleInput source) {
        super(source);
        this.numerator = source.getSourceNumeratorTriangle();
        this.denominator = source.getSourceDenominatorTriangle();
        doRecalculate();
    }

    /**
     * Returns the source input of this triangle.
     */
    @Override
    public RatioTriangleInput getSourceInput() {
        return source;
    }

    /**
     * Returns the claim tirnalge used as numerator for the
     * calculation.
     * 
     * @see RatioTriangleInput#getSourceNumeratorTriangle().
     */
    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return numerator;
    }

    /**
     * Returns the claim tirnalge used as denominator for the
     * calculation.
     * 
     * @see RatioTriangleInput#getSourceDenominatorTriangle().
     */
    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return denominator;
    }
    
    @Override
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return withinBounds(accident)? values[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                values[accident][development] :
                Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        calculateAccidentBounds();
        calculateDevelopmentBounds();
        
        for(int a=0; a<accidents; a++) {
            double[] vals = values[a];
            int devs = vals.length;
            for(int d=0; d<devs; d++)
                vals[d] = calculateRatio(a, d);
        }
    }
    
    private void calculateAccidentBounds() {
        int a1 = numerator.getAccidentCount();
        int a2 = denominator.getAccidentCount();
        accidents = Math.min(a1, a2);
        values = new double[accidents][];
    }
    
    private void calculateDevelopmentBounds() {
        developments = 0;
        for(int a=0; a<accidents;a++) {
            int d1 = numerator.getDevelopmentCount(a);
            int d2 = denominator.getDevelopmentCount(a);
            int d = Math.min(d1, d2);
            values[a] = new double[d];
            if(a==0)
                developments = d;
        }
    }
    
    private double calculateRatio(int accident, int development) {
        double n = numerator.getValue(accident, development);
        double d = denominator.getValue(accident, development);
        return n / d;
    }
    
    @Override
    public DefaultRatioTriangle copy() {
        return new DefaultRatioTriangle(source.copy());
    }
}
