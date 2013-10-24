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
package org.jreserve.gui.calculations.factor.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractModifiableCalculationProvider;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
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
    private final static String CATEGORY = "FactorTriangle";
    
    final static String FACTOR_ELEMENT = "factors";
    private final static String SOURCE_ELEMENT = "claimTriangle";
    
    private final FactorDataObject dObj;
    private final FactorBundleImpl bundle;
    private ClaimTriangleCalculation source;
    private FactorTriangle factors;
    
    FactorTriangleCalculationImpl(FactorDataObject obj, Element root, FactorBundleImpl bundle) throws Exception {
        super(obj, root, CATEGORY);
        this.dObj = obj;
        this.bundle = bundle;
        
        initSource(root);
        recalculate();
    }
    
    private void initSource(Element root) throws IOException {
        source = super.lookupSource(ClaimTriangleCalculation.class, root, SOURCE_ELEMENT);
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
    public FactorTriangle getCalculation() throws Exception {
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
    public synchronized Element toXml() {
        Element root = new Element(FACTOR_ELEMENT);
        JDomUtil.addElement(root, SOURCE_ELEMENT, source.getPath());
        root.addContent(super.toXml());
        return root;
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
