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

package org.jreserve.grscript.plot.ratiotriangle

import groovy.transform.PackageScope
import org.jreserve.jrlib.triangle.ratio.RatioTriangle
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset
import org.jreserve.grscript.plot.AbstractLinePlot

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class RatiosPlot extends AbstractLinePlot {

    private RatioTriangle ratios
    private double minValue
    private double maxValue
    
    RatiosPlot(RatioTriangle ratios, PlotFormat format) {
        super(format)
        this.ratios = ratios
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int accidents = ratios.getAccidentCount()
        for(int a=0; a<accidents; a++) {
            String rowKey = getRowKey(a)
            int devs = ratios.getDevelopmentCount(a)
            for(int d=0; d<devs; d++) {
                double v = ratios.getValue(a, d)
                calculateBounds(v)
                ds.addValue(v, rowKey, Integer.valueOf(d))
            }
        }
        
        double[][] values = ratios.toArray()
        minValue = values.min()
        maxValue = values.max()
        
        return ds
    }
    
    private void calculateBounds(double value) {
        
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



