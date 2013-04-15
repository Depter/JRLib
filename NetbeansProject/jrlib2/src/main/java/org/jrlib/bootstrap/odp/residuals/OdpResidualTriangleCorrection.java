package org.jrlib.bootstrap.odp.residuals;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.Cell;
import org.jrlib.triangle.TriangleCorrection;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * Instances of this class modify the value of one cell in an
 * {@link OdpResidualTriangle OdpResidualTriangle}.
 * 
 * @see TriangleCorrection
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpResidualTriangleCorrection 
    extends TriangleCorrection<OdpResidualTriangle> 
    implements ModifiedOdpResidualTriangle {

    public OdpResidualTriangleCorrection(OdpResidualTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }

    public OdpResidualTriangleCorrection(OdpResidualTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    @Override
    public OdpResidualTriangle getSourceOdpResiduals() {
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
}
