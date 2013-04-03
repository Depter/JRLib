package org.jreserve.estimate;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CapeCodEstiamte extends AbstractEstimate<CapeCodEstimateInput> {

    private ClaimTriangle cik;
    
    public CapeCodEstiamte(LinkRatio lrs, Vector exposure) {
        this(new CapeCodEstimateInput(lrs, exposure));
    }
    
    public CapeCodEstiamte(CapeCodEstimateInput source) {
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
        double[] gammas = getGammas();
        double kappa = getKappa(gammas);
        
        for(int a=0; a<accidents; a++) {
            double ultimate = source.getExposure(a) * kappa;
            int observedDevs = cik.getDevelopmentCount(a);
            
            for(int d=0; d<observedDevs; d++)
                values[a][d] = cik.getValue(a, d);
            
            for(int d=observedDevs; d<developments; d++)
                values[a][d] = gammas[d] * ultimate;
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
    
    @Override
    public CapeCodEstiamte copy() {
        return new CapeCodEstiamte(source.copy());
    }
}
