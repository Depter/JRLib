package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.bootstrap.AbstractCenteredResidualTriangle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CenteredOdpResidualTriangle 
    extends AbstractCenteredResidualTriangle<OdpResidualTriangle> 
    implements ModifiedOdpResidualTriangle {

    public CenteredOdpResidualTriangle(OdpResidualTriangle source) {
        super(source);
    }
    
    @Override
    public OdpResidualTriangle getSourceOdpResidualTriangle() {
        return source;
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public double getFittedValue(int accident, int development) {
        return source.getFittedValue(accident, development);
    }

    @Override
    public double[][] toArrayFittedValues() {
        return source.toArrayFittedValues();
    }
}
