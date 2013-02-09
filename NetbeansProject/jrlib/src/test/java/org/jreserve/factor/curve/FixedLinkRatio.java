package org.jreserve.factor.curve;

import javax.swing.event.ChangeListener;
import org.jreserve.CalculationData;
import org.jreserve.factor.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FixedLinkRatio implements LinkRatio {

    //Link Ratios based on Paid triangle in JRLibTestSuit, 
    //with weighted average method
    private final static double[] PAID_LR = {
        1.24694402, 1.01584722, 1.00946012, 1.00954295, 
        1.00347944, 1.00335199, 1.00191164
    };

    //Link Ratios based on Incurred triangle in JRLibTestSuit, 
    //with weighted average method
    private final static double[] INCURRED_LR = {
        1.19471971, 0.99540619, 0.99507566, 1.01018160, 
        1.00310913, 1.00031727, 0.98794674

    };
    
    static LinkRatio getPaid() {
        return new FixedLinkRatio(PAID_LR);
    }
    
    static LinkRatio getIncurred() {
        return new FixedLinkRatio(INCURRED_LR);
    }

    private double[] factors;

    FixedLinkRatio(double[] factors) {
        this.factors = factors;
    }

    @Override
    public int getDevelopmentCount() {
        return factors.length;
    }

    @Override
    public double getValue(int development) {
        return factors[development];
    }

    @Override
    public double[] toArray() {
        return factors;
    }

    @Override
    public CalculationData getSource() {
        return null;
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
}
