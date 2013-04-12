package org.jrlib.claimratio;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.triangle.claim.ClaimTriangle;
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
