package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.bootstrap.ResidualGenerator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ResidualODPEstimateSimulator implements ProcessSimulator {
    
    private final ResidualGenerator residuals;
    private ODPScaledResidualTriangle odp;
    
    ResidualODPEstimateSimulator(ResidualGenerator residuals, ODPScaledResidualTriangle odpResiduals) {
        this.residuals = residuals;
        this.odp = odpResiduals;
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double r = residuals.getValue(accident, development);
        double scale = odp.getScale(development);
        double adjustment = odp.getAdjustment(development);
        double sqrtCik = Math.sqrt(cik<0d? -cik : cik);
        return r * scale / adjustment * sqrtCik + cik;
    }
}
