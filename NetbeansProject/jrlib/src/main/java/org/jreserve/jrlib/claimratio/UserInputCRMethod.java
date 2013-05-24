/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.util.method.AbstractVectorUserInput;

/**
 * Implementation of {@link ClaimRatioMethod ClaimRatioMethod} interface, 
 * which allows manual input. 
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputCRMethod extends AbstractVectorUserInput<ClaimRatio> implements ClaimRatioMethod {

    /**
     * Creates an empty instance.
     */
    public UserInputCRMethod() {
    }

    /**
     * Creates an instance, with the given index set to the given value. All
     * other values will be NaN.
     * 
     * @throws IllegalArgumentException if `index` is less then 0.
     */
    public UserInputCRMethod(int index, double value) {
        super(index, value);
    }

    /**
     * Creates an instance, with the given values.
     * 
     * @throws NullPointerException if `values` is null.
     */
    public UserInputCRMethod(double[] values) {
        super(values);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof UserInputCRMethod);
    }
    
    @Override
    public int hashCode() {
        return UserInputCRMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "UserInputCRMethod "+values;
    }
}
