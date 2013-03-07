package org.jreserve.factor.linkratio.standarderror;

import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.util.AbstractMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSESelection extends AbstractMethodSelection<LinkRatioSE, LinkRatioSEFunction> implements LinkRatioSESelection {
    
    private int developments;
    private double[] values;

    public DefaultLinkRatioSESelection(LinkRatioScale scales) {
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

    @Override
    public void setDefaultMethod(LinkRatioSEFunction defaultMethod) {
        if(defaultMethod == null)
            defaultMethod = new DefaultLinkRatioSEFunction();
        super.setDefaultMethod(defaultMethod);
    }

    @Override
    public LinkRatioScale getSourceLRScales() {
        return source.getSourceLRScales();
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
}