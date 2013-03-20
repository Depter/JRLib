package org.jreserve.estimate.mcl;

import javax.swing.event.ChangeListener;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclRhoCalculator implements MclRho {
    
    private ClaimTriangle numerator;
    private ClaimTriangle denominator;
    
    private double[] ratios;
    private double[] rhos;
    private int developments;
    private int accidents;

    MclRhoCalculator(ClaimTriangle numerator, ClaimTriangle denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        recalculate();
    }
    
    @Override
    public void detach() {
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
    }
    
    @Override
    public void removeChangeListener(ChangeListener listener) {
    }
    
    @Override
    public ClaimTriangle getNumerator() {
        return numerator;
    }
    
    @Override
    public ClaimTriangle getDenominator() {
        return denominator;
    }
    
    @Override
    public int getDevelopmentCount() {
        return developments;
    }
    
    @Override
    public double getRatio(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return ratios[development];
    }
    
    @Override
    public double[] ratiosToArray() {
        return TriangleUtil.copy(ratios);
    }
    
    @Override
    public double getRho(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return rhos[development];
    }
    
    @Override
    public double[] rhosToArray() {
        return TriangleUtil.copy(rhos);
    }
    
    @Override
    public final void recalculate() {
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

    public MclRho copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
