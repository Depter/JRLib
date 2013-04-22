package org.jreserve.jrlib.bootstrap;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;

/**
 * Dummy implementation of the {@link ProcessSimulator ProcessSimulator}
 * interface, which simply adds 1 to the estimated value.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DummyProcessSimulator implements ProcessSimulator {

    @Override
    public double simulateEstimate(double cad, int accident, int development) {
        return cad + 1d;
    }
}
