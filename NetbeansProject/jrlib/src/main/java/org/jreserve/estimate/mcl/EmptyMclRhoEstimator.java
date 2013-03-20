package org.jreserve.estimate.mcl;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EmptyMclRhoEstimator implements MclRhoEstimator {

    private MclRho rhos;
    
    @Override
    public void fit(MclRho source) {
        this.rhos = source;
    }

    @Override
    public double getValue(int development) {
        return rhos==null? Double.NaN : rhos.getRho(development);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof EmptyMclRhoEstimator);
    }
    
    @Override
    public int hashCode() {
        return EmptyMclRhoEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "EmptyMclRhoEstimator";
    }
    
    @Override
    public EmptyMclRhoEstimator copy() {
        EmptyMclRhoEstimator copy = new EmptyMclRhoEstimator();
        copy.rhos = rhos;
        return copy;
    }
}
