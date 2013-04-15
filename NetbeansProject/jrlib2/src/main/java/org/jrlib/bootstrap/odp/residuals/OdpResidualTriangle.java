package org.jrlib.bootstrap.odp.residuals;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.Triangle;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface OdpResidualTriangle extends Triangle {

    public LinkRatio getSourceLinkRatios();
    
    public ClaimTriangle getSourceTriangle();
}
