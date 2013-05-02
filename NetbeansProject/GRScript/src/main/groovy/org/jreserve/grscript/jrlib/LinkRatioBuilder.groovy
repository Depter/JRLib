package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.linkratio.*
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors

/**
 *
 * @author Peter Decsi
 */
class LinkRatioBuilder {
    
    private DefaultLinkRatioSelection lrs
    private java.util.Map<Integer, LinkRatioMethod> methods = new HashMap<Integer, LinkRatioMethod>()
    private Map methodInstances = [:]
    
    LinkRatioBuilder(FactorTriangle factors) {
        WeightedAverageLRMethod method = new WeightedAverageLRMethod()
        methodInstances[WeightedAverageLRMethod.class] = method
        lrs = new DefaultLinkRatioSelection(factors, method);
    }
    
    LinkRatio getLinkRatios() {
        return lrs
    }
    
    void average(int index) {
        LinkRatioMethod method = getCachedMethod(AverageLRMethod.class) {new AverageLRMethod()}
        lrs.setMethod(method, index)
    }
    
    private LinkRatioMethod getCachedMethod(Class clazz, Closure cl) {
        LinkRatioMethod method = methodInstances[clazz]
        if(method == null) {
            method = cl()
            methodInstances[clazz] = method
        }
        return method
    }
    
    void average(Collection<Integer> indices) {
        for(i in indices)
            average(i)
    }
    
    void mack(int index) {
        LinkRatioMethod method = getCachedMethod(MackRegressionLRMethod.class) {new MackRegressionLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void mack(Collection<Integer> indices) {
        for(i in indices)
            mack(i)
    }
    
    void max(int index) {
        LinkRatioMethod method = getCachedMethod(MaxLRMethod.class) {new MaxLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void max(Collection<Integer> indices) {
        for(i in indices)
            max(i)
    }

    void min(int index) {
        LinkRatioMethod method = getCachedMethod(MinLRMethod.class) {new MinLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void min(Collection<Integer> indices) {
        for(i in indices)
            min(i)
    }

    void weightedAverage(int index) {
        LinkRatioMethod method = getCachedMethod(WeightedAverageLRMethod.class) {new WeightedAverageLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void weightedAverage(Collection<Integer> indices) {
        for(i in indices)
            weightedAverage(i)
    }
    
    void fixed(int index, double value) {
        LinkRatioMethod method = getCachedMethod(UserInputLRMethod.class) {new UserInputLRMethod()}
        (method as UserInputLRMethod).setValue(index, value)
        lrs.setMethod(method, index)
    }
    
    void fixed(Collection<Integer> indices, double... values) {
        int count = 0
        for(i in indices)
            fixed(i, values[count++])
    }
}

