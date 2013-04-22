package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.vector.Vector;

/**
 * The class calculates the Bornhuetter-Ferguson reserve estimates. The
 * Bornhuetter-Ferguson method allocates the ultimate amounts, alculated
 * from the expected loss-ratios and exposures to development periods
 * based on the link-ratios.
 * 
 * The formulas to calculate the incremental amount for development
 * period `d`:
 *      I(a,d) = U(a) * q(d),
 *      U(a) = E(a) * ELR(a)
 * where:
 * -    `I(a,d)` is the incremental amount for accident period `a` and
 *      development period `d`.
 * -    `U(a)` is the expected ultimate
 * -    `E(a)` is the exposure for accident period `a`.
 * -    `ELR(a)` is the expected loss-ratio for accident period `a`.
 * -    `q(d)` is the rate of the ultimate amount allocated to development
 *      period `d`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class BornhuetterFergusonEstimate extends AbstractEstimate<LossRatioEstimateInput> {

    private ClaimTriangle ciks;
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public BornhuetterFergusonEstimate(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        this(new LossRatioEstimateInput(lrs, exposure, lossRatio));
    }
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public BornhuetterFergusonEstimate(LossRatioEstimateInput source) {
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
            
            double prev = (devs > 0)? values[a][devs-1] : Double.NaN;
            for(int d=devs; d<developments; d++) {
                prev += ultimates[a] * quotas[d];
                values[a][d] = prev;
            }
        }
    }
    
    private double[] calculateQuotas() {
        double[] quotas = new double[developments];
        
        quotas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = source.getLinkRatio(d);
            quotas[d] = (lr == 0d)? Double.NaN : quotas[d+1] / lr;
            quotas[d+1] = quotas[d+1] - quotas[d];
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