package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
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
        simulateProcess();
    }
    
    public MclEstimateInput getSourceCalculationBundle() {
        return source;
    }

    @Override
    public void recalculate() {
        pseudoData.recalculate();
        super.recalculate();
        simulateProcess();
    }
    
//    @Override
//    protected void recalculateLayer() {
//        pseudoData.recalculate();
//        super.recalculate();
//        simulateProcess();
//    }
    
    private void simulateProcess() {
        for(int a=0; a<accidents; a++)
            for(int d=devIndices[a]; d<developments; d++)
                simulateProcess(a, d);
    }
    
    private void simulateProcess(int a, int d) {
        double cad = paidValues[a][d];
        paidValues[a][d] = paidSimulator.simulateEstimate(cad, a, d);
        
        cad = incurredValues[a][d];
        incurredValues[a][d] = incurredSimulator.simulateEstimate(cad, a, d);
    }
    
}
