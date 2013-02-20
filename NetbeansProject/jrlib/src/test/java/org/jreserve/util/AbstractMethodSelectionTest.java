package org.jreserve.util;

import java.util.HashMap;
import java.util.Map;
import org.jreserve.CalculationData;
import org.jreserve.ChangeCounter;
import org.jreserve.TestData;
import org.jreserve.triangle.Cell;
import org.jreserve.vector.InputVector;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractMethodSelectionTest {

    private final static Cell DEFAULT = new Cell(0, 0);
    private AbstractOutputSelectionImpl selection;
    private ChangeCounter counter;
    
    public AbstractMethodSelectionTest() {
    }

    @Before
    public void setUp() {
        InputVector source = new InputVector(TestData.EXPOSURE);
        selection = new AbstractOutputSelectionImpl(source, DEFAULT);
        counter = new ChangeCounter();
        selection.addChangeListener(counter);
    }
    
    @Test(expected=NullPointerException.class)
    public void testConstructor_NullMethod() {
        InputVector source = new InputVector(TestData.EXPOSURE);
        selection = new AbstractOutputSelectionImpl(source, null);
    }
    
    @Test
    public void testGetDefaultMethod() {
        assertEquals(DEFAULT, selection.getDefaultMethod());
    }

    @Test
    public void testSetDefaultMethod() {
        Cell method = new Cell(1,1);
        selection.setDefaultMethod(method);
        assertEquals(method, selection.getDefaultMethod());
    }

    @Test(expected=NullPointerException.class)
    public void testSetDefaultMethod_Null() {
        selection.setDefaultMethod(null);
    }

    @Test
    public void testGetMethod() {
        for(int d=0; d<5; d++)
            assertEquals(DEFAULT, selection.getMethod(d));
    }

    @Test
    public void testSetMethod() {
        Cell method = new Cell(1, 1);
        selection.setMethod(method, 1);
        assertEquals(method, selection.getMethod(1));
        assertEquals(1, counter.getChangeCount());
        
        
        selection.setMethod(null, 1);
        assertEquals(DEFAULT, selection.getMethod(1));
        assertEquals(2, counter.getChangeCount());
    }

    @Test
    public void testSetMethods() {
        Map<Integer, Cell> methods = new HashMap<Integer, Cell>();
        methods.put(1, new Cell(1, 1));
        methods.put(2, new Cell(1, 2));
        methods.put(3, new Cell(1, 3));
        selection.setMethods(methods);
        assertEquals(1, counter.getChangeCount());
        
        for(int i=1; i<4; i++) {
            Cell cell = selection.getMethod(i);
            assertEquals(i, cell.getDevelopment());
        }
    }

    public class AbstractOutputSelectionImpl extends AbstractMethodSelection<CalculationData, Cell> {

        public AbstractOutputSelectionImpl(CalculationData source, Cell defaultCell) {
            super(source, defaultCell);
        }

        @Override
        protected void recalculateLayer() {
        }
    }
}