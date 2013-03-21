package org.jreserve.bootstrap;

import org.jreserve.estimate.Estimate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class Bootstrapper {

    protected final Estimate estimate;
    private final int bootstrapCount;
    
    protected final int devCount;
    protected final int accidents;
    protected final int[] observedDevCount;
    
    protected double [][] reserves;
    
    protected Bootstrapper(Estimate estimate, int bootstrapCount) {
        this.bootstrapCount = bootstrapCount;
        this.reserves = new double[bootstrapCount][];
        
        this.estimate = estimate;
        this.estimate.detach();
        devCount = estimate.getDevelopmentCount();
        accidents = estimate.getAccidentCount();
        observedDevCount = new int[accidents];
        for(int a=0; a<accidents; a++)
            observedDevCount[a] = estimate.getObservedDevelopmentCount(a);
    }
    
    public void run() {
        for(int n=0; n<bootstrapCount; n++)
            reserves[n] = calculatePseudoReserve();
    }
    
    public double[][] getReserves() {
        return reserves;
    }
    
    protected abstract double[] calculatePseudoReserve();
}
