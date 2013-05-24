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
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.CenteredOdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.InputOdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangleCorrection
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimResidualDelegate extends AbstractDelegate {
    
    private MapUtil mapUtil = MapUtil.getInstance();
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.residuals    << this.&residuals
        emc.adjust       << this.&adjust
        emc.center       << this.&center
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
    
    OdpResidualTriangle center(OdpResidualTriangle residuals) {
        return new CenteredOdpResidualTriangle(residuals)
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

        void center() {
            residuals = delegate.center(residuals)
        }
        
        void exclude(int accident, int development) {
            residuals = delegate.exclude(residuals, accident, development)
        }
        
        void exclude(Map map) {
            residuals = delegate.exclude(residuals, map)            
        }
    }
}

