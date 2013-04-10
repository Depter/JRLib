package org.jrlib.estimate.mcl;

import org.jrlib.AbstractCalculationData;
import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioResiduals;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioResiduals;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;

/**
 * MclCorrelation calculates the correlation coefficient between
 * the link- and the claim-ratio residuals.
 * 
 * The following formula is used:
 *     corr = sum(f * q) / sum(q * q)
 * where:
 * -   `corr` is the correlation.
 * -   `sum()` mean an iteration for all accident and development periods.
 * -   `f` is the residual for the link-ratio for a given accident and
 *     development period.
 * -   `q` is the residual for the claim-ratio for a given accident and
 *     development period.
 * 
 * If one of `f` or `q` is a `NaN`, than the cell is ignored. If 
 * `sum(q * q)` is 0, or the input is empty, the result is `NaN`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCorrelation extends AbstractCalculationData<MclCorrelationInput> {

    private double correlation;
    
    public MclCorrelation(MclCorrelationInput source) {
        super(source);
        doRecalculate();
    }
    
    /**
     * Returns the input used for the calculation.
     */
    public MclCorrelationInput getSourceInput() {
        return source;
    }
    
    /**
     * Returns the residual triangle for the link-ratios.
     */
    public LinkRatioResiduals getSourceLinkRatioResiduals() {
        return source.getSourceLinkRatioResiduals();
    }
    
    /**
     * Returns the scale parameters for the link-rarios.
     */
    public LinkRatioScale getSourceLinkRatioScaless() {
        return source.getSourceLinkRatioScaless();
    }
    
    /**
     * Returns the link-ratios.
     */
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }
    
    /**
     * Returns the development factors, used to calculate the
     * link-ratios.
     */
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }
    
    /**
     * Returns the claim triangle, used to calculate the link-ratios. The value 
     * returned from this method is the same as the one returned from 
     * {@link #getSourceDenominatorTriangle() getSourceDenominatorTriangle}.
     */
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    /**
     * Returns the residual triangle for the claim-ratios.
     */
    public ClaimRatioResiduals getSourceClaimRatioResiduals() {
        return source.getSourceClaimRatioResiduals();
    }
    
    /**
     * Returns the scale parameters for the claim-rarios.
     */
    public ClaimRatioScale getSourceClaimRatioScales() {
        return source.getSourceClaimRatioScales();
    }
    
    /**
     * Returns the claim-ratios.
     */
    public ClaimRatio getSourceClaimRatios() {
        return source.getSourceClaimRatios();
    }
    
    /**
     * Returns the ratio triangle, used to calculate the
     * claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }
    
    /**
     * Returns the triangle used as denominator for the claim-ratios. The value 
     * returned from this method is the same as the one returned from 
     * {@link #getSourceTriangle() getSourceTriangle}.
     */
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
    
    /**
     * Returns the triangle used as numerator for the claim-ratios.
     */
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }
    
    /**
     * Returns the correlation coefficient.
     */
    public double getValue() {
        return correlation;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        double sn = 0d;
        double sd = 0d;
        
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(accidents);
            for(int d=0; d<devs; d++) {
                double f = source.getLinkRatioResidual(a, d);
                double q = source.getClaimRatioResidual(a, d);
                
                if(!Double.isNaN(f) && !Double.isNaN(q)) {
                    sn += f * q;
                    sd += q * q;
                }
            }
        }
        
        correlation = sd==0d? Double.NaN : sn / sd; 
    }
}