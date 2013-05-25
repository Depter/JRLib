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
import org.jreserve.grscript.plot.AbstractPlot
import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset
import org.jreserve.grscript.plot.AbstractLinePlot

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class FactorsPlot extends AbstractLinePlot {

    private FactorTriangle factors
    private double minValue
    private double maxValue
    
    FactorsPlot(FactorTriangle factors, PlotFormat format) {
        super(format)
        this.factors = factors
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int accidents = factors.getAccidentCount()
        for(int a=0; a<accidents; a++) {
            String rowKey = getRowKey(a)
            int devs = factors.getDevelopmentCount(a)
            for(int d=0; d<devs; d++)
                ds.addValue(factors.getValue(a, d), rowKey, Integer.valueOf(d))
        }
        
        double[][] values = factors.toArray()
        minValue = values.min()
        maxValue = values.max()
        
        return ds
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

