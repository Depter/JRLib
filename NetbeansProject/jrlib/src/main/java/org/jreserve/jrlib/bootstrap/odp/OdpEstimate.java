package org.jreserve.jrlib.bootstrap.odp;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.estimate.ChainLadderEstimate;
import org.jreserve.jrlib.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpEstimate extends ChainLadderEstimate {

    private final ProcessSimulator processSimulator;
    
    public OdpEstimate(LinkRatio lrs, ProcessSimulator processSimulator) {
        super(lrs);
        this.detach();

        if(processSimulator == null)
            throw new NullPointerException("ProcessSimulator is null!");
        this.processSimulator = processSimulator;
        
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
