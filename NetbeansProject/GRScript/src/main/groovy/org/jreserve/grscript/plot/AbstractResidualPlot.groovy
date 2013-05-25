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
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractResidualPlot extends AbstractLinePlot {
    final int RESIDUAL = 0;
    final int ZERO = 1;
    
    private double minY = Double.NaN
    private double maxY = Double.NaN
    
    AbstractResidualPlot(PlotFormat format) {
        super(format)
    }
    
    protected DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset()
        String residualKey = getResidualKey()
        String zeroKey = getZeroKey()
        
        int length = getDomainLength()
        for(int d=0; d<length; d++) {
            PlotLabel dName = getDomainName(d)
            double[] residuals = getResiduals(d)
            for(int r=0; r<residuals.length; r++) {
                double v = residuals[r]
                calculateBounds(v)
                ds.addValue(v, residualKey, dName)
            }

            ds.addValue(0d, zeroKey, dName)
        }
        
        return ds;
    } 
    
    protected PlotLabel getResidualKey() {
        return new PlotLabel(RESIDUAL, "Residuals")
    }
    
    protected String getZeroKey() {
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
        
        
        renderer.setSeriesLinesVisible(RESIDUAL, false)
        renderer.setSeriesShapesVisible(RESIDUAL, true)
        Color color = Color.BLACK;//super.format.nextColor()
        renderer.setSeriesPaint(RESIDUAL, color);
        renderer.setSeriesFillPaint(RESIDUAL, color)
        
        renderer.setSeriesLinesVisible(ZERO, true)
        renderer.setSeriesShapesVisible(ZERO, false)
        color = Color.RED;//super.format.nextColor()
        renderer.setSeriesPaint(ZERO, color);
        renderer.setSeriesFillPaint(ZERO, color)
    }	

    
    protected double getMinValue() {
        minY
    }
    
    protected double getMaxValue() {
        maxY
    }
}

