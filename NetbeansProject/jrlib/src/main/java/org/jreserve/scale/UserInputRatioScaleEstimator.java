package org.jreserve.scale;

import org.jreserve.util.AbstractVectorUserInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputRatioScaleEstimator<T extends RatioScaleInput> extends AbstractVectorUserInput implements RatioScaleEstimator<T> {

    public UserInputRatioScaleEstimator() {
    }

    public UserInputRatioScaleEstimator(double[] values) {
        super(values);
    }
    
    @Override
    public void fit(RatioScale<T> scales) {
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputRatioScaleEstimator);
    }
    
    @Override
    public int hashCode() {
        return UserInputRatioScaleEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "UserInputRatioScaleEstimator";
    }
    
    @Override
    public UserInputRatioScaleEstimator copy() {
        return new UserInputRatioScaleEstimator(values);
    }
}