package org.jreserve.factor.linkratio;

import org.jreserve.JRLibTestSuite;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractVectorUserInputTest {
    
    public AbstractVectorUserInputTest() {
    }
    
    @Test
    public void testConstructor_array() {
        double[] expected = new double[]{1d, 2d, 3d};
        AbstractVectorUserInput ui = new AbstractVectorUserInput(expected);
        
        for(int i=0; i<expected.length; i++)
            assertEquals(expected[i], ui.getValue(i), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, ui.getValue(expected.length), JRLibTestSuite.EPSILON);
        
        double e = expected[1];
        expected[1] += 10d;
        assertEquals(e, ui.getValue(1), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testSetValue() {
        AbstractVectorUserInput ui = new AbstractVectorUserInput();
        assertEquals(Double.NaN, ui.getValue(-1), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, ui.getValue(5), JRLibTestSuite.EPSILON);
        
        ui.setValue(5, 1d);
        for(int i=0; i<5; i++)
            assertEquals(Double.NaN, ui.getValue(i), JRLibTestSuite.EPSILON);
        assertEquals(1d, ui.getValue(5), JRLibTestSuite.EPSILON);
    }
}