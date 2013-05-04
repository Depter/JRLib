package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing
import org.jreserve.jrlib.linkratio.curve.LinkRatioCurve
import org.jreserve.jrlib.linkratio.curve.ExponentialLRCurve
import org.jreserve.jrlib.linkratio.curve.PowerLRCurve
import org.jreserve.jrlib.linkratio.curve.InversePowerLRCurve
import org.jreserve.jrlib.linkratio.curve.WeibulLRCurve
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.util.method.Excludeable
import org.jreserve.grscript.util.MapUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioCurveBuilder extends AbstractMethodSelectionBuilder<LinkRatioCurve> {
    
    DefaultLinkRatioSmoothing smoothing;
    
    LinkRatioCurveBuilder(LinkRatio lrs, int length) {
        smoothing = new DefaultLinkRatioSmoothing(lrs)
        smoothing.setDevelopmentCount(length)
    }
    
    void exponential(Closure cl) {
        LinkRatioCurve curve = getCachedMethod(ExponentialLRCurve.class) {new ExponentialLRCurve()}
        delegate(cl, curve)
    }
    
    void power(Closure cl) {
        LinkRatioCurve curve = getCachedMethod(PowerLRCurve.class) {new PowerLRCurve()}
        delegate(cl, curve)
    }
    
    void inversePower(Closure cl) {
        LinkRatioCurve curve = getCachedMethod(InversePowerLRCurve.class) {new InversePowerLRCurve()}
        delegate(cl, curve)
    }
    
    void weibul(Closure cl) {
        LinkRatioCurve curve = getCachedMethod(WeibulLRCurve.class) {new WeibulLRCurve()}
        delegate(cl, curve)
    }
    
    private void delegate(Closure cl, LinkRatioCurve curve) {
        CurveBuilder builder = new CurveBuilder(smoothing, curve)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }
    
    void fixed(int index, double value) {
        UserInputLRCurve curve = (UserInputLRCurve) getCachedMethod(UserInputLRCurve.class) {new UserInputLRCurve()}
        curve.setValue(index, value)
        smoothing.setMethod(curve, index)
    }
    
    void fixed(Map map) {
        map.each {index, value -> fixed(index, value)}
    }
    
    class CurveBuilder {
        private LinkRatioCurve curve
        private DefaultLinkRatioSmoothing smoothing;
        
        private CurveBuilder(DefaultLinkRatioSmoothing smoothing, LinkRatioCurve curve) {
            this.smoothing = smoothing
            this.curve = curve
        }
        
        void apply(int development) {
            smoothing.setMethod(curve, development)
        }
        
        void apply(Collection developments) {
            developments.each() {apply(it)}
        }
        
        void exclude(int development) {
            if(curve instanceof Excludeable) {
                ((Excludeable)curve).setExcluded(development, true)
            } else {
                throwNotExcludeable(type)
            }
        }
    
        private void throwNotExcludeable(String type) {
            String msg = "Can not exclude input values from ${type}!"
            throw new IllegalArgumentException(msg)
        }
        
        void exclude(Collection developments) {
            if(curve instanceof Excludeable) {
                Excludeable e = (Excludeable) curve;
                developments.each() {e.setExcluded(it, true)}
            } else {
                throwNotExcludeable(type)
            }
        }
    }
}