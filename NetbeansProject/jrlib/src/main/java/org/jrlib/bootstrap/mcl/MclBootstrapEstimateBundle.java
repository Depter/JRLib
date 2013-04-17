package org.jrlib.bootstrap.mcl;

import org.jrlib.bootstrap.mack.MackProcessSimulator;
import org.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jrlib.estimate.mcl.MclCalculationBundle;
import org.jrlib.estimate.mcl.MclEstimateBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapEstimateBundle extends MclEstimateBundle {
    
    private final MclPseudoData pseudoData;
    private final MackProcessSimulator paidSimulator;
    private final MackProcessSimulator incurredSimulator;
    
    public MclBootstrapEstimateBundle(MclCalculationBundle source, MclPseudoData pseudoData, MackProcessSimulator paidProcessSimulator, MackProcessSimulator incurredProcessSimulator) {
        super(source);
        this.detach();
        this.pseudoData = pseudoData;
        paidSimulator = paidProcessSimulator;
        paidSimulator.setEstimate(super.paidProxy);
        
        incurredSimulator = incurredProcessSimulator;
        incurredSimulator.setEstimate(super.incurredProxy);
        simulateProcess();
    }

    @Override
    protected void recalculateLayer() {
        pseudoData.recalculate();
        super.recalculateLayer();
        simulateProcess();
    }
    
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
