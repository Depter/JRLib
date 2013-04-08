package org.jreserve.linkratio.smoothing;

import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
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
    
    private DefaultLinkRatioSmoothing(DefaultLinkRatioSmoothing original) {
        super(original);
        this.developments = original.developments;
        doRecalculate();
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source;
    }
    
    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }
    
    @Override
    public void setSource(FactorTriangle factors) {
        source.setSource(factors);
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
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
        super.fitMethods();
        values = super.getFittedValues(developments);
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
    
    @Override
    public double getWeight(int accident, int development) {
        return source.getWeight(accident, development);
    }
    
    @Override
    public DefaultLinkRatioSmoothing copy() {
        return new DefaultLinkRatioSmoothing(this);
    }
}