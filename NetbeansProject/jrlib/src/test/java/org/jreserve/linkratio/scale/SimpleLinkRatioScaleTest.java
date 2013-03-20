package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class SimpleLinkRatioScaleTest {

    private final static double[] EXPECTED = {
        1336.96846717, 988.47642646, 440.13971098, 206.98511051, 
         164.19978361,  74.60176287,  35.49315669,  16.88652015
    };
    
    private LinkRatio lrs;
    private SimpleLinkRatioScale scales;
    
    public SimpleLinkRatioScaleTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        lrs = new SimpleLinkRatio(new DevelopmentFactors(cik));
        scales = new SimpleLinkRatioScale(lrs);
    }

    @Test
    public void testGetSourceLinkRatio() {
        assertEquals(lrs, scales.getSourceLinkRatios());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(lrs.getDevelopmentCount(), scales.getDevelopmentCount());
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, scales.getValue(-1), JRLibTestUtl.EPSILON);
        
        int devs = EXPECTED.length;
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], scales.getValue(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, scales.getValue(devs), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testToArray() {
        assertArrayEquals(EXPECTED, scales.toArray(), JRLibTestUtl.EPSILON);
    }
}