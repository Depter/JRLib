package org.jrlib.bootstrap.mcl;

import org.jrlib.bootstrap.Bootstrapper;
import org.jrlib.estimate.Estimate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapper extends Bootstrapper<MclBootstrapEstimateBundle> {
    
    private Estimate paid;
    private Estimate incurred;
    
    protected final int devCount;
    protected final int accidents;
    protected final int[] observedDevCount;
    protected final double [][] paidReserves;
    protected final double [][] incurredReserves;
    private int iteration = 0;

    public MclBootstrapper(MclBootstrapEstimateBundle source, int bootstrapCount) {
        super(source, bootstrapCount);

        devCount = source.getDevelopmentCount();
        accidents = source.getAccidentCount();
        observedDevCount = new int[accidents];
        for(int a=0; a<accidents; a++)
            observedDevCount[a] = source.getObservedDevelopmentCount(a);
        
        paid = source.getPaidEstimate();
        incurred = source.getIncurredEstimate();
        
        paidReserves = new double[bootstrapCount][];
        incurredReserves = new double[bootstrapCount][];
    }
    
    @Override
    protected void bootstrap() {
        source.recalculate();
        paidReserves[iteration] = paid.toArrayReserve();
        incurredReserves[iteration] = incurred.toArrayReserve();
        iteration++;
    }
    
    public double[][] getPaidReserves() {
        return paidReserves;
    }
    
    public double[][] getIncurredReserves() {
        return incurredReserves;
    }
}
