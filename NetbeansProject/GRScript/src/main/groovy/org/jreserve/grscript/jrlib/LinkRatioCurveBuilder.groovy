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
class LinkRatioCurveBuilder {
    
    DefaultLinkRatioSmoothing smoothing;
    
    LinkRatioCurveBuilder(LinkRatio lrs, int length) {
        smoothing = new DefaultLinkRatioSmoothing(lrs)
        smoothing.setDevelopmentCount(length)
    }
    
    void exponential(Closure cl) {
        delegate(cl, new ExponentialLRCurve())
    }
    
    void power(Closure cl) {
        delegate(cl, new PowerLRCurve())
    }
    
    void inversePower(Closure cl) {
        delegate(cl, new InversePowerLRCurve())
    }
    
    void weibul(Closure cl) {
        delegate(cl, new WeibulLRCurve())
    }
    
    private void delegate(Closure cl, LinkRatioCurve curve) {
        CurveBuilder builder = new CurveBuilder(smoothing, curve)
        cl.delegate = cl
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }
    
    void fixed(Closure cl) {
        FixedBuilder builder = new FixedBuilder(smoothing)
        cl.delegate = cl
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
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
                ((Excludeable)curve).setExcluded(exclude, true)
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

    class FixedBuilder {
        private DefaultLinkRatioSmoothing smoothing
        private UserInputLRCurve curve = new UserInputLRCurve()
        private MapUtil mapUtil = MapUtil.getInstance()
        
        FixedBuilder(DefaultLinkRatioSmoothing smoothing) {
            this.smoothing = smoothing;
        }
        
        void cell(int development, double value) {
            curve.setValue(development, value)
            smoothing.setMethod(curve, development)
        }
        
        void cell(Map map) {
            int development = mapUtil.getDevelopment(map) 
            double value = mapUtil.getDouble(map, "value", "v")
            cell(development, value)
        }
    }
}