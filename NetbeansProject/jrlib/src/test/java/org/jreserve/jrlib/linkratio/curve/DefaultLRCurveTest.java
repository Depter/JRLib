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
public class DefaultLRCurveTest {
    
    private DefaultLRCurve df;

    @Before
    public void setUp() {
        df = new DefaultLRCurve();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = TestData.getLinkRatio(TestData.PAID);
        df.fit(lr);
        double[] lrs = lr.toArray();
        for(int d=0; d<lrs.length; d++)
            assertEquals(lrs[d], df.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, df.getValue(lrs.length), TestConfig.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = TestData.getLinkRatio(TestData.INCURRED);
        df.fit(lr);
        double[] lrs = lr.toArray();
        for(int d=0; d<lrs.length; d++)
            assertEquals(lrs[d], df.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, df.getValue(lrs.length), TestConfig.EPSILON);
    }

    @Test
    public void testGetValue() {
        for(int d=0; d<10; d++)
            assertEquals(Double.NaN, df.getValue(d), TestConfig.EPSILON);
    }

    @Test
    public void testEquals() {
        DefaultLRCurve df1 = new DefaultLRCurve();
        DefaultLRCurve df2 = null;
        assertFalse(df1.equals(df2));
        
        df2 = new DefaultLRCurve();
        assertTrue(df1.equals(df2));
        assertTrue(df2.equals(df1));
        
        df1.fit(TestData.getLinkRatio(TestData.PAID));
        df2.fit(TestData.getLinkRatio(TestData.INCURRED));
        assertTrue(df1.equals(df2));
        assertTrue(df2.equals(df1));
    }
}
