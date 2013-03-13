package org.jreserve.estimate.mcl;

import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class RhoCalculator  {
    
    private Triangle numerator;
    private Triangle denominator;
    
    private double[] ratios;
    private double[] rhos;
    private int developments;
    private int accidents;

    RhoCalculator(Triangle numerator, Triangle denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        recalculate();
    }
    
    int getDevelopmentCount() {
        return developments;
    }
    
    double getRatio(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return ratios[development];
    }
    
    double getRho(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return rhos[development];
    }
    
    final void recalculate() {
        initState();
        calculateRatios();
        calculateRhos();
    }
    
    private void initState() {
        initDevelopments();
        initAccidents();
    }
    
    private void initDevelopments() {
        int d1 = numerator.getDevelopmentCount();
        int d2 = denominator.getDevelopmentCount();
        developments = Math.max(d1, d2);
    }
    
    private void initAccidents() {
        int a1 = numerator.getAccidentCount();
        int a2 = denominator.getAccidentCount();
        accidents = Math.max(a1, a2);
    }
    
    private void calculateRatios() {
        ratios = new double[developments];
        for(int d=0; d<developments; d++)
            ratios[d] = calculateRatio(d);
    }
    
    private double calculateRatio(int development) {
        double sumN = 0d;
        double sumD = 0d;
        
        for(int a=0; a<accidents; a++) {
            double n = numerator.getValue(a, development);
            double d = denominator.getValue(a, development);
            if(!Double.isNaN(n) && !Double.isNaN(d)) {
                sumN += n;
                sumD += d;
            }
        }
        
        return (sumD == 0d)? Double.NaN : sumN / sumD;
    }
    
    private void calculateRhos() {
        rhos = new double[developments];
        for(int d=0; d<developments; d++)
            rhos[d] = calculateRho(d);
    }
    
    private double calculateRho(int development) {
        double ratio = ratios[development];
        if(Double.isNaN(ratio))
            return Double.NaN;
        
        int count = 0;
        double sum = 0d;
        
        for(int a=0; a<accidents; a++) {
            double n = numerator.getValue(a, development);
            double d = denominator.getValue(a, development);
            if(!Double.isNaN(n) && !Double.isNaN(d) && d!=0) {
                count++;
                sum += d * Math.pow(n/d - ratio, 2d);
            }
        }
        
        if(--count < 1)
            return Double.NaN;
        sum /= (double)count;
        return (sum == 0d)? 0d : (sum > 0d)? Math.sqrt(sum) : Double.NaN;
    }
}
