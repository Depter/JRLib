package org.jreserve.jrlib.bootstrap.mcl;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclConstantProcessSimulator implements MclProcessSimulator {
    
    @Override
    public void setBundle(MclBootstrapEstimateBundle bundle) {
    }

    @Override
    public double simulateEstimate(double cad, int accident, int development) {
        return cad;
    }

}
