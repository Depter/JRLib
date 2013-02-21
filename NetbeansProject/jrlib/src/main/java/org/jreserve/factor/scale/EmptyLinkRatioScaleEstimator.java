package org.jreserve.factor.scale;

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

}
