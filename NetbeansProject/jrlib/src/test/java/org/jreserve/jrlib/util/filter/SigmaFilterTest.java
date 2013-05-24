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
package org.jreserve.jrlib.util.filter;

import static org.jreserve.jrlib.TestConfig.EPSILON;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SigmaFilterTest {

    private final static double[] DATA = {
        1.21380200, 1.02595959, 1.00242243, 1.00094901
    };
    
    private final static boolean[] TRESHOLD_1 = {
        true, false, false, false
    };
    
    private final static boolean[] TRESHOLD_0_5 = {
        true, false, true, true
    };
    
    private SigmaFilter filter;
    
    public SigmaFilterTest() {
    }

    @Before
    public void setUp() {
        filter = new SigmaFilter();
    }

    @Test
    public void testConstructor() {
        assertEquals(SigmaFilter.DEFAULT_TRESHOLD, filter.getTreshold(), EPSILON);
        
        filter = new SigmaFilter(-1d);
        assertEquals(0d, filter.getTreshold(), EPSILON);
        
        filter = new SigmaFilter(5d);
        assertEquals(5d, filter.getTreshold(), EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NaN() {
        filter = new SigmaFilter(Double.NaN);
    }

    @Test
    public void testSetTreshold() {
        filter.setTreshold(-1d);
        assertEquals(0d, filter.getTreshold(), EPSILON);
        
        filter.setTreshold(1d);
        assertEquals(1d, filter.getTreshold(), EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetTreshold_NaN() {
        filter.setTreshold(Double.NaN);
    }

    @Test
    public void testFilter() {
        filter = new SigmaFilter(1d);
        boolean[] found = filter.filter(DATA);
        assertEquals(TRESHOLD_1.length, found.length);
        for(int i=0; i<found.length; i++)
            assertEquals("For treshold 1.5, index "+i+".", TRESHOLD_1[i], found[i]);
        
        filter.setTreshold(0.5);
        found = filter.filter(DATA);
        assertEquals(TRESHOLD_0_5.length, found.length);
        for(int i=0; i<found.length; i++)
            assertEquals("For treshold 1, index "+i+".", TRESHOLD_0_5[i], found[i]);
    }

}
