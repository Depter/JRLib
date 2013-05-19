package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMclProcessSimulator implements MclProcessSimulator {
    
    protected boolean isPaid;
    protected MclBootstrapEstimateBundle bundle;
    private LinkRatioScale scales;
    private double[] originalScales;
    private int[] devIndices;
    
    protected AbstractMclProcessSimulator(LinkRatioScale scales, boolean isPaid) {
        this.isPaid = isPaid;
        this.scales = scales;
        originalScales = scales.toArray();
    }
    
    @Override
    public void setBundle(MclBootstrapEstimateBundle bundle) {
        if(bundle == null)
            throw new NullPointerException("Bundle is null!");
        this.bundle = bundle;
        initDevIndices();
    }
    
    private void initDevIndices() {
        int accidents = bundle.getAccidentCount();
        devIndices = new int[accidents];
        for(int a=0; a<accidents; a++)
            devIndices[a] = bundle.getObservedDevelopmentCount(a);
    }

    public double simulateEstimate(double cik, int accident, int development) {
        double variance = getVariance(accident, development);
        return random(cik, variance);
    }

    protected abstract double getVariance(int accident, int development);
    
    /**
     * Returns the scale parameter for the variance.
     */
    protected double getScale(int accident, int development) {
        if(0 <= development && development <= devIndices[accident])
            return originalScales[development];
        return scales.getValue(development);
    }
    
    /**
     * Extending clases should generate a random value with the given mean
     * and variance.
     */
    protected abstract double random(double mean, double variance);
}
