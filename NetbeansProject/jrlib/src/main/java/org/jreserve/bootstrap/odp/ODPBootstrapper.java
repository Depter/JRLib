package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.Bootstrapper;
import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.estimate.Estimate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ODPBootstrapper extends Bootstrapper<Estimate> {

    private final ProcessSimulator simulator;
    
    public ODPBootstrapper(Estimate estimate, int boostrapCount, ProcessSimulator simulator) {
        super(estimate, boostrapCount);
        if(simulator == null)
            throw new NullPointerException("Simulator is null!");
        this.simulator = simulator;
    }

    @Override
    protected double[] calculatePseudoReserve() {
        source.recalculate();
        double[][] values = source.toArray();
        double[] reserve = new double[accidents];
        
        for(int a=0; a<accidents; a++) {
            double[] row = values[a];
            double r = 0d;
            int firstDev = observedDevCount[a];
            if(firstDev >= devCount)
                continue;
            
            double prev = firstDev==0? 0d : row[firstDev-1];
            
            for(int d=firstDev; d<devCount; d++) {
                double dik = row[d];
                r += simulator.simulateEstimate(dik - prev, a, d);
                prev = dik;
            }
            reserve[a] = r;
        }
        
        return reserve;
    }
}