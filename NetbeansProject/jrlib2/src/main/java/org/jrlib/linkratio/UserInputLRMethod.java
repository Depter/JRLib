package org.jrlib.linkratio;

import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.util.method.AbstractVectorUserInput;

/**
 * Implementation of {@link LinkRatioMethod LinkRatioMethod} interface, 
 * which allows manual input. An instance of this class will always
 * have a Mack-alpha parameter of 0 and weight 1 for all accident
 * and development period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRMethod extends AbstractVectorUserInput<FactorTriangle> implements LinkRatioMethod {
    
    public final static double MACK_ALPHA = 0d;

    /**
     * Creates an empty instance.
     */
    public UserInputLRMethod() {
    }
    
    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    public UserInputLRMethod(int index, double value) {
        super(index, value);
    }

    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
    public UserInputLRMethod(double[] values) {
        super(values);
    }
    
    /**
     * Returns 0.
     */
    @Override
    public double getMackAlpha() {
        return MACK_ALPHA;
    }

    /**
     * Returns 1.
     */
    @Override
    public double getWeight(int accident, int development) {
        return 1d;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputLRMethod);
    }
    
    @Override
    public int hashCode() {
        return UserInputLRMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "UserInputLRMethod "+values;
    }
}
