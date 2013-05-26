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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class EstimateReservePlot extends AbstractBarPlot {

    private Estimate estimate
    private double maxValue
    private double minValue
    
    EstimateReservePlot(Estimate estimate, PlotFormat format) {
        super(format)
        this.estimate = estimate
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        double[] reserves = estimate.toArrayReserve()
        reserves.eachWithIndex {value, index -> ds.addValue(value, "Reserve", getLabel(index))}
        maxValue = reserves.max()
        minValue = reserves.min()
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

