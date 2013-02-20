package org.jreserve.vector;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
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
    
    public InputVectorTest() {
    }

    @Before
    public void setUp() {
        vector = new InputVector(TestData.EXPOSURE);
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
        
        assertEquals(Double.NaN, vector.getValue(-1), JRLibTestSuite.EPSILON);
        for(int i=0; i<data.length; i++)
            assertEquals(data[i], vector.getValue(i), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, vector.getValue(data.length), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetLength() {
        assertEquals(TestData.EXPOSURE.length, vector.getLength());
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, vector.getValue(-1), JRLibTestSuite.EPSILON);
        for(int i=0; i<TestData.EXPOSURE.length; i++)
            assertEquals(TestData.EXPOSURE[i], vector.getValue(i), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, vector.getValue(TestData.EXPOSURE.length), JRLibTestSuite.EPSILON);
    }

}