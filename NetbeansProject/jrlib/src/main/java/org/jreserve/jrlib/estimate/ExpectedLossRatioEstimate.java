package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.vector.Vector;

/**
 * The class calculates the expected loss-ratio reserve estimates. The 
 * cells of the input claim triangle `C(a,d)` are calculated as:
 *       C(a,d) = E(a) * ELR(a) * g(d)
 * where:
 * -   `C(a,d)` is the estimated claims for accident period `a` and
 *     development period `d`.
 * -   `E(a)` is the exposure for accident period `a`.
 * -   `ELR(a)` is the expected loss-ratio for accident period `a`.
 * -   `g(d)` is the {@link EstimateUtil#getCompletionRatios(LinkRatio) complaetion-ratio}
 *     for development period `d`.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpectedLossRatioEstimate extends AbstractEstimate<LossRatioEstimateInput> {

    private ClaimTriangle ciks;
    
    public ExpectedLossRatioEstimate(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        this(new LossRatioEstimateInput(lrs, exposure, lossRatio));
    }
    
    public ExpectedLossRatioEstimate(LossRatioEstimateInput source) {
        super(source);
        this.ciks = source.getSourceLinkRatio().getSourceTriangle();
        doRecalculate();
    }

    public LossRatioEstimateInput getSource() {
        return source;
    }

    @Override
    public int getObservedDevelopmentCount(int accident) {
        return ciks.getDevelopmentCount(accident);
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initDimensions();
        fillValues();
    }
    
    private void initDimensions() {
        accidents = ciks.getAccidentCount();
        developments = source.getDevelopmentCount()+1;
        values = new double[accidents][developments];
    }
    
    private void fillValues() {
        double[] quotas = calculateQuotas();
        double[] ultimates = calculateUltimates();
        
        for(int a=0; a<accidents; a++) {
            int devs = ciks.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                values[a][d] = ciks.getValue(a, d);
            for(int d=devs; d<developments; d++)
                values[a][d] = ultimates[a] * quotas[d];
        }
    }
    
    private double[] calculateQuotas() {
        double[] quotas = new double[developments];
        
        quotas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = source.getLinkRatio(d);
            quotas[d] = (lr == 0d)? Double.NaN : quotas[d+1] / lr;
        }
        
        return quotas;
    }
    
    private double[] calculateUltimates() {
        double[] ultimates = new double[accidents];
        for(int a=0; a<accidents; a++)
            ultimates[a] = source.getExposure(a) * source.getLossRatio(a);
        return ultimates;
    }
}
