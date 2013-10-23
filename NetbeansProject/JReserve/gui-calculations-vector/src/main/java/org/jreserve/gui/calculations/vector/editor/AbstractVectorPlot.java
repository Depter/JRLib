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
package org.jreserve.gui.calculations.vector.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.vector.impl.VectorCalculationImpl;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.plot.ChartWrapper;
import org.jreserve.gui.plot.PlotFactory;
import org.jreserve.gui.plot.PlotFormat;
import org.jreserve.gui.plot.PlotLabel;
import org.jreserve.gui.plot.PlotSerie;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.vector.Vector;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractVectorPlot extends AbstractExpandableElement {

    private JPanel panel;
    private ChartWrapper plot;
    private Component plotComponent;
    protected VectorCalculationImpl calculation;
    private InstanceContent ic = new InstanceContent();
    private ClipboardUtil.Copiable copiable;
    private Lookup lkp;

    protected Vector vector;
    protected TriangleGeometry geometry;
    protected int accidents;
    
    protected AbstractVectorPlot(Lookup context) {
        this.calculation = context.lookup(VectorCalculationImpl.class);
        this.lkp = new AbstractLookup(ic);
    }

    @Override
    public Lookup getLookup() {
        return lkp;
    }
    
    @Override
    protected Component createVisualComponent() {
        if(panel == null) {
            panel = new JPanel(new BorderLayout());
            initPlot();
        }
        return panel;
    }
    
    private void initPlot() {
        plot = createPlot();
        initPlotComponent();
        initCopiable();
    }

    private ChartWrapper createPlot() {
        List<PlotSerie> series;
        if(calculation == null) {
            series = Collections.EMPTY_LIST;
        } else {
            initInputData();
            series = createSeries();
        }
        
        PlotFormat format = createFormat();
        clearCalculations();
        return PlotFactory.createLinePlot(format, series);
    }
    
    private void initInputData() {
        vector = calculation.getCalculation();
        accidents = vector.getLength();
        geometry = calculation.getGeometry();
    }

    protected abstract List<PlotSerie> createSeries();
    
    protected PlotLabel createAccidentLabel(int accident) {
        MonthDate date = geometry.getAccidentDate(accident);
        String name = date.toString();
        return new PlotLabel(accident, name);
    }

    private PlotFormat createFormat() {
        return new PlotFormat()
                .setSeriesNames("values")
                .setLegendVisible(false);
    }

    private void clearCalculations() {
        this.vector = null;
        this.geometry = null;
        this.accidents = 0;
    }
    
    private void initCopiable() {
        if(copiable != null)
            ic.remove(copiable);
        copiable = plot.createCopiable();
        ic.add(copiable);
    }
    
    private void initPlotComponent() {
        if(plotComponent != null)
            panel.remove(plotComponent);
        plotComponent = plot.getChartComponent();
        panel.add(plotComponent);
    }
    
    @Override
    protected boolean openMaximized() {
        return false;
    }

    @Override
    public void componentOpened() {
        super.componentOpened();
        EventBusManager.getDefault().subscribe(this);
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
        EventBusManager.getDefault().unsubscribe(this);
    }
    
    @EventBusListener(forceEDT = true)
    public void calculationChanged(CalculationEvent.ValueChanged evt) {
        if(panel!=null && calculation==evt.getCalculationProvider()) {
            initPlot();
            panel.revalidate();
            panel.repaint();
        }
    }  
    
}
