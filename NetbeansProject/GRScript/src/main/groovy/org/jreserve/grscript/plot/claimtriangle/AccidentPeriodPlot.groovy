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
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import org.jfree.chart.axis.ValueAxis
import org.jreserve.grscript.plot.AbstractPlot
import org.jreserve.grscript.plot.PlotFormat
import java.awt.BasicStroke
import java.awt.Color
import org.jreserve.grscript.plot.AbstractLinePlot

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class AccidentPeriodPlot extends AbstractLinePlot {

    private ClaimTriangle triangle
    private double minValue
    private double maxValue
    
    AccidentPeriodPlot(ClaimTriangle triangle, PlotFormat format) {
        super(format)
        this.triangle = triangle
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int accidents = triangle.getAccidentCount()
        for(int a=0; a<accidents; a++) {
            String rowKey = getRowKey(a)
            int devs = triangle.getDevelopmentCount(a)
            for(int d=0; d<devs; d++)
                ds.addValue(triangle.getValue(a, d), rowKey, Integer.valueOf(d))
        }
        
        double[][] values = triangle.toArray()
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

