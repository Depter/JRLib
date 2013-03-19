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
    private final double[] reserves;
    
    protected final int ultimateDev;
    protected final int accidents;
    protected final int[] lastObserved;
    
    public Bootstrapper(Estimate estimate, int bootstrapCount) {
        this.bootstrapCount = bootstrapCount;
        this.reserves = new double[bootstrapCount];
        
        this.estimate = estimate;
        ultimateDev = estimate.getDevelopmentCount();
        accidents = estimate.getAccidentCount();
        lastObserved = new int[accidents];
        for(int a=0; a<accidents; a++)
            lastObserved[a] = estimate.getObservedDevelopmentCount(a);
    }
    
    public void run() {
        for(int n=0; n<bootstrapCount; n++)
            reserves[n] = calculatePseudoReserve();
    }
    
    protected abstract double calculatePseudoReserve();
}
