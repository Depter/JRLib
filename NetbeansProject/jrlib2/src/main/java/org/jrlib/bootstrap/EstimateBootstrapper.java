package org.jrlib.bootstrap;

import org.jrlib.estimate.Estimate;

/**
 * An isntance of EstimateBootstrapper bootstraps {@link Estimate Estimate}
 * instances. The input estimate should be detached (do not fire event
 * at recalculations), to increas speed.
 * 
 * By each call to {@link #bootstrap() bootstrap()} the class performs two
 * steps:
 * 1.  Call {@link Estimate#recalculate() recalculate()} on the estimate.
 * 2.  Call {@link Estimate#toArrayReserve() toArrayReserve()} on the estimate
 *     to store the reserves for the bootstrap iteration.
 * This means that the provided estimate should take care of calculating the 
 * process variance, parameter variance and other bootstrap related tasks.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class EstimateBootstrapper<T extends Estimate> extends Bootstrapper<T> {
    
    protected final int devCount;
    protected final int accidents;
    protected final int[] observedDevCount;
    protected double [][] reserves;
    private int iteration = 0;
    
    public EstimateBootstrapper(T source, int bootstrapCount) {
        super(source, bootstrapCount);
        
        devCount = source.getDevelopmentCount();
        accidents = source.getAccidentCount();
        observedDevCount = new int[accidents];
        for(int a=0; a<accidents; a++)
            observedDevCount[a] = source.getObservedDevelopmentCount(a);
        reserves = new double[bootstrapCount][];
    }
    
    /**
     * Recalculates the {@link Estimate Estimate} instance and
     * stores the reserves from the iteration.
     */
    @Override
    protected void bootstrap() {
        source.recalculate();
        reserves[iteration++] = source.toArrayReserve();
    }
    
    /**
     * Retunrs the bootstrapped reserve estimates. The returned
     * array has the following dimensions: `reserve[bootstrapCount][accidents]`,
     * where `bootstrapCount` can be obtained from 
     * {@link Bootstrapper#getBootstrapCount() getBootstrapCount()} and
     * `accdents` can be obtained from
     * {@link Estimate#getAccidentCount() source.getAccidentCount()}.
     */
    public double[][] getReserves() {
        return reserves;
    }
}
