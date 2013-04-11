package org.jrlib.linkratio.curve;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.util.AbstractVectorUserInput;

/**
 * Class that allows manual smoothing of link-ratios.
 * 
 * @see AbstractVectorUserInput
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRCurve extends AbstractVectorUserInput<LinkRatio> implements LinkRatioCurve {

    public UserInputLRCurve() {
    }

    public UserInputLRCurve(double[] values) {
        super(values);
    }
    
    public UserInputLRCurve(int development, double value) {
        super.setValue(development, value);
    }
    
    @Override
    public void fit(LinkRatio lr) {
    }
    
    @Override
    public UserInputLRCurve copy() {
        return new UserInputLRCurve(values);
    }
}