package org.jrlib.util.method;

import java.util.Arrays;
import org.jrlib.util.RegressionUtil;

/**
 * The class is a base class to all {@link SelectableMethod SelectableMethod}
 * implementations, which wishes to use a one dimensional linear regression
 * (y ~ intercept + slope * y).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLinearRegression<T> implements SelectableMethod<T> {

    private boolean[] isExcluded = new boolean[0];
    protected double intercept;
    protected double slope;
    
    /**
     * Excludes/Includes the element at the given index from the fitting 
     * procedure.
     */
    public void setExcluded(int index, boolean excluded) {
        if(isExcluded.length <= index)
            isExcluded = Arrays.copyOf(isExcluded, index+1);
        isExcluded[index] = excluded;
    }
    
    /**
     * Returns `true` if the given element is included in the
     * fitting procedure.
     */
    public boolean isExcluded(int index) {
        if(index < 0 || index >=isExcluded.length)
            return false;
        return isExcluded[index];
    }
    
    @Override
    public void fit(T source) {
        double[] fitted = fitParameters(source);
        slope = fitted[RegressionUtil.SLOPE];
        intercept = fitted[RegressionUtil.INTERCEPT];
    }
    
    private double[] fitParameters(T source) {
        double[] x = getXs(source);
        double[] y = getYs(source);
        excludeValues(y);
        return RegressionUtil.linearRegression(x, y);
    }
    
    /**
     * Should return the non null y values. Implementations
     * should return all values, exclusion/inclusion will be 
     * taken care of. 
     * 
     * The length of the array returned by this  method should 
     * be of same length as the one returned from
     * {@link #getXs(java.lang.Object) getX()}.
     */
    protected abstract double[] getYs(T source);

    /**
     * Should return the non null x values. Implementations
     * should return all values, exclusion/inclusion will be 
     * taken care of.
     * 
     * The length of the array returned by this  method should 
     * be of same length as the one returned from
     * {@link #getYs(java.lang.Object) getY()}.
     */
    protected abstract double[] getXs(T source);
    
    /**
     * Retunrs 0-based indices. This is a utility method for regressions
     * that make a regression based on time. It returns an array of
     * {0, ..., length-1}.
     * 
     * @throws IllegalArgumentException when length is less then 0.
     */
    protected final double[] getXZeroBased(int length) {
        double[] x = new double[length];
        for(int i=0; i<length; i++)
            x[i] = (double)i;
        return x;
    }
    
    /**
     * Retunrs 1-based indices. This is a utility method for regressions
     * that make a regression based on time. It returns an array of
     * {1, ..., length}.
     * 
     * @throws IllegalArgumentException when length is less then 0.
     */
    protected final double[] getXOneBased(int length) {
        double[] x = new double[length];
        for(int i=0; i<length; i++)
            x[i] = (double)(i+1);
        return x;
    }
    
    /**
     * Replaces the elements of the array with NaNs if they
     * are excluded.
     * 
     * @see #isExcluded(int) 
     * @see #setExcluded(int, boolean) 
     */
    protected void excludeValues(double[] y) {
        int size = Math.min(isExcluded.length, y.length);
        for(int i=0; i<size; i++)
            if(isExcluded[i])
                y[i] = Double.NaN;
    }
    
    /**
     * Returns the fitted value. This method assumes that the index
     * is equals to x, thus the result is `slope + intercept * x`.
     * 
     * @see #getSlope() 
     * @see #getIntercept() 
     */
    @Override
    public double getValue(int index) {
        double x = (double) index;
        return intercept + slope * x;
    }
    
    /**
     * Retunrs the estimated intercept.
     */
    public double getIntercept() {
        return intercept;
    }
    
    /**
     * Returns the estimated slope.
     */
    public double getSlope() {
        return slope;
    }
    
    /**
     * Utility class to implement the {@link Copyable Copyable}
     * interface. The inner state of the `original` instance will
     * be copied to this instance (exlusions, intercept and slope).
     */
    protected void copyStateFrom(AbstractLinearRegression original) {
        int size = original.isExcluded.length;
        this.isExcluded = new boolean[size];
        System.arraycopy(original.isExcluded, 0, isExcluded, 0, size);
        
        this.intercept = original.intercept;
        this.slope = original.slope;
    }
}
