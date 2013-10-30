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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationContents;
import org.jreserve.gui.calculations.api.CalculationEvent;
import org.jreserve.gui.calculations.api.modification.triangle.AbstractModifiableTriangleProvider;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.calculations.factor.impl.BundleUtils;
import org.jreserve.gui.calculations.factor.impl.FactorBundleImpl;
import org.jreserve.gui.calculations.factor.impl.FactorDataObject;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
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
    extends AbstractModifiableTriangleProvider<FactorTriangle> 
    implements FactorTriangleCalculation {

    private final static Logger logger = Logger.getLogger(FactorTriangleCalculationImpl.class.getName());
    public final static String CATEGORY = "FactorTriangle";
    
    public final static String ROOT_ELEMENT = "developmentFactors";
    public final static String SOURCE_ELEMENT = "source";
    
    private final FactorDataObject dObj;
    private final FactorBundleImpl bundle;
    private ClaimTriangleCalculation source;
    
    public FactorTriangleCalculationImpl(FactorDataObject obj, Element root, FactorBundleImpl bundle) throws Exception {
        super(obj, root, CATEGORY);
        EventBusManager.getDefault().subscribe(this);
        
        this.dObj = obj;
        this.bundle = bundle;
        
        initSource(root);
    }
    
    private void initSource(Element root) throws IOException {
        String path = JDomUtil.getExistingString(root, SOURCE_ELEMENT);
        source = CalculationContents.getCalculation(getProject(), path, ClaimTriangleCalculation.class);
        if(source == null)
            source = ClaimTriangleCalculation.EMPTY;
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
            recalculateIfExists();
            events.fireChange(Bundle.LBL_FactorTriangleCalculationImpl_DataSource_Change(source.getPath()));
        }
    }
    
    public TriangleGeometry getGeometry() {
        synchronized(lock) {
            return source.getGeometry();
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
    
    @EventBusListener
    public void sourceChanged(CalculationEvent.ValueChanged evt) {
        if(source == evt.getCalculationProvider()) {
            recalculateIfExists();
            events.fireChange();
        }
    } 

    @Override
    protected ModificationCalculator createCalculator() {
        return new RecalculateTask();
    }

    @Override
    protected FactorTriangle createDummyCalculation() {
        return BundleUtils.createEmptyFactors();
    }
    
    private class RecalculateTask extends ModificationCalculator {
        private final ClaimTriangle claims;
                
        private RecalculateTask() {
            synchronized(lock) {
                this.claims = FactorTriangleCalculationImpl.this.source.getCalculation();
            }
        }
        
        @Override
        protected FactorTriangle getRootCalculation() {
            try {
                return new DevelopmentFactors(claims);
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
