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
package org.jreserve.jrlib.triangle.ratio;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultRatioTriangleTest {

    private final static double[][] NUMERATOR_DATA = {
        {1, 2, 3, 4, 5},
        {6, 7, 8},
        {9}
    };
    
    private final static double[][] DENOMINATOR_DATA = {
        {2, 4, 6},
        {12},
    };
    
    private RatioTriangle ratios;
    
    @Before
    public void setUp() {
        ClaimTriangle numerator = new InputClaimTriangle(NUMERATOR_DATA);
        ClaimTriangle denominator = new InputClaimTriangle(DENOMINATOR_DATA);
        ratios = new DefaultRatioTriangle(numerator, denominator);
    }
    
    @Test
    public void testGetAccidentCount() {
        assertEquals(DENOMINATOR_DATA.length, ratios.getAccidentCount());
    }
    
    @Test
    public void testGetDevelopmentCount() {
        assertEquals(DENOMINATOR_DATA[0].length, ratios.getDevelopmentCount());
    }
    
    @Test
    public void testGetDevelopmentCount_Int() {
        assertEquals(0, ratios.getDevelopmentCount(-1));
        
        int accidents = DENOMINATOR_DATA.length;
        for(int a=0; a<accidents; a++) {
            int expected = DENOMINATOR_DATA[a].length;
            assertEquals(expected, ratios.getDevelopmentCount(a));
        }
        
        assertEquals(0, ratios.getDevelopmentCount(accidents));
    }
    
    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, ratios.getValue(-1, 0), TestConfig.EPSILON);
        
        int accidents = DENOMINATOR_DATA.length;
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, ratios.getValue(a, -1), TestConfig.EPSILON);
            int devs = DENOMINATOR_DATA[a].length;
        
            for(int d=0; d<devs; d++)
                assertEquals(0.5d, ratios.getValue(a, d), TestConfig.EPSILON);
            
            assertEquals(Double.NaN, ratios.getValue(a, devs), TestConfig.EPSILON);
        }        
        
        assertEquals(Double.NaN, ratios.getValue(accidents, 0), TestConfig.EPSILON);
    }
}