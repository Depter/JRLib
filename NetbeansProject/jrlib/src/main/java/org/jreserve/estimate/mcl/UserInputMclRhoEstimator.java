package org.jreserve.estimate.mcl;

import org.jreserve.factor.linkratio.AbstractVectorUserInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputMclRhoEstimator extends AbstractVectorUserInput implements MclRhoEstimator {

    public UserInputMclRhoEstimator() {
    }

    public UserInputMclRhoEstimator(double[] values) {
        super(values);
    }
    
    @Override
    public void fit(MclRho scales) {
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputMclRhoEstimator);
    }
    
    @Override
    public int hashCode() {
        return UserInputMclRhoEstimator.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "UserInputMclRhoEstimator";
    }
}
