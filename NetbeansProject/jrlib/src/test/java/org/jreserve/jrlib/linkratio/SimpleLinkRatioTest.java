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
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioTest {

    private final static double[] EXPECTED_WA = {
        1.19471971, 0.99540619, 0.99507566, 1.01018160, 
        1.00310913, 1.00031727, 0.98794674
    };
    
    private final static double[] EXPECTED_A = {
        1.19745695, 0.99507487, 0.99595362, 1.01091703, 
        1.00373787, 1.00032791, 0.98794674
    };
    
    private FactorTriangle source;
    private SimpleLinkRatio lr;
    
    public SimpleLinkRatioTest() {
    }

    @Before
    public void setUp() {
        source = new DevelopmentFactors(TestData.getCummulatedTriangle(TestData.INCURRED));
        lr = new SimpleLinkRatio(source);
    }

    @Test
    public void testGetDevelopmentFactors() {
        assertTrue(source == lr.getSourceFactors());
    }

    @Test
    public void testGetLength() {
        assertEquals(source.getDevelopmentCount(), lr.getLength());
    }

    @Test
    public void testGetValue() {
        int length = EXPECTED_WA.length;
        assertEquals(Double.NaN, lr.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_WA[d], lr.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, lr.getValue(length), TestConfig.EPSILON);
        
        lr = new SimpleLinkRatio(source, new AverageLRMethod());
        length = EXPECTED_A.length;
        assertEquals(Double.NaN, lr.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_A[d], lr.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, lr.getValue(length), TestConfig.EPSILON);        
    }

    @Test
    public void testToArray() {
        double[] found = lr.toArray();
        assertArrayEquals(EXPECTED_WA, found, TestConfig.EPSILON);
    }

    @Test
    public void testGetMackAlpha() {
        double expected = WeightedAverageLRMethod.DEFAULT_MACK_ALPHA;
        assertEquals(expected, lr.getMackAlpha(0), TestConfig.EPSILON);
        
        expected = AverageLRMethod.MACK_ALPHA;
        double found = new SimpleLinkRatio(source, new AverageLRMethod()).getMackAlpha(5);
        assertEquals(expected, found, TestConfig.EPSILON);
    }
}
