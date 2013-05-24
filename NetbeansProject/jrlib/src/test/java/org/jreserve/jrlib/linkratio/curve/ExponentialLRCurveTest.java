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
package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialLRCurveTest {
    
    private ExponentialLRCurve ef;
    
    public ExponentialLRCurveTest() {
    }

    @Before
    public void setUp() {
        ef = new ExponentialLRCurve();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = TestData.getLinkRatio(TestData.PAID);
        ef.fit(lr);
        assertEquals(0.139573203, ef.getA(), TestConfig.EPSILON);
        assertEquals(0.667524784, ef.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = TestData.getLinkRatio(TestData.INCURRED);
        ef.fit(lr);
        assertEquals(0.84980128, ef.getA(), TestConfig.EPSILON);
        assertEquals(1.21259482, ef.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testGetA() {
        assertEquals(1d, ef.getA(), TestConfig.EPSILON);
    }

    @Test
    public void testGetB() {
        assertEquals(1d, ef.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testGetValue() {
        double[] expected = {
            1.36787944117144, 1.13533528323661, 1.04978706836786, 1.01831563888873, 
            1.00673794699909, 1.00247875217667, 1.00091188196555
        
        };
        for(int d=0; d<7; d++)
            assertEquals(expected[d], ef.getValue(d), TestConfig.EPSILON);
    }

    @Test
    public void testEquals() {
        ExponentialLRCurve ef1 = new ExponentialLRCurve();
        ExponentialLRCurve ef2 = null;
        assertFalse(ef1.equals(ef2));
        
        ef2 = new ExponentialLRCurve();
        assertTrue(ef1.equals(ef2));
        assertTrue(ef2.equals(ef1));
        
        ef1.fit(TestData.getLinkRatio(TestData.PAID));
        ef2.fit(TestData.getLinkRatio(TestData.INCURRED));
        assertTrue(ef1.equals(ef2));
        assertTrue(ef2.equals(ef1));
    }
}
