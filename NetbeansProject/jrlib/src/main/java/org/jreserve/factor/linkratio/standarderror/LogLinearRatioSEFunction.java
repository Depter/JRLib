package org.jreserve.factor.linkratio.standarderror;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.util.AbstractLinearRegression;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LogLinearRatioSEFunction extends AbstractLinearRegression<LinkRatioSE> implements LinkRatioSEFunction {

    private LinkRatio lrs;
    
    @Override
    protected double[] getYs(LinkRatioSE source) {
        lrs = source.getSourceLinkRatios();
        
        int developments = source.getDevelopmentCount();
        double[] y = new double[developments];
        for(int d=0; d<developments; d++)
            y[d] = getY(d, source);
        
        return y;
    }
    
    private double getY(int development, LinkRatioSE ses) {
        double lr = lrs.getValue(development);
        if(Double.isNaN(lr) || lr <= 0d) return Double.NaN;
        
        double se = ses.getValue(development);
        return Math.log(se / lr);
    }

    @Override
    protected double[] getXs(LinkRatioSE source) {
        return super.getXOneBased(source.getDevelopmentCount());
    }

    @Override
    public double getValue(int development) {
        double lr = lrs.getValue(development);
        if(Double.isNaN(lr)) 
            return Double.NaN;
        
        double x = (double) (development + 1);
        return lr * Math.exp(intercept + slope * x);
    }
}
