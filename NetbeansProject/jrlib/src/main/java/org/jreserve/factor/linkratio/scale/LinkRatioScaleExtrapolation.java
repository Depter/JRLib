package org.jreserve.factor.linkratio.scale;

import org.jreserve.util.AbstractLinearRegression;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleExtrapolation extends AbstractLinearRegression<LinkRatioScale> implements LinkRatioScaleEstimator {
    
    private boolean isEmptyModel = true;

    @Override
    public void fit(LinkRatioScale source) {
        int developments = (source==null)? 0 : source.getDevelopmentCount();
        if(developments <= 1) {
            fitEmptyModel();
        } else {
            isEmptyModel = false;
            super.fit(source);
        }
    } 
    
    private void fitEmptyModel() {
        isEmptyModel = true;
        intercept = Double.NaN;
        slope = Double.NaN;
    }
    
    @Override
    protected double[] getYs(LinkRatioScale source) {
        int developments = source.getDevelopmentCount();
        double[] y = new double[developments];
        for(int d=0; d<developments; d++)
            y[d] = getLnY(source.getValue(d));
        return y;
    }
    
    private double getLnY(double y) {
        if(!Double.isNaN(y) && y > 0d)
            return Math.log(y);
        return Double.NaN;
    }

    @Override
    protected double[] getXs(LinkRatioScale source) {
        int developments = source.getDevelopmentCount();
        return super.getXOneBased(developments);
    }

    @Override
    public double getValue(int development) {
        if(isEmptyModel || development<0)
            return Double.NaN;
        double x = (double) (development+1);
        return Math.exp(intercept + slope * x);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof LinkRatioScaleExtrapolation);
    }
    
    @Override
    public int hashCode() {
        return LinkRatioScaleExtrapolation.class.hashCode();
    }
    
    @Override
    public String toString() {
        if(isEmptyModel)
            return "LinkRatioScaleExtrapolation [ln(sigma(k)) = NaN]";
        return String.format(
            "LinkRatioScaleExtrapolation [ln(sigma(k)) = %f + (%f) * k]",
            intercept, slope);
    }
}
