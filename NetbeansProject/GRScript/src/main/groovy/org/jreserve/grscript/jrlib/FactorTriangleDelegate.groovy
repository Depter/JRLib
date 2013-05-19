package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.triangle.factor.*
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle

/**
 *
 * @author Peter Decsi
 */
class FactorTriangleDelegate extends AbstractTriangleDelegate<FactorTriangle> {
	
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.factors     << this.&factors
        emc.corrigate   << this.&corrigate  << {FactorTriangle t, Map map -> corrigate(t, map)}
        emc.exclude     << this.&exclude    << {FactorTriangle t, Map map -> exclude(t, map)}
        emc.smooth      << this.&smooth     << {FactorTriangle t, Closure cl -> smooth(t, cl)}
    }
    
    FactorTriangle factors(double[][] data) {
        return factors(new InputClaimTriangle(data))
    }
    
    FactorTriangle factors(ClaimTriangle triangle) {
        return new DevelopmentFactors(triangle)
    }
    
    FactorTriangle factors(double[][] data, Closure cl) {
        return factors(new InputClaimTriangle(data), cl)
    }
    
    FactorTriangle factors(ClaimTriangle triangle, Closure cl) {
        FactorTriangle factors = new DevelopmentFactors(triangle)
        AbstractTriangleBuilder<FactorTriangle> builder = new AbstractTriangleBuilder<FactorTriangle>(factors, this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.getTriangle()
    }
    
    @Override
    FactorTriangle corrigate(FactorTriangle factors, int accident, int development, double correction) {
        return new FactorTriangleCorrection(factors, accident, development, correction)
    }
    
    @Override
    FactorTriangle exclude(FactorTriangle factors, int accident, int development) {
        return corrigate(factors, accident, development, Double.NaN)
    }
    
    @Override
    FactorTriangle smooth(FactorTriangle factors, TriangleSmoothing smoothing) {
        return new SmoothedFactorTriangle(factors, smoothing)
    }
}

