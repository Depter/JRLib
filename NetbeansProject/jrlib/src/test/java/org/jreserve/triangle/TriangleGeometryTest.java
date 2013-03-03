package org.jreserve.triangle;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class TriangleGeometryTest {

    private final static int ACCIDENTS = 4;
    private final static int DEVELOPMENTS = 7;
    private final static int ACCIDENT_LENGTH = 2;
    
    private TriangleGeometry geometry;
    
    public TriangleGeometryTest() {
    }

    @Before
    public void setUp() {
        geometry = new TriangleGeometry(ACCIDENTS, DEVELOPMENTS, ACCIDENT_LENGTH);
    }

    @Test
    public void testGetParameters() {
        assertEquals(ACCIDENTS, geometry.getAccidents());
        assertEquals(DEVELOPMENTS, geometry.getDevelopments());
        assertEquals(ACCIDENT_LENGTH, geometry.getAccidentLength());
    }

    @Test
    public void testEquals_Object() {
        Object o = null;
        assertFalse(geometry.equals(o));
        
        o = new Object();
        assertFalse(geometry.equals(o));
    }

    @Test
    public void testEquals_TriangleGeometry() {
        assertTrue(geometry.equals(geometry));
        TriangleGeometry o = null;
        assertFalse(geometry.equals(o));
        
        o = new TriangleGeometry(ACCIDENTS, DEVELOPMENTS, ACCIDENT_LENGTH);
        assertTrue(geometry.equals(o));
        assertTrue(o.equals(geometry));
        
        o = new TriangleGeometry(ACCIDENTS, DEVELOPMENTS, ACCIDENT_LENGTH - 1);
        assertFalse(geometry.equals(o));
        assertFalse(o.equals(geometry));
        
        o = new TriangleGeometry(ACCIDENTS, DEVELOPMENTS - 1, ACCIDENT_LENGTH);
        assertFalse(geometry.equals(o));
        assertFalse(o.equals(geometry));
        
        o = new TriangleGeometry(ACCIDENTS - 1, DEVELOPMENTS, ACCIDENT_LENGTH);
        assertFalse(geometry.equals(o));
        assertFalse(o.equals(geometry));
    }

    @Test
    public void testToString() {
        String expected = 
            "TriangleGeometry ["+
                ACCIDENTS+"; "+DEVELOPMENTS+"; "+
                ACCIDENT_LENGTH+"]";
        assertEquals(expected, geometry.toString());
    }

}