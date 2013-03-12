package org.jreserve.factor.linkratio.scale;

import org.jreserve.AbstractCalculationData;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioScale extends AbstractCalculationData<LinkRatioScaleCaclulator> implements LinkRatioScale {

    private int developments;
    private double[] values;
    private LinkRatioScaleEstimator estimator;
    
    public SimpleLinkRatioScale(LinkRatio lrs) {
        this(lrs, new LinkRatioScaleMinMaxEstimator());
    }
    
    public SimpleLinkRatioScale(LinkRatio lrs, LinkRatioScaleEstimator estimator) {
        this(new LinkRatioScaleCaclulator(lrs), estimator);
    }
    
    public SimpleLinkRatioScale(LinkRatioScaleCaclulator calculator) {
        this(calculator, new LinkRatioScaleMinMaxEstimator());
    }
    
    public SimpleLinkRatioScale(LinkRatioScaleCaclulator calculator, LinkRatioScaleEstimator estimator) {
        super(calculator);
        this.estimator = estimator;
        doRecalculate();
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
    public Triangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public double getValue(int development) {
        if(development >= 0 && development < developments)
            return values[development];
        return Double.NaN;
    }

    @Override
    public double[] toArray() {
        double[] result = new double[developments];
        if(developments > 0)
            System.arraycopy(values, 0, result, 0, developments);
        return result;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        developments = source.getDevelopmentCount();
        values = source.toArray();
        estimator.fit(source);
        fillNaNs();
    }
    
    private void fillNaNs() {
        for(int d=0; d<developments; d++)
            if(Double.isNaN(values[d]))
                values[d] = estimator.getValue(d);
    }
}
