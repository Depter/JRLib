package org.jreserve.bootstrap;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProcessSimulator {
    
    public double simulateEstimate(double cik, int accident, int development);
}
