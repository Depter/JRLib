package org.jreserve.factor.linkratio.curve;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.util.AbstractLinearRegression;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLinkRatioFunction extends AbstractLinearRegression<LinkRatio> implements LinkRatioFunction {

    protected double pa = 1d;
    protected double pb = 1d;
    
    public double getA() {
        return pa;
    }
    
    public double getB() {
        return pb;
    }
    
    @Override
    protected double[] getYs(LinkRatio source) {
        int devs = source.getDevelopmentCount();
        double[] y = new double[devs];
        for(int d=0; d<devs; d++)
            y[d] = getY(source.getValue(d));
        return y;
    }
    
    protected abstract double getY(double lr);

    @Override
    protected double[] getXs(LinkRatio source) {
        return super.getXOneBased(source.getDevelopmentCount());
    }

}
