package org.jrlib.bootstrap.odp;

import org.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jrlib.triangle.Cell;
import org.jrlib.triangle.TriangleCorrection;

/**
 * Instances of OdpSRTriangleCorrection modify one cell in
 * their source triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpSRTriangleCorrection extends TriangleCorrection<OdpSRTriangle> implements ModifiedOdpSRTriangle {

    /**
     * Creates an instance for the given source and cell.
     * 
     * @throws NullPointerException if `source` or `cell` is null.
     */
    public OdpSRTriangleCorrection(OdpSRTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    /**
     * Creates an instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public OdpSRTriangleCorrection(OdpSRTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }

    @Override
    public OdpSRTriangle getSourceOdpSRTriangle() {
        return source;
    }

    @Override
    public OdpResidualScale getSourceOdpResidualScales() {
        return source.getSourceOdpResidualScales();
    }
}
