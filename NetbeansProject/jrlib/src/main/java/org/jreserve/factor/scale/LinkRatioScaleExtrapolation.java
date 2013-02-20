package org.jreserve.factor.scale;

import org.jreserve.util.RegressionUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleExtrapolation implements LinkRatioScaleEstimator {

    private boolean isEmptyModel = true;
    private double intercept;
    private double slope;
    
    public void fit(LinkRatioScale source) {
        int developments = (source==null)? 0 : source.getDevelopmentCount();
        if(developments <= 1) {
            emptyModel();
        } else {
            fitModel(source.toArray());
        }
    }
    
    private void emptyModel() {
        isEmptyModel = true;
        intercept = Double.NaN;
        slope = Double.NaN;
    }
    
    private void fitModel(double[] scales) {
        isEmptyModel = false;
        double[] params = RegressionUtil.linearRegression(getLnY(scales));
        intercept = params[RegressionUtil.INTERCEPT];
        slope = params[RegressionUtil.SLOPE];
    }
    
    private double[] getLnY(double[] scales) {
        int developments = scales.length;
        for(int d=0; d<developments; d++)
            scales[d] = getLnY(scales[d]);
        return scales;
    }
    
    private double getLnY(double y) {
        if(!Double.isNaN(y) && y > 0d)
            return Math.log(y);
        return Double.NaN;
    }

    public double getValue(int development) {
        if(isEmptyModel)
            return Double.NaN;
        double x = (double) development;
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
