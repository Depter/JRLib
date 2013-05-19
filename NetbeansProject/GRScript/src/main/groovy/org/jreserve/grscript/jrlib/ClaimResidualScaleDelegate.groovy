
package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle
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

