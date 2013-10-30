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

import org.jreserve.gui.calculations.factor.impl.factors.FactorTriangleCalculationImpl;
import org.jreserve.gui.calculations.factor.impl.linkratio.LinkRatioCalculationImpl;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractCalculationProvider;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.LinkRatioSECalculation;
import org.jreserve.gui.calculations.factor.LinkRatioScaleCalculation;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.CalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorBundleImpl 
    extends AbstractCalculationProvider<CalculationData> 
    implements FactorBundle {

    public final static String ROOT_ELEMENT = "factorBundle";
    public final static String AUDIT_ID_ELEMENT = "auditId";
//    public final static String SOURCE_ELEMENT = "source";
//    public final static String FACTORS_ELEMENT = "developmentFactors";
//    public final static String LINK_RATIO_ELEMENT = "linkRatios";
//    public final static String TAIL_ELEMENT = "linkRatioTail";
//    public final static String SCALE_ELEMENT = "linkRatioScale";
//    public final static String SE_ELEMENT = "linkRatioSE";
    
    private final long auditId;
    private final FactorTriangleCalculationImpl factors;
    private final LinkRatioCalculationImpl linkRatios;
    
    public FactorBundleImpl(FactorDataObject obj, Element root) throws Exception {
        super(obj);
        auditId = JDomUtil.getExistingLong(root, AUDIT_ID_ELEMENT);
        
        Element cr = JDomUtil.getExistingChild(root, FactorTriangleCalculationImpl.ROOT_ELEMENT);
        factors = new FactorTriangleCalculationImpl(obj, cr, this);
        
        cr = JDomUtil.getExistingChild(root, LinkRatioCalculationImpl.ROOT_ELEMENT);
        linkRatios = new LinkRatioCalculationImpl(obj, cr, this);
        
    }
    
    @Override
    protected Element toXml() {
        Element root = new Element(ROOT_ELEMENT);
        root.addContent(factors.toXml());
        root.addContent(linkRatios.toXml());
        return root;
    }

    @Override
    public long getAuditId() {
        return auditId;
    }
    
    @Override
    public Class<CalculationData> getCalculationClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CalculationData getCalculation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FactorTriangleCalculationImpl getFactors() {
        return factors;
    }

    @Override
    public LinkRatioCalculationImpl getLinkRatio() {
        return linkRatios;
    }

    @Override
    public LinkRatioScaleCalculation getScale() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkRatioSECalculation getStandardError() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void fireCreated() {
        events.fireCreated();
        factors.fireCreated();
        linkRatios.fireCreated();
        //TODO scales.fireCreated();
        //TODO ses.fireCreated();
    }

    @Override
    protected CalculationData createDummyCalculation() {
        return null;
    }

    @Override
    protected Calculator<CalculationData> createCalculator() {
        return null;
    }
}
