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
package org.jreserve.gui.plot.charts;

import java.awt.Color;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.Dataset;
import org.jreserve.gui.plot.ChartType;
import org.jreserve.gui.plot.PlotFormat;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractChart {
    protected final static double MARGIN = 0.05;

    protected final ChartType type;
    protected final PlotFormat format;
    protected Dataset dataset;
    protected JFreeChart chart;
    
    protected AbstractChart(ChartType type, PlotFormat format) {
        this.type = type;
        this.format = format;
    }

    public ChartType getType() {
        return type;
    }
    
    public JFreeChart buildChart() {
        dataset = createDataset();
        chart = ChartFactory.createChart(this);
        formatChart();
        return chart;
    }
    
    protected abstract Dataset createDataset();
    
    protected void formatChart() {
        format.getColors().reset();
        chart.setBackgroundPaint(format.getBackgroundColor());
        
        Plot plot = chart.getPlot();
        formatPlot(plot);
        
        if(plot instanceof CategoryPlot) {
            CategoryPlot cPlot = (CategoryPlot) plot;
            formatAxis(cPlot.getDomainAxis());
            formatAxis(cPlot.getRangeAxis());
        }
        
        LegendTitle legend = chart.getLegend();
        if(legend != null)
            formatLegend(legend);
    }

    protected abstract void formatPlot(Plot plot);
    
    protected void formatAxis(Axis axis) {
        formatAxisColors(axis);
        if(axis instanceof ValueAxis)
            setAxisMargins((ValueAxis) axis);
    }
    
    protected void formatAxisColors(Axis axis) {
        Color color = format.getForeColor();
        axis.setAxisLinePaint(color);
        axis.setTickMarkPaint(color);
        axis.setTickLabelPaint(color);
        axis.setLabelPaint(color);
    }
    
    private void setAxisMargins(ValueAxis axis) {
        axis.setUpperMargin(MARGIN);
        axis.setLowerMargin(MARGIN);
    }
    
    protected void formatLegend(LegendTitle legend) {
        legend.setBackgroundPaint(format.getBackgroundColor());
        legend.setItemPaint(format.getForeColor());
    }

    protected void formatRangeAxis(double min, double max) {
        Plot plot = chart.getPlot();
        if(plot instanceof CategoryPlot)
            formatAxis(((CategoryPlot)plot).getRangeAxis(), min, max);
    }
    
    protected void formatDomainAxis(double min, double max) {
        Plot plot = chart.getPlot();
        if(plot instanceof CategoryPlot) {
            Axis axis = ((CategoryPlot)plot).getDomainAxis();
            if(axis instanceof ValueAxis)
                formatAxis((ValueAxis)axis, min, max);
        }
    }

    private void formatAxis(ValueAxis axis, double min, double max) {
        double range = max-min;
        max = max + MARGIN * range;
        min = min - MARGIN * range;
        if(min == max) {
            min -= 1;
            max += 1;
        }
        axis.setRange(min, max);
    }
    
    protected void formatZeroRangeAxis(double max) {
        max += MARGIN * max;
        Plot plot = chart.getPlot();
        if(plot instanceof CategoryPlot)
            ((CategoryPlot)plot).getRangeAxis().setRange(0, max);
    }
}
