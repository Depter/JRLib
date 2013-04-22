package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.linkratio.curve.DefaultLRCurve;
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
