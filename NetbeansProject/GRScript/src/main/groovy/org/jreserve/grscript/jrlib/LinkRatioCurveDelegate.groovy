package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.curve.SimpleLinkRatioSmoothing
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing
import org.jreserve.jrlib.linkratio.curve.LinkRatioCurve
import org.jreserve.jrlib.linkratio.curve.ExponentialLRCurve
import org.jreserve.jrlib.linkratio.curve.PowerLRCurve
import org.jreserve.jrlib.linkratio.curve.InversePowerLRCurve
import org.jreserve.jrlib.linkratio.curve.WeibulLRCurve
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.util.method.Excludeable
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothing
import org.jreserve.jrlib.util.RegressionUtil
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioCurveDelegate extends AbstractDelegate {
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.smooth    << this.&smooth
        emc.smoothAll << this.&smoothAll
        emc.rSquare   << this.&rSquare
    }
    
    LinkRatio smooth(LinkRatio lrs) {
        return new SimpleLinkRatioSmoothing(lrs, getDefaultCurve())
    }
    
    private LinkRatioCurve getDefaultCurve() {
        return new ExponentialLRCurve()
    }
    
    LinkRatio smooth(LinkRatio lrs, int length) {
        return createSmooth(lrs, length, getDefaultCurve())
    }
    
    private LinkRatio createSmooth(LinkRatio lrs, int length, LinkRatioCurve curve) {
        SimpleLinkRatioSmoothing smoothing = new SimpleLinkRatioSmoothing(lrs, curve)
        smoothing.setDevelopmentCount(length)
        return smoothing;
    }
    
    LinkRatio smooth(LinkRatio lrs, int length, String type) {
        LinkRatioCurve curve = createCurve(type);
        return createSmooth(lrs, length,  curve)
    }
    
    private LinkRatioCurve createCurve(String type) {
        switch(type.toLowerCase()) {
            case "exp":
            case "exponential":
                return new ExponentialLRCurve();
            case "pow":
            case "power":
                return new PowerLRCurve();
            case "inversepower":
            case "inv.pow":
            case "inverse-power":
                return new InversePowerLRCurve();
            case "wei":
            case "weibul":
                return new WeibulLRCurve();
            default:
                String msg = "Unknow LR-Curve type: ${type}! Valid types are "+
                "exponential, power, inverse-power and weibul.";
                throw new IllegalArgumentException(msg)
        }
    }
    
    LinkRatio smooth(LinkRatio lrs, int length, String type, int exclude) {
        LinkRatioCurve curve = createCurve(type, exclude)
        return createSmooth(lrs, length,  curve)
    }
    
    private LinkRatioCurve createCurve(String type, int exclude) {
        LinkRatioCurve curve = createCurve(type)
        if(curve instanceof Excludeable) {
            ((Excludeable)curve).setExcluded(exclude, true)
            return curve;
        } else {
            throwNotExcludeable(type)
        }
    }
    
    private void throwNotExcludeable(String type) {
        String msg = "Can not exclude input values from ${type}!"
        throw new IllegalArgumentException(msg)
    }
    
    LinkRatio smooth(LinkRatio lrs, int length, String type, Collection exclude) {
        LinkRatioCurve curve = createCurve(type, exclude)
        return createSmooth(lrs, length,  curve)
    }
    
    private LinkRatioCurve createCurve(String type, Collection exclude) {
        LinkRatioCurve curve = createCurve(type)
        if(curve instanceof Excludeable) {
            Excludeable e = (Excludeable) curve;
            exclude.each() {e.setExcluded(it, true)}
            return e;
        } else {
            throwNotExcludeable(type)
        }
    }

    LinkRatio smoothAll(LinkRatio lrs) {
        return new DefaultLinkRatioSmoothing(lrs, getDefaultCurve())
    }
    
    LinkRatio smoothAll(LinkRatio lrs, int length) {
        return createSmoothAll(lrs, length, getDefaultCurve())
    }
    
    private LinkRatio createSmoothAll(LinkRatio lrs, int length, LinkRatioCurve curve) {
        DefaultLinkRatioSmoothing smooth = new DefaultLinkRatioSmoothing(lrs, curve)
        smooth.setDevelopmentCount(length)
        return smooth
    }
    
    LinkRatio smoothAll(LinkRatio lrs, int length, String type) {
        LinkRatioCurve curve = createCurve(type);
        return createSmoothAll(lrs, length,  curve)
    }
    
    LinkRatio smoothAll(LinkRatio lrs, int length, String type, int exclude) {
        LinkRatioCurve curve = createCurve(type, exclude)
        return createSmoothAll(lrs, length,  curve)
    }
    
    LinkRatio smoothAll(LinkRatio lrs, int length, String type, Collection exclude) {
        LinkRatioCurve curve = createCurve(type, exclude)
        return createSmoothAll(lrs, length,  curve)
    }
    
    LinkRatio smooth(LinkRatio lrs, int length, Closure cl) {
        LinkRatioCurveBuilder builder = new LinkRatioCurveBuilder(lrs, length)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.getSmoothing()
    }
    
    double rSquare(LinkRatioSmoothing smoothing) {
        LinkRatio lrs = smoothing.getSourceLinkRatios()
        int size = Math.min(lrs.getLength(), smoothing.getLength())
        
        double[] x = new double[size]
        double[] y = new double[size]
        for(i in 0..<size) {
            x[i] = lrs.getValue(i)
            y[i] = smoothing.getValue(i)
        }
        
        return RegressionUtil.rSquare(x, y)
    }
}