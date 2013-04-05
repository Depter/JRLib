package org.jreserve.linkratio.standarderror;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.scale.RatioScale;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.util.AbstractMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSESelection extends AbstractMethodSelection<LinkRatioSE, LinkRatioSEFunction> implements LinkRatioSESelection {
    
    private int developments;
    private double[] values;

    public DefaultLinkRatioSESelection(RatioScale<LinkRatioScaleInput> scales) {
        this(new LinkRatioSECalculator(scales), null);
    }

    public DefaultLinkRatioSESelection(LinkRatioSE source) {
        this(source, null);
    }
    
    public DefaultLinkRatioSESelection(LinkRatioSE source, LinkRatioSEFunction defaultMethod) {
        super(source, defaultMethod==null? new DefaultLinkRatioSEFunction() : defaultMethod);
        this.developments = source.getDevelopmentCount();
        doRecalculate();
    }

    private DefaultLinkRatioSESelection(DefaultLinkRatioSESelection original) {
        super(original);
        this.developments = original.developments;
        doRecalculate();
    }
    
    @Override
    public void setDefaultMethod(LinkRatioSEFunction defaultMethod) {
        if(defaultMethod == null)
            defaultMethod = new DefaultLinkRatioSEFunction();
        super.setDefaultMethod(defaultMethod);
    }

    @Override
    public RatioScale<LinkRatioScaleInput> getSourceLRScales() {
        return source.getSourceLRScales();
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }
    
    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
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
        developments = (source==null)? 0 : source.getDevelopmentCount();
        super.fitMethods();
        values = super.getFittedValues(developments);
    }
    
    @Override
    public DefaultLinkRatioSESelection copy() {
        return new DefaultLinkRatioSESelection(this);
    }
}