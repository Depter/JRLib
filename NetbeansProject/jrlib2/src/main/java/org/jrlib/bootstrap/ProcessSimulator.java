package org.jrlib.bootstrap;

/**
 * Instances of this interface can generate process variation for
 * a bootstrapped triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ProcessSimulator {
    
    /**
     * Applies process variation to the value `cad`, at accident 
     * period `a` and development period `d`.
     */
    public double simulateEstimate(double cad, int accident, int development);

}
