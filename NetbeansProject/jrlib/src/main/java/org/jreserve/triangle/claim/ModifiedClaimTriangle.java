package org.jreserve.triangle.claim;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedClaimTriangle extends ClaimTriangle {
    
    public ClaimTriangle getSourceTriangle();
    
    @Override
    public ModifiedClaimTriangle copy();
}
