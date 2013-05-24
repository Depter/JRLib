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
package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestConfig
import org.jreserve.jrlib.bootstrap.util.HistogramData

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class BootstrapUtilDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "double[][] data = [\n" +
        "  [1 , 2 , 3],\n"      +
        "  [4 , 5 , 6],\n"      +
        "  [7 , 8 , 9],\n"      +
        "  [10, 11, 12],\n"     +
        "  [13, 14, 15]\n"      +
        "]\n"                   ;
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new BootstrapUtilDelegate())
    }
    
    @Test
    void testTotalReserves() {
        double[] totals = executeScript "totalReserves(data)"
        double[] expected = [6, 15, 24, 33, 42]
        assertArrayEquals(expected, totals, TestConfig.EPSILON)
    }
    
    def executeScript(String script) {
        executor.runScript(BASE_SCRIPT + script)
    }
    
    @Test
    void testAccidentReserves() {
        double[] totals = executeScript "accidentReserves(data, 1)"
        double[] expected = [2, 5, 8, 11, 14]
        assertArrayEquals(expected, totals, TestConfig.EPSILON)
    }
    
    @Test
    void testMeanReserve() {
        double mean = executeScript "meanReserve(data)"
        assertEquals(24, mean, TestConfig.EPSILON)
    }
    
    @Test
    void testMeanReserve_int() {
        double mean = executeScript "meanReserve(data, 1)"
        assertEquals(8, mean, TestConfig.EPSILON)
    }
    
    @Test
    void testAccidentMeans() {
        double[] means = executeScript "accidentMeans(data)"
        double[] expected = [7, 8, 9]
        assertArrayEquals(expected, means, TestConfig.EPSILON)
    }
    
    @Test
    void testShift() {
        String script = "total = totalReserves(data)\n shift(total, 25)\n total"
        double[] shifted = executeScript(script)
        double[] expected = [7, 16, 25, 34, 43]
        assertArrayEquals(expected, shifted, TestConfig.EPSILON)
    }
    
    @Test
    void testShift_all() {
        String script = 
            "double[] means = [8, 9, 10]\n" +
            "shift(data, means)\n"          +
            "data"                         ;
        
        double[][] shifted = executeScript(script)
        double[][] expected = [
            [2 , 3 , 4],
            [5 , 6 , 7],
            [8 , 9 , 10],
            [11, 12, 13],
            [14, 15, 16]
        ]
        
        assertEquals(expected.length, shifted.length)
        for(int i=0; i<expected.length; i++)
            assertArrayEquals(expected[i], shifted[i], TestConfig.EPSILON)
    }
    
    @Test
    void testScale() {
        String script = "total = totalReserves(data)\n scale(total, 25)\n total"
        double[] scaled = executeScript(script)
        double[] expected = [6.25, 15.625, 25, 34.375, 43.75]
        assertArrayEquals(expected, scaled, TestConfig.EPSILON)
    }
    
    @Test
    void testScale_all() {
        String script = 
            "double[] means = [14, 24, 4.5]\n" +
            "scale(data, means)\n"          +
            "data"                         ;
        
        double[][] scaled = executeScript(script)
        double[][] expected = [
            [2 , 6 , 1.5],
            [8 , 15, 3.0],
            [14, 24, 4.5],
            [20, 33, 6.0],
            [26, 42, 7.5]
        ]
        
        assertEquals(expected.length, scaled.length)
        for(int i=0; i<expected.length; i++)
            assertArrayEquals(expected[i], scaled[i], TestConfig.EPSILON)
    }
    
    @Test
    public void testPercentile() {
        double mean = executeScript "total = totalReserves(data) \n percentile(total, 0.5)\n"
        assertEquals(24, mean, TestConfig.EPSILON)
    }
    
    @Test
    public void testHistogram() {
        String script = 
            "total = totalReserves(data)\n" +
            "histogram(total)\n"            ;
        HistogramData data = executeScript(script)
        assertEquals(2, data.getIntervalCount())
    }
    
    @Test
    public void testHistogram_int() {
        String script = 
            "total = totalReserves(data)\n" +
            "histogram(total, 3)\n"         ;
        HistogramData data = executeScript(script)
        assertEquals(3, data.getIntervalCount())
    }
    
    @Test
    public void testHistogram_double_double() {
        String script = 
            "total = totalReserves(data)\n" +
            "histogram(total, 5, 5)\n"         ;
        HistogramData data = executeScript(script)
        assertEquals(9, data.getIntervalCount())
    }
}

