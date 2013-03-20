package org.jreserve.vector;

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
public class AbstractVectorModificationTest {

    private AbstractVectorModificationImpl vector;
    private Vector source;
    
    public AbstractVectorModificationTest() {
    }

    @Before
    public void setUp() {
        source = new InputVector(getCachedVector(EXPOSURE));
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
        assertEquals(expected, found, JRLibTestUtl.EPSILON);
        
        int length = source.getLength();
        for(int i=0; i<length; i++) {
            expected = source.getValue(i);
            found = vector.getValue(i);
            assertEquals(expected, found, JRLibTestUtl.EPSILON);
        }
        
        expected = source.getValue(length);
        found = vector.getValue(length);
        assertEquals(expected, found, JRLibTestUtl.EPSILON);
    }

    private class AbstractVectorModificationImpl extends AbstractVectorModification {

        public AbstractVectorModificationImpl(Vector source) {
            super(source);
        }
        
        @Override
        protected void recalculateLayer() {
        }

        public ModifiedVector copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}