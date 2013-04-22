package org.jreserve.jrlib.triangle.claim;

import org.jreserve.jrlib.triangle.AbstractTriangleModification;
import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 * A cummulated claim triangle takes the values of it's source triangle and
 * cummulates them.
 * 
 * @see TriangleUtil#cummulate(double[][]) 
 * @author Peter Decsi
 * @version 1.0
 */
public class CummulatedClaimTriangle extends AbstractTriangleModification<ClaimTriangle> implements ModifiedClaimTriangle {

    private double[][] values;
    
    /**
     * Creates an instance with the given source triangle.
     * 
     * @throws NullPointerException if `source` is null!
     */
    public CummulatedClaimTriangle(ClaimTriangle source) {
        super(source);
        values = source.toArray();
        TriangleUtil.cummulate(values);
    }

    @Override
    public ClaimTriangle getSourceClaimTriangle() {
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
        values = source.toArray();
        TriangleUtil.cummulate(values);
    }
}
