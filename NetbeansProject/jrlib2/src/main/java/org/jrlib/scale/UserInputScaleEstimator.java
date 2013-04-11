package org.jrlib.scale;

import org.jrlib.util.AbstractVectorUserInput;

/**
 * Implementation of {@link ScaleEstimator ScaleEstimator} interface, 
 * which allows manual input. 
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputScaleEstimator<T extends ScaleInput> extends AbstractVectorUserInput<Scale<T>> implements ScaleEstimator<T> {

    public UserInputScaleEstimator() {
    }

    public UserInputScaleEstimator(double[] values) {
        super(values);
    }
    
    @Override
    public void fit(Scale<T> scales) {
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputScaleEstimator);
    }
    
    @Override
    public int hashCode() {
        return UserInputScaleEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "UserInputScaleEstimator";
    }
    
    @Override
    public UserInputScaleEstimator copy() {
        return new UserInputScaleEstimator(values);
    }
}