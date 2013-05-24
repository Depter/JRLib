/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.triangle;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
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
