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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleExponentialSmoothingMethodTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private DoubleExponentialSmoothingMethod smoothing;

    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaLess0() {
        new DoubleExponentialSmoothingMethod(-EPSILON, 0.5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaMore1() {
        new DoubleExponentialSmoothingMethod(1+EPSILON, 0.5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_BetaLess0() {
        new DoubleExponentialSmoothingMethod(0.5, -EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_BetaMore1() {
        new DoubleExponentialSmoothingMethod(0.5, 1+EPSILON);
    }

    @Test
    public void testSmooth_0_0() {
        smoothing = new DoubleExponentialSmoothingMethod(0d, 0d);
        double[] expected = new double[] {
            4873558, 5130849, 5388140, 5645431, 
            5902722, 6160013, 6417304, 6674595
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

    @Test
    public void testSmooth_1_1() {
        smoothing = new DoubleExponentialSmoothingMethod(1d, 1d);
        smoothing.smooth(input);
        for(int i=0; i<INPUT.length; i++)
            assertEquals(INPUT[i], input[i], EPSILON);
    }

    @Test
    public void testSmooth_8_2() {
        smoothing = new DoubleExponentialSmoothingMethod(0.8, 0.2);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5834116.00000000, 6541897.24000000, 
            7008907.68960000, 8107822.78918400, 8934358.48283136, 9519164.58430781
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

    @Test
    public void testSmooth_2_8() {
        smoothing = new DoubleExponentialSmoothingMethod(0.2, 0.8);
        double[] expected = new double[] {
            4873558.00000000, 5130849.00000000, 5499634.00000000, 6003340.36000000, 
            6584676.95040000, 7370449.35065600, 8288850.69475584, 9239648.77887478
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }
}
