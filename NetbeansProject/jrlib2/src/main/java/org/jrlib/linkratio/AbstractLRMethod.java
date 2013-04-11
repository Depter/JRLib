package org.jrlib.linkratio;

import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

/**
 * Base class for calculating link-ratios. The class provides
 * easy access to the {@link FactorTriangle FactorTriangle},
 * {@link ClaimTriangle ClaimTriangle} and to the number
 * of accident periods within the given development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLRMethod implements LinkRatioMethod {
    
    public final static double DEFAULT_MACK_ALPHA = 1d;
    
    protected int developments;
    protected double[] values;
    
    protected FactorTriangle factors;
    protected ClaimTriangle cik;
    
    @Override
    public void fit(FactorTriangle factors) {
        initState(factors);
        int accidents = factors.getAccidentCount();
        for(int d=0; d<developments; d++)
            values[d] = getLinkRatio(accidents, d);
    }
    
    private void initState(FactorTriangle factors) {
        this.factors = factors;
        this.cik = factors.getSourceTriangle();
        developments = factors.getDevelopmentCount();
        values = new double[developments];
    }
    
    /**
     * Extending classes should calculate the link-ratio based on the input.
     */
    protected abstract double getLinkRatio(int accidents, int dev);
    
    /**
     * Returns the link-ratio for the given development period. If
     * the given development period falls outside the bounds, then
     * NaN is returned.
     */
    @Override
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return values[development];
    }
    
    /**
     * Retunrs Mack's alpha parameter for this method.
     * 
     * @see LinkRatioMethod#getMackAlpha().
     */
    @Override
    public double getMackAlpha() {
        return DEFAULT_MACK_ALPHA;
    }

    /**
     * Retunrs the sed weight for the given cell. In general
     *      w(a,d) = c(a,d)^alpha
     * where w(a,d) is the weight for accident period
     * `a` and development period `d` and c(a,d) is the
     * claim for accident period `a` and development period `d`.
     * 
     * If the development factor for the given accident/development
     * period is NaN, then the weight is also NaN.
     * 
     * @see LinkRatioMethod#getWeight(int, int).
     */
    @Override
    public double getWeight(int accident, int development) {
        double fik = factors.getValue(accident, development);
        if(Double.isNaN(fik))
            return Double.NaN;
        double c = cik.getValue(accident, development);
        return Double.isNaN(c)? Double.NaN : Math.pow(c, getMackAlpha());
    }
}
