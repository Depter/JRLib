package org.jreserve.factor;

import java.util.HashMap;
import java.util.Map;
import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;
        
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSelection extends AbstractCalculationData<Triangle> implements LinkRatioSelection {

    private LinkRatioMethod[] methods = new LinkRatioMethod[0];
    private LinkRatioMethod defaultMethod = new WeightedAverageLRMethod();
    
    private int developments;
    private double[] values;
    
    public DefaultLinkRatioSelection(Triangle source) {
        super(source);
        doRecalculate();
    }

    @Override
    public Triangle getDevelopmentFactors() {
        return getSource();
    }
    
    @Override
    public LinkRatioMethod getDefaultMethod() {
        return defaultMethod;
    }
    
    @Override
    public void setDefaultMethod(LinkRatioMethod method) {
        if(method == null)
            method = new WeightedAverageLRMethod();
        this.defaultMethod = method;
    }
    
    @Override
    public void setMethod(LinkRatioMethod method, int development) {
        if(development >= 0) {
            saveMethodAt(method, development);
            doRecalculate();
            fireChange();
        }
    }
    
    private void saveMethodAt(LinkRatioMethod method, int development) {
        if(method == null) {
            if(development < methods.length)
                methods[development] = null;
        } else {
            setMethodsSize(development);
            methods[development] = method;
        }
    }
    
    private void setMethodsSize(int development) {
        if(development >= methods.length) {
            LinkRatioMethod[] redim = new LinkRatioMethod[development+1];
            System.arraycopy(methods, 0, redim, 0, methods.length);
            methods = redim;
        }
    }
    
    @Override
    public void setMethods(Map<Integer, LinkRatioMethod> methods) {
        for(Integer development : methods.keySet())
            if(development > 0)
                saveMethodAt(methods.get(development), development);
        doRecalculate();
        fireChange();
    }
    
    @Override
    public LinkRatioMethod getMethod(int development) {
        if(development < 0)
            throw new IllegalArgumentException("Development must be at least 0, but was "+development+"!");
        LinkRatioMethod method = (development<methods.length)? methods[development] : null;
        return method==null? defaultMethod : method;
    }
    
    @Override
    public int getDevelopmentCount() {
        return source==null? 0 : source.getDevelopmentCount();
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initState();
        if(developments > 0)
            recalculateLinkRatios();
    }
    
    private void initState() {
        developments = (source==null)? 0 : source.getDevelopmentCount();
        values = new double[developments];
    }
    
    private void recalculateLinkRatios() {
        Map<LinkRatioMethod, double[]> cahce = cahceLRs();
        for(int d=0; d<developments; d++) {
            LinkRatioMethod method = getMethod(d);
            values[d] = cahce.get(method)[d];
        }
    }
    
    private Map<LinkRatioMethod, double[]> cahceLRs() {
        Map<LinkRatioMethod, double[]> cache = new HashMap<LinkRatioMethod, double[]>();
        for(int d=0; d<developments; d++) {
            LinkRatioMethod method = getMethod(d);
            if(!cache.containsKey(method))
                cache.put(method, method.getLinkRatios(source));
        }
        return cache;
    }
    
    @Override
    public double getValue(int development) {
        if(withinBound(development))
            return values[development];
        return Double.NaN;
    }
    
    private boolean withinBound(int development) {
        return development >= 0 &&
               development < developments;
    }
    
    @Override
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }
    
    @Override
    public double getMackAlpha(int development) {
        if(withinBound(development))
            return getMethod(development).getMackAlpha();
        return Double.NaN;
    }
}