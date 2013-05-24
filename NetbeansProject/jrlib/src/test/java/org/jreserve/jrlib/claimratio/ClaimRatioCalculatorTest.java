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
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class ClaimRatioCalculatorTest {
    
    private final static double[] EXPECTED = {
        1.23814637, 1.17121069, 1.13371303, 1.10002119, 
        1.09383745, 1.08878712, 1.08710960, 1.02798997
    };
    
    private RatioTriangle ratios;
    private ClaimRatioCalculator crm;
    
    public ClaimRatioCalculatorTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ratios = new DefaultRatioTriangle(incurred, paid);
        crm = new ClaimRatioCalculator(ratios);
    }

    @Test
    public void testRecalculate() {
        int devs = ratios.getDevelopmentCount();
        assertEquals(devs, crm.getLength());
        
        assertEquals(Double.NaN, crm.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], crm.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, crm.getValue(devs), TestConfig.EPSILON);
    }
}