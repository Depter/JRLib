package org.jreserve.linkratio.scale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EmptyLinkRatioScaleEstimator implements LinkRatioScaleEstimator {

    private LinkRatioScale scales;

    public void fit(LinkRatioScale scales) {
        this.scales = scales;
    }

    public double getValue(int development) {
        return scales==null? 0 : scales.getValue(development);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof EmptyLinkRatioScaleEstimator);
    }
    
    @Override
    public int hashCode() {
        return EmptyLinkRatioScaleEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "EmptyLinkRatioScaleEstimator";
    }
    
    @Override
    public EmptyLinkRatioScaleEstimator copy() {
        EmptyLinkRatioScaleEstimator copy = new EmptyLinkRatioScaleEstimator();
        copy.scales = scales;
        return copy;
    }
}
