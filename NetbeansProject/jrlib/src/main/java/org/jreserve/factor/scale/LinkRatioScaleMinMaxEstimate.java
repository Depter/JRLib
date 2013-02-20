package org.jreserve.factor.scale;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleMinMaxEstimate implements LinkRatioScaleEstimator {

    private int length = 0;
    private double[] values ;
    
    public void fit(LinkRatioScale scales) {
        values = scales.toArray();
        length = values.length;
        if(length > 2)
            estimateValues();
    }
    
    private void estimateValues() {
        double min2 = values[2];
        double min1 = values[1];
        int index = 1;
        while(++index < length) {
            double v = values[index];
            if(Double.isNaN(v) && canEstimate(min1, min2))
                values[index] = estimate(min1, min2);
            min2 = min1;
            min1 = v;
        }
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

    public double getValue(int development) {
        if(withinBound(development))
            return values[development];
        return Double.NaN;
    }
    
    private boolean withinBound(int development) {
        return development >= 0 &&
               development < length;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof LinkRatioScaleMinMaxEstimate);
    }
    
    @Override
    public int hashCode() {
        return LinkRatioScaleMinMaxEstimate.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "LinkRatioScaleMinMaxEstimate";
    }
}
