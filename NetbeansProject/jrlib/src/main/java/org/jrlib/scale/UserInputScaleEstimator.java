package org.jrlib.scale;

import org.jrlib.util.method.AbstractVectorUserInput;

/**
 * Implementation of {@link ScaleEstimator ScaleEstimator} interface, 
 * which allows manual input. 
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputScaleEstimator<T extends ScaleInput> extends AbstractVectorUserInput<Scale<T>> implements ScaleEstimator<T> {

    /**
     * Creates an empty instance.
     */
    public UserInputScaleEstimator() {
    }

    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    public UserInputScaleEstimator(int index, double value) {
        super(index, value);
    }
    
    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
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
}