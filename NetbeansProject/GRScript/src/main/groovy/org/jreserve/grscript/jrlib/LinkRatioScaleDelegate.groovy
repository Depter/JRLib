package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale
import org.jreserve.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection
import org.jreserve.jrlib.scale.MinMaxScaleEstimator
import org.jreserve.jrlib.scale.ScaleExtrapolation
import org.jreserve.jrlib.scale.UserInputScaleEstimator
import org.jreserve.jrlib.scale.ScaleEstimator

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioScaleDelegate implements FunctionProvider {
    
    private ScaleEstimatorFactory estimators = ScaleEstimatorFactory.getInstance()
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.scale    << this.&scale
    }
    
    LinkRatioScale scale(LinkRatio lrs) {
        return scale(lrs, "minmax")
    }
    
    LinkRatioScale scale(LinkRatio lrs, String type) {
        ScaleEstimator estimator = estimators.createEstimator(type);
        return new SimpleLinkRatioScale(lrs, estimator)
    }
    
    LinkRatioScale scale(LinkRatio lrs, Closure cl) {
        LinkRatioScaleBuilder builder = new LinkRatioScaleBuilder(lrs)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.scales
    }
}

