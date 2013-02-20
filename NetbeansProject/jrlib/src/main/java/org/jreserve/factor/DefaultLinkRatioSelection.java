package org.jreserve.factor;

import java.util.HashMap;
import java.util.Map;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.util.AbstractMethodSelection;
        
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSelection extends AbstractMethodSelection<Triangle, LinkRatioMethod> implements LinkRatioSelection {
    
    private int developments;
    private double[] values;
    
    public DefaultLinkRatioSelection(Triangle source) {
        this(source, null);
    }
    
    public DefaultLinkRatioSelection(Triangle source, LinkRatioMethod defaultMethod) {
        super(source, defaultMethod==null? new WeightedAverageLRMethod() : defaultMethod);
        doRecalculate();
    }

    @Override
    public void setDefaultMethod(LinkRatioMethod defaultMethod) {
        if(defaultMethod == null)
            defaultMethod = new WeightedAverageLRMethod();
        super.setDefaultMethod(defaultMethod);
    }
    
    @Override
    public Triangle getDevelopmentFactors() {
        return getSource();
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