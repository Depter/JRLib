package org.jreserve.scale;

import org.jreserve.util.AbstractLinearRegression;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioScaleExtrapolation<T extends RatioScaleInput> extends AbstractLinearRegression<RatioScale<T>> implements RatioScaleEstimator<T> {
    
    private boolean isEmptyModel = true;

    @Override
    public void fit(RatioScale<T> source) {
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
    protected double[] getYs(RatioScale<T> source) {
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
    protected double[] getXs(RatioScale<T> source) {
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
        return (o instanceof RatioScaleExtrapolation);
    }
    
    @Override
    public int hashCode() {
        return RatioScaleExtrapolation.class.hashCode();
    }
    
    @Override
    public String toString() {
        if(isEmptyModel)
            return "RatioScaleExtrapolation [ln(sigma(k)) = NaN]";
        return String.format(
            "RatioScaleExtrapolation [ln(sigma(k)) = %f + (%f) * k]",
            intercept, slope);
    }

    @Override
    public RatioScaleExtrapolation copy() {
        RatioScaleExtrapolation copy = new RatioScaleExtrapolation();
        copy.isEmptyModel = isEmptyModel;
        copy.copyStateFrom(this);
        return copy;
    }
}