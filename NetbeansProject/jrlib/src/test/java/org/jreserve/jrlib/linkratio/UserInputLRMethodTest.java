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
package org.jreserve.jrlib.linkratio;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class UserInputLRMethodTest {

    @Test
    public void testGetLinkRatios() {
        UserInputLRMethod ui = new UserInputLRMethod();
        int userValueIndex = 2;
        double userValue = 5d;
        ui.setValue(userValueIndex, userValue);
        
        FactorTriangle factors = TestData.getDevelopmentFactors(TestData.PAID);
        ui.fit(factors);
        
        for(int d=0; d<(userValueIndex+2); d++) {
            double expected = (d==userValueIndex)? userValue : Double.NaN;
            assertEquals(expected, ui.getValue(d), TestConfig.EPSILON);
        }
    }

    @Test
    public void testGetMackAlpha() {
        UserInputLRMethod ui = new UserInputLRMethod();
        assertEquals(UserInputLRMethod.MACK_ALPHA, ui.getMackAlpha(), TestConfig.EPSILON);
    }

}
