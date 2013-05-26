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

import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.JFreeChart
import org.jfree.chart.ChartFactory
import org.jfree.data.general.Dataset
import org.jfree.chart.axis.ValueAxis

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractPlot {
    
    final static int CT_LINE = 0
    final static int CT_BAR = 1
    final static int CT_XY_LINE = 2
    final static int CT_XY_BAR = 3
    
    final static PlotOrientation ORIENTATION = PlotOrientation.VERTICAL
    final static boolean SHOW_URLS = false
    final static double MARGIN = 0.05

    protected int type
    protected PlotFormat format
    protected JFreeChart chart
    protected Dataset dataSet
    
    AbstractPlot(int type, PlotFormat format) {
        this.type = type
        this.format = format
    }
    
    JFreeChart buildChart() {
        dataSet = createDataSet()
        initChart()
        formatChart()
        chart
    }
    
    protected void initChart() {
        switch(type) {
            case CT_LINE:
                initChart(ChartFactory.&createLineChart)
                break
            case CT_BAR:
                initChart(ChartFactory.&createBarChart)
                break
            case CT_XY_LINE:
                initChart(ChartFactory.&createXYLineChart)
                break
            case CT_XY_BAR:
                initXYBarChart()
                break
            default:
                throw new IllegalStateException("Unknown chart type: ${type}!")
        }
    }
    
    protected void initChart(Closure cl) {
        chart = cl(
            getChartTitle(), format.xTitle, format.yTitle,
            dataSet, ORIENTATION,
            format.showLegend, format.showTooltips, SHOW_URLS
        )
    }
    
    protected void initXYBarChart() {
        chart = ChartFactory.createXYBarChart(
            getChartTitle(), format.xTitle, false, format.yTitle,
            dataSet, ORIENTATION, 
            format.showLegend, format.showTooltips, SHOW_URLS
        )
    }
    
    protected String getChartTitle() {
        return format.title
    }
    
    protected abstract def createDataSet()
    
    protected void formatChart() {
        format.resetColors()
        chart.setBackgroundPaint(format.backgroundColor)
        formatPlot(chart.getPlot())
    }
    
    protected abstract void formatPlot(def plot);

    protected void formatRangeAxis(double min, double max) {
        formatAxis(chart.getPlot().getRangeAxis(), min, max)
    }

    private void formatAxis(def axis, double min, double max) {
        double range = max-min
        max = max + MARGIN * range
        min = min - MARGIN * range
        if(min == max) {
            min -= 1
            max += 1
        }
        axis.setRange(min, max)
    }
    
    protected void formatDomainAxis(double min, double max) {
        formatAxis(chart.getPlot().getDomainAxis(), min, max)
    }
    
    protected void formatZeroRangeAxis(double max) {
        max += MARGIN * max
        chart.getPlot().getRangeAxis().setRange(0, max)
    }
}

