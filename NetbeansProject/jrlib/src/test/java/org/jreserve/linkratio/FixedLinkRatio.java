package org.jreserve.linkratio;

import org.jreserve.linkratio.LinkRatio;
import javax.swing.event.ChangeListener;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FixedLinkRatio implements LinkRatio {

    private static FactorTriangle createDevelopmentFactors(String path) {
        return new DevelopmentFactors(TestData.getCummulatedTriangle(path));
    }
    
    //Link Ratios based on Paid triangle in JRLibTestSuit, 
    //with weighted average method
    private final static double[] PAID_LR = {
        1.24694402, 1.01584722, 1.00946012, 1.00954295, 
        1.00347944, 1.00335199, 1.00191164
    };
    private final static FactorTriangle PAID_SOURCE = createDevelopmentFactors(TestData.PAID);
    
    //Link Ratios based on Incurred triangle in JRLibTestSuit, 
    //with weighted average method
    private final static double[] INCURRED_LR = {
        1.19471971, 0.99540619, 0.99507566, 1.01018160, 
        1.00310913, 1.00031727, 0.98794674
    };
    private final static FactorTriangle INCURRED_SOURCE = createDevelopmentFactors(TestData.INCURRED);
    
    public static LinkRatio getPaid() {
        return new FixedLinkRatio(PAID_LR, PAID_SOURCE);
    }
    
    public static LinkRatio getIncurred() {
        return new FixedLinkRatio(INCURRED_LR, INCURRED_SOURCE);
    }

    private double[] factors;
    private FactorTriangle source;
    private int size;
    
    public FixedLinkRatio(double[] factors, FactorTriangle source) {
        this.factors = factors;
        this.size = factors.length;
        this.source = source;
    }

    @Override
    public int getDevelopmentCount() {
        return size;
    }

    @Override
    public double getValue(int development) {
        return factors[development];
    }

    @Override
    public double[] toArray() {
        double[] result = new double[size];
        System.arraycopy(factors, 0, result, 0, size);
        return result;
    }

    @Override
    public void recalculate() {
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
    public FactorTriangle getSourceFactors() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }

    @Override
    public double getMackAlpha(int development) {
        return 1d;
    }
    
    @Override
    public double getWeight(int accident, int development) {
        double fik = source.getValue(accident, development);
        if(Double.isNaN(fik)) return Double.NaN;
        return source.getSourceTriangle().getValue(accident, development);
    }

    public LinkRatio copy() {
        return this;
    }
}
