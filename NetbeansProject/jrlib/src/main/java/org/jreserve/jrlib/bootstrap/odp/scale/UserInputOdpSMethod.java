package org.jreserve.jrlib.bootstrap.odp.scale;

import org.jreserve.jrlib.util.method.AbstractVectorUserInput;

/**
 * Implementation of {@link OdpRSMethod OdpRSMethod} interface, 
 * which allows manual input. 
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputOdpSMethod extends AbstractVectorUserInput<OdpResidualScale> implements OdpRSMethod {

    /**
     * Creates an empty instance.
     */
    public UserInputOdpSMethod() {
    }

    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    public UserInputOdpSMethod(int index, double value) {
        super(index, value);
    }

    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
    public UserInputOdpSMethod(double[] values) {
        super(values);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputOdpSMethod);
    }
    
    @Override
    public int hashCode() {
        return UserInputOdpSMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "UserInputOdpResidualMethod "+values;
    }
}
