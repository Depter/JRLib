package org.jreserve.vector;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestUtl;
import static org.jreserve.TestData.EXPOSURE;
import static org.jreserve.TestData.getCachedVector;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class InputVectorTest {

    private InputVector vector;
    private ChangeCounter changeCounter;
    
    private final static double[] INPUT = getCachedVector(EXPOSURE);
    
    public InputVectorTest() {
    }

    @Before
    public void setUp() {
        vector = new InputVector(INPUT);
        changeCounter = new ChangeCounter();
        vector.addChangeListener(changeCounter);
    }
    
    @Test
    public void testConstructor_Null() {
        vector = new InputVector(null);
        assertEquals(0, vector.getLength());
    }

    @Test
    public void testSetData() {
        double data[] = null;
        vector.setData(data);
        assertEquals(0, vector.getLength());
        assertEquals(1, changeCounter.getChangeCount());
        
        data = new double[]{1d, 2d, 3d, 4d};
        vector.setData(data);
        assertEquals(data.length, vector.getLength());
        assertEquals(2, changeCounter.getChangeCount());
        
        assertEquals(Double.NaN, vector.getValue(-1), JRLibTestUtl.EPSILON);
        for(int i=0; i<data.length; i++)
            assertEquals(data[i], vector.getValue(i), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, vector.getValue(data.length), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetLength() {
        assertEquals(INPUT.length, vector.getLength());
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, vector.getValue(-1), JRLibTestUtl.EPSILON);
        for(int i=0; i<INPUT.length; i++)
            assertEquals(INPUT[i], vector.getValue(i), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, vector.getValue(INPUT.length), JRLibTestUtl.EPSILON);
    }
}