package org.jreserve.jrlib.linkratio.scale.residuals;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleCorrection;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 * Instances of LRResidualTriangleCorrection modifiying one value
 * of their source triangle, without modifying it's dimensions.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LRResidualTriangleCorrection 
    extends TriangleCorrection<LRResidualTriangle>
    implements ModifiedLRResidualTriangle {

    /**
     * Creates a new instance.
     * 
     * @throws NullPointerException if `source` or `cell` is null.
     */
    public LRResidualTriangleCorrection(LRResidualTriangle source, Cell cell, double correction) {
        super(source, cell, correction);
    }

    /**
     * Creates a new instance.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public LRResidualTriangleCorrection(LRResidualTriangle source, int accident, int development, double correction) {
        super(source, accident, development, correction);
    }

    
    @Override
    public LRResidualTriangle getSourceLRResidualTriangle() {
        return source;
    }

    @Override
    public LinkRatioScale getSourceLinkRatioScales() {
        return source.getSourceLinkRatioScales();
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
}