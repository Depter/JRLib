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

import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import java.awt.BasicStroke
import java.awt.Color
import org.jfree.chart.ChartFactory
import java.awt.Shape
import org.jfree.util.ShapeUtilities
import java.awt.Rectangle
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractResidualPlot extends AbstractLinePlot {
    final int ZERO = 0;
    final int RESIDUAL = 1;
    
    private final static int RESIDUAL_SIZE = 4
    
    private double minY = Double.NaN
    private double maxY = Double.NaN
    
    AbstractResidualPlot(PlotFormat format) {
        super(format)
    }

    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        PlotLabel zeroKey = getZeroKey()
        
        int length = getDomainLength()
        for(int d=0; d<length; d++) {
            PlotLabel dName = getDomainName(d)
            ds.addValue(0d, zeroKey, dName)
            
            double[] residuals = getResiduals(d)
            for(int r=0; r<residuals.length; r++) {
                double v = residuals[r]
                if(!Double.isNaN(v)) {
                    calculateBounds(v)
                    ds.addValue(v, getResidualKey(r), dName)
                }
            }
        }

        return ds;
    } 
    
    protected PlotLabel getResidualKey(int domain) {
        return new PlotLabel(domain+1, "Residuals")
    }
    
    protected PlotLabel getZeroKey() {
        return new PlotLabel(ZERO, "Zero")
    }
    
    protected abstract int getDomainLength();
    
    protected abstract double[] getResiduals(int index);
    
    protected PlotLabel getDomainName(int index) {
        String name = format.getSeriesName(index)
        new PlotLabel(index, name ?: "${index}")
    }
    
    private void calculateBounds(def v) {
        if(Double.isNaN(minY) || minY > v) minY = v
        if(Double.isNaN(maxY) || maxY < v) maxY = v
    }
    
    protected void formatSeries(LineAndShapeRenderer renderer) {
        renderer.setDrawOutlines(true)
        renderer.setUseFillPaint(true)
        renderer.setStroke(new BasicStroke(2));
        
        renderer.setSeriesLinesVisible(ZERO, true)
        renderer.setSeriesShapesVisible(ZERO, false)
        Color color = Color.RED;//super.format.nextColor()
        renderer.setSeriesPaint(ZERO, color);
        renderer.setSeriesFillPaint(ZERO, color)
        
        int residualCount = dataSet.getRowCount()
        color = Color.BLACK;//super.format.nextColor()
        Shape shape = new Rectangle(RESIDUAL_SIZE, RESIDUAL_SIZE);
            
        for(int r=1; r<residualCount; r++) {
            renderer.setSeriesShape(r, shape)
            renderer.setSeriesLinesVisible(r, false)
            renderer.setSeriesShapesVisible(r, true)
            renderer.setSeriesPaint(r, color);
            renderer.setSeriesFillPaint(r, color)
            
            if(r > 1)
                renderer.setSeriesVisibleInLegend(r, false)
        }
    }	

    
    protected double getMinValue() {
        minY
    }
    
    protected double getMaxValue() {
        maxY
    }
}

