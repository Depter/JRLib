package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleCorrection;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpScaledResidualTriangleCorrection 
    extends TriangleCorrection<OdpScaledResidualTriangle> 
    implements ModifiedOdpScaledResidualTriangle {
    
    /**
     * Creates a new instance for the given input.
     * 
     * @throws NullPointerException if `source` or `cell` is null.
     */
    public OdpScaledResidualTriangleCorrection(OdpScaledResidualTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    /**
     * Creates a new instance for the given input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public OdpScaledResidualTriangleCorrection(OdpScaledResidualTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }
    
    @Override
    public OdpScaledResidualTriangle getSourceOdpScaledResidualTriangle() {
        return source;
    }

    @Override
    public OdpResidualScale getSourceOdpResidualScales() {
        return source.getSourceOdpResidualScales();
    }

    @Override
    public OdpResidualTriangle getSourceOdpResidualTriangle() {
        return source.getSourceOdpResidualTriangle();
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
