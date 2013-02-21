package org.jreserve.factor.curve;

import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.util.AbstractMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSmoothing extends AbstractMethodSelection<LinkRatio, LinkRatioFunction> implements LinkRatioSmoothingSelection {

    private int developments;
    private double[] values;
    
    public DefaultLinkRatioSmoothing(LinkRatio source) {
        this(source, null);
    }
    
    public DefaultLinkRatioSmoothing(LinkRatio source, LinkRatioFunction defaultFunction) {
        super(source, defaultFunction==null? new DefaultLRFunction() : defaultFunction);
        developments = (source==null)? 0 : source.getDevelopmentCount();
        doRecalculate();
    }
    
    @Override
    public FactorTriangle getInputFactors() {
        return source.getInputFactors();
    }

    @Override
    public void setDefaultMethod(LinkRatioFunction defaultMethod) {
        if(defaultMethod == null)
            defaultMethod = new DefaultLRFunction();
        super.setDefaultMethod(defaultMethod);
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
            values[d] = getMethod(d).getValue(d+1);
    }
    
    private void recalculateLinkRatios() {
        for(LinkRatioFunction function : getMethods())
            function.fit(source);
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

    @Override
    public double getMackAlpha(int development) {
        return source.getMackAlpha(development);
    }
}
