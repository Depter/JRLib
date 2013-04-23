package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.grscript.PrintDelegate
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.linkratio.SimpleLinkRatio
import org.jreserve.jrlib.linkratio.WeightedAverageLRMethod
import org.jreserve.jrlib.triangle.claim.ClaimTriangle

/**
 *
 * @author Peter Decsi
 */
class LinkRatioDelegate implements FunctionProvider {
    
    void initFunctions(ExpandoMetaClass emc) {
        emc.linkRatio << this.&linkRatio
    }
    
    LinkRatio linkRatio(FactorTriangle factors) {
        return new SimpleLinkRatio(factors, new WeightedAverageLRMethod())
    }
    
    LinkRatio linkRatio(ClaimTriangle triangle) {
        return new SimpleLinkRatio(factors, new WeightedAverageLRMethod())
    }
    
    LinkRatio linkRatio(FactorTriangle factors, Closure cl) {
        LinkRatioBuilder builder = new LinkRatioBuilder(factors)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.getLinkRatios()
    }
    
    LinkRatio linkRatio(ClaimTriangle triangle, Closure cl) {
        LinkRatioBuilder builder = new LinkRatioBuilder(factors)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.getLinkRatios()
    }
}