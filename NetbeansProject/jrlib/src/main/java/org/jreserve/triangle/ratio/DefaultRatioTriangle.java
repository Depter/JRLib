package org.jreserve.triangle.ratio;

import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultRatioTriangle extends AbstractTriangle<RatioTriangleInput> implements RatioTriangle{
    
    public DefaultRatioTriangle(RatioTriangleInput source) {
        super(source);
    }

    @Override
    public RatioTriangleInput getSourceTriangles() {
        return source;
    }
    
    @Override
    public ClaimTriangle getSourcePaidTriangle() {
        return source.getSourcePaidTriangle();
    }
    
    @Override
    public ClaimTriangle getSourceIncurredTriangle() {
        return source.getSourceIncurredTriangle();
    }
    
    @Override
    public int getAccidentCount() {
        return source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return source.getDevelopmentCount(accident);
    }

    @Override
    public double getValue(int accident, int development) {
        return source.getValue(accident, development);
    }
    
    @Override
    public double getPperI(int accident, int development) {
        return getValue(accident, development);
    }
    
    @Override
    public double getIperP(int accident, int development) {
        return 1d / getValue(accident, development);
    }

    @Override
    protected void recalculateLayer() {
    }
}
