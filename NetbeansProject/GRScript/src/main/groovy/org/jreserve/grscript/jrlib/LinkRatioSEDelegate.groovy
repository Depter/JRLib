package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale
import org.jreserve.jrlib.linkratio.standarderror.LinkRatioSE
import org.jreserve.jrlib.linkratio.standarderror.DefaultLinkRatioSESelection
import org.jreserve.jrlib.linkratio.standarderror.FixedRatioSEFunction
import org.jreserve.jrlib.linkratio.standarderror.LinkRatioSEFunction
import org.jreserve.jrlib.linkratio.standarderror.LogLinearRatioSEFunction
import org.jreserve.jrlib.linkratio.standarderror.SimpleLinkRatioSE
import org.jreserve.jrlib.linkratio.standarderror.UserInputLinkRatioSEFunction
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioSEDelegate extends AbstractDelegate {
    
    private final static String STANDARD_METHOD = "Log-Linear";
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.standardError    << this.&standardError
    }
    
    LinkRatioSE standardError(LinkRatioScale scales) {
        return standardError(scales, STANDARD_METHOD)
    }
    
    LinkRatioSE standardError(LinkRatioScale scales, String type) {
        LinkRatioSEFunction function = createFunction(type)
        return new SimpleLinkRatioSE(scales, function)
    }
    
    private LinkRatioSEFunction createFunction(String type) {
        switch(type?.toLowerCase()) {
            case "loglin":
            case "log lin":
            case "log-lin":
            case "loglinear":
            case "log linear":
            case "log-linear":
                return new LogLinearRatioSEFunction()
            case "fixedrate":
            case "fixed rate":
            case "fixed-rate":
                return new FixedRatioSEFunction()
            default:
                String msg = "Unknow LinkRatioSEFunction type: ${type}! "+
                "Valid types are ([LogLin | Log-Lin | Log Lin | LogLinear "+
                "Log Linear | Log-Linear], [FixedRate | Fixed Rate | Fixed-Rate]).";
                throw new IllegalArgumentException(msg)
        }
    }
    
    LinkRatioSE standardError(LinkRatioScale scales, Closure cl) {
        Builder builder = new Builder(scales)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.ses
    }
    
    private class Builder extends AbstractMethodSelectionBuilder<LinkRatioSEFunction> {
        
        DefaultLinkRatioSESelection ses;
        
        private Builder(LinkRatioScale scales) {
            ses = new DefaultLinkRatioSESelection(scales)
        }
        
        void logLinear(int index) {
            LinkRatioSEFunction function = getCachedMethod(LogLinearRatioSEFunction.class) {new LogLinearRatioSEFunction()}
            ses.setMethod(function, index)
        }
        
        void logLinear(Collection indices) {
            indices.each() {logLinear(it)}
        }
        
        void fixedRate(int index) {
            LinkRatioSEFunction function = getCachedMethod(FixedRatioSEFunction.class) {new FixedRatioSEFunction()}
            ses.setMethod(function, index)
        }
        
        void fixedRate(Collection indices) {
            indices.each(){fixedRate(it)}
        }
        
        void fixed(int index, double value) {
            UserInputLinkRatioSEFunction function = getCachedMethod(UserInputLinkRatioSEFunction.class) {new UserInputLinkRatioSEFunction()}
            function.setValue(index, value)
            ses.setMethod(function, index)
        }
        
        void fixed(Map map) {
            map.each {index, value -> fixed(index, value)}
        }
    }
}