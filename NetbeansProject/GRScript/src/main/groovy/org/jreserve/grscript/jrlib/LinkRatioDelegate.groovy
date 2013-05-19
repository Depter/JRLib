package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.linkratio.SimpleLinkRatio
import org.jreserve.jrlib.linkratio.WeightedAverageLRMethod
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 */
class LinkRatioDelegate extends AbstractDelegate {
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.linkRatio << this.&linkRatio
    }
    
    LinkRatio linkRatio(double[][] data) {
        return linkRatio(new InputClaimTriangle(data))
    }
    
    LinkRatio linkRatio(FactorTriangle factors) {
        return new SimpleLinkRatio(factors, new WeightedAverageLRMethod())
    }
    
    LinkRatio linkRatio(ClaimTriangle triangle) {
        return new SimpleLinkRatio(triangle, new WeightedAverageLRMethod())
    }
    
    LinkRatio linkRatio(double[][] data, Closure cl) {
        return linkRatio(new InputClaimTriangle(data), cl)
    }
    
    LinkRatio linkRatio(ClaimTriangle triangle, Closure cl) {
        return linkRatio(new DevelopmentFactors(triangle), cl)
    }
    
    LinkRatio linkRatio(FactorTriangle factors, Closure cl) {
        LinkRatioBuilder builder = new LinkRatioBuilder(factors)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.getLinkRatios()
    }
}