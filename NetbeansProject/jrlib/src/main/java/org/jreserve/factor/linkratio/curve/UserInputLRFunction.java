package org.jreserve.factor.linkratio.curve;

import org.jreserve.factor.linkratio.AbstractVectorUserInput;
import org.jreserve.factor.linkratio.LinkRatio;

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
}
