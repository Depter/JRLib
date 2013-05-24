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
package org.jreserve.jrlib.vector.smoothing;

import static org.jreserve.jrlib.TestConfig.EPSILON;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HarmonicMovingAverageMethodTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private HarmonicMovingAverageMethod smoothing;

    public HarmonicMovingAverageMethodTest() {
    }

    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }
    
    @Test
    public void testSmooth_Ma3() {
        smoothing = new HarmonicMovingAverageMethod(3);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5279090.20265317, 5838053.64907144, 
            6501939.95319046, 7245523.54772155, 8013328.14998032, 8899928.53423601
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }
    
    @Test
    public void testSmooth_Ma5() {
        smoothing = new HarmonicMovingAverageMethod(5);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5945610.00000000, 6632221.00000000, 
            5803903.47601912, 6433850.54463213, 7211399.89342641, 7932846.33745969
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

}
