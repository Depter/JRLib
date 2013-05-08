package org.jreserve.jrlib.bootstrap.odp.scaledresiduals;

import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpScaledResidualTriangle extends Triangle {
    
    /**
     * Returns the source, used to scale the input.
     */
    public OdpResidualScale getSourceOdpResidualScales();
    
    /**
     * Retunrs the residuals used to calculate the scales.
     */
    public OdpResidualTriangle getSourceOdpResidualTriangle();
    
    /**
     * Retunrs the link-ratios used to calculate the residuals.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Returns the claims, used to calculate the link-ratios.
     */
    public ClaimTriangle getSourceTriangle();
    
    /**
     * Retunrs the fitted value for the given accident
     * and development period.
     */
    public double getFittedValue(int accident, int development);
    
    /**
     * Returns the fitted values.
     */
    public double[][] toArrayFittedValues();
}
