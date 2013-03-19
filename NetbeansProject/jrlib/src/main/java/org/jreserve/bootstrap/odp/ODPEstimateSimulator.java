package org.jreserve.bootstrap.odp;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ODPEstimateSimulator {
    
    public double simulateEstimate(double cik, int accident, int development);
}
