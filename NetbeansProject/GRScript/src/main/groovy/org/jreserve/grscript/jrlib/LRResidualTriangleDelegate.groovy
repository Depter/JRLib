package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.grscript.util.MapUtil
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangleCorrection
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LRResidualTriangleDelegate implements FunctionProvider {
    
    private MapUtil mapUtil = MapUtil.getInstance();
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.residuals    << this.&residuals
        emc.adjust       << this.&adjust
        emc.exclude      << this.&exclude
    }
    
    LRResidualTriangle residuals(LinkRatioScale scales) {
        return residuals(scales, true)
    }
    
    LRResidualTriangle residuals(LinkRatioScale scales, boolean adjusted) {
        return adjusted?
            new AdjustedLinkRatioResiduals(scales) :
            new LinkRatioResiduals(scales)
    }
    
    LRResidualTriangle adjust(LRResidualTriangle residuals) {
        return new AdjustedLinkRatioResiduals(residuals)
    }
    
    LRResidualTriangle exclude(LRResidualTriangle residuals, int accident, int development) {
        return new LRResidualTriangleCorrection(residuals, accident, development, Double.NaN)
    }
    
    LRResidualTriangle exclude(LRResidualTriangle residuals, Map map) {
        int accident = mapUtil.getAccident(map)
        int development = mapUtil.getDevelopment(map)
        return exclude(residuals, accident, development)
    }
    
    LRResidualTriangle residuals(LinkRatioScale scales, Closure cl) {
        Builder builder = new Builder(scales, this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.residuals
    }
    
    private class Builder {
        private LRResidualTriangle residuals
        private LRResidualTriangleDelegate delegate
        
        Builder(LinkRatioScale scales, LRResidualTriangleDelegate delegate) {
            this.residuals = new LinkRatioResiduals(scales)
            this.delegate = delegate
        }

        void adjust() {
            residuals = new AdjustedLinkRatioResiduals(residuals)
        }
        
        void exclude(int accident, int development) {
            residuals = delegate.exclude(residuals, accident, development)
        }
        
        void exclude(Map map) {
            residuals = delegate.exclude(residuals, map)            
        }
    }
}
