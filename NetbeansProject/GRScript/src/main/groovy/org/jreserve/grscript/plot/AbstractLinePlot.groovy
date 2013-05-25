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

import org.jfree.chart.renderer.category.LineAndShapeRenderer
import java.awt.BasicStroke
import java.awt.Color
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractLinePlot extends AbstractPlot {
    
    AbstractLinePlot(PlotFormat format) {
        this(AbstractPlot.CT_LINE, format)
    }
    
    AbstractLinePlot(int type, PlotFormat format) {
        super(type, format)
    }
    
    protected void formatPlot(def plot) {
        plot.setBackgroundPaint(format.backgroundColor)
        plot.setRangeGridlinePaint(format.gridColor)
        plot.setDomainGridlinePaint(format.gridColor)
        
        formatSeries(plot.getRenderer())
        formatRangeAxis(getMinValue(), getMaxValue())
    }
    
    protected void formatSeries(LineAndShapeRenderer renderer) {
        renderer.setShapesVisible(true)
        renderer.setDrawOutlines(true)
        renderer.setUseFillPaint(true)
        renderer.setStroke(new BasicStroke(2));

        for(index in 0..<dataSet.getRowCount()) {
            Color color = super.format.nextColor()
            renderer.setSeriesPaint(index, color);
            renderer.setSeriesFillPaint(index, color)
        }
    }	
    
    protected abstract double getMinValue();
    
    protected abstract double getMaxValue();
}

