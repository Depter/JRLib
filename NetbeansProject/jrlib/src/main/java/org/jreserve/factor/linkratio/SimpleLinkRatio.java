package org.jreserve.factor.linkratio;

import org.jreserve.AbstractCalculationData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatio extends AbstractCalculationData<FactorTriangle> implements LinkRatio {

    private LinkRatioMethod method;
    private int developmentCount;

    public SimpleLinkRatio(Triangle triangle) {
        this(new DevelopmentFactors(triangle), null);
    }

    public SimpleLinkRatio(Triangle triangle, LinkRatioMethod method) {
        this(new DevelopmentFactors(triangle), method);
    }
    
    public SimpleLinkRatio(FactorTriangle source) {
        this(source, null);
    }
    
    public SimpleLinkRatio(FactorTriangle source, LinkRatioMethod method) {
        super(source);
        this.method = (method==null)? new WeightedAverageLRMethod() : method;
        doRecalculate();
    }
    
    @Override
    public FactorTriangle getSourceFactors() {
        return source;
    }

    @Override
    public int getDevelopmentCount() {
        return developmentCount;
    }

    @Override
    public double getValue(int development) {
        if(development>=0 && development < developmentCount)
            return method.getValue(development);
        return Double.NaN;
    }

    @Override
    public double[] toArray() {
        double[] result = new double[developmentCount];
        for(int d=0; d<developmentCount; d++)
            result[d] = method.getValue(d);
        return result;
    }

    @Override
    public double getMackAlpha(int development) {
        return method.getMackAlpha();
    }
    
    @Override
    public double getWeight(int accident, int development) {
        return method.getWeight(accident, development);
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        if(source == null) {
            developmentCount = 0;
        } else {
            developmentCount = source.getDevelopmentCount();
            method.fit(source);
        }
    }
}