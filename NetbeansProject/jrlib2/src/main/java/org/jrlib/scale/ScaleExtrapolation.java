package org.jrlib.scale;

import org.jrlib.util.AbstractLinearRegression;

/**
 * Calculates a scale parameter by using linear regression on the input.
 * The model for the linear regression is specified as:
 *      ln(s(d)) ~ a + b * t
 *      t = d + 1
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ScaleExtrapolation<T extends ScaleInput> extends AbstractLinearRegression<Scale<T>> implements ScaleEstimator<T> {
    
    private boolean isEmptyModel = true;

    public ScaleExtrapolation() {
    }
    
    @Override
    public void fit(Scale<T> source) {
        int developments = (source==null)? 0 : source.getLength();
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
    
    /**
     * Returns the natural logarithm of the scale parameters from 
     * the source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    @Override
    protected double[] getYs(Scale<T> source) {
        int developments = source.getLength();
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

    /**
     * Returns an array containing the values 
     * `[1, ..., {@link Scale#getLength() source.getLength()}]`.
     * 
     * @throws NullPointerException if `source` is null.
     */
    @Override
    protected double[] getXs(Scale<T> source) {
        int developments = source.getLength();
        return super.getXOneBased(developments);
    }
    
    /**
     * Retunrs the estimated value for the given development period.
     */
    @Override
    public double getValue(int development) {
        if(isEmptyModel || development<0)
            return Double.NaN;
        double x = (double) (development+1);
        return Math.exp(intercept + slope * x);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ScaleExtrapolation);
    }
    
    @Override
    public int hashCode() {
        return ScaleExtrapolation.class.hashCode();
    }
    
    @Override
    public String toString() {
        if(isEmptyModel)
            return "ScaleExtrapolation [ln(sigma(k)) = NaN]";
        return String.format(
            "ScaleExtrapolation [ln(sigma(k)) = %f + (%f) * k]",
            intercept, slope);
    }

    @Override
    public ScaleExtrapolation copy() {
        ScaleExtrapolation copy = new ScaleExtrapolation();
        copy.isEmptyModel = isEmptyModel;
        copy.copyStateFrom(this);
        return copy;
    }

    /**
     * The method copies the inner state of the given original instance.
     * 
     * @throws NullPointerException if `original` is null.
     * @see AbstractLinearRegression#copyStateFrom(AbstractLinearRegression) 
     */
    protected void copyStateFrom(ScaleExtrapolation<T> original) {
        super.copyStateFrom(original);
        isEmptyModel = original.isEmptyModel;
    }
}