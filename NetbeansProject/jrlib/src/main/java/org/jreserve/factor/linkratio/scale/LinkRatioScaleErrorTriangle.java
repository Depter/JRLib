package org.jreserve.factor.linkratio.scale;

import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleErrorTriangle extends AbstractTriangle<Triangle> {

    private LinkRatioScale scales;
    private LinkRatio lrs;
    private FactorTriangle factors;
    
    private int accidents;
    private int developments;
    private double[][] values;

    public LinkRatioScaleErrorTriangle(LinkRatioScale scales) {
        this.scales = scales;
        this.lrs = scales.getSourceLinkRatios();
        this.factors = scales.getSourceFactors();
        doRecalculate();
    }
    
    @Override
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(withinBounds(accident))
            return values[accident].length;
        return 0;
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        accidents = factors.getAccidentCount();
        developments = factors.getDevelopmentCount();
        fillValues();
    }

    private void fillValues() {
        values = new double[accidents][];
        for(int a=0; a<accidents; a++)
            values[a] = calculateAccident(a);
    }
    
    private double[] calculateAccident(int accident) {
        int devs = factors.getDevelopmentCount(accident);
        double[] errors = new double[devs];
        for(int d=0; d<devs; d++)
            errors[d] = calculateValue(accident, d);
        return errors;
    }

    private double calculateValue(int accident, int development) {
        double f = factors.getValue(accident, development);
        double w = lrs.getWeight(accident, development);
        double lr = lrs.getValue(development);
        double s = scales.getValue(development);
        return Math.sqrt(w) * (f - lr) / s;
    }
}
