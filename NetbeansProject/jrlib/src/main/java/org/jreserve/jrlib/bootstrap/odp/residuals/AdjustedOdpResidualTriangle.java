package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.bootstrap.AbstractAdjustedResidualTriangle;
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
public class AdjustedOdpResidualTriangle 
    extends AbstractAdjustedResidualTriangle<OdpResidualTriangle> 
    implements ModifiedOdpResidualTriangle {
    
    /**
     * Creates an instance for the given link-ratios.
     * 
     * @throws NullPointerException if `source` is null.
     * @see InputOdpResidualTriangle#InputOdpResidualTriangle(LinkRatio) 
     */
    public AdjustedOdpResidualTriangle(LinkRatio lrs) {
        this(new InputOdpResidualTriangle(lrs));
    }
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AdjustedOdpResidualTriangle(OdpResidualTriangle source) {
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
