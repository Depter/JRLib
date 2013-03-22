package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.bootstrap.Random;
import org.jreserve.util.RndGamma;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GammaODPProcessSimulator implements ProcessSimulator {

    private final RndGamma rnd;
    private final ODPScaledResidualTriangle odp;
    
    public GammaODPProcessSimulator(Random rnd, ODPScaledResidualTriangle odpResiduals) {
        this.rnd = new RndGamma(rnd);
        this.odp = odpResiduals;
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double scale = odp.getScale(development);
        double mean = cik<0d? -cik : cik;
        
        double alpha = mean / scale; //mean ^2 / variance;
        double lambda = 1d / scale; //1d / (variance / mean)
        double random = rnd.rndGamma(alpha, lambda);
        
        return cik < 0d? (random + 2 * cik) : random;
    }
}
