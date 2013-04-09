package org.jrlib.estimate.mcl;

import org.jrlib.AbstractMultiSourceCalculationData;
import org.jrlib.CalculationData;
import org.jrlib.claimratio.scale.ClaimRatioResiduals;
import org.jrlib.linkratio.scale.LinkRatioResiduals;
import org.jrlib.triangle.claim.ClaimTriangle;
import static java.lang.Math.min;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCorrelationInput extends AbstractMultiSourceCalculationData<CalculationData> {

    private LinkRatioResiduals lrResiduals;
    private ClaimRatioResiduals crResiduals;
    
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
    public MclCorrelationInput(LinkRatioResiduals lrResiduals, ClaimRatioResiduals crResiduals) {
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
     * Returns the link-ratios.
     */
    public LinkRatioResiduals getSourceLinkRatioResiduals() {
        return lrResiduals;
    }
    
    /**
     * Returns the claim-ratios.
     */
    public ClaimRatioResiduals getSourceClaimRatioResiduals() {
        return crResiduals;
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
