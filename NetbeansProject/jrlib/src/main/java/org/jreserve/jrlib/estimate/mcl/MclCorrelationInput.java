package org.jreserve.jrlib.estimate.mcl;

import org.jreserve.jrlib.AbstractMultiSourceCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static java.lang.Math.min;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCorrelationInput extends AbstractMultiSourceCalculationData<CalculationData> {

    private LRResidualTriangle lrResiduals;
    private CRResidualTriangle crResiduals;
    
    private int accidents;
    private int[] developments;
    
    /**
     * Creates a new instance from the given input. The link-ratios
     * and the claim-ratios must be based on the same claims triangle 
     * (same object, not equal object).
     * 
     * @throws NullPointerException if one of the input sources is null.
     * @throws IllegalArgumentException if the deiiferent ratios are based on
     * different triangles.
     */
    public MclCorrelationInput(LRResidualTriangle lrResiduals, CRResidualTriangle crResiduals) {
        super(lrResiduals, crResiduals);
        this.lrResiduals = lrResiduals;
        this.crResiduals = crResiduals;
        checkInput();
        doRecalculate();
    }
    
    private void checkInput() {
        ClaimTriangle lrClaims = lrResiduals.getSourceTriangle();
        ClaimTriangle crClaims = crResiduals.getSourceDenominatorTriangle();
        if(lrClaims != crClaims)
            throw unmatchingInputException();
    }
    
    private IllegalArgumentException unmatchingInputException() {
        String msg = "LinkRatio residuals is based on another triangle "+
                "then ClaimRatio residuals! \"%s\" != \"%s\"";
        msg = String.format(msg, lrResiduals, crResiduals);
        return new IllegalArgumentException(msg);
    }
    
    /**
     * Returns the residual triangle for the link-ratios.
     */
    public LRResidualTriangle getSourceLinkRatioResiduals() {
        return lrResiduals;
    }
    
    /**
     * Returns the scale parameters for the link-rarios.
     */
    public LinkRatioScale getSourceLinkRatioScaless() {
        return lrResiduals.getSourceLinkRatioScales();
    }
    
    /**
     * Returns the link-ratios.
     */
    public LinkRatio getSourceLinkRatios() {
        return lrResiduals.getSourceLinkRatios();
    }
    
    /**
     * Returns the development factors, used to calculate the
     * link-ratios.
     */
    public FactorTriangle getSourceFactors() {
        return lrResiduals.getSourceFactors();
    }
    
    /**
     * Returns the claim triangle, used to calculate the link-ratios. The value 
     * returned from this method is the same as the one returned from 
     * {@link #getSourceDenominatorTriangle() getSourceDenominatorTriangle}.
     */
    public ClaimTriangle getSourceTriangle() {
        return lrResiduals.getSourceTriangle();
    }
    
    /**
     * Returns the residual triangle for the claim-ratios.
     */
    public CRResidualTriangle getSourceClaimRatioResiduals() {
        return crResiduals;
    }
    
    /**
     * Returns the scale parameters for the claim-rarios.
     */
    public ClaimRatioScale getSourceClaimRatioScales() {
        return crResiduals.getSourceClaimRatioScales();
    }
    
    /**
     * Returns the claim-ratios.
     */
    public ClaimRatio getSourceClaimRatios() {
        return crResiduals.getSourceClaimRatios();
    }
    
    /**
     * Returns the ratio triangle, used to calculate the
     * claim-ratios.
     */
    public RatioTriangle getSourceRatioTriangle() {
        return crResiduals.getSourceRatioTriangle();
    }
    
    /**
     * Returns the triangle used as denominator for the claim-ratios. The value 
     * returned from this method is the same as the one returned from 
     * {@link #getSourceTriangle() getSourceTriangle}.
     */
    public ClaimTriangle getSourceDenominatorTriangle() {
        return crResiduals.getSourceDenominatorTriangle();
    }
    
    /**
     * Returns the triangle used as numerator for the claim-ratios.
     */
    public ClaimTriangle getSourceNumeratorTriangle() {
        return crResiduals.getSourceNumeratorTriangle();
    }
    
    /**
     * Returns the number of accident periods, where both input triangles
     * has a value.
     */
    public int getAccidentCount() {
        return accidents;
    }
    
    /**
     * Returns the number of development periods within the given accident
     * period where both input triangles has a value.
     */
    public int getDevelopmentCount(int accident) {
        return (0 <= accident && accident < accidents)?
                developments[accident] :
                0;
    }
    
    /**
     * Returns the residual of the link-ratio for the given accident and
     * development periods.
     */
    public double getLinkRatioResidual(int accident, int development) {
        return lrResiduals.getValue(accident, development);
    }
    
    /**
     * Returns the residual of the claim-ratio for the given accident and
     * development periods.
     */
    public double getClaimRatioResidual(int accident, int development) {
        return crResiduals.getValue(accident, development);
    }
    
    /**
     * Recalculates the dimensions.
     * 
     * @see #getAccidentCount() 
     * @see #getDevelopmentCount(int) 
     */
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        recalculateAccidents();
        developments = new int[accidents];
        for(int a=0; a<accidents; a++)
            recalculateDevelopments(a);
    }
    
    private void recalculateAccidents() {
        int lrAccidents = lrResiduals.getAccidentCount();
        int crAccidents = crResiduals.getAccidentCount();
        accidents = min(lrAccidents, crAccidents);
    }

    private void recalculateDevelopments(int accident) {
        int lrDevs = lrResiduals.getDevelopmentCount(accident);
        int crDevs = crResiduals.getDevelopmentCount(accident);
        developments[accident] = min(lrDevs, crDevs);
    }
}
