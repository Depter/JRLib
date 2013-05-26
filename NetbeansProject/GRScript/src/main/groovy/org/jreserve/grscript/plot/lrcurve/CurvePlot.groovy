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

package org.jreserve.grscript.plot.lrcurve

import org.jreserve.grscript.plot.PlotFormat
import org.jreserve.jrlib.linkratio.LinkRatio
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CurvePlot {
    
    static Map createPlot(LinkRatio lrs, Map curves, PlotFormat format) {
        [
            "Curves": new LrCurvePlot(lrs, curves, format).buildChart()
        ]
    }
    
    static Map createPlot(LinkRatio lrs, Collection curves, PlotFormat format) {
        Map curveMap = createCurveMap(curves, format)
        createPlot(lrs, curveMap, format)
    }
    
    private static Map createCurveMap(Collection curves, PlotFormat format) {
        Map result = [:]
        curves.eachWithIndex{curve, int index -> 
            String name = format.getSeriesName(index+1)
            result.put(name ?: "Curve_${index+1}", curve)
        }
        return result
    }
	
}

