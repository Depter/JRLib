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
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class MclEstimateReservePlot extends AbstractBarPlot {
    
    private MclEstimateBundle bundle
    private Estimate paid
    private Estimate incurred
    private double maxValue
    private double minValue
    
    MclEstimateReservePlot(MclEstimateBundle bundle, PlotFormat format) {
        super(format)
        this.bundle = bundle
        this.paid = bundle.getPaidEstimate()
        this.incurred = bundle.getIncurredPaidEstimate()
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        
        int accidents = bundle.getAccidentCount()
        double[] paidReserves = paid.toArrayReserve()
        double[] incurredReserves = incurred.toArrayReserve()
        for(int a=0; a<accidents; a++) {
            PlotLabel label = getLabel(a)
            ds.addValue(paidReserves[a], "Paid", label)
            ds.addValue(incurredReserves[a], "Incurred", label)
        }
        
        maxValue = Math.max(paidReserves.max(), incurredReserves.max())
        minValue = Math.min(paidReserves.min(), incurredReserves.min())
        return ds
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

