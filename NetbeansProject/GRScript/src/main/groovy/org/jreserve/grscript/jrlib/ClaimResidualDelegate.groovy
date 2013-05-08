package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.grscript.util.MapUtil
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.InputOdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangleCorrection
import org.jreserve.jrlib.linkratio.LinkRatio

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimResidualDelegate implements FunctionProvider {
    
    private MapUtil mapUtil = MapUtil.getInstance();
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.residuals    << this.&residuals
        emc.adjust       << this.&adjust
        emc.exclude      << this.&exclude
    }
    
    OdpResidualTriangle residuals(LinkRatio lrs) {
        return residuals(lrs, false)
    }
    
    OdpResidualTriangle residuals(LinkRatio lrs, boolean adjust) {
        return adjust?
            new AdjustedOdpResidualTriangle(lrs) :
            new InputOdpResidualTriangle(lrs)
    }
    
    OdpResidualTriangle adjust(OdpResidualTriangle residuals) {
        return new AdjustedOdpResidualTriangle(residuals)
    }
    
    OdpResidualTriangle exclude(OdpResidualTriangle residuals, Map map) {
        int accident = mapUtil.getAccident(map)
        int development = mapUtil.getDevelopment(map)
        return this.exclude(residuals, accident, development)
    }
    
    OdpResidualTriangle exclude(OdpResidualTriangle residuals, int accident, int development) {
        return new OdpResidualTriangleCorrection(residuals, accident, development, Double.NaN)
    }
	
    OdpResidualTriangle residuals(LinkRatio lrs, Closure cl) {
        Builder builder = new Builder(lrs, this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.residuals
    }
    
    private class Builder {
        private OdpResidualTriangle residuals
        private ClaimResidualDelegate delegate
        
        Builder(LinkRatio lrs, ClaimResidualDelegate delegate) {
            this.residuals = new InputOdpResidualTriangle(lrs)
            this.delegate = delegate
        }

        void adjust() {
            residuals = delegate.adjust(residuals)
        }
        
        void exclude(int accident, int development) {
            residuals = delegate.exclude(residuals, accident, development)
        }
        
        void exclude(Map map) {
            residuals = delegate.exclude(residuals, map)            
        }
    }
}

