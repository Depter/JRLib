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

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSelectionTest {
    
    private final static double[] EXPECTED = {
        1.23814637, 1.17121069, 1.13371303, 1.10002119, 
        1.09383745, 1.08878712, 1.08710960, 1.02798997
    };
    private final static double LAST = 1d;
    
    private DefaultClaimRatioSelection crs;

    @Before
    public void setUp() {
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        crs = new DefaultClaimRatioSelection(incurred, paid);
        
        int lastIndex = EXPECTED.length - 1;
        crs.setMethod(new UserInputCRMethod(lastIndex, LAST), lastIndex);
    }
    
    @Test
    public void testGetLength() {
        assertEquals(EXPECTED.length, crs.getLength());
    }
    
    @Test
    public void testGetValues() {
        assertEquals(Double.NaN, crs.getValue(-1), TestConfig.EPSILON);
        int devs = EXPECTED.length;
        for(int d=0; d<(devs-1); d++)
            assertEquals(EXPECTED[d], crs.getValue(d), TestConfig.EPSILON);
        assertEquals(LAST, crs.getValue(devs-1), TestConfig.EPSILON);
        assertEquals(Double.NaN, crs.getValue(devs), TestConfig.EPSILON);
    }
}
