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
public class ExponentialSmoothingMethodTest {

    private final static double[] INPUT = {
        4873558, 5130849, 5945610, 6632221, 
        7020974, 8275452, 9000367, 9511539
    };
    
    private double[] input;
    private ExponentialSmoothingMethod smoothing;

    @Before
    public void setUp() {
        int size = INPUT.length;
        input = new double[size];
        System.arraycopy(INPUT, 0, input, 0, size);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaLess0() {
        new ExponentialSmoothingMethod(-EPSILON);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_AlphaMore1() {
        new ExponentialSmoothingMethod(1+EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha0() {
        smoothing = new ExponentialSmoothingMethod(0d);
        smoothing.smooth(input);
        for(int i=0; i<input.length; i++)
            assertEquals(INPUT[0], input[i], EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha1() {
        smoothing = new ExponentialSmoothingMethod(1d);
        smoothing.smooth(input);
        for(int i=0; i<input.length; i++)
            assertEquals(INPUT[i], input[i], EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha0_2() {
        smoothing = new ExponentialSmoothingMethod(0.2);
        double[] expected = new double[] {
            4873558.00000000, 4925016.20000000, 5129134.96000000, 5429752.16800000, 
            5747996.53440000, 6253487.62752000, 6802863.50201600, 7344598.60161280
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }
    
    @Test
    public void testSmooth_Alpha0_8() {
        smoothing = new ExponentialSmoothingMethod(0.8);
        double[] expected = new double[] {
            4873558.00000000, 5079390.80000000, 5772366.16000000, 6460250.03200000, 
            6908829.20640000, 8002127.44128000, 8800719.08825600, 9369375.01765120
        };
        smoothing.smooth(input);
        assertArrayEquals(expected, input, EPSILON);
    }

}
