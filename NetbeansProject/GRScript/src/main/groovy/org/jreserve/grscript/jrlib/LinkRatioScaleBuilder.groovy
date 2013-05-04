package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.scale.ScaleEstimator
import org.jreserve.jrlib.scale.MinMaxScaleEstimator
import org.jreserve.jrlib.scale.ScaleExtrapolation
import org.jreserve.jrlib.scale.ScaleExtrapolation
import org.jreserve.jrlib.scale.UserInputScaleEstimator
import org.jreserve.grscript.util.MapUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioScaleBuilder extends AbstractMethodSelectionBuilder<ScaleEstimator> {
    
    DefaultLinkRatioScaleSelection scales;
    
    LinkRatioScaleBuilder(LinkRatio lrs) {
        scales = new DefaultLinkRatioScaleSelection(lrs)
    }
    
    void minMax(int index) {
        ScaleEstimator estimator = getCachedMethod(MinMaxScaleEstimator.class) {new MinMaxScaleEstimator()}
        scales.setMethod(estimator, index)
    }
    
    void minMax(Collection indices) {
        indices.each(){minMax(it)}
    }
    
    void logLinear(int index) {
        ScaleEstimator estimator = getCachedMethod(ScaleExtrapolation.class) {new ScaleExtrapolation()}
        scales.setMethod(estimator, index)
    }
    
    void logLinear(Collection indices) {
        indices.each(){logLinear(it)}
    }
    
    void fixed(int index, double value) {
        ScaleEstimator estimator = getCachedMethod(UserInputScaleEstimator.class) {new UserInputScaleEstimator()}
        ((UserInputScaleEstimator)estimator).setValue(index, value)
        scales.setMethod(estimator, index)
    }
    
    void fixed(Map map) {
        map.each {index, value -> fixed(index, value)}
    }
}