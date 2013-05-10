package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.estimate.Estimate;

/**
 * Implementation of the {@link MackProcessSimulator MackProcessSimulator}
 * interface, which does not add any process variance to the bootstrapping
 * process.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MackConstantProcessSimulator implements MackProcessSimulator {

    @Override
    public void setEstimate(Estimate estimate) {
    }

    @Override
    public double simulateEstimate(double cad, int accident, int development) {
        return cad;
    }
}
