package org.jreserve.factor.linkratio;

import org.jreserve.factor.FactorTriangle;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLRMethod implements LinkRatioMethod {
    
    public final static double DEFAULT_MACK_ALPHA = 1d;
    
    protected int developments;
    protected double[] values;
    
    private FactorTriangle factors;
    private Triangle cik;
    
    @Override
    public void fit(FactorTriangle factors) {
        initState(factors);
        int accidents = factors.getAccidentCount();
        for(int d=0; d<developments; d++)
            values[d] = getLinkRatio(factors, accidents, d);
    }
    
    private void initState(FactorTriangle factors) {
        this.factors = factors;
        this.cik = factors.getSourceTriangle();
        developments = factors.getDevelopmentCount();
        values = new double[developments];
    }

    protected abstract double getLinkRatio(FactorTriangle factors, int accidents, int dev);
    
    @Override
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return values[development];
    }
    
    @Override
    public double getMackAlpha() {
        return DEFAULT_MACK_ALPHA;
    }

    @Override
    public double getWeight(int accident, int development) {
        double fik = factors.getValue(accident, development);
        if(Double.isNaN(fik))
            return Double.NaN;
        double c = cik.getValue(accident, development);
        return Double.isNaN(c)? Double.NaN : Math.pow(c, getMackAlpha());
    }
}
