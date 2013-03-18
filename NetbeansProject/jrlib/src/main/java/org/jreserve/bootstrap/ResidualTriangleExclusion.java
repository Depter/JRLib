package org.jreserve.bootstrap;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ResidualTriangleExclusion extends ResidualTriangle {

    public void excludeResidual(int accident, int development);
    
    public void includeResidual(int accident, int development);
    
    public boolean isExcluded(int accident, int development);
    
    public ResidualTriangle getSourceResidualTriangle();
}
