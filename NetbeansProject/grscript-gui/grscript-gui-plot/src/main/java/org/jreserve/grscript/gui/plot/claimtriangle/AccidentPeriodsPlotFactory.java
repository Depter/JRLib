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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jreserve.grscript.gui.plot.plots.PlotFormat;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AccidentPeriodsPlotFactory {
    
    private ClaimTriangle triangle;
    private PlotFormat format;
    private JFreeChart chart;
    
    public AccidentPeriodsPlotFactory(ClaimTriangle triangle) {
        this.triangle = triangle;
    }
    
    public JFreeChart createChart() {
        initChart();
        formatChart();
        return chart;
    }
    
    private void initChart() {
        chart = ChartFactory.createLineChart(
                format.getTitle(), 
                format.getDomainTitle(), 
                format.getRangeTitle(), 
                createDataSet(), 
                format.getOrientation(), 
                format.isShowLegend(), 
                format.isShowTooltip(), 
                false);
    }
    
    private DefaultCategoryDataset createDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int accidents = triangle.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            String rKey = "Accident_"+a;
            int devs = triangle.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double v = triangle.getValue(a, d);
                dataset.addValue(v, rKey, Integer.valueOf(d));
            }
        }
        return dataset;
    }
    
    private void formatChart() {
        chart.setBackgroundPaint(format.getBgColor());
        CategoryPlot plot = chart.getCategoryPlot();
        
        plot.setBackgroundPaint(format.getBgColor());
        plot.setRangeGridlinePaint(format.getGridLineColor());
        plot.setDomainGridlinePaint(format.getGridLineColor());
    }
}
