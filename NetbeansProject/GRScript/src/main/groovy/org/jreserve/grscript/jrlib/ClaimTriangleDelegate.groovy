package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.triangle.claim.*
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing

/**
 *
 * @author Peter Decsi
 */
class ClaimTriangleDelegate extends AbstractTriangleDelegate<ClaimTriangle> {
	
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.triangle    << this.&triangle
        emc.cummulate   << this.&cummulate
        emc.corrigate   << this.&corrigate  << {ClaimTriangle t, Map map -> corrigate(t, map)}
        emc.exclude     << this.&exclude    << {ClaimTriangle t, Map map -> exclude(t, map)}
        emc.smooth      << this.&smooth     << {ClaimTriangle t, Closure cl -> smooth(t, cl)}
    }
    
    ClaimTriangle triangle(double[][] data) {
        return new InputClaimTriangle(data)
    }
    
    ClaimTriangle triangle(double[][] data, Closure cl) {
        ClaimTriangle triangle = new InputClaimTriangle(data);
        ClaimTriangleBuilder builder = new ClaimTriangleBuilder(triangle, this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.getTriangle()
    }
    
    @Override
    ClaimTriangle corrigate(ClaimTriangle triangle, int accident, int development, double correction) {
        return new ClaimTriangleCorrection(triangle, accident, development, correction)
    }
    
    @Override
    ClaimTriangle exclude(ClaimTriangle triangle, int accident, int development) {
        return corrigate(triangle, accident, development, Double.NaN)
    }
    
    @Override
    ClaimTriangle smooth(ClaimTriangle triangle, TriangleSmoothing smoothing) {
        return new SmoothedClaimTriangle(triangle, smoothing)
    }
    
    ClaimTriangle cummulate(ClaimTriangle triangle) {
        return new CummulatedClaimTriangle(triangle)
    }
    
    private class ClaimTriangleBuilder extends AbstractTriangleBuilder<ClaimTriangle> {
        
        ClaimTriangleBuilder(ClaimTriangle triangle, ClaimTriangleDelegate delegate) {
            super(triangle, delegate)
        }
        
        void cummulate() {
            triangle = new CummulatedClaimTriangle(triangle)
        }
    }
}

