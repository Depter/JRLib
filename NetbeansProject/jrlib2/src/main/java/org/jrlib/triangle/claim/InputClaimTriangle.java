package org.jrlib.triangle.claim;

import org.jrlib.triangle.InputTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputClaimTriangle extends InputTriangle implements ClaimTriangle {
    
    public InputClaimTriangle(double[][] data) {
        super(data);
    }
    
    @Override
    public InputClaimTriangle copy() {
        return new InputClaimTriangle(super.toArray());
    }

}
