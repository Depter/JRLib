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
import org.jreserve.grscript.plot.AbstractLinePlot
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.grscript.plot.PlotFormat
import org.jfree.data.category.DefaultCategoryDataset
import org.jreserve.grscript.plot.PlotLabel
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Stroke
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@PackageScope class MclEstimateDevelopmentPlot extends AbstractLinePlot {
    
    private MclEstimateBundle bundle
    private Estimate paid
    private Estimate incurred
    private double minValue = Double.NaN
    private double maxValue = Double.NaN
	
    MclEstimateDevelopmentPlot(MclEstimateBundle bundle, PlotFormat format) {
        super(format)
        this.bundle = bundle;
        this.paid = bundle.getPaidEstimate()
        this.incurred = bundle.getIncurredEstimate()
    }
    
    protected String getChartTitle() {
        return null
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        int accidents = bundle.getAccidentCount()
        for(int a=0; a<accidents; a++)
            addAccident(ds, a)
        return ds
    }
    
    private void addAccident(DefaultCategoryDataset ds, int accident) {
        int devs = bundle.getDevelopmentCount()
        int obsDevs = bundle.getObservedDevelopmentCount(accident)
        PlotLabel obsKey = getObservedLabel(accident)
        
        for(int d=0; d<obsDevs; d++) {
            double v = getRate(accident, d)
            calculateBounds(v)
            ds.addValue(v, obsKey, Integer.valueOf(d))
        }
        
        PlotLabel estKey = getEstimateLabel(accident)
        for(int d=(obsDevs-1); d<devs; d++) {
            double v = getRate(accident, d)
            calculateBounds(v)
            ds.addValue(v, estKey, Integer.valueOf(d))
        }
    }
    
    private PlotLabel getObservedLabel(int accident) {
        String name = format.getSeriesName(accident)
        new PlotLabel(accident * 2, name ?: "Accident_${accident}")
    }

    private PlotLabel getEstimateLabel(int accident) {
        String name = format.getSeriesName(accident)
        name = "Estimated_${name ?: accident}"
        new PlotLabel(accident*2+1, name)
    }
    
    private void calculateBounds(double v) {
        if(Double.isNaN(minValue) || minValue > v) minValue = v
        if(Double.isNaN(maxValue) || maxValue < v) maxValue = v
    }
    
    private double getRate(int accident, int development) {
        double p = paid.getValue(accident, development)
        double i = incurred.getValue(accident, development)
        return p/i
    }
    
    protected double getMinValue() {
        minValue
    }
    
    protected double getMaxValue() {
        maxValue
    }
    
    protected void formatSeries(LineAndShapeRenderer renderer) {
        renderer.setDrawOutlines(true)
        renderer.setUseFillPaint(true)
        renderer.setLinesVisible(true)
        renderer.setShapesVisible(false)
        
        float width = 2
        Stroke line = new BasicStroke(width);
        float[] dash = [10]
        Stroke dashed = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f)
        
        int accidents = bundle.getAccidentCount()
        for(int a=0; a<accidents; a++) {
            Color color = format.nextColor()
            int index = a*2
            renderer.setSeriesPaint(index, color);
            renderer.setSeriesFillPaint(index, color)
            renderer.setSeriesStroke(index, line);
            
            index++
            renderer.setSeriesPaint(index, color);
            renderer.setSeriesFillPaint(index, color)
            renderer.setSeriesStroke(index, dashed);
            renderer.setSeriesVisibleInLegend(index, false)
        }
        
    }	
}

