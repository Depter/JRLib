package org.jrlib.linkratio.curve;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.util.method.AbstractVectorUserInput;

/**
 * Class that allows manual smoothing of link-ratios.
 * 
 * @see AbstractVectorUserInput
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRCurve extends AbstractVectorUserInput<LinkRatio> implements LinkRatioCurve {

    /**
     * Creates an empty instance.
     */
    public UserInputLRCurve() {
    }

    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
    public UserInputLRCurve(double[] values) {
        super(values);
    }
    
    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    public UserInputLRCurve(int development, double value) {
        super(development, value);
    }
    
    @Override
    public void fit(LinkRatio lr) {
    }
}