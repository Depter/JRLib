package org.jrlib.estimate;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.vector.Vector;

/**
 * The class calculates the Cape-Cod reserve estimates. The Cape-Cod
 * method estimates the expected loss-ratio kappa based on an exposure 
 * and link-ratios. Kappa is calculated as follows:
 *             sum(S(a))
 *      k = ----------------
 *          sum(E(a) * g(a))
 * where:
 * -   `S(a)` is the last observed claim for development period `a`.
 * -   `E(a)` is the exposure for development period `a`.
 * -   `g(a)` is the {@link EstimateUtil#getCompletionRatios(LinkRatio)  completion-ratio} 
 *     for the development period, that belongs to `S(a)`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class CapeCodEstimate extends AbstractEstimate<CapeCodEstimateInput> {

    private ClaimTriangle cik;
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public CapeCodEstimate(LinkRatio lrs, Vector exposure) {
        this(new CapeCodEstimateInput(lrs, exposure));
    }
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public CapeCodEstimate(CapeCodEstimateInput source) {
        super(source);
        cik = source.getSourceLinkRatio().getSourceTriangle();
        doRecalculate();
    }
    
    public CapeCodEstimateInput getSource() {
        return source;
    }
    
    @Override
    public int getObservedDevelopmentCount(int accident) {
        return cik.getDevelopmentCount(accident);
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initCalculation();
        double[] gammas = EstimateUtil.getCompletionRatios(source.getSourceLinkRatio());
        double kappa = getKappa(gammas);
        
        for(int a=0; a<accidents; a++) {
            double ultimate = source.getExposure(a) * kappa;
            int observedDevs = cik.getDevelopmentCount(a);
            
            for(int d=0; d<observedDevs; d++)
                values[a][d] = cik.getValue(a, d);
            
            for(int d=observedDevs; d<developments; d++)
                values[a][d] = (d==(developments-1)? 1d: gammas[d]) * ultimate;
        }
    }
    
    private void initCalculation() {
        accidents = cik.getAccidentCount();
        developments = source.getDevelopmentCount() + 1;
        values = new double[accidents][developments];
    }
    
    private double[] getGammas() {
        double[] gammas = new double[developments];
        gammas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = source.getLinkRatio(d);
            gammas[d] = gammas[d+1] / lr;
        }
        return gammas;
    }
    
    private double getKappa(double[] gammas) {
        int gammaLength = gammas.length;
        double sumS = 0d;
        double sumEG = 0d;
        
        for(int a=0; a<accidents; a++) {
            int dev = cik.getDevelopmentCount(a) - 1;
            
            sumS += cik.getValue(a, dev);
            double gamma = (dev<0)? Double.NaN : dev >= gammaLength? 1d : gammas[dev];
            
            sumEG += (gamma * source.getExposure(a));
        }
        
        return (sumEG == 0d)? Double.NaN : (sumS / sumEG);
    }
}