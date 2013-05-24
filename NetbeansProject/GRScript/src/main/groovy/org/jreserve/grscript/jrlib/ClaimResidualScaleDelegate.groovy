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

package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.residuals.InputOdpResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.scale.ConstantOdpResidualScale
import org.jreserve.jrlib.bootstrap.odp.scale.DefaultOdpResidualScaleSelection
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale
import org.jreserve.jrlib.bootstrap.odp.scale.VariableOdpResidualScale
import org.jreserve.jrlib.bootstrap.odp.scale.UserInputOdpSMethod
import org.jreserve.jrlib.bootstrap.odp.scale.SimpleOdpRSEstimate
import org.jreserve.jrlib.bootstrap.odp.scale.EmptyOdpResidualScale
import org.jreserve.jrlib.bootstrap.odp.scale.OdpRSMethod
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimResidualScaleDelegate extends AbstractDelegate {
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.constantScale << this.&constantScale
        emc.variableScale << this.&variableScale
    }
    
    OdpResidualScale constantScale(LinkRatio lrs) {
        return new ConstantOdpResidualScale(lrs)
    }
    
    OdpResidualScale constantScale(OdpResidualTriangle residuals) {
        return new ConstantOdpResidualScale(residuals)
    }
    
    OdpResidualScale constantScale(LinkRatio lrs, double scale) {
        this.constantScale(new InputOdpResidualTriangle(lrs), scale)
    }
    
    OdpResidualScale constantScale(OdpResidualTriangle residuals, double scale) {
        int length = residuals.getDevelopmentCount()
        UserInputOdpSMethod method = new UserInputOdpSMethod()
        for(int d=0; d<length; d++)
            method.setValue(d, scale)
        OdpResidualScale scales = new EmptyOdpResidualScale(residuals)
        return new DefaultOdpResidualScaleSelection(scales, method)
    }
    
    OdpResidualScale variableScale(OdpResidualTriangle residuals) {
        return new VariableOdpResidualScale(residuals)
    }
    
    OdpResidualScale variableScale(LinkRatio lrs) {
        return new VariableOdpResidualScale(lrs)
    }
    
    OdpResidualScale variableScale(OdpResidualTriangle residuals, Closure cl) {
        Builder builder = new Builder(residuals)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.scales
    }
    
    OdpResidualScale variableScale(LinkRatio lrs, Closure cl) {
        Builder builder = new Builder(lrs)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.scales
    }
    
    private class Builder extends AbstractMethodSelectionBuilder<OdpRSMethod> {
        private DefaultOdpResidualScaleSelection scales
        
        Builder(LinkRatio lrs) {
            OdpResidualScale s = new VariableOdpResidualScale(lrs)
            scales = new DefaultOdpResidualScaleSelection(s)
        }
        
        Builder(OdpResidualTriangle residuals) {
            OdpResidualScale s = new VariableOdpResidualScale(residuals)
            scales = new DefaultOdpResidualScaleSelection(s)
        }
    
        void fixed(int index, double value) {
            OdpRSMethod estimator = getCachedMethod(UserInputOdpSMethod.class) {new UserInputOdpSMethod()}
            ((UserInputOdpSMethod)estimator).setValue(index, value)
            scales.setMethod(estimator, index)
        }
    
        void fixed(Map map) {
            map.each {index, value -> fixed(index, value)}
        }        
    }
}

