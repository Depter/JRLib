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
package org.jreserve.jrlib.vector;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jreserve.jrlib.TestConfig;
        
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorCorrectionTest {

    private final static double[] DATA = {1 , 2 , 3 , 4 , 5 , 6 , 7};

    private final static double CORRECTION = 0.5;
    private final static int INDEX = 3;

    private Vector source;
    private VectorCorrection corrigated;
    private VectorCorrection outsider;
    
    public VectorCorrectionTest() {
    }
    
    @Before
    public void setUp() {
        source = new InputVector(DATA);
        corrigated = new VectorCorrection(source, INDEX, CORRECTION);
        int last = source.getLength();
        outsider = new VectorCorrection(source, last, CORRECTION);
    }


    @Test
    public void testGetIndex() {
        assertEquals(INDEX, corrigated.getCorrigatedIndex());
        assertEquals(DATA.length, outsider.getCorrigatedIndex());
    }

    @Test
    public void testGetCorrection() {
        assertEquals(CORRECTION, corrigated.getCorrigatedValue(), TestConfig.EPSILON);
        assertEquals(CORRECTION, outsider.getCorrigatedValue(), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetLength() {
        assertEquals(source.getLength(), corrigated.getLength());
        assertEquals(source.getLength(), outsider.getLength());
    }

    @Test
    public void testGetValue() {
        int length = source.getLength();
        
        assertTrue(Double.isNaN(corrigated.getValue(-1)));
        assertTrue(Double.isNaN(outsider.getValue(-1)));
        for(int i=0; i<length; i++) {
            double expected = source.getValue(i);
            assertEquals(expected, outsider.getValue(i), TestConfig.EPSILON);
                
            if(i == INDEX)
                expected = CORRECTION;
            assertEquals(expected, corrigated.getValue(i), TestConfig.EPSILON);
        }
        assertTrue(Double.isNaN(corrigated.getValue(length)));
        assertTrue(Double.isNaN(outsider.getValue(length)));
    }
}