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
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.plot.ChartCopiable;
import org.jreserve.gui.plot.PlotLabel;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DevelopmentFactorPlot {
    private final static Color FACTOR = Color.RED;
    private final static Color LINK_RATIO = Color.BLUE;
    
    private FactorTriangleCalculation calculation;
    private DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
    private List<PlotLabel> dLabels = new ArrayList<PlotLabel>();
    private int developments;
    
    private JFreeChart chart;
    private CategoryItemRenderer renderer;
    private Component component;
    
    DevelopmentFactorPlot(FactorTriangleCalculation calculation) {
        this.calculation = calculation;
        calculatePlotData();
        component = createPlotComponent();
    }
    
    private void calculatePlotData() {
        clearData();
        FactorTriangle factors = calculation.getCalculation();
        SimpleLinkRatio lrs = new SimpleLinkRatio(factors);
        TriangleGeometry geometry = calculation.getSource().getGeometry();
        developments = factors.getDevelopmentCount();
        
        createDevelopmentLabels();
        List<PlotLabel> lrLabels = createLRLabels();
        
        int accidents = factors.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            PlotLabel aLabel = new PlotLabel(a, geometry.getAccidentDate(a).toString());
            
            int devs = factors.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                PlotLabel dLabel = dLabels.get(d);
                PlotLabel lrLabel = lrLabels.get(d);
                
                double factor = factors.getValue(a, d);
                double lr = lrs.getValue(d);
                
                dataSet.addValue(factor, dLabel, aLabel);
                dataSet.addValue(lr, lrLabel, aLabel);
            }
        }
    }
    
    private void clearData() {
        dLabels.clear();
        dataSet.clear();
        developments = 0;
    }
    
    private void createDevelopmentLabels() {
        for(int d=0; d<developments; d++) {
            String label = ""+(d+1) + " / " + (d+2);
            dLabels.add(new PlotLabel(d, label));
        }
    }
    
    private List<PlotLabel> createLRLabels() {
        List<PlotLabel> result = new ArrayList<PlotLabel>(developments);
        for(int d=0; d<developments; d++) {
            String label = "LR "+(d+1) + " / " + (d+2);
            result.add(new PlotLabel(developments+d, label));
        }
        return result;
    }
    
    private Component createPlotComponent() {
        
        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;
        chart = ChartFactory.createLineChart(null, null, null, 
                dataSet, PlotOrientation.VERTICAL, legend, tooltips, urls);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        
        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setAutoRangeIncludesZero(false);
        axis.setAutoRangeStickyZero(true);
        
        renderer = plot.getRenderer();
        if(renderer instanceof LineAndShapeRenderer) {
            LineAndShapeRenderer lasr = (LineAndShapeRenderer) renderer;
            lasr.setBaseShapesVisible(true);
            lasr.setDrawOutlines(true);
            lasr.setUseFillPaint(true);
            lasr.setBaseStroke(new BasicStroke(2));

            
            int r = 3;
            Shape circle = new Ellipse2D.Float(-r, -r, 2*r, 2*r);
            int count = dataSet.getRowCount();
            for(int i=0; i<count; i++) {
                PlotLabel label = (PlotLabel) dataSet.getRowKey(i);
                boolean isLr = label.getId() >= developments;
                
                Color color = isLr? LINK_RATIO : FACTOR;
                lasr.setSeriesPaint(i, color);
                lasr.setSeriesFillPaint(i, color);
                lasr.setSeriesShape(i, circle);
                
                lasr.setSeriesShapesVisible(i, !isLr);
                lasr.setSeriesLinesVisible(i, isLr);
            }
        }
        
        return new ChartPanel(chart);
    }
    
    Component getPlot() {
        return component;
    }
    
    void showSeries(int index) {
        int size = dataSet.getRowCount();
        for(int row=0; row<size; row++) {
            int id = ((PlotLabel) dataSet.getRowKey(row)).getId();
            boolean visible = (id==index || (id-developments)==index); 
            renderer.setSeriesVisible(row, visible);
        }
    }
    
    void recalculate() {
        this.calculatePlotData();
    }
    
    List<PlotLabel> getDevelopments() {
        return dLabels;
    }
    
    ClipboardUtil.Copiable createCopiable() {
        return new ChartCopiable(chart, component);
    }

}
