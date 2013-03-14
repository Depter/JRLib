package org.jreserve.estimate.mcl;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclRhoMinMaxEstimator implements MclRhoEstimator {

    private double[] source;
    private int sourceLength;

    
    @Override
    public void fit(MclRho rhos) {
        this.source = rhos.rhosToArray();
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
        double current = Double.NaN;
        int index = 1;
        
        while(++index <= development) {
            current = index<sourceLength? source[index] : Double.NaN;
            if(Double.isNaN(current) && canEstimate(min1, min2))
                current = estimate(min1, min2);
            min2 = min1;
            min1 = current;
        }
        
        return current;
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

    @Override
    public boolean equals(Object o) {
        return (o instanceof MclRhoEstimator);
    }
    
    @Override
    public int hashCode() {
        return MclRhoEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "MclRhoEstimator";
    }
}
