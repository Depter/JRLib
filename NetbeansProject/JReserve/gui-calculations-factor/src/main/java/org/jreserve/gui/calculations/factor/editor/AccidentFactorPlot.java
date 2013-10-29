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
package org.jreserve.gui.calculations.factor.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.plot.ChartCopiable;
import org.jreserve.gui.plot.ColorGenerator;
import org.jreserve.gui.plot.PlotLabel;
import org.jreserve.gui.plot.PlotSerie;
import org.jreserve.gui.plot.DefaultColorGenerator;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AccidentFactorPlot {
    
    private FactorTriangleCalculation calculation;
    private DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
    
    private List<PlotLabel> aLabels = new ArrayList<PlotLabel>();
    private List<PlotLabel> dLabels = new ArrayList<PlotLabel>();
    private Map<Integer, PlotSerie<PlotLabel, PlotLabel>> series = new HashMap<Integer, PlotSerie<PlotLabel, PlotLabel>>();
    
    private JFreeChart chart;
    private CategoryItemRenderer renderer;
    private Component component;
    
    AccidentFactorPlot(FactorTriangleCalculation calculation) {
        this.calculation = calculation;
        calculatePlotData();
        component = createPlotComponent();
    }
    
    private void calculatePlotData() {
        clearData();
        FactorTriangle factors = calculation.getCalculation();
        TriangleGeometry geometry = calculation.getSource().getGeometry();
        
        int developments = factors.getDevelopmentCount();
        for(int d=0; d<developments; d++)
            dLabels.add(new PlotLabel(d, String.format("%d / %d", d+1, d+2)));
        
        int accidents = factors.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            PlotLabel aLabel = new PlotLabel(a, geometry.getAccidentDate(a).toString());
            aLabels.add(aLabel);
            PlotSerie<PlotLabel, PlotLabel> serie = new PlotSerie<PlotLabel, PlotLabel>(aLabel);
            
            int devs = factors.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                serie.addEntry(dLabels.get(d), factors.getValue(a, d));
        }
    }
    
    private void clearData() {
        dataSet.clear();
        aLabels.clear();
        dLabels.clear();
        series.clear();
    }
    
    private Component createPlotComponent() {
        
        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;
        chart = ChartFactory.createLineChart("title", "categoryAxis", "valueAxis", 
                dataSet, PlotOrientation.VERTICAL, legend, tooltips, urls);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.GRAY);
        renderer = plot.getRenderer();
        if(renderer instanceof LineAndShapeRenderer) {
            LineAndShapeRenderer lasr = (LineAndShapeRenderer) renderer;
            lasr.setBaseShapesVisible(true);
            lasr.setDrawOutlines(true);
            lasr.setUseFillPaint(true);
            lasr.setBaseStroke(new BasicStroke(2));

            ColorGenerator colors = new DefaultColorGenerator();
            int count = aLabels.size();
            for(int i=0; i<count; i++) {
                Paint color = colors.nextColor();
                lasr.setSeriesPaint(i, color);
                lasr.setSeriesFillPaint(i, color);
            }
        
        }
        
        return new ChartPanel(chart);
    }
    
    Component getPlot() {
        return component;
    }
    
    void setSeriesVisible(int index, boolean visible) {
        renderer.setSeriesVisible(index, visible);
    }
    
    void recalculate() {
        this.calculatePlotData();
    }
    
    List<PlotLabel> getAccidents() {
        return aLabels;
    }
    
    ClipboardUtil.Copiable createCopiable() {
        return new ChartCopiable(chart, component);
    }
}
