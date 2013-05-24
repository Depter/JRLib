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
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals
import org.jreserve.jrlib.claimratio.scale.residuals.AdjustedClaimRatioResiduals
import org.jreserve.jrlib.claimratio.scale.residuals.CenteredClaimRatioResiduals
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CRResidualTriangleDelegate extends AbstractDelegate {
    
    private MapUtil mapUtil = MapUtil.getInstance();
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.residuals    << this.&residuals
        emc.adjust       << this.&adjust
        emc.center       << this.&center
        emc.exclude      << this.&exclude
    }
    
    CRResidualTriangle residuals(ClaimRatioScale scales) {
        return residuals(scales, true)
    }
    
    CRResidualTriangle residuals(ClaimRatioScale scales, boolean adjusted) {
        return adjusted?
            new AdjustedClaimRatioResiduals(scales) :
            new ClaimRatioResiduals(scales)
    }
    
    CRResidualTriangle adjust(CRResidualTriangle residuals) {
        return new AdjustedClaimRatioResiduals(residuals)
    }
    
    CRResidualTriangle center(CRResidualTriangle residuals) {
        return new CenteredClaimRatioResiduals(residuals)
    }
    
    CRResidualTriangle exclude(CRResidualTriangle residuals, int accident, int development) {
        return new ClaimRatioResidualTriangleCorrection(residuals, accident, development, Double.NaN)
    }
    
    CRResidualTriangle exclude(CRResidualTriangle residuals, Map map) {
        int accident = mapUtil.getAccident(map)
        int development = mapUtil.getDevelopment(map)
        return exclude(residuals, accident, development)
    }
    
    CRResidualTriangle residuals(ClaimRatioScale scales, Closure cl) {
        Builder builder = new Builder(scales, this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.residuals
    }
    
    private class Builder {
        private CRResidualTriangle residuals
        private CRResidualTriangleDelegate delegate
        
        Builder(ClaimRatioScale scales, CRResidualTriangleDelegate delegate) {
            this.residuals = new ClaimRatioResiduals(scales)
            this.delegate = delegate
        }

        void adjust() {
            residuals = new AdjustedClaimRatioResiduals(residuals)
        }
        
        void center() {
            residuals = new CenteredClaimRatioResiduals(residuals)
        }
        
        void exclude(int accident, int development) {
            residuals = delegate.exclude(residuals, accident, development)
        }
        
        void exclude(Map map) {
            residuals = delegate.exclude(residuals, map)            
        }
    }
}
