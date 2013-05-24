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
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageLRMethodTest {
        
    private final static double[] EXPECTED = {
        1.24532988,
        1.01587187,
        1.00893837,
        1.00965880,
        1.00361278,
        1.00334656,
        1.00191166
    };

    private DevelopmentFactors factors;

    public AverageLRMethodTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle source = TestData.getCummulatedTriangle(TestData.PAID);
        factors = new DevelopmentFactors(source);
    }

    @Test
    public void testGetLinkRatios() {
        AverageLRMethod lrs = new AverageLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED[d], lrs.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        AverageLRMethod lrs = new AverageLRMethod();
        assertEquals(0d, lrs.getMackAlpha(), TestConfig.EPSILON);
    }
}
