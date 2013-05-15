package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.estimate.Estimate;
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle;
import org.jreserve.jrlib.estimate.mcl.MclEstimateInput;

/**
 * Utility class for {@link MclBootstrapper MclBootstrapper}. The class
 * wraps an estimate for paid and incurred claims., and after each 
 * recalculation simulates the process error.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapEstimateBundle extends MclEstimateBundle {
    
    private final MclPseudoData pseudoData;
    private final MackProcessSimulator paidSimulator;
    private final MackProcessSimulator incurredSimulator;
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public MclBootstrapEstimateBundle(MclPseudoData pseudoData, MackProcessSimulator paidProcessSimulator, MackProcessSimulator incurredProcessSimulator) {
        super(pseudoData.createPseudoBundle());
        this.detach();
        this.pseudoData = pseudoData;
        paidSimulator = paidProcessSimulator;
        paidSimulator.setEstimate(super.getPaidEstimate());
        
        incurredSimulator = incurredProcessSimulator;
        incurredSimulator.setEstimate(super.getIncurredEstimate());
    }
    
    public MclEstimateInput getSourceCalculationBundle() {
        return source;
    }

    @Override
    public void recalculate() {
        pseudoData.recalculate();
        super.recalculate();
    }
    
    @Override
    protected double calculateIncurred(int a, int d) {
        double value = super.calculateIncurred(a, d);
        if(shouldSimulateProcess(a, d, false))
            value = incurredSimulator.simulateEstimate(value, a, d);
        return value;
    }
    
    private boolean shouldSimulateProcess(int accident, int development, boolean isPaid) {
        MackProcessSimulator s = isPaid? paidSimulator : incurredSimulator;
        Estimate estimate = isPaid? getPaidEstimate() : getIncurredEstimate();
        return s!=null && 
               development >= estimate.getObservedDevelopmentCount(accident);
    }
    
    @Override
    protected double calculatePaid(int a, int d) {
        double value = super.calculatePaid(a, d);
        if(shouldSimulateProcess(a, d, true))
            value = paidSimulator.simulateEstimate(value, a, d);
        return value;
    }
}
