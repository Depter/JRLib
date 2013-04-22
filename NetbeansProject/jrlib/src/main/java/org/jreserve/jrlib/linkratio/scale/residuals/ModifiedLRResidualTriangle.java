package org.jreserve.jrlib.linkratio.scale.residuals;

/**
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedLRResidualTriangle extends LRResidualTriangle {

    /**
     * Returns the triangle, modified by this instance.
     */
    public LRResidualTriangle getSourceLRResidualTriangle();
}
