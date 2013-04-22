package org.jreserve.jrlib.bootstrap.util;

import org.jreserve.jrlib.util.MathUtil;

/**
 * Factory class for {@link HistogramData HistogramData} instances. This class
 * provides two ways to define histograms.
 * 
 * The first way, is to define the number of intervals to use. In this
 * case the class calculates the interval bounds and interval sizes.
 * 
 * The second way is to define the upper bound for the first interval
 * and the size of the intervals. In this case the class calculates
 * the number of intervals to use.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class HistogramDataFactory {
    
    private double[] data;
    
    private int intervalCount;
    private double intervalSize;
    private double firstUpperBound;
    
    /**
     * Creates a new instance for the given input data. The class uses
     * the formula 'Math.round(Math.sqrt(data.length))' to calculate
     * the number of intervals.
     * 
     * @throws NullPointerException if `data` is null.
     */
    public HistogramDataFactory(double[] data) {
        if(data == null)
            throw new NullPointerException("Data is null!");
        this.data = data;
     
        intervalCount = (int) Math.round(Math.sqrt(data.length));
        initFromStepCount();
    }
    
    private void initFromStepCount() {
        if(intervalCount == 0) {
            firstUpperBound = Double.NaN;
            intervalSize = Double.NaN;
        } else if(intervalCount == 1) {
            double min = MathUtil.min(data);
            double max = MathUtil.max(data);
            firstUpperBound = max+0.01;
            intervalSize = firstUpperBound - min;
        } else {
            double min = MathUtil.min(data);
            double max = MathUtil.max(data);
            intervalSize = (max-min) / (double)(intervalCount-1);
            firstUpperBound = min + intervalSize/2d;
        }
        
    }
    
    /**
     * Retunrs the number of intervals to use.
     */
    public int getIntervalCount() {
        return intervalCount;
    }
    
    /**
     * Retunrs the interval size to use.
     */
    public double getIntervalSize() {
        return intervalSize;
    }
    
    /**
     * Retunrs the upper bound of the first interval to use.
     */
    public double getFirstUpperBound() {
        return firstUpperBound;
    }
    
    /**
     * Sets the number of intervals. Interval size and first upper bound
     * will be adjusted accordingly.
     * 
     * @throws IllegalArgumentException if `intervalCount` is less then 1.
     */
    public HistogramDataFactory setIntervalCount(int intervalCount) {
        if(intervalCount < 1)
            throw new IllegalArgumentException("StepCount must be at least 1! "+intervalCount);
        this.intervalCount = intervalCount;
        initFromStepCount();
        return this;
    }
    
    /**
     * Sets the upper bound for the first interval, and the size of the intervals.
     * Number of intervals will be calculated accordingly.
     * 
     * @throws IllegalArgumentException if one of the parameters is NaN,
     * infinite, or `intervalSize < 0`.
     */
    public HistogramDataFactory setIntervals(double firstUpperBound, double intervalSize) {
        checkFirstUpperBound(firstUpperBound);
        checkIntervalSize(intervalSize);
        this.firstUpperBound = firstUpperBound;
        this.intervalSize = intervalSize;
        initFromIntervals();
        return this;
    }
    
    private void checkFirstUpperBound(double firstUpperBound) {
        if(Double.isNaN(firstUpperBound))
            throw new IllegalArgumentException("First upper bound is NaN!");
        if(Double.isInfinite(firstUpperBound))
            throw new IllegalArgumentException("First upper bound is infinite!");
    }
    
    private void checkIntervalSize(double intervalSize) {
        if(Double.isNaN(intervalSize))
            throw new IllegalArgumentException("Interval size is NaN!");
        if(Double.isInfinite(intervalSize))
            throw new IllegalArgumentException("Interval size is infinite!");
        if(intervalSize <= 0d)
            throw new IllegalArgumentException("Interval size must be greater then 0! "+intervalSize);
    }
    
    private void initFromIntervals() {
        this.intervalCount = 1;
        double upperBound = firstUpperBound;
        double max = MathUtil.max(data);
        while(max >= upperBound) {
            intervalCount++;
            upperBound += intervalSize;
        }
    }
    
    /**
     * Creates a histogram data for the given input.
     */
    public HistogramData buildData() {
        return new HistogramData(firstUpperBound, intervalSize, intervalCount, data);
    }
}
