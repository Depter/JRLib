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
package org.jreserve.grscript.gui.plot.claimtriangle;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import org.jreserve.grscript.gui.plot.PlotPanel;
import org.jreserve.grscript.gui.plot.plots.PlotFormat;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 */
public class ClaimTrianglePlot {
    
    private ClaimTriangle triangle;
    private PlotFormat format;
    
    public ClaimTrianglePlot(ClaimTriangle triangle, PlotFormat format) {
        this.triangle = triangle;
        this.format = format;
    }
    
    public void show() {
        Map<String, JFreeChart> charts = createCharts();
        String title = getTitle();
        PlotPanel.showCharts(title, charts);
    }
    
    private Map<String, JFreeChart> createCharts() {
        Map<String, JFreeChart> charts = new LinkedHashMap<String, JFreeChart>(3);
        charts.put("Accident Periods", new AccidentPeriodsPlotFactory(triangle).createChart());
        return charts;
    }
    
    private String getTitle() {
        String title = format.getTitle();
        return title!=null? title : "Claim Triangle";
    }
}
