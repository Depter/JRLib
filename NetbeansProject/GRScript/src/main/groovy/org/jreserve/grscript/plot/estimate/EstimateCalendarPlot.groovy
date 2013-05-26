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

import groovy.transform.PackageScope
import org.jreserve.grscript.plot.AbstractBarPlot
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset
import org.jreserve.grscript.plot.PlotLabel
import org.jreserve.jrlib.triangle.TriangleUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class EstimateCalendarPlot extends AbstractBarPlot {

    private Estimate estimate
    private double maxValue
    private double minValue
    
    EstimateCalendarPlot(Estimate estimate, PlotFormat format) {
        super(format)
        this.estimate = estimate
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int devs = estimate.getDevelopmentCount()
        int accidents = estimate.getAccidentCount()
        int count = devs - estimate.getObservedDevelopmentCount(accidents-1)
        
        double[][] values = estimate.toArray()
        TriangleUtil.deCummulate(values)
        
        for(int d=0; d<count; d++) {
            double sum = 0d;
            
            for(int a=0; a<accidents; a++) {
                int dev = estimate.getObservedDevelopmentCount(a) + d
                if(dev < devs && !Double.isNaN(values[a][dev]))
                    sum += values[a][dev]
            }
            
            calculateBounds(sum)
            ds.addValue(sum, "Payments", getLabel(d))
        }
        
        return ds
    }
    
    private void calculateBounds(double v) {
        if(Double.isNaN(minValue) || minValue > v) minValue = v
        if(Double.isNaN(maxValue) || maxValue < v) maxValue = v
    }
    
    private PlotLabel getLabel(int accident) {
        String name = format.getSeriesName(accident)
        new PlotLabel(accident, name ?: "${accident}")
    }
    
    protected double getMinValue() {
        return minValue
    }
    
    protected double getMaxValue() {
        return maxValue
    }	
}

