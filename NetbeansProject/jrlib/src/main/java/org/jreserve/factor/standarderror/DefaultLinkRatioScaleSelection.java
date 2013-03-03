package org.jreserve.factor.standarderror;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.util.AbstractMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioScaleSelection extends AbstractMethodSelection<LinkRatioScale, LinkRatioScaleEstimator> implements LinkRatioScaleSelection {
    
    private int developments;
    private double[] values;
    
    public DefaultLinkRatioScaleSelection(LinkRatio lrs) {
        this(new LinkRatioScaleCaclulator(lrs), null);
    }
    
    public DefaultLinkRatioScaleSelection(LinkRatioScale source) {
        this(source, source.getDevelopmentCount(), null);
    }
    
    public DefaultLinkRatioScaleSelection(LinkRatioScale source, LinkRatioScaleEstimator defaultMethod) {
        this(source, source.getDevelopmentCount(), defaultMethod);
    }
    
    public DefaultLinkRatioScaleSelection(LinkRatioScale source, int developmentCount, LinkRatioScaleEstimator defaultMethod) {
        super(source, defaultMethod==null? new EmptyLinkRatioScaleEstimator() : defaultMethod);
        this.developments = developmentCount<0? 0 : developmentCount;
        doRecalculate();
    }

    @Override
    public LinkRatio getLinkRatios() {
        return source.getLinkRatios();
    }

    @Override
    public void setDefaultMethod(LinkRatioScaleEstimator defaultMethod) {
        if(defaultMethod == null)
            defaultMethod = new EmptyLinkRatioScaleEstimator();
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
            values[d] = getMethod(d).getValue(d);
    }
    
    private void recalculateLinkRatios() {
        for(LinkRatioScaleEstimator estimator : getMethods())
            estimator.fit(source);
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
