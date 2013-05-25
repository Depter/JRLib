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

package org.jreserve.grscript.plot.ratio

import org.jreserve.grscript.plot.PlotFormat
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.triangle.Triangle
import org.jreserve.jrlib.triangle.TriangleUtil
import org.jreserve.jrlib.claimratio.ClaimRatio
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class RatioPlot {

    static Map createPlot(LinkRatio lrs, PlotFormat format) {
        int devs = lrs.getLength()
        Map map = [:]
        for(int d=0; d<devs; d++) {
            map.put(getFittedLRChartName(d), new DevelopmentLinkRatioPlot(lrs, d, format).buildChart())
            map.put(getResidualChartName(d), new DevelopmentRatioResidualPlot(lrs, d, format).buildChart())
        }
        return map
    }
    
    private static String getFittedLRChartName(int dev) {
        "Period_${dev}/${dev+1}"
    }
    
    private static String getResidualChartName(int dev) {
        "Residuals_${dev}"
    }

    static Map createPlot(ClaimRatio crs, PlotFormat format) {
        int devs = crs.getLength()
        Map map = [:]
        for(int d=0; d<devs; d++) {
            map.put(getFittedCRChartName(d), new DevelopmentClaimRatioPlot(crs, d, format).buildChart())
            map.put(getResidualChartName(d), new DevelopmentRatioResidualPlot(crs, d, format).buildChart())
        }
        return map
    }
    
    private static String getFittedCRChartName(int dev) {
        "Ratio_${dev}"
    }
}

