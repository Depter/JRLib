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
public class AbstractVectorModificationTest {

    private AbstractVectorModificationImpl vector;
    private Vector source;
    
    public AbstractVectorModificationTest() {
    }

    @Before
    public void setUp() {
        source = new InputVector(TestData.EXPOSURE);
        vector = new AbstractVectorModificationImpl(source);
    }

    @Test
    public void testGetLength() {
        assertEquals(source.getLength(), vector.getLength());
    }

    @Test
    public void testGetValue() {
        double expected = source.getValue(-1);
        double found = vector.getValue(-1);
        assertEquals(expected, found, JRLibTestSuite.EPSILON);
        
        int length = source.getLength();
        for(int i=0; i<length; i++) {
            expected = source.getValue(i);
            found = vector.getValue(i);
            assertEquals(expected, found, JRLibTestSuite.EPSILON);
        }
        
        expected = source.getValue(length);
        found = vector.getValue(length);
        assertEquals(expected, found, JRLibTestSuite.EPSILON);
    }

    private class AbstractVectorModificationImpl extends AbstractVectorModification {

        public AbstractVectorModificationImpl(Vector source) {
            super(source);
        }
        
        @Override
        protected void recalculateLayer() {
        }
    }

}