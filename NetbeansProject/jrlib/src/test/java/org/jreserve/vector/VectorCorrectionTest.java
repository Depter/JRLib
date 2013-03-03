package org.jreserve.vector;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class VectorCorrectionTest {

    private final static double CORRECTION = 0.5;
    private final static int INDEX = 0;

    private Vector source;
    private VectorCorrection corrigated;
    private VectorCorrection outsider;
    
    
    public VectorCorrectionTest() {
    }

    @Before
    public void setUp() {
        source = new InputVector(TestData.getCachedVector(TestData.EXPOSURE));
        corrigated = new VectorCorrection(source, INDEX, CORRECTION);
        outsider = new VectorCorrection(source, source.getLength(), CORRECTION);
    }

    @Test
    public void testGetIndex() {
        assertEquals(INDEX, corrigated.getIndex());
        assertEquals(source.getLength(), outsider.getIndex());
    }

    @Test
    public void testGetCorrection() {
        assertEquals(CORRECTION, corrigated.getCorrection(), JRLibTestSuite.EPSILON);
        assertEquals(CORRECTION, outsider.getCorrection(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue() {
        int length = source.getLength();
        
        for(int i=0; i<length; i++) {
            double expected = source.getValue(i);
            assertEquals(expected, outsider.getValue(i), JRLibTestSuite.EPSILON);
                
            if(i == INDEX)
                expected = CORRECTION;
            assertEquals(expected, corrigated.getValue(i), JRLibTestSuite.EPSILON);
        }
        
        int index = outsider.getIndex();
        assertTrue(Double.isNaN(outsider.getValue(index)));
    }
}