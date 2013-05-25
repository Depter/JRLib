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

package org.jreserve.grscript.plot.claimtriangle

import groovy.transform.PackageScope
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.grscript.plot.AbstractPlot
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.renderer.category.BarRenderer
import org.jreserve.grscript.plot.AbstractBarPlot

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class CalendarPeriodPlot extends AbstractBarPlot {
    
    private ClaimTriangle triangle
    private double maxValue
    private double minValue
    
    CalendarPeriodPlot(ClaimTriangle triangle, PlotFormat format) {
        super(format)
        this.triangle = triangle
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        double[] sums = getCalendarTotals()
        sums.eachWithIndex {value, index -> ds.addValue(value, "Total", Integer.valueOf(index))}
        maxValue = sums.max()
        minValue = sums.min()
        return ds
    }
    
    private double[] getCalendarTotals() {
        int devs = triangle.getDevelopmentCount()
        double[] sums = new double[devs]
        int accidents = triangle.getAccidentCount()
        
        for(int d=(devs-1); d>=0; d--)
            sums[d] = sumDiagonal(devs-d-1, accidents)
        
        return sums
    }
    
    private double sumDiagonal(int d, int accidents) {
        double s = 0d
        for(int a=0; a<accidents; a++) {
            int dev = triangle.getDevelopmentCount(a) - d - 1
            double v = triangle.getValue(a, dev)
            if(!Double.isNaN(v))
                s += v
        }
        return s
    }
    
    protected double getMinValue() {
        return minValue
    }
    
    protected double getMaxValue() {
        return maxValue
    }
}

