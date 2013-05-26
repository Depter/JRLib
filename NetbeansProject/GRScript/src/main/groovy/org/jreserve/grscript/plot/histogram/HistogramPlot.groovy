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

package org.jreserve.grscript.plot.histogram

import groovy.transform.PackageScope
import org.jreserve.grscript.plot.AbstractBarPlot
import org.jreserve.jrlib.bootstrap.util.HistogramData
import org.jreserve.grscript.plot.AbstractPlot
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.xy.IntervalXYDataset
import org.jfree.data.xy.XYIntervalSeries
import org.jreserve.grscript.plot.PlotLabel
import org.jfree.data.xy.XYIntervalSeriesCollection
import org.jfree.chart.renderer.xy.XYBarRenderer
import java.awt.Color
import org.jfree.chart.renderer.xy.StandardXYBarPainter

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class HistogramPlot extends AbstractPlot {
	
    private HistogramData data
    private Map values
    private double minX = Double.NaN
    private double maxX = Double.NaN
    private double minY = Double.NaN
    private double maxY = Double.NaN

    
    HistogramPlot(HistogramData data, Map values, PlotFormat format) {
        super(AbstractPlot.CT_XY_BAR, format)
        this.data = data
        this.values = values
    }
    
    protected IntervalXYDataset createDataSet() {
        XYIntervalSeriesCollection ds = new XYIntervalSeriesCollection()
        XYIntervalSeries dataSeries = new XYIntervalSeries(getDataLabel(), false, true)
        int size = data.getIntervalCount()
        double range = data.getIntervalSize()
        double margin = 0.2 * range
        
        for(int i=0; i<(size-1); i++) {
            double y = data.getCount(i)
            calculateYBounds(y)
            
            double xHigh = data.getUpperBound(i)
            double xLow = xHigh - range
            double x = xHigh-range/2d
            xLow += margin
            xHigh -= margin
            
            calculateXBounds(xLow)
            calculateXBounds(xHigh)
            dataSeries.add(x, xLow, xHigh, y, y, y)
        }
        
        if(size > 0) {
            int i = size-1
            double y = data.getCount(i)
            calculateYBounds(y)
            
            double xLow = data.getLowerBound(i)
            double xHigh = xLow+range
            double x = xLow+range/2d
            xLow += margin
            xHigh -= margin
            
            calculateXBounds(xLow)
            calculateXBounds(xHigh)
            dataSeries.add(x, xLow, xHigh, y, y, y)
        }
        
        ds.addSeries(dataSeries)
        
        int index = 1
        double correction = range / 2d - margin
        values.each {key, value -> 
            XYIntervalSeries valueSeries = new XYIntervalSeries(new PlotLabel(index++, key), false, true)
            valueSeries.add(value, value-correction, value+correction, maxY, maxY, maxY)
            ds.addSeries(valueSeries)
        }
        
        return ds
    }
    
    private PlotLabel getDataLabel() {
        return new PlotLabel(0, "Count")
    }
    
    private void calculateYBounds(def v) {
        if(Double.isNaN(minY) || minY > v) minY = v
        if(Double.isNaN(maxY) || maxY < v) maxY = v
    }
    
    private void calculateXBounds(def v) {
        if(Double.isNaN(minX) || minX > v) minX = v
        if(Double.isNaN(maxX) || maxX < v) maxX = v
    }
    
    protected void formatPlot(def plot) {
        plot.setBackgroundPaint(format.backgroundColor)
        plot.setRangeGridlinePaint(format.gridColor)
        plot.setDomainGridlinePaint(format.gridColor)
        
        formatSeries(plot.getRenderer())
        formatRangeAxis(minY, maxY)
        formatDomainAxis(minX, maxX)
    }
   
    protected void formatSeries(XYBarRenderer renderer) {
        renderer.setBarPainter(new StandardXYBarPainter())
        renderer.setDrawBarOutline(true)
        
        for(index in 0..<dataSet.getSeriesCount()) {
            Color color = format.nextColor()
            renderer.setSeriesPaint(index, color);
            renderer.setSeriesFillPaint(index, color)
        }
    }
    
    protected double getMinValue() {
        minY
    }
    
    protected double getMaxValue() {
        maxY
    }
}

