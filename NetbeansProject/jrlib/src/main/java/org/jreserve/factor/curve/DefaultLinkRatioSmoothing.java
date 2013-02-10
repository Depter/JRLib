package org.jreserve.factor.curve;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jreserve.AbstractCalculationData;
import org.jreserve.factor.LinkRatio;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSmoothing extends AbstractCalculationData<LinkRatio> implements LinkRatioSmoothing {

    private LinkRatioFunction[] functions = new LinkRatioFunction[0];
    private LinkRatioFunction defaultFunction = new DefaultLRFunction();
    
    private int developments;
    private double[] values;
    
    public DefaultLinkRatioSmoothing(LinkRatio source) {
        super(source);
        developments = (source==null)? 0 : source.getDevelopmentCount();
        doRecalculate();
    }
    
    @Override
    public LinkRatioFunction getDefaultFunction() {
        return defaultFunction;
    }
    
    @Override
    public void setDefaultFunction(LinkRatioFunction function) {
        if(function == null)
            function = new DefaultLRFunction();
        this.defaultFunction = function;
    }
    
    @Override
    public void setFunction(LinkRatioFunction function, int development) {
        if(development >= 0) {
            saveFunctionAt(function, development);
            doRecalculate();
            fireChange();
        }
    }
    
    private void saveFunctionAt(LinkRatioFunction function, int development) {
        if(function == null) {
            if(development < functions.length)
                functions[development] = null;
        } else {
            setFunctionsSize(development);
            functions[development] = function;
        }
    }
    
    private void setFunctionsSize(int development) {
        if(development >= functions.length) {
            LinkRatioFunction[] redim = new LinkRatioFunction[development+1];
            System.arraycopy(functions, 0, redim, 0, functions.length);
            functions = redim;
        }
    }
    
    @Override
    public void setFunctions(Map<Integer, LinkRatioFunction> functions) {
        for(Integer development : functions.keySet())
            if(development >= 0)
                saveFunctionAt(functions.get(development), development);
        doRecalculate();
        fireChange();
    }
    
    @Override
    public LinkRatioFunction getFunction(int development) {
        if(development < 0)
            throw new IllegalArgumentException("Development must be at least 0, but was "+development+"!");
        LinkRatioFunction function = (development<functions.length)? functions[development] : null;
        return function==null? defaultFunction : function;
    }
    
    @Override
    public int getDevelopmentCount() {
        return developments;
    }
    
    @Override
    public void setDevelopmentCount(int developments) {
        this.developments = (developments<0)? 0 : developments;
        doRecalculate();
        fireChange();
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        values = new double[developments];
        recalculateLinkRatios();
        for(int d=0; d<developments; d++)
            values[d] = getFunction(d).getValue(d+1);
    }
    
    private void recalculateLinkRatios() {
        Set<LinkRatioFunction> cache = new HashSet<LinkRatioFunction>();
        for(LinkRatioFunction function : functions) {
            if(function != null && !cache.contains(function)) {
                cache.add(function);
                function.fit(source);
            }
        }
        if(!cache.contains(defaultFunction))
            defaultFunction.fit(source);
    }
    
    @Override
    public double getValue(int development) {
        if(development < 0)
            return Double.NaN;
        if(development < developments)
            return values[development];
        return 1d;
    }
    
    @Override
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }
}
