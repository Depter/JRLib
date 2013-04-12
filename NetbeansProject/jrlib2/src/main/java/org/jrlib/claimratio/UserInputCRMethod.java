package org.jrlib.claimratio;

import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.util.AbstractVectorUserInput;

/**
 * Implementation of {@link ClaimRatioMethod ClaimRatioMethod} interface, 
 * which allows manual input. 
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputCRMethod extends AbstractVectorUserInput<RatioTriangle> implements ClaimRatioMethod {

    /**
     * Creates an empty instance.
     */
    public UserInputCRMethod() {
    }

    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    public UserInputCRMethod(int index, double value) {
        super(index, value);
    }

    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
    public UserInputCRMethod(double[] values) {
        super(values);
    }
    
    @Override
    public UserInputCRMethod copy() {
        return new UserInputCRMethod(values);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputCRMethod);
    }
    
    @Override
    public int hashCode() {
        return UserInputCRMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "UserInputCRMethod "+values;
    }
}
