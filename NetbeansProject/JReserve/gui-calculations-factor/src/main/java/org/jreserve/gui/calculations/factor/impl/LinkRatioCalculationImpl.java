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

import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractMethodCalculationProvider;
import org.jreserve.gui.calculations.api.CalculationMethod;
import org.jreserve.gui.calculations.factor.impl.linkratio.WeightedAverageCalculationMethod;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.LinkRatioCalculation;
import org.jreserve.jrlib.linkratio.DefaultLinkRatioSelection;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.LinkRatioMethod;
import org.jreserve.jrlib.linkratio.UserInputLRMethod;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.AbstractVectorUserInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioCalculationImpl 
    extends AbstractMethodCalculationProvider<LinkRatio, FactorTriangle>
    implements LinkRatioCalculation {
    
    private final static String CATEGORY = "LinkRatio";
    private final static String LINK_RATIO_ELEMENT = "linkRatio";
    
    private final FactorBundleImpl bundle;
    private final WeightedAverageCalculationMethod defaultMethod = 
            new WeightedAverageCalculationMethod();
    private final UserInputLRMethod fixedMethod =
            new UserInputLRMethod();
    
    LinkRatioCalculationImpl(FactorDataObject obj, Element root, FactorBundleImpl bundle) throws Exception {
        super(obj, root, CATEGORY);
        this.bundle = bundle;
        recalculate();
    }
    
    private void recalculate() {
        TaskUtil.execute(new RecalculateTask());
    }

    @Override
    protected CalculationMethod<FactorTriangle> getDefaultMethod() {
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
        return super.toXml(LINK_RATIO_ELEMENT);
    }

    @Override
    public Class<LinkRatio> getCalculationClass() {
        return LinkRatio.class;
    }

    @Override
    public LinkRatio getCalculation() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getAuditId() {
        return bundle.getAuditId();
    }

    @Override
    public FactorBundle getBundle() {
        return bundle;
    }
    
    private class RecalculateTask implements Runnable {
        
        private RecalculateTask() {
            synchronized(lock) {
                
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
                DefaultLinkRatioSelection sel = createSelection();
                set
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
        
        private DefaultLinkRatioSelection createSelection() throws Exception {
            FactorTriangle factors = bundle.getFactors().getCalculation();
            LinkRatioMethod defMethod = defaultMethod.createMethod(factors);
            return new DefaultLinkRatioSelection(factors, defMethod);
        }
    }
}
