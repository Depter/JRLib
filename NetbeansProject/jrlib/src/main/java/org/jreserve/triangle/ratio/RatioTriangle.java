package org.jreserve.triangle.ratio;

import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioTriangle extends Triangle {

    public RatioTriangleInput getSourceTriangles();
    
    public ClaimTriangle getSourcePaidTriangle();
    
    public ClaimTriangle getSourceIncurredTriangle();
    
    public double getPperI(int accident, int development);
    
    public double getIperP(int accident, int development);
}
