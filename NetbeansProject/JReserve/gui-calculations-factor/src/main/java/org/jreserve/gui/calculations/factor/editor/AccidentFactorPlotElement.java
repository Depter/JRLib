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

import java.awt.Component;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.calculations.factor.impl.FactorDataObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.AccidentFactorPlotElement.Title",
    mimeType = FactorDataObject.MIME_TYPE,
    position = 300,
    prefferedID = "org.jreserve.gui.calculations.factor.editor.AccidentFactorPlotElement",
    background = "#COLOR.AccidentFactorPlotElement.Background",
    iconBase = "org/jreserve/gui/plot/icons/chart_line.png"
)
@Messages({
    "LBL.AccidentFactorPlotElement.Title=Accident Periods",
    "COLOR.AccidentFactorPlotElement.Background=FF7D30"
})
public class AccidentFactorPlotElement extends AbstractExpandableElement {

    private FactorTriangleCalculation calculation;
    private InstanceContent ic = new InstanceContent();
    private ClipboardUtil.Copiable copiable;
    private Lookup lkp;
    private AccidentFactorPlotComponent panel;

    public AccidentFactorPlotElement() {
        this(Lookup.EMPTY);
    }
    
    public AccidentFactorPlotElement(Lookup context) {
        FactorBundle bundle = context.lookup(FactorBundle.class);
        this.calculation = bundle.getFactors();
        this.lkp = new AbstractLookup(ic);
    }
    

    @Override
    public Lookup getLookup() {
        return lkp;
    }
    
    @Override
    protected Component createVisualComponent() {
        if(panel == null) {
            panel = new AccidentFactorPlotComponent(calculation);
            initCopiable();
        }
        return panel;
    }
    
    private void initCopiable() {
        if(copiable != null)
            ic.remove(copiable);
        copiable = panel.createCopiable();
        ic.add(copiable);
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
            panel.recalculate();
        }
    }  
}
