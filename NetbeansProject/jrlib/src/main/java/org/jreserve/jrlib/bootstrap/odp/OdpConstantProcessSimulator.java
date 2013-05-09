package org.jreserve.jrlib.bootstrap.odp;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;

/**
 * Implementation of the {@link ProcessSimulator ProcessSimulator} which
 * does not add any process variance, but simply returns the estimated value.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpConstantProcessSimulator implements ProcessSimulator {

    @Override
    public double simulateEstimate(double cad, int accident, int development) {
        return cad;
    }
}
