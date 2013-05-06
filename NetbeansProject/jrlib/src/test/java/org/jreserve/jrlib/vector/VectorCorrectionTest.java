package org.jreserve.jrlib.vector;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jreserve.jrlib.TestConfig;
        
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorCorrectionTest {

    private final static double[] DATA = {1 , 2 , 3 , 4 , 5 , 6 , 7};

    private final static double CORRECTION = 0.5;
    private final static int INDEX = 3;

    private Vector source;
    private VectorCorrection corrigated;
    private VectorCorrection outsider;
    
    public VectorCorrectionTest() {
    }
    
    @Before
    public void setUp() {
        source = new InputVector(DATA);
        corrigated = new VectorCorrection(source, INDEX, CORRECTION);
        int last = source.getLength();
        outsider = new VectorCorrection(source, last, CORRECTION);
    }


    @Test
    public void testGetIndex() {
        assertEquals(INDEX, corrigated.getCorrigatedIndex());
        assertEquals(DATA.length, outsider.getCorrigatedIndex());
    }

    @Test
    public void testGetCorrection() {
        assertEquals(CORRECTION, corrigated.getCorrigatedValue(), TestConfig.EPSILON);
        assertEquals(CORRECTION, outsider.getCorrigatedValue(), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetLength() {
        assertEquals(source.getLength(), corrigated.getLength());
        assertEquals(source.getLength(), outsider.getLength());
    }

    @Test
    public void testGetValue() {
        int length = source.getLength();
        
        assertTrue(Double.isNaN(corrigated.getValue(-1)));
        assertTrue(Double.isNaN(outsider.getValue(-1)));
        for(int i=0; i<length; i++) {
            double expected = source.getValue(i);
            assertEquals(expected, outsider.getValue(i), TestConfig.EPSILON);
                
            if(i == INDEX)
                expected = CORRECTION;
            assertEquals(expected, corrigated.getValue(i), TestConfig.EPSILON);
        }
        assertTrue(Double.isNaN(corrigated.getValue(length)));
        assertTrue(Double.isNaN(outsider.getValue(length)));
    }
}