package org.jreserve.factor;

import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatio extends AbstractCalculationData<Triangle> implements LinkRatio {

    private LinkRatioMethod method;
    
    private int developmentCount;
    private double[] values = new double[0];

    public SimpleLinkRatio(Triangle source) {
        this(source, null);
    }
    
    public SimpleLinkRatio(Triangle source, LinkRatioMethod method) {
        super(source);
        this.method = (method==null)? new WeightedAverageLRMethod() : method;
        doRecalculate();
    }
    
    @Override
    public Triangle getDevelopmentFactors() {
        return getSource();
    }

    @Override
    public int getDevelopmentCount() {
        return developmentCount;
    }

    @Override
    public double getValue(int development) {
        if(development>=0 && development < developmentCount)
            return values[development];
        return Double.NaN;
    }

    @Override
    public double[] toArray() {
        double[] result = new double[developmentCount];
        System.arraycopy(values, 0, result, 0, developmentCount);
        return result;
    }

    @Override
    public double getMackAlpha(int development) {
        return method.getMackAlpha();
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        if(source == null) {
            developmentCount = 0;
            values = new double[0];
        } else {
            developmentCount = source.getDevelopmentCount();
            values = method.getLinkRatios(source);
        }
    }
}
