package org.jreserve.factor.standarderror;

import org.jreserve.factor.linkratio.AbstractVectorUserInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLinkRatioScaleEstimator extends AbstractVectorUserInput implements LinkRatioScaleEstimator {

    public UserInputLinkRatioScaleEstimator() {
    }

    public UserInputLinkRatioScaleEstimator(double[] values) {
        super(values);
    }
    
    @Override
    public void fit(LinkRatioScale scales) {
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputLinkRatioScaleEstimator);
    }
    
    @Override
    public int hashCode() {
        return UserInputLinkRatioScaleEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "UserInputLinkRatioScaleEstimator";
    }
}
