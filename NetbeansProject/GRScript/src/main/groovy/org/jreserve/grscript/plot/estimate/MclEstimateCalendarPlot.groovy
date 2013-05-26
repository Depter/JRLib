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
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class MclEstimateCalendarPlot extends AbstractBarPlot {

    private MclEstimateBundle bundle
    private Estimate paid
    private Estimate incurred
    private double maxValue
    private double minValue
    
    MclEstimateCalendarPlot(MclEstimateBundle bundle, PlotFormat format) {
        super(format)
        this.bundle = bundle
        this.paid = bundle.getPaidEstimate()
        this.incurred = bundle.getIncurredEstimate()
    }
    
    protected String getChartTitle() {
        return null
    }
	
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int devs = bundle.getDevelopmentCount()
        int accidents = bundle.getAccidentCount()
        int count = devs - bundle.getObservedDevelopmentCount(accidents-1)
        
        double[][] paidValues = paid.toArray()
        TriangleUtil.deCummulate(paidValues)
        
        double[][] incurredValues = incurred.toArray()
        TriangleUtil.deCummulate(incurredValues)
        
        for(int d=0; d<count; d++) {
            double paidSum = 0d;
            double incurredSum = 0d;
            
            for(int a=0; a<accidents; a++) {
                int dev = bundle.getObservedDevelopmentCount(a) + d
                if(dev <devs) {
                    if(!Double.isNaN(paidValues[a][dev]))
                        paidSum += paidValues[a][dev]
                    if(!Double.isNaN(incurredValues[a][dev]))
                        incurredSum += incurredValues[a][dev]
                }
            }
            
            calculateBounds(paidSum)
            calculateBounds(incurredSum)
            
            PlotLabel label = getLabel(d)
            ds.addValue(paidSum, "Paid", label)
            ds.addValue(incurredSum, "Incurred", label)
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

