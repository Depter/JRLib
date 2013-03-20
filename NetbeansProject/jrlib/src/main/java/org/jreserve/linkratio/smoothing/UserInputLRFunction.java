package org.jreserve.linkratio.smoothing;

import org.jreserve.util.AbstractVectorUserInput;
import org.jreserve.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRFunction extends AbstractVectorUserInput implements LinkRatioFunction {

    public UserInputLRFunction() {
    }

    public UserInputLRFunction(double[] values) {
        super(values);
    }
    
    public UserInputLRFunction(int development, double value) {
        super.setValue(development, value);
    }
    
    @Override
    public void fit(LinkRatio lr) {
    }
    
    @Override
    public UserInputLRFunction copy() {
        return new UserInputLRFunction(values);
    }
}
