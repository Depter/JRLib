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
package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleScaleTest {

    private final static double[] EXPECTED = {
        1336.96846717, 988.47642646, 440.13971098, 206.98511051, 
         164.19978361,  74.60176287,  35.49315669,  16.88652015
    };
    
    private LinkRatioScaleInput source;
    private SimpleScale<LinkRatioScaleInput> scales;

    @Before
    public void setUp() {
        ClaimTriangle claims = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        source = new LinkRatioScaleInput(claims);
        scales = new SimpleScale<LinkRatioScaleInput>(source);
    }

    @Test
    public void testGetSourceInput() {
        assertEquals(source, scales.getSourceInput());
    }

    @Test
    public void testGetLength() {
        assertEquals(source.getDevelopmentCount(), scales.getLength());
    }

    @Test
    public void testGetValue() {
        assertEquals(Double.NaN, scales.getValue(-1), TestConfig.EPSILON);
        
        int devs = EXPECTED.length;
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], scales.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, scales.getValue(devs), TestConfig.EPSILON);
    }

    @Test
    public void testToArray() {
        assertArrayEquals(EXPECTED, scales.toArray(), TestConfig.EPSILON);
    }
}
