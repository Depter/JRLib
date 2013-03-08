package org.jreserve.estimate;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CapeCodEstiamte extends AbstractEstimate {
    
    private Vector exposure;
    private LinkRatio lrs;
    private Triangle cik;
    
    public CapeCodEstiamte(LinkRatio lrs, Vector exposure) {
        this.lrs = lrs;
        this.cik = lrs.getSourceFactors().getSourceTriangle();
        this.exposure = exposure;
        attachSources();
        doRecalculate();
    }
    
    private void attachSources() {
        attachSource(lrs);
        attachSource(exposure);
    }

    @Override
    protected void recalculateSource() {
        lrs.recalculate();
        exposure.recalculate();
    }

    @Override
    protected void detachSource() {
        lrs.detach();
        exposure.detach();
    }
    
    @Override
    protected int getObservedDevelopmentCount(int accident) {
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
            double ultimate = exposure.getValue(a) * kappa;
            int observedDevs = cik.getDevelopmentCount(a);
            
            for(int d=0; d<observedDevs; d++)
                values[a][d] = cik.getValue(a, d);
            
            for(int d=observedDevs; d<developments; d++)
                values[a][d] = gammas[d] * ultimate;
        }
    }
    
    private void initCalculation() {
        accidents = cik.getAccidentCount();
        developments = lrs.getDevelopmentCount() + 1;
        values = new double[accidents][developments];
    }
    
    private double[] getGammas() {
        double[] gammas = new double[developments];
        gammas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = lrs.getValue(d);
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
            
            double s = cik.getValue(a, dev);
            
            sumS += cik.getValue(a, dev);
            double gamma = (dev<0)? Double.NaN : dev >= gammaLength? 1d : gammas[dev];
            
            double eg = gamma * exposure.getValue(a);
            sumEG += (gamma * exposure.getValue(a));
        }
        
        return (sumEG == 0d)? Double.NaN : (sumS / sumEG);
    }
}
