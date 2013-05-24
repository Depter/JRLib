/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.grscript.util.MapUtil
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals
import org.jreserve.jrlib.linkratio.scale.residuals.CenteredLinkRatioResiduals
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangleCorrection
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LRResidualTriangleDelegate extends AbstractDelegate {
    
    private MapUtil mapUtil = MapUtil.getInstance();
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.residuals    << this.&residuals
        emc.adjust       << this.&adjust
        emc.center       << this.&center
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
    
    LRResidualTriangle center(LRResidualTriangle residuals) {
        return new CenteredLinkRatioResiduals(residuals)
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
        
        void center() {
            residuals = new CenteredLinkRatioResiduals(residuals)
        }
        
        void exclude(int accident, int development) {
            residuals = delegate.exclude(residuals, accident, development)
        }
        
        void exclude(Map map) {
            residuals = delegate.exclude(residuals, map)            
        }
    }
}
