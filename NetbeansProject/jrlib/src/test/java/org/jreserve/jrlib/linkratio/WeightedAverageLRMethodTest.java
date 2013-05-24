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
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WeightedAverageLRMethodTest {
        
    private final static double[] EXPECTED_PAID = {
        1.24694402,
        1.01584722,
        1.00946012,
        1.00954295,
        1.00347944,
        1.00335199,
        1.00191164
    };

    private final static double[] EXPECTED_INCURRED = {
        1.19471971,
        0.99540619,
        0.99507566,
        1.01018160,
        1.00310913,
        1.00031727,
        0.98794674
    };
    
    private DevelopmentFactors factors;

    public WeightedAverageLRMethodTest() {
    }

    @Test
    public void testGetLinkRatios_Paid() {
        initFactors(TestData.PAID);
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED_PAID.length;
        assertEquals(Double.NaN, lrs.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_PAID[d], lrs.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), TestConfig.EPSILON);
    }
    
    private void initFactors(String path) {
        factors = new DevelopmentFactors(TestData.getCummulatedTriangle(path));
    }

    @Test
    public void testGetLinkRatios_Incurred() {
        initFactors(TestData.INCURRED);
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        lrs.fit(factors);
        
        int length = EXPECTED_INCURRED.length;
        assertEquals(Double.NaN, lrs.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_INCURRED[d], lrs.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, lrs.getValue(length), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetMackAlpha() {
        WeightedAverageLRMethod lrs = new WeightedAverageLRMethod();
        assertEquals(1d, lrs.getMackAlpha(), TestConfig.EPSILON);
    }
}
