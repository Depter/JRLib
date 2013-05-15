package org.jreserve.jrlib.scale;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;
import org.jreserve.jrlib.triangle.TriangleUtil;

/**
 * Calculates a scale parameter with a min-max formula as suggested by Mack.
 * The exact formula to calculate the scale parameter `s(d)` for
 * development period `d` is:
 * 
 *       s(d)^2 = min(C; min(A; B))
 * where:
 *       A = s(d-1)^2,
 *       B = s(d-2)^2,
 *           A^2
 *       C = ---
 *            B
 * 
 * If `s(d-1)` and `s(d-2)` can be obtained from the input data, then those
 * values are used. If they are not obrainable from the input data, then
 * they are recursivly calculated.
 * 
 * If either `s(d-1)` or `s(d-2)` is NaN, then `s(d)` is also NaN.
 * 
 * @see "Mack[1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @author Peter Decsi
 * @version 1.0
 */
public class MinMaxScaleEstimator<T extends ScaleInput> implements ScaleEstimator<T> {
    
    private double[] source;
    private int sourceLength;
    
    @Override
    public void fit(Scale<T> scales) {
        this.source = scales.toArray();
        sourceLength = source.length;
    }
    
    @Override
    public double getValue(int development) {
        if(development < 0)
            return Double.NaN;
        if(development < 2)
            return development < sourceLength? source[development] : Double.NaN;
        return estimate(development);
    }
    
    private double estimate(int development) {
        double min2 = source[0];
        double min1 = source[1];
        double current;
        int index = 1;
        
//        while(++index <= development) {
//            current = index<sourceLength? source[index] : Double.NaN;
//            if(Double.isNaN(current) && canEstimate(min1, min2))
//                current = estimate(min1, min2);
//            min2 = min1;
//            min1 = current;
//        }
//        
//        return current;
        while(++index < development) {
            current = index<sourceLength? source[index] : Double.NaN;
            if(Double.isNaN(current) && canEstimate(min1, min2))
                current = estimate(min1, min2);
            min2 = min1;
            min1 = current;
        }
        
        return estimate(min1, min2);
    }
    
    private boolean canEstimate(double min1, double min2) {
        return !Double.isNaN(min1) && min1 != 0d &&
               !Double.isNaN(min2) && min2 != 0d; 
    }
    
    private double estimate(double min1, double min2) {
        double min2_2 = min2 * min2;
        double min1_2 = min1 * min1;
        double min1_4 = min1_2 * min1_2;
        return sqrt(min(min1_4 / min2_2, min(min2_2, min1_2)));
    }

    /**
     * The method copies the inner state (length and values) of the given 
     * original instance.
     * 
     * @throws NullPointerException if `original` is null.
     */
    protected void copyStateFrom(MinMaxScaleEstimator<T> original) {
        sourceLength = original.sourceLength;
        if(sourceLength > 0)
            source = TriangleUtil.copy(original.source);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MinMaxScaleEstimator);
    }
    
    @Override
    public int hashCode() {
        return MinMaxScaleEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "MinMaxScaleEstimator";
    }
}