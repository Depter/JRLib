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
package org.jreserve.gui.calculations.factor.impl.linkratio;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.method.AbstractMethodCalculationProvider;
import org.jreserve.gui.calculations.api.method.CalculationMethod;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.LinkRatioCalculation;
import org.jreserve.gui.calculations.factor.impl.BundleUtils;
import org.jreserve.gui.calculations.factor.impl.FactorBundleImpl;
import org.jreserve.gui.calculations.factor.impl.FactorDataObject;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.jreserve.jrlib.linkratio.DefaultLinkRatioSelection;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.LinkRatioMethod;
import org.jreserve.jrlib.linkratio.UserInputLRMethod;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.AbstractVectorUserInput;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.LinkRatioCalculationImpl.Calculation.Error=Unable to calculate link ratios!"
})
public class LinkRatioCalculationImpl 
    extends AbstractMethodCalculationProvider<LinkRatio, LinkRatioMethod>
    implements LinkRatioCalculation {
    
    private final static Logger logger = Logger.getLogger(LinkRatioCalculationImpl.class.getName());
    
    private final static String CATEGORY = "LinkRatio";
    public final static String ROOT_ELEMENT = "linkRatio";
    
    private final FactorBundleImpl bundle;
    private final WeightedAverageCalculationMethod defaultMethod = 
            new WeightedAverageCalculationMethod();
    private final UserInputLRMethod fixedMethod =
            new UserInputLRMethod();
    
    private LinkRatio linkRatios;
    
    public LinkRatioCalculationImpl(FactorDataObject obj, Element root, FactorBundleImpl bundle) throws Exception {
        super(obj, root, CATEGORY);
        this.bundle = bundle;
    }
    
    private void recalculate() {
        TaskUtil.execute(new RecalculateTask());
    }

    @Override
    protected CalculationMethod<LinkRatioMethod> getDefaultMethod() {
        return defaultMethod;
    }

    @Override
    protected AbstractVectorUserInput<FactorTriangle> getFixedMethod() {
        return fixedMethod;
    }

    @Override
    protected void methodsChanged() {
        recalculate();
    }

    @Override
    protected Element toXml() {
        return super.toXml(ROOT_ELEMENT);
    }

    @Override
    public Class<LinkRatio> getCalculationClass() {
        return LinkRatio.class;
    }

    @Override
    public LinkRatio getCalculation() throws Exception {
        synchronized(lock) {
            if(linkRatios == null) {
                recalculate();
                return BundleUtils.createEmptyLinkRatios();
            }
            return linkRatios;
        }
    }

    @Override
    public long getAuditId() {
        return bundle.getAuditId();
    }

    @Override
    public FactorBundle getBundle() {
        return bundle;
    }
    
    public void fireCreated() {
        synchronized(lock) {
            events.fireCreated();
        }
    }
    
    private class RecalculateTask implements Runnable {
        
        private RecalculateTask() {
        }
        
        @Override
        public void run() {
            synchronized(lock) {
                linkRatios = calculateResult();
                events.fireValueChanged();
            }
        }
        
        private LinkRatio calculateResult() {
            try {
                FactorTriangle factors = bundle.getFactors().getCalculation();
                LinkRatioMethod defMethod = defaultMethod.createMethod();
                DefaultLinkRatioSelection sel = new DefaultLinkRatioSelection(factors, defMethod);
                sel.setMethods(createMethods());
                return sel;
            } catch (Exception ex) {
                String msg = "Unable to calculate vector!";
                logger.log(Level.SEVERE, msg, ex);
                String title = Bundle.MSG_LinkRatioCalculationImpl_Calculation_Error();
                BubbleUtil.showException(title, getPath(), ex);
                return BundleUtils.createEmptyLinkRatios();
            }
        }
    }
}
