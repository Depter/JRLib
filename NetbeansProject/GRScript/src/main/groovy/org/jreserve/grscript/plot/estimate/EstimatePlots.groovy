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

package org.jreserve.grscript.plot.estimate

import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.grscript.plot.PlotFormat
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EstimatePlots {
    
    static Map createPlot(Estimate estimate, PlotFormat format) {
        [
            "Development":new EstimateDevelopmentPlot(estimate, format).buildChart(),
            "Reserves":new EstimateReservePlot(estimate, format).buildChart(),
            "Calendar claims":new EstimateCalendarPlot(estimate, format).buildChart()
        ]
    }
    
    static Map createPlot(MclEstimateBundle bundle, PlotFormat format) {
        [
            "Paid-Development":new EstimateDevelopmentPlot(bundle.getPaidEstimate(), format).buildChart(),
            "Incurred-Development":new EstimateDevelopmentPlot(bundle.getIncurredEstimate(), format).buildChart(),
            "P/I":new MclEstimateDevelopmentPlot(bundle, format).buildChart(),
            "Reserves":new MclEstimateReservePlot(bundle, format).buildChart(),
            "Calendar claims":new MclEstimateCalendarPlot(bundle, format).buildChart()
        ]
    }
}

