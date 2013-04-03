package org.jreserve.triangle.claim;

import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CummulatedClaimTriangle extends AbstractTriangleModification<ClaimTriangle> implements ModifiedClaimTriangle {

    private double[][] values;

    public CummulatedClaimTriangle(ClaimTriangle source) {
        super(source);
        values = (source==null)? new double[0][] : source.toArray();
        TriangleUtil.cummulate(values);
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source;
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        values = (source==null)? new double[0][] : source.toArray();
        TriangleUtil.cummulate(values);
    }
    
    @Override
    public CummulatedClaimTriangle copy() {
        return new CummulatedClaimTriangle(source.copy());
    }
}
