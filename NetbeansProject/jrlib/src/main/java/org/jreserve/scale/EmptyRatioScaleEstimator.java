package org.jreserve.scale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EmptyRatioScaleEstimator<T extends RatioScaleInput> implements RatioScaleEstimator<T> {
    
    private RatioScale<T> scales;

    public void fit(RatioScale<T> scales) {
        this.scales = scales;
    }

    public double getValue(int development) {
        return scales==null? 0 : scales.getValue(development);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof EmptyRatioScaleEstimator);
    }
    
    @Override
    public int hashCode() {
        return EmptyRatioScaleEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "EmptyRatioScaleEstimator";
    }

    @Override
    public EmptyRatioScaleEstimator<T> copy() {
        EmptyRatioScaleEstimator copy = new EmptyRatioScaleEstimator();
        copy.scales = scales;
        return copy;
    }
}
