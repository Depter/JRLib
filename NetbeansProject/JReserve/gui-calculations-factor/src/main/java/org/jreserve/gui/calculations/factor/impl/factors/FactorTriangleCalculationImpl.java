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
package org.jreserve.gui.calculations.factor.impl.factors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.modification.AbstractModifiableCalculationProvider;
import org.jreserve.gui.calculations.api.CalculationContents;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.api.modification.CalculationModifier;
import org.jreserve.gui.calculations.api.modification.triangle.TriangleModifier;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.calculations.factor.impl.BundleUtils;
import org.jreserve.gui.calculations.factor.impl.FactorBundleImpl;
import org.jreserve.gui.calculations.factor.impl.FactorDataObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.trianglewidget.DefaultTriangleLayer;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.triangle.factor.ModifiedFactorTriangle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - sourcePath",
    "LBL.FactorTriangleCalculationImpl.DataSource.Change=Claim triangle set to ''{0}''.",
    "MSG.FactorTriangleCalculationImpl.Calculation.Error=Unable to calculate factor triangle!",
    "LBL.FactorTriangleCalculationImpl.Layer.Base=Input"
})
public class FactorTriangleCalculationImpl 
    extends AbstractModifiableCalculationProvider<FactorTriangle> 
    implements FactorTriangleCalculation {

    private final static Logger logger = Logger.getLogger(FactorTriangleCalculationImpl.class.getName());
    public final static String CATEGORY = "FactorTriangle";
    
    public final static String ROOT_ELEMENT = "developmentFactors";
    public final static String SOURCE_ELEMENT = "source";
    
    private final FactorDataObject dObj;
    private final FactorBundleImpl bundle;
    private ClaimTriangleCalculation source;
    private FactorTriangle factors;
    
    public FactorTriangleCalculationImpl(FactorDataObject obj, Element root, FactorBundleImpl bundle) throws Exception {
        super(obj, root, CATEGORY);
        this.dObj = obj;
        this.bundle = bundle;
        
        initSource(root);
        recalculate();
    }
    
    private void initSource(Element root) throws IOException {
        String path = JDomUtil.getExistingString(root, SOURCE_ELEMENT);
        source = CalculationContents.getCalculation(getProject(), path, ClaimTriangleCalculation.class);
        if(source == null)
            source = ClaimTriangleCalculation.EMPTY;
    }
    
    private void recalculate() {
        TaskUtil.execute(new RecalculateTask());
    }
    
    @Override
    public ClaimTriangleCalculation getSource() {
        synchronized(lock) {
            return source;
        }
    }
    
    @Override
    public void setSource(ClaimTriangleCalculation source) {
        synchronized(lock) {
            if(source == null)
                throw new NullPointerException("ClaimTriangleCalculation is null!");
            this.source = source;
            dObj.setModified(true);
            recalculate();
            events.fireChange(Bundle.LBL_FactorTriangleCalculationImpl_DataSource_Change(source.getPath()));
        }
    }

    @Override
    public FactorBundle getBundle() {
        return bundle;
    }

    @Override
    public Class<FactorTriangle> getCalculationClass() {
        return FactorTriangle.class;
    }

    @Override
    public FactorTriangle getCalculation() {
        synchronized(lock) {
            if(factors == null) {
                recalculate();
                return BundleUtils.createEmptyFactors();
            }
            return factors;
        }
    }

    @Override
    public FactorTriangle getCalculation(int layer) {
        synchronized(lock) {
            try {
                ClaimTriangle claims = source.getCalculation();
                FactorTriangle result = new DevelopmentFactors(claims);
                return super.modifyCalculation(result, layer);
            } catch (Exception ex) {
                String msg = "Unable to calculate claim triangle!";
                logger.log(Level.SEVERE, msg, ex);
                String title = Bundle.MSG_FactorTriangleCalculationImpl_Calculation_Error();
                BubbleUtil.showException(title, getPath(), ex);
                return BundleUtils.createEmptyFactors();
            }
        }
    }
    
    @Override
    protected void modificationsChanged() {
        recalculate();
        events.fireChange();
        this.dObj.setModified(true);
    }

    @Override
    public long getAuditId() {
        return bundle.getAuditId();
    }
    
    @Override
    public Element toXml() {
        synchronized(lock) {
            Element root = new Element(ROOT_ELEMENT);
            JDomUtil.addElement(root, SOURCE_ELEMENT, source.getPath());
            root.addContent(super.toXml());
            return root;
        }
    }
    
    public void fireCreated() {
        synchronized(lock) {
            events.fireCreated();
        }
    }
    
    public List<TriangleLayer> createLayers() {
        synchronized(lock) {
            List<FactorTriangle> triangles = getCalculationLayers();
            int size = triangles.size();
            List<TriangleLayer> result = new ArrayList<TriangleLayer>(size);
            result.add(createBaseLayer(triangles.get(0)));
            
            for(int i=1; i<size; i++) {
                TriangleModifier modifier = (TriangleModifier) getModificationAt(i-1);
                FactorTriangle layer = triangles.get(i);
                result.add(modifier.createLayer(layer));
            }
            return result;
        }
    }
    
    private List<FactorTriangle> getCalculationLayers() {
        FactorTriangle layer = factors;
        int count = getModificationCount();
        List<FactorTriangle> result = new ArrayList<FactorTriangle>(count+1);
        
        for(int i=0; i<count; i++) {
            result.add(0, layer);
            layer = ((ModifiedFactorTriangle)layer).getSourceFactorTriangle();
        }
        result.add(0, layer);
        
        return result;
    }
    
    private TriangleLayer createBaseLayer(FactorTriangle input) {
        String name = Bundle.LBL_FactorTriangleCalculationImpl_Layer_Base();
        return new DefaultTriangleLayer(input, name);
    }
    
    @EventBusListener
    public void sourceChanged(CalculationEvent.ValueChanged evt) {
        if(source == evt.getCalculationProvider()) {
            recalculate();
            events.fireChange();
        }
    } 
    
    private class RecalculateTask implements Runnable {
        
        private final ClaimTriangleCalculation source;
        private final List<CalculationModifier<FactorTriangle>> modifications;
        
        private RecalculateTask() {
            synchronized(lock) {
                this.source = FactorTriangleCalculationImpl.this.source;
                this.modifications = new ArrayList<CalculationModifier<FactorTriangle>>(FactorTriangleCalculationImpl.this.modifications);
            }
        }
        
        @Override
        public void run() {
            final FactorTriangle result = calculateResult();
            synchronized(lock) {
                factors = result;
                events.fireValueChanged();
            }
        }
        
        private FactorTriangle calculateResult() {
            try {
                ClaimTriangle input = this.source.getCalculation();
                FactorTriangle result = new DevelopmentFactors(input);
                for(CalculationModifier<FactorTriangle> cm : this.modifications)
                    result = cm.createCalculation(result);
                return result;
            } catch (Exception ex) {
                String msg = "Unable to calculate vector!";
                logger.log(Level.SEVERE, msg, ex);
                String title = Bundle.MSG_FactorTriangleCalculationImpl_Calculation_Error();
                BubbleUtil.showException(title, getPath(), ex);
                return BundleUtils.createEmptyFactors();
            }
        }
    }
}
