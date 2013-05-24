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
package org.jreserve.jrlib.util;

import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MathUtilTest {

    public MathUtilTest() {
    }
    
    @Test
    public void testMean() {
        double[] x = {1d, Double.NaN, 2d, 3d, Double.NaN, 4d, 5d, 6d};
        assertEquals(3.5, MathUtil.mean(x), TestConfig.EPSILON);
    }
    
    @Test
    public void testMean_Empty() {
        double[] x = new double[0];
        assertEquals(Double.NaN, MathUtil.mean(x), TestConfig.EPSILON);
    }
    
    @Test
    public void testSigma() {
        double[] x = {1d, Double.NaN, 2d, 3d, Double.NaN, 4d, 5d, 6d};
        double expected = 1.87082869;
        assertEquals(expected, MathUtil.standardDeviation(x), TestConfig.EPSILON);
    }

}
