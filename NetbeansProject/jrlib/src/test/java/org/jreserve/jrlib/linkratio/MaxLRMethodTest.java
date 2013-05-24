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
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MaxLRMethodTest {
        
    private final static double[] EXPECTED = {
        1.32749558,
        1.02595957,
        1.01671671,
        1.01683530,
        1.00480590,
        1.00659039,
        1.00191166
    };

    private FactorTriangle factors;

    public MaxLRMethodTest() {
    }

    @Before
    public void setUp() {
        factors = TestData.getDevelopmentFactors(TestData.PAID);
    }

    @Test
    public void testGetLinkRatios() {
        MaxLRMethod lrs = new MaxLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED[d], lrs.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        MaxLRMethod lrs = new MaxLRMethod();
        assertEquals(AbstractLRMethod.DEFAULT_MACK_ALPHA, lrs.getMackAlpha(), TestConfig.EPSILON);
    }
}
