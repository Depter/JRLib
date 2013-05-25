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

package org.jreserve.grscript.plot.factortriangle

import groovy.transform.PackageScope
import org.jreserve.grscript.plot.AbstractLinePlot
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class CummulatedFactorsPlot extends AbstractLinePlot {

    private FactorTriangle factors
    private double minValue = Double.NaN
    private double maxValue = Double.NaN
    
    CummulatedFactorsPlot(FactorTriangle factors, PlotFormat format) {
        super(format)
        this.factors = factors
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int accidents = factors.getAccidentCount()
        for(int a=0; a<accidents; a++)
            addAccidentPeriod(ds, a)
        return ds
    }
    
    private void addAccidentPeriod(DefaultCategoryDataset ds, int accident) {
        String rowKey = getRowKey(accident)
        int devs = factors.getDevelopmentCount(accident)
        
        double factor = 1d
        for(int d=0; d<devs; d++) {
            factor *= factors.getValue(accident, d)
            initBounds(factor)
            ds.addValue(factor, rowKey, Integer.valueOf(d))
        }
    }
    
    private void initBounds(double value) {
        if(Double.isNaN(maxValue) || maxValue < value)
            maxValue = value
        if(Double.isNaN(minValue) || minValue > value)
            minValue = value
    }
    
    private String getRowKey(int accident) {
        String name = format.getSeriesName(accident)
        return name ?: "Accident_${accident}"
    }
    
    protected double getMinValue() {
        minValue
    }
    
    protected double getMaxValue() {
        maxValue
    }	
}

