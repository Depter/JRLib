package org.jrlib.bootstrap;

/**
 * StaticProcessSimulator applies no variance to it's input values. 
 * Instances of this class can be used by bootstrappers, if they do
 * not wish to include any process variance in the bootstrap process.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class StaticProcessSimulator implements ProcessSimulator {
    
    /**
     * Simply retunrs `cad`.
     */
    @Override
    public double simulateEstimate(double cad, int accident, int development) {
        return cad;
    }
}
