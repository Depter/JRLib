package org.jreserve.triangle;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class CellTest {

    private final static int ACCIDENT = 1;
    private final static int DEVELOPMENT = 3;
    
    private Cell cell;
    
    public CellTest() {
    }

    @Before
    public void setUp() {
        cell = new Cell(ACCIDENT, DEVELOPMENT);
    }

    @Test
    public void testGetAccident() {
        assertEquals(ACCIDENT, cell.getAccident());
    }

    @Test
    public void testGetDevelopment() {
        assertEquals(DEVELOPMENT, cell.getDevelopment());
    }

    @Test
    public void testCompareTo() {
        Cell o = new Cell(ACCIDENT, DEVELOPMENT);
        assertTrue(cell.compareTo(o) == 0);
        assertTrue(o.compareTo(cell) == 0);
        
        o = new Cell(ACCIDENT, DEVELOPMENT+1);
        assertTrue(cell.compareTo(o) < 0);
        assertTrue(o.compareTo(cell) > 0);
        
        o = new Cell(ACCIDENT+1, DEVELOPMENT);
        assertTrue(cell.compareTo(o) < 0);
        assertTrue(o.compareTo(cell) > 0);
        
        o = new Cell(ACCIDENT, DEVELOPMENT-1);
        assertTrue(cell.compareTo(o) > 0);
        assertTrue(o.compareTo(cell) < 0);
        
        o = new Cell(ACCIDENT-1, DEVELOPMENT);
        assertTrue(cell.compareTo(o) > 0);
        assertTrue(o.compareTo(cell) < 0);
    }

    @Test(expected=NullPointerException.class)
    public void testCompareTo_Null() {
        cell.compareTo(null);
    }

    @Test
    public void testEquals() {
        Cell o = null;
        assertFalse(cell.equals(o));
        
        o = new Cell(ACCIDENT, DEVELOPMENT);
        assertTrue(cell.equals(o));
        assertTrue(o.equals(cell));
        
        o = new Cell(ACCIDENT, DEVELOPMENT+1);
        assertFalse(cell.equals(o));
        assertFalse(o.equals(cell));
        
        o = new Cell(ACCIDENT+1, DEVELOPMENT);
        assertFalse(cell.equals(o));
        assertFalse(o.equals(cell));
        
        o = new Cell(ACCIDENT, DEVELOPMENT-1);
        assertFalse(cell.equals(o));
        assertFalse(o.equals(cell));
        
        o = new Cell(ACCIDENT-1, DEVELOPMENT);
        assertFalse(cell.equals(o));
        assertFalse(o.equals(cell));
    }

    @Test
    public void testToString() {
        String expected = "Cell ["+ACCIDENT+"; "+DEVELOPMENT+"]";
        assertEquals(expected, cell.toString());
    }

}