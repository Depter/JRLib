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

package org.jreserve.grscript.plot

import org.jfree.data.xy.XYSeriesCollection
import org.jfree.data.xy.XYSeries
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import java.awt.BasicStroke
import java.awt.Color
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractXYLinePlot extends AbstractLinePlot {
    final int ORIGINAL = 0;
    final int FITTED = 1;
    
    private double minX = Double.NaN
    private double maxX = Double.NaN
    private double minY = Double.NaN
    private double maxY = Double.NaN
    
    AbstractXYLinePlot(PlotFormat format) {
        super(AbstractPlot.CT_XY_LINE, format)
    }
    
    protected XYSeriesCollection createDataSet() {
        double[] fX = getXValues()
        double[] fY = getYValues()
        double[] fF = getFittedValues()
        
        XYSeries original = new XYSeries(getRowKey(ORIGINAL))
        XYSeries fitted = new XYSeries(getRowKey(FITTED))
        
        int size = Math.min(fX.length, fY.length)
        for(int i=0; i<size; i++) {
            double x = fX[i]
            double y = fY[i]
            double f = fF[i]
            original.add(x, y)
            fitted.add(x, f)
            calculateBounds(x, y, f)
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(original);
        dataset.addSeries(fitted);
        return dataset;
    }   
    
    protected abstract double[] getXValues();
    
    protected abstract double[] getYValues();
    
    protected abstract double[] getFittedValues();


    protected String getRowKey(int index) {
        String name = format.getSeriesName(index)
        if(name) return name
        else if(index == ORIGINAL) return "Original"
        else return "Fitted"
    }
    
    private void calculateBounds(double x, double y, double f) {
        minX = min(minX, x)
        maxX = max(maxX, x)
        minY = min(minY, y<f? y : f)
        maxY = max(maxY, y>f? y : f)
    }
    
    private double max(double o, double v) {
        (Double.isNaN(o) || o < v)? v : o;
    }
    
    private double min(double o, double v) {
        (Double.isNaN(o) || o > v)? v : o;
    }
    
    protected void formatPlot(def plot) {
        super.formatPlot(plot)
        formatDomainAxis(getMinXValue(), getMaxXValue())
    }
    
    protected void formatSeries(XYLineAndShapeRenderer renderer) {
        renderer.setDrawOutlines(true)
        renderer.setUseFillPaint(true)
        renderer.setStroke(new BasicStroke(2));
        
        renderer.setSeriesLinesVisible(ORIGINAL, false)
        renderer.setSeriesShapesVisible(ORIGINAL, true)
        Color color = Color.BLACK;//super.format.nextColor()
        renderer.setSeriesPaint(ORIGINAL, color);
        renderer.setSeriesFillPaint(ORIGINAL, color)
        
        renderer.setSeriesLinesVisible(FITTED, true)
        renderer.setSeriesShapesVisible(FITTED, false)
        color = Color.RED;//super.format.nextColor()
        renderer.setSeriesPaint(FITTED, color);
        renderer.setSeriesFillPaint(FITTED, color)
    }	

    
    protected double getMinValue() {
        minY
    }
    
    protected double getMaxValue() {
        maxY
    }
    
    protected double getMinXValue() {
        return minX
    }
    
    protected double getMaxXValue() {
        return maxX
    }    
}

