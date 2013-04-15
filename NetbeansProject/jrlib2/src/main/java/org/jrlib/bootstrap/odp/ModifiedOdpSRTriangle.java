package org.jrlib.bootstrap.odp;

/**
 * Instances of ModifiedOdpSRTriangle modify their source triangles.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedOdpSRTriangle extends OdpSRTriangle {
    
    /**
     * Returns the triangle, modified by this instance.
     */
    public OdpSRTriangle getSourceOdpSRTriangle();
}
