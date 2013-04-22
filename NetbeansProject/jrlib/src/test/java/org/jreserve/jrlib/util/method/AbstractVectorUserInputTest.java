package org.jreserve.jrlib.util.method;

import org.jreserve.jrlib.util.method.AbstractVectorUserInput;
import org.jreserve.jrlib.util.method.SelectableMethod;
import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractVectorUserInputTest {
    
    public AbstractVectorUserInputTest() {
    }
    
    @Test
    public void testConstructor_array() {
        double[] expected = new double[]{1d, 2d, 3d};
        AbstractVectorUserInput ui = new AbstractVectorUserInputImpl(expected);
        
        for(int i=0; i<expected.length; i++)
            assertEquals(expected[i], ui.getValue(i), TestConfig.EPSILON);
        assertEquals(Double.NaN, ui.getValue(expected.length), TestConfig.EPSILON);
        
        double e = expected[1];
        expected[1] += 10d;
        assertEquals(e, ui.getValue(1), TestConfig.EPSILON);
    }

    @Test
    public void testSetValue() {
        AbstractVectorUserInput ui = new AbstractVectorUserInputImpl();
        assertEquals(Double.NaN, ui.getValue(-1), TestConfig.EPSILON);
        assertEquals(Double.NaN, ui.getValue(5), TestConfig.EPSILON);
        
        ui.setValue(5, 1d);
        for(int i=0; i<5; i++)
            assertEquals(Double.NaN, ui.getValue(i), TestConfig.EPSILON);
        assertEquals(1d, ui.getValue(5), TestConfig.EPSILON);
    }
    
    private class AbstractVectorUserInputImpl extends AbstractVectorUserInput {

        private AbstractVectorUserInputImpl(double[] values) {
            super(values);
        }

        private AbstractVectorUserInputImpl() {
        }
        
        public SelectableMethod copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
