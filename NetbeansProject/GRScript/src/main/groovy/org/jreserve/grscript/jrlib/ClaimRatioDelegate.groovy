package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.triangle.ratio.RatioTriangle
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle
import org.jreserve.jrlib.claimratio.ClaimRatio
import org.jreserve.jrlib.claimratio.ClaimRatioMethod
import org.jreserve.jrlib.claimratio.DefaultClaimRatioSelection
import org.jreserve.jrlib.claimratio.LrCrExtrapolation
import org.jreserve.jrlib.claimratio.UserInputCRMethod
import org.jreserve.jrlib.claimratio.SimpleClaimRatio
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.grscript.util.MapUtil
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClaimRatioDelegate extends AbstractDelegate {
    
    private MapUtil mapUtil = MapUtil.getInstance()
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.ratios << this.&ratios
    }
    
    ClaimRatio ratios(ClaimTriangle numerator, ClaimTriangle denominator) {
        return ratios(new DefaultRatioTriangle(numerator, denominator))
    }
    
    ClaimRatio ratios(RatioTriangle ratios) {
        return new SimpleClaimRatio(ratios)
    }
    
    ClaimRatio ratios(LinkRatio numerator, LinkRatio denominator) {
        ClaimTriangle n = numerator.getSourceTriangle()
        ClaimTriangle d = denominator.getSourceTriangle()
        RatioTriangle ratios = new DefaultRatioTriangle(n, d)
        return this.ratios(ratios, numerator, denominator)
    }
    
    ClaimRatio ratios(Map map) {
        def numerator = mapUtil.getValue(map, "numerator", "num", "n")
        def denominator = mapUtil.getValue(map, "denominator", "denom", "d")
        return ratios(numerator, denominator)
    }
    
    ClaimRatio ratios(RatioTriangle ratios, LinkRatio numerator, LinkRatio denominator) {
        int length = Math.min(numerator.getLength(), denominator.getLength())
        return this.ratios(ratios, length, numerator, denominator)
    }
    
    ClaimRatio ratios(RatioTriangle ratios, int length, LinkRatio numerator, LinkRatio denominator) {
        LrCrExtrapolation method = new LrCrExtrapolation(numerator, denominator)
        DefaultClaimRatioSelection crs = new DefaultClaimRatioSelection(ratios)
        crs.setDevelopmentCount(length)
        
        for(int i=ratios.getDevelopmentCount(); i<length; i++) 
        crs.setMethod(method, i)
        
        return crs
    }
    
    ClaimRatio ratios(LinkRatio numerator, LinkRatio denominator, Closure cl) {
        int length = Math.min(numerator.getLength(), denominator.getLength())
        return this.ratios(numerator, denominator, length, cl)
    }
    
    ClaimRatio ratios(LinkRatio numerator, LinkRatio denominator, int length, Closure cl) {
        ClaimTriangle n = numerator.getSourceTriangle()
        ClaimTriangle d = denominator.getSourceTriangle()
        RatioTriangle ratios = new DefaultRatioTriangle(n, d)
        return this.ratios(ratios, length, cl)
    }
    
    ClaimRatio ratios(RatioTriangle ratios, int length, Closure cl) {
        Builder builder = new Builder(this, ratios, length)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.crs
    }
    
    class Builder extends AbstractMethodSelectionBuilder<ClaimRatioMethod> {
        
        DefaultClaimRatioSelection crs;
        ClaimRatioDelegate ratioDelegate;
        
        private Builder(ClaimRatioDelegate ratioDelegate, RatioTriangle ratios, int length) {
            this.ratioDelegate = ratioDelegate
            crs = new DefaultClaimRatioSelection(ratios)
            crs.setDevelopmentCount(length)
        }
        
        def getProperty(String name) {
            return getProperties().containsKey(name)?
                super.getProperty(name) :
                this.ratioDelegate.getProperty(name)
        }
        
        void lrExtrapolation(LinkRatio numerator, LinkRatio denominator, Collection indices) {
            LrCrExtrapolation method = new LrCrExtrapolation(numerator, denominator)
            indices.each {crs.setMethod(method, it)}
        }
        
        void lrExtrapolation(LinkRatio numerator, LinkRatio denominator, int... indices) {
            LrCrExtrapolation method = new LrCrExtrapolation(numerator, denominator)
            indices.each {crs.setMethod(method, it)}
        }
        
        
        void fixed(int index, double value) {
            UserInputCRMethod method = getCachedMethod(UserInputCRMethod.class) {new UserInputCRMethod()}
            method.setValue(index, value)
            crs.setMethod(method, index)
        }
        
        void fixed(Map map) {
            map.each {index, value -> fixed(index, value)}
        }
    }
}