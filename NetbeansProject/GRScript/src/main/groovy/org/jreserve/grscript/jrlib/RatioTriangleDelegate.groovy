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
import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle
import org.jreserve.jrlib.triangle.ratio.ModifiedRatioTriangle
import org.jreserve.jrlib.triangle.ratio.RatioTriangle
import org.jreserve.jrlib.triangle.ratio.RatioTriangleCorrection
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput
import org.jreserve.jrlib.triangle.ratio.SmoothedRatioTriangle
import org.jreserve.grscript.util.MapUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class RatioTriangleDelegate extends AbstractTriangleDelegate<RatioTriangle> {
	
    private MapUtil mapUtil = MapUtil.getInstance()
    
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.ratioTriangle    << this.&ratioTriangle
        emc.corrigate   << this.&corrigate  << {RatioTriangle t, Map map -> corrigate(t, map)}
        emc.exclude     << this.&exclude    << {RatioTriangle t, Map map -> exclude(t, map)}
        emc.smooth      << this.&smooth     << {RatioTriangle t, Closure cl -> smooth(t, cl)}
    }
    
    RatioTriangle ratioTriangle(ClaimTriangle numerator, ClaimTriangle denominator) {
        return new DefaultRatioTriangle(numerator, denominator)
    }
    
    RatioTriangle ratioTriangle(Map map) {
        ClaimTriangle numerator = mapUtil.getValue(map, "numerator", "num", "n")
        ClaimTriangle denominator = mapUtil.getValue(map, "denominator", "denom", "d")
        return ratioTriangle(numerator, denominator)
    }
    
    RatioTriangle ratioTriangle(ClaimTriangle numerator, ClaimTriangle denominator, Closure cl) {
        RatioTriangle ratios = new DefaultRatioTriangle(numerator, denominator);
        AbstractTriangleBuilder<ClaimTriangle> builder = new AbstractTriangleBuilder<ClaimTriangle>(ratios, this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.getTriangle()
    }
    
    @Override
    RatioTriangle corrigate(RatioTriangle triangle, int accident, int development, double correction) {
        return new RatioTriangleCorrection(triangle, accident, development, correction)
    }
    
    @Override
    RatioTriangle exclude(RatioTriangle triangle, int accident, int development) {
        return corrigate(triangle, accident, development, Double.NaN)
    }
    
    @Override
    RatioTriangle smooth(RatioTriangle triangle, TriangleSmoothing smoothing) {
        return new SmoothedRatioTriangle(triangle, smoothing)
    }
}
