package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.scale.SimpleScale;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleScaleTest {

    private final static double[] EXPECTED = {
        1336.96846717, 988.47642646, 440.13971098, 206.98511051, 
         164.19978361,  74.60176287,  35.49315669,  16.88652015
    };
    
    private LinkRatioScaleInput source;
    private SimpleScale<LinkRatioScaleInput> scales;

    @Before
    public void setUp() {
        ClaimTriangle claims = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        source = new LinkRatioScaleInput(claims);
        scales = new SimpleScale<LinkRatioScaleInput>(source);
    }

    @Test
    public void testGetSourceInput() {
        assertEquals(source, scales.getSourceInput());
    }

    @Test
    public void testGetLength() {
        assertEquals(source.getDevelopmentCount(), scales.getLength());
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, scales.getValue(-1), TestConfig.EPSILON);
        
        int devs = EXPECTED.length;
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], scales.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, scales.getValue(devs), TestConfig.EPSILON);
    }

    @Test
    public void testToArray() {
        assertArrayEquals(EXPECTED, scales.toArray(), TestConfig.EPSILON);
    }
}
