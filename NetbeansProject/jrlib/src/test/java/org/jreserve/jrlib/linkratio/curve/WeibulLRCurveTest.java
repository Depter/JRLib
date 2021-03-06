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
public class WeibulLRCurveTest {
    
    private WeibulLRCurve wf;

    public WeibulLRCurveTest() {
    }

    @Before
    public void setUp() {
        wf = new WeibulLRCurve();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = TestData.getLinkRatio(TestData.PAID);
        wf.fit(lr);
        assertEquals(2.16807672, wf.getA(), TestConfig.EPSILON);
        assertEquals(1.19042212, wf.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = TestData.getLinkRatio(TestData.INCURRED);
        wf.fit(lr);
        assertEquals(1.36002236, wf.getA(), TestConfig.EPSILON);
        assertEquals(1.34407394, wf.getB(), TestConfig.EPSILON);

    }

    @Test
    public void testGetA() {
        assertEquals(1d, wf.getA(), TestConfig.EPSILON);
    }

    @Test
    public void testGetB() {
        assertEquals(1d, wf.getB(), TestConfig.EPSILON);
    }

    @Test
    public void testGetValue() {
        for(int d=0; d<7; d++)
            assertEquals(1.581976707, wf.getValue(d+1), TestConfig.EPSILON);
    }

    @Test
    public void testEquals() {
        WeibulLRCurve wf1 = new WeibulLRCurve();
        WeibulLRCurve wf2 = null;
        assertFalse(wf1.equals(wf2));
        
        wf2 = new WeibulLRCurve();
        assertTrue(wf1.equals(wf2));
        assertTrue(wf2.equals(wf1));
        
        wf1.fit(TestData.getLinkRatio(TestData.PAID));
        wf2.fit(TestData.getLinkRatio(TestData.INCURRED));
        assertTrue(wf1.equals(wf2));
        assertTrue(wf2.equals(wf1));
    }
}
