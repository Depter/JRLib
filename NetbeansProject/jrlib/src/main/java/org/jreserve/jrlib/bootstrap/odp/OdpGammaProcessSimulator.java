package org.jreserve.jrlib.bootstrap.odp;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.util.random.RndGamma;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpGammaProcessSimulator implements ProcessSimulator {

    private final RndGamma rnd;
    private final double[] scales;
    private final int scaleCount;
    
    public OdpGammaProcessSimulator(Random rnd, OdpResidualScale scales) {
        this.rnd = new RndGamma(rnd);
        this.scales = scales.toArray();
        scaleCount = this.scales.length;
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double scale = getScale(development);
        double mean = cik<0d? -cik : cik;
        
        double alpha = mean / scale; //mean ^2 / variance;
        double lambda = 1d / scale; //1d / (variance / mean)
        double random = rnd.nextGamma(alpha, lambda);
        
        return cik < 0d? (random + 2 * cik) : random;
    }
    
    private double getScale(int development) {
        if(development < 0)
            development = 0;
        if(development >= scaleCount)
            development = (scaleCount-1);
        return scales[development];
//        return (0<= development && development < scaleCount)?
//                scales[development] :
//                Double.NaN;
    }
}
