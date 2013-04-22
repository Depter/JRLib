package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.bootstrap.Bootstrapper;
import org.jreserve.jrlib.estimate.Estimate;

/**
 * Bootstrapper for MCL-bootstrapping. The class keeps trak of both
 * the incurred and paid reserves.
 * 
 * The MCL-bootsrap method extends the Mack-bootstrap method, by including
 * the Paid/Incurred and Incurred/Paid ratios. In order to preserve
 * the correlation between paid and incurred claims, the residuals of the
 * four residual triangle (link-ratios and claim-ratios) are linked together.
 * This means that if cell (1,1) in the paid link-ratio triangle got
 * it's residual from cell(2,3), the all other residuals will get their
 * residuals from the same cell.
 * 
 * @see "Liu, Verrall [2010]: Bootstrap Estimation of the Predictive Distributions of Reserves Using Paid and Incurred Claims, Variance 4:2, 2010, pp. 121-135."
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
    
    /**
     * Creates in instance, which will bootstrap the given source.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws IllegalArgumentException if `bootstrapCount` is less then 1.
     */
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
    
    /**
     * Retunrs the paid pseudo reserves. Modifiying the returned array
     * will change the state of this instance.
     */
    public double[][] getPaidReserves() {
        return paidReserves;
    }
    
    /**
     * Retunrs the incurred pseudo reserves. Modifiying the returned array
     * will change the state of this instance.
     */
    public double[][] getIncurredReserves() {
        return incurredReserves;
    }
}
