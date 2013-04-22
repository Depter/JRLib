package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.estimate.ChainLadderEstimate;
import org.jreserve.jrlib.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackBootstrapEstimate extends ChainLadderEstimate {

    private final MackProcessSimulator processSimulator;
    
    public MackBootstrapEstimate(LinkRatio lrs, MackProcessSimulator processSimulator) {
        super(lrs);
        this.detach();
        this.processSimulator = processSimulator;
        processSimulator.setEstimate(this);
        simulateProcess();
    }

    @Override
    protected void recalculateLayer() {
        super.recalculateLayer();
        simulateProcess();
    }
    
    private void simulateProcess() {
        for(int a=0; a<accidents; a++)
            for(int d=getObservedDevelopmentCount(a); d<developments; d++)
                values[a][d] = processSimulator.simulateEstimate(values[a][d], a, d);
    }
}
