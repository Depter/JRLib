package org.jreserve.bootstrap.mack;

import org.jreserve.bootstrap.Bootstrapper;
import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.estimate.ChainLadderEstimate;
import org.jreserve.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackBootstrapper extends Bootstrapper<ChainLadderEstimate> {

    private final ProcessSimulator simulator;
    
    public MackBootstrapper(ChainLadderEstimate estimate, int boostrapCount, ProcessSimulator simulator) {
        super(estimate, boostrapCount);
        if(simulator == null)
            throw new NullPointerException("Simulator is null!");
        this.simulator = simulator;
    }

    @Override
    protected double[] calculatePseudoReserve() {
        source.recalculate();
        LinkRatio lr = source.getSourceLinkRatios();
        
        double[] reserve = new double[accidents];
        
        for(int a=0; a<accidents; a++) {
            int firstDev = observedDevCount[a];
            if(0>=firstDev || firstDev >= devCount)
                continue;
            
            double observedUltimate = source.getValue(a, firstDev-1);
            double dik = observedUltimate;
            
            for(int d=firstDev; d<devCount; d++) {
                dik *= lr.getValue(d-1);
                dik = simulator.simulateEstimate(dik, a, d);
            }

            reserve[a] = dik - observedUltimate;
        }
        
        return reserve;        
    }
}
