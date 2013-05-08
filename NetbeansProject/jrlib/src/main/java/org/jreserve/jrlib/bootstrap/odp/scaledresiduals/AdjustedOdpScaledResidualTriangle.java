package org.jreserve.jrlib.bootstrap.odp.scaledresiduals;

import org.jreserve.jrlib.bootstrap.AbstractAdjustedResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * Adjusts the input residuals in order to compensate for
 * the bootstrap bias.
 * 
 * The adjustment factor `a` is calculated with the following formula:
 *               N
 *     adj^2 = -----
 *             N - p
 * where:
 * -   `N` is the number of cells, where the residual is not NaN.
 * -   `p` is equals to the number of accident periods in the source
 *     claims triangle, plus the number of development periods in
 *     the source triangle plus 1
 *     ({@link ClaimTriangle#getAccidentCount() cik.getAccidentCount()} + {@link ClaimTriangle#getDevelopmentCount() cik.getDevelopmentCount()} - 1).
* 
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedOdpScaledResidualTriangle
    extends AbstractAdjustedResidualTriangle<OdpScaledResidualTriangle> 
    implements ModifiedOdpScaledResidualTriangle {
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AdjustedOdpScaledResidualTriangle(OdpResidualScale source) {
        this(new DefaultOdpScaledResidualTriangle(source));
    }
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AdjustedOdpScaledResidualTriangle(OdpScaledResidualTriangle source) {
        super(source);
    }

    public OdpScaledResidualTriangle getSourceOdpScaledResidualTriangle() {
        return source;
    }

    public OdpResidualScale getSourceOdpResidualScales() {
        return source.getSourceOdpResidualScales();
    }

    public OdpResidualTriangle getSourceOdpResidualTriangle() {
        return source.getSourceOdpResidualTriangle();
    }

    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    public double getFittedValue(int accident, int development) {
        return source.getFittedValue(accident, development);
    }

    public double[][] toArrayFittedValues() {
        return source.toArrayFittedValues();
    }

}
