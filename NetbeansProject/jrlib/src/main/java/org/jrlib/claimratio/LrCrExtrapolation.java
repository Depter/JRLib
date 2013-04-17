package org.jrlib.claimratio;

import org.jrlib.linkratio.LinkRatio;

/**
 * Estimates the claim-ratio based on the previous claim ratio and
 * link ratios. If the prevoius claim ratio does not exists, then
 * it is also estimated, with the same method.
 * 
 * The formula to calculate the claim-ratio `cr(d)` for development period
 * `d` is:
 *                         lrN(d-1)
 *       cr(d) = cr(d-1) * --------
 *                         lrD(d-1)
 * where:
 * -   `lrN(d)` is the link-ratio used as numerator for development period `d`.
 * -   `lrD(d)` is the link-ratio used as denominator for development period `d`.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LrCrExtrapolation implements ClaimRatioMethod {

    private LinkRatio num;
    private LinkRatio denom;
    
    private ClaimRatio cr;
    
    /**
     * Creates an instance, which uses the given input as numerator
     * and denominator.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public LrCrExtrapolation(LinkRatio numerator, LinkRatio denominator) {
        if(numerator == null) throw new NullPointerException("Numerator is null!");
        if(denominator == null) throw new NullPointerException("Denominator is null!");
        this.num = numerator;
        this.denom = denominator;
    }
    
    @Override
    public void fit(ClaimRatio source) {
        this.cr = source;
    }

    @Override
    public double getValue(int development) {
        int prevD = development - 1;
        double prev = getPreviousRatio(prevD);
        return prev * num.getValue(prevD) / denom.getValue(prevD);
    }
    
    private double getPreviousRatio(int development) {
        if(development < 0) 
            return Double.NaN;
        double prev = cr.getValue(development);
        return Double.isNaN(prev)? getValue(development) : prev;
    }
}
