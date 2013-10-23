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
package org.jreserve.gui.calculations.claimtriangle.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import javax.swing.JPanel;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.plot.ChartWrapper;
import org.jreserve.gui.plot.PlotLabel;
import org.jreserve.gui.plot.PlotSerie;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractPlotElement extends AbstractExpandableElement {

    private JPanel panel;
    private ChartWrapper plot;
    private Component plotComponent;
    protected ClaimTriangleCalculationImpl calculation;
    private InstanceContent ic = new InstanceContent();
    private ClipboardUtil.Copiable copiable;
    private Lookup lkp;
    
    protected AbstractPlotElement(Lookup context) {
        this.calculation = context.lookup(ClaimTriangleCalculationImpl.class);
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

    protected abstract ChartWrapper createPlot();
    
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
    
    protected String[] getSeriesNames(List<PlotSerie> series) {
        int size = series.size();
        String[] names = new String[size];
        for(int a=0; a<size; a++)
            names[a] = ((PlotLabel)series.get(a).getKey()).getName();
        return names;
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
