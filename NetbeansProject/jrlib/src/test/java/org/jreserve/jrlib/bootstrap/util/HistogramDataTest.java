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
package org.jreserve.jrlib.bootstrap.util;

import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HistogramDataTest {
    
    private final static double[] DATA = {
         0.72357985,  0.10094794, -1.12564889,  0.21082691, -0.12782709, -1.00468624, -2.01513583, -1.55732554,  1.47743869, -1.13904243, 
        -1.11587228,  0.83299096, -1.36872375, -0.18172149,  0.27847089, -0.55920859,  1.19840351, -0.96365140, -1.04940305, -0.42256035, 
        -1.32480054, -0.20072318, -1.02054928, -0.28456858,  0.71299122, -0.39576593, -0.36693587, -1.07229462, -0.86663096,  1.20579886, 
        -0.45140298, -1.25386090, -0.27728748,  2.10496368, -0.55614304, -0.53997980, -1.36298419,  2.86299670, -0.66080415,  1.91198772, 
         1.27364749, -0.60846915,  1.43107131, -0.62525078, -0.02008894,  1.06088225,  1.02233089,  0.75797482,  0.28432777,  1.54290740, 
        -0.00940073,  0.69041377,  1.56410993,  0.06655731, -1.92506154, -0.16970427,  0.60206416,  0.89736471, -0.69795590, -0.59851813, 
         2.21860103, -0.09596256, -0.11625549,  0.72500603, -1.21526011, -0.38839992,  0.55407580,  1.31829916, -1.88994863, -0.35130027, 
         2.48947307, -0.81123061, -0.11856241, -2.18873699, -2.44957704,  0.02854348,  0.26468524, -0.32195421,  0.12692397,  1.21773016, 
        -0.58561398, -0.19648000, -1.60907255, -0.25836783,  0.1328569,  -0.68193059, -0.05601889, -0.37988900, -0.46512262,  0.45675103, 
         0.24251842, -1.22069735, -0.47339959, -1.79239063,  1.54646582,  1.22639956, -0.39653442, -1.22582793, -0.52039959, -0.57593259, 
        -0.99334043,  0.47434468, -1.89818153,  1.39409356,  1.18944086,  1.26565750,  2.01640039, -0.59438188, -0.02418918,  1.80128148
    };
    private final static double FIRST_UPPER = -2.15443405;
    private final static double SIZE = 0.59028597;
    private final static int COUNT = 10;
    
    private HistogramData hist;
    
    @Before
    public void setUp() {
        hist = new HistogramData(FIRST_UPPER, SIZE, COUNT, DATA);
    }
    
    @Test
    public void testIntervalCount() {
        assertEquals(COUNT, hist.getIntervalCount());
    }
    
    @Test
    public void testGetCount() {
        int[] expected = {2, 6, 16, 23, 24, 14, 12, 8, 4, 1};
        assertEquals(0, hist.getCount(-1));
        for(int i=0; i<COUNT; i++)
            assertEquals(expected[i], hist.getCount(i));
        assertEquals(0, hist.getCount(COUNT));
    }
    
    @Test
    public void testGetLowerBound() {
        double[] expected = {
            Double.NEGATIVE_INFINITY, -2.15443405, -1.56414808, -0.97386211, 
            -0.38357614, 0.20670983, 0.79699580, 1.38728177, 1.97756774, 
            2.56785371
        };
        assertEquals(Double.NaN, hist.getLowerBound(-1), TestConfig.EPSILON);
        for(int i=0; i<COUNT; i++)
            assertEquals(expected[i], hist.getLowerBound(i), TestConfig.EPSILON);
        assertEquals(Double.NaN, hist.getLowerBound(COUNT), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetUpperBound() {
        double[] expected = {
            -2.15443405, -1.56414808, -0.97386211, -0.38357614, 0.20670983, 
             0.79699580,  1.38728177,  1.97756774,  2.56785371,
            Double.POSITIVE_INFINITY

        };
        assertEquals(Double.NaN, hist.getUpperBound(-1), TestConfig.EPSILON);
        for(int i=0; i<COUNT; i++)
            assertEquals(expected[i], hist.getUpperBound(i), TestConfig.EPSILON);
        assertEquals(Double.NaN, hist.getUpperBound(COUNT), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetBounds_int() {
        double[][] expected = {
            {Double.NEGATIVE_INFINITY, -2.15443405},
            {-2.15443405, -1.56414808},
            {-1.56414808, -0.97386211},
            {-0.97386211, -0.38357614},
            {-0.38357614,  0.20670983},
            { 0.20670983,  0.79699580},
            { 0.79699580,  1.38728177},
            { 1.38728177,  1.97756774},
            { 1.97756774,  2.56785371},
            { 2.56785371,  Double.POSITIVE_INFINITY}
        };
        assertArrayEquals(null, hist.getBounds(-1), TestConfig.EPSILON);
        for(int i=0; i<COUNT; i++)
            assertArrayEquals(expected[i], hist.getBounds(i), TestConfig.EPSILON);
        assertArrayEquals(null, hist.getBounds(COUNT), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetBounds() {
        double[][] expected = {
            {Double.NEGATIVE_INFINITY, -2.15443405},
            {-2.15443405, -1.56414808},
            {-1.56414808, -0.97386211},
            {-0.97386211, -0.38357614},
            {-0.38357614,  0.20670983},
            { 0.20670983,  0.79699580},
            { 0.79699580,  1.38728177},
            { 1.38728177,  1.97756774},
            { 1.97756774,  2.56785371},
            { 2.56785371,  Double.POSITIVE_INFINITY}
        };
        double[][] found = hist.getBounds();
        assertEquals(expected.length, found.length);
        for(int i=0; i<found.length; i++)
            assertArrayEquals(expected[i], found[i], TestConfig.EPSILON);
    }
    
    @Test
    public void testGetCounts() {
        int[] expected = {2, 6, 16, 23, 24, 14, 12, 8, 4, 1};
        assertArrayEquals(expected, hist.getCounts());
    }
    
    @Test
    public void testToArray() {
        double[][] expected = {
            {Double.NEGATIVE_INFINITY, -2.15443405, 2d},
            {-2.15443405, -1.56414808, 6d},
            {-1.56414808, -0.97386211, 16d},
            {-0.97386211, -0.38357614, 23d},
            {-0.38357614,  0.20670983, 24d},
            { 0.20670983,  0.79699580, 14d},
            { 0.79699580,  1.38728177, 12d},
            { 1.38728177,  1.97756774, 8d},
            { 1.97756774,  2.56785371, 4d},
            { 2.56785371,  Double.POSITIVE_INFINITY, 1d}
        };
        double[][] found = hist.toArray();
        assertEquals(expected.length, found.length);
        for(int i=0; i<found.length; i++)
            assertArrayEquals(expected[i], found[i], TestConfig.EPSILON);
    }
}
