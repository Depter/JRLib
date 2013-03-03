package org.jreserve.factor.standarderror;

import org.jreserve.util.AbstractMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSESelection extends AbstractMethodSelection<LinkRatioScale, LinkRatioSEFunction> implements LinkRatioSESelection {
    
    private int developments;
    private double[] values;

    public DefaultLinkRatioSESelection(LinkRatioScale source) {
        this(source, null);
    }
    
    public DefaultLinkRatioSESelection(LinkRatioScale source, LinkRatioSEFunction defaultMethod) {
        super(source, defaultMethod==null? new DefaultLinkRatioSEFunction() : defaultMethod);
        this.developments = source.getDevelopmentCount();
        doRecalculate();
    }

    @Override
    public void setDefaultMethod(LinkRatioSEFunction defaultMethod) {
        if(defaultMethod == null)
            defaultMethod = new DefaultLinkRatioSEFunction();
        super.setDefaultMethod(defaultMethod);
    }

    @Override
    public LinkRatioScale getLinkRatioScales() {
        return getSource();
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
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return values[development];
    }

    @Override
    public double[] toArray() {
        double[] copy = new double[developments];
        System.arraycopy(values, 0, copy, 0, developments);
        return copy;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        values = new double[developments];
        recalculateLinkRatiosSES();
        for(int d=0; d<developments; d++)
            values[d] = getMethod(d).getValue(d);
    }
    
    private void recalculateLinkRatiosSES() {
        for(LinkRatioSEFunction estimator : getMethods())
            estimator.fit(source);
    }
}