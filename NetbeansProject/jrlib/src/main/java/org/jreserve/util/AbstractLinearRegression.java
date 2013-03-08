package org.jreserve.util;

import java.util.Arrays;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractLinearRegression<T> implements SelectableMethod<T> {

    private boolean[] isExcluded = new boolean[0];
    protected double intercept;
    protected double slope;
    
    public void setExcluded(int index, boolean excluded) {
        if(isExcluded.length <= index)
            isExcluded = Arrays.copyOf(isExcluded, index+1);
        isExcluded[index] = excluded;
    }
    
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
    
    protected abstract double[] getYs(T source);

    protected abstract double[] getXs(T source);
    
    protected final double[] getXZeroBased(int length) {
        double[] x = new double[length];
        for(int i=0; i<length; i++)
            x[i] = (double)i;
        return x;
    }
    
    protected final double[] getXOneBased(int length) {
        double[] x = new double[length];
        for(int i=0; i<length; i++)
            x[i] = (double)(i+1);
        return x;
    }
    
    protected void excludeValues(double[] y) {
        int size = Math.min(isExcluded.length, y.length);
        for(int i=0; i<size; i++)
            if(isExcluded[i])
                y[i] = Double.NaN;
    }
    
    @Override
    public double getValue(int index) {
        double x = (double) index;
        return intercept + slope * x;
    }
    
    public double getIntercept() {
        return intercept;
    }
    
    public double getSlope() {
        return slope;
    }
}