package org.jreserve.bootstrap;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FixedProcessSimulator implements ProcessSimulator {

    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        return cik;
    }

}
