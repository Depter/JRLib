package org.jreserve.factor.linkratio.curve;

import org.jreserve.factor.linkratio.curve.DefaultLRFunction;
import org.jreserve.JRLibTestSuite;
import org.jreserve.factor.linkratio.FixedLinkRatio;
import org.jreserve.factor.linkratio.LinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLRFunctionTest {
    
    private DefaultLRFunction df;

    public DefaultLRFunctionTest() {
    }

    @Before
    public void setUp() {
        df = new DefaultLRFunction();
    }

    @Test
    public void testFit_Paid() {
        LinkRatio lr = FixedLinkRatio.getPaid();
        df.fit(lr);
        double[] lrs = lr.toArray();
        for(int d=0; d<lrs.length; d++)
            assertEquals(lrs[d], df.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(1d, df.getValue(lrs.length), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testFit_Incurred() {
        LinkRatio lr = FixedLinkRatio.getIncurred();
        df.fit(lr);
        double[] lrs = lr.toArray();
        for(int d=0; d<lrs.length; d++)
            assertEquals(lrs[d], df.getValue(d), JRLibTestSuite.EPSILON);
        assertEquals(1d, df.getValue(lrs.length), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue() {
        for(int d=0; d<10; d++)
            assertEquals(1d, df.getValue(d), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testEquals() {
        DefaultLRFunction df1 = new DefaultLRFunction();
        DefaultLRFunction df2 = null;
        assertFalse(df1.equals(df2));
        
        df2 = new DefaultLRFunction();
        assertTrue(df1.equals(df2));
        assertTrue(df2.equals(df1));
        
        df1.fit(FixedLinkRatio.getPaid());
        df2.fit(FixedLinkRatio.getIncurred());
        assertTrue(df1.equals(df2));
        assertTrue(df2.equals(df1));
    }
}