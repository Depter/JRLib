package org.jreserve.factor.linkratio.scale;

import org.jreserve.util.SelectableMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioScaleEstimator extends SelectableMethod<LinkRatioScale> {

    public void fit(LinkRatioScale scales);
    
    public double getValue(int development);
}
