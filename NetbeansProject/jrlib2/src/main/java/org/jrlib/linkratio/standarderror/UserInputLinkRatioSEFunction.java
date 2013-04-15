package org.jrlib.linkratio.standarderror;

import org.jrlib.util.method.AbstractVectorUserInput;

/**
 * Allows to use manually entered values for the standard error of the link-ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLinkRatioSEFunction extends AbstractVectorUserInput<LinkRatioSE> implements LinkRatioSEFunction {

    /**
     * Creates an empty instance.
     */
    public UserInputLinkRatioSEFunction() {
    }

    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    public UserInputLinkRatioSEFunction(int index, double value) {
        super(index, value);
    }
    
    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
    public UserInputLinkRatioSEFunction(double[] values) {
        super(values);
    }

    @Override
    public void fit(LinkRatioSE ses) {
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputLinkRatioSEFunction);
    }
    
    @Override
    public int hashCode() {
        return UserInputLinkRatioSEFunction.class.hashCode();
    }
    
    @Override
    public String toString() {
        return "UserInputLinkRatioSEFunction";
    }
}
