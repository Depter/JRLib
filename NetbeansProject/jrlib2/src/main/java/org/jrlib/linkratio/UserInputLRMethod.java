package org.jrlib.linkratio;

import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.util.AbstractVectorUserInput;

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
    public UserInputLRMethod copy() {
        return new UserInputLRMethod();
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
