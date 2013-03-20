package org.jreserve.estimate.mcl;

import org.jreserve.util.AbstractLinearRegression;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclRhoExtrapolation extends AbstractLinearRegression<MclRho> implements MclRhoEstimator {
    
    private boolean isEmptyModel = true;

    @Override
    public void fit(MclRho source) {
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
    protected double[] getYs(MclRho source) {
        int developments = source.getDevelopmentCount();
        double[] y = new double[developments];
        for(int d=0; d<developments; d++)
            y[d] = getLnY(source.getRho(d));
        return y;
    }
    
    private double getLnY(double y) {
        if(!Double.isNaN(y) && y > 0d)
            return Math.log(y);
        return Double.NaN;
    }

    @Override
    protected double[] getXs(MclRho source) {
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
        return (o instanceof MclRhoExtrapolation);
    }
    
    @Override
    public int hashCode() {
        return MclRhoExtrapolation.class.hashCode();
    }
    
    @Override
    public String toString() {
        if(isEmptyModel)
            return "MclRhoExtrapolation [ln(rho(k)) = NaN]";
        return String.format(
            "MclRhoExtrapolation [ln(rho(k)) = %f + (%f) * k]",
            intercept, slope);
    }
    
    @Override
    public MclRhoExtrapolation copy() {
        MclRhoExtrapolation copy = new MclRhoExtrapolation();
        copy.isEmptyModel = isEmptyModel;
        copy.copyStateFrom(this);
        return copy;
    }
}
