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

import org.jreserve.gui.calculations.factor.impl.linkratio.LinkRatioCalculationImpl;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.AbstractCalculationProvider;
import org.jreserve.gui.calculations.factor.FactorBundle;
import org.jreserve.gui.calculations.factor.FactorTriangleCalculation;
import org.jreserve.gui.calculations.factor.LinkRatioCalculation;
import org.jreserve.gui.calculations.factor.LinkRatioSECalculation;
import org.jreserve.gui.calculations.factor.LinkRatioScaleCalculation;
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
    public final static String SOURCE_ELEMENT = "source";
    public final static String FACTORS_ELEMENT = "developmentFactors";
    public final static String LINK_RATIO_ELEMENT = "linkRatios";
    public final static String TAIL_ELEMENT = "linkRatioTail";
    public final static String SCALE_ELEMENT = "linkRatioScale";
    public final static String SE_ELEMENT = "linkRatioSE";
    
    public FactorBundleImpl(FactorDataObject obj, Element root) throws Exception {
        super(obj);
    }
    
    @Override
    protected Element toXml() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getAuditId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Class<CalculationData> getCalculationClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CalculationData getCalculation() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FactorTriangleCalculationImpl getFactors() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkRatioCalculationImpl getLinkRatio() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
