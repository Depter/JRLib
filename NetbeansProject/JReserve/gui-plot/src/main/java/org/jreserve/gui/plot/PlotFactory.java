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
package org.jreserve.gui.plot;

import java.awt.Component;
import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jreserve.gui.plot.charts.AbstractBarChart;
import org.jreserve.gui.plot.charts.AbstractLineChart;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PlotFactory {
    
    public static Component createLinePlot(PlotFormat format, List<PlotSerie> series) {
        AbstractLineChart plot = new AbstractLineChart(format, series);
        return new ChartPanel(plot.buildChart());
    }
    
    public static Component createBarPlot(PlotFormat format, List<PlotSerie> series) {
        AbstractBarChart plot = new AbstractBarChart(format, series);
        return new ChartPanel(plot.buildChart());
    }
}
