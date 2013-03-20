package org.jreserve.linkratio.smoothing;

import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioSmoothing extends AbstractCalculationData<LinkRatio> implements LinkRatioSmoothing {
    
    private int developments;
    private double[] values;
    private LinkRatioFunction function;

    public SimpleLinkRatioSmoothing(LinkRatio source, LinkRatioFunction function) {
        this(source, source.getDevelopmentCount(), function);
    }

    public SimpleLinkRatioSmoothing(LinkRatio source, int developments, LinkRatioFunction function) {
        super(source);
        this.developments = developments<0? 0 : developments;
        initFunction(function);
        doRecalculate();
    }
    
    private void initFunction(LinkRatioFunction function) {
        if(function == null)
            throw new NullPointerException("Function can not be null!");
        this.function = function;
    }
    
    @Override
    public void setDevelopmentCount(int developments) {
        this.developments = (developments<0)? 0 : developments;
        doRecalculate();
        fireChange();
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
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    public double getValue(int development) {
        if(development < 0)
            return Double.NaN;
        if(development < developments)
            return values[development];
        return 1d;
    }

    @Override
    public double[] toArray() {
        double[] copy = new double[developments];
        System.arraycopy(values, 0, copy, 0, developments);
        return copy;
    }

    @Override
    public double getMackAlpha(int development) {
        int maxDev = source.getDevelopmentCount();
        development = maxDev<development? maxDev : development;
        return source.getMackAlpha(development);
    }
    
    @Override
    public double getWeight(int accident, int development) {
        return source.getWeight(accident, development);
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        values = new double[developments];
        if(developments > 0)
            fillValues();
    }
    
    private void fillValues() {
        function.fit(source);
        for(int d=0; d<developments; d++) {
            double s = source.getValue(d);
            values[d] = Double.isNaN(s)? function.getValue(d) : s;
        }
    }
    
    @Override
    public String toString() {
        return String.format("SimpleLinkRatioSmoothing [%s]", function);
    }
    
    @Override
    public SimpleLinkRatioSmoothing copy() {
        return new SimpleLinkRatioSmoothing(source.copy(), 
                developments, 
                function.copy()
                );
    }
}
