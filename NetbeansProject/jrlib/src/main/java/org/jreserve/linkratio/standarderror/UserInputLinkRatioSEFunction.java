package org.jreserve.linkratio.standarderror;

import org.jreserve.util.AbstractVectorUserInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLinkRatioSEFunction extends AbstractVectorUserInput implements LinkRatioSEFunction {

    public UserInputLinkRatioSEFunction() {
    }

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
    
    @Override
    public UserInputLinkRatioSEFunction copy() {
        return new UserInputLinkRatioSEFunction(values);
    }
}