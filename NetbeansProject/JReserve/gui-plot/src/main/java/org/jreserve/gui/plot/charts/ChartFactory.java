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

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jreserve.gui.plot.PlotFormat;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ChartFactory {
    
    private final static PlotOrientation ORIENTATION = PlotOrientation.VERTICAL;
    private final static boolean SHOW_URLS = false;
    
    static JFreeChart createChart(AbstractChart chart) {
        switch(chart.getType()) {
            case LINE:
                return createLineChart(chart.format, chart.dataset);
            case BAR:
                return createBarChart(chart.format, chart.dataset);
            case XY_LINE:
                return createXYLineChart(chart.format, chart.dataset);
            case XY_BAR:
                return createXYBarChart(chart.format, chart.dataset);
            default:
                throw new IllegalArgumentException("Unknown ChartType: "+chart.type);
        }
    }
    
    private static JFreeChart createLineChart(PlotFormat format, Dataset dataset) {
        return org.jfree.chart.ChartFactory.createLineChart(
                format.getTitle(), format.getXTitle(), format.getYTitle(), 
                (CategoryDataset) dataset, ORIENTATION, 
                format.isLegendVisible(), format.isTooltipsVisible(), SHOW_URLS);
    }
    
    private static JFreeChart createBarChart(PlotFormat format, Dataset dataset) {
        return org.jfree.chart.ChartFactory.createBarChart(
                format.getTitle(), format.getXTitle(), format.getYTitle(), 
                (CategoryDataset) dataset, ORIENTATION, 
                format.isLegendVisible(), format.isTooltipsVisible(), SHOW_URLS);
    }
    
    private static JFreeChart createXYLineChart(PlotFormat format, Dataset dataset) {
        return org.jfree.chart.ChartFactory.createBarChart(
                format.getTitle(), format.getXTitle(), format.getYTitle(), 
                (CategoryDataset) dataset, ORIENTATION, 
                format.isLegendVisible(), format.isTooltipsVisible(), SHOW_URLS);
    }
    
    private static JFreeChart createXYBarChart(PlotFormat format, Dataset dataset) {
        boolean dateAxis = false;
        return org.jfree.chart.ChartFactory.createXYBarChart(
                format.getTitle(), format.getXTitle(), dateAxis, format.getYTitle(), 
                (IntervalXYDataset) dataset, ORIENTATION, 
                format.isLegendVisible(), format.isTooltipsVisible(), SHOW_URLS);
    }
    
    private ChartFactory() {}    
}
