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
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.jrlib.vector.Vector as JRVector

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class VectorDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "data = apcExposure()\n"+
        "exposure = vector(data)\n";
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new VectorDelegate())
    }
    
    @Test
    public void testConstructor() {
        JRVector v = runScript("")
        assertEquals(8, v.getLength())
        for(i in 0..<8)
            assertFalse(Double.isNaN(v.getValue(i)))
    }
    
    @Test
    public void testConstructor_List() {
        JRVector v = executor.runScript "vector([1, 2, 3])"
        assertEquals(3, v.getLength())
        for(i in 0..2)
            assertEquals((double)i+1, v.getValue(i), TestConfig.EPSILON)
    }
    
    private JRVector runScript(String script) {
        script = BASE_SCRIPT + script
        return (JRVector) executor.runScript(script)
    }
    
    @Test
    public void testConstructor_int_double() {
        JRVector v = runScript("exposure = vector(5, 3.4)")
        assertEquals(5, v.getLength())
        for(i in 0..<5)
            assertEquals(3.4, v.getValue(i), TestConfig.EPSILON)
    }
    
    @Test
    public void testCorrigate_int_double() {
        JRVector v = runScript("exposure = corrigate(exposure, 2, 100.5)\n")
        double[] data = (double[]) executor.getVariable("data")
        
        assertEquals(8, v.getLength())
        for(i in 0..<8) {
            double expected = i==2? 100.5 : data[i]
            assertEquals(expected, v.getValue(i), TestConfig.EPSILON)
        }
    }
    
    @Test
    public void testCorrigate_Map() {
        JRVector v = runScript("exposure = corrigate(exposure, [1:100.5, 3:50.2])\n")
        double[] data = (double[]) executor.getVariable("data")
        
        assertEquals(8, v.getLength())
        for(i in 0..<8) {
            if(i==1) {
                assertEquals(100.5, v.getValue(i), TestConfig.EPSILON)
            } else if(i==3) {
                assertEquals(50.2, v.getValue(i), TestConfig.EPSILON)
            } else {
                assertEquals(data[i], v.getValue(i), TestConfig.EPSILON)
            }
        }
    }
    
    @Test
    public void testExclude_intArr() {
        JRVector v = runScript("exposure = exclude(exposure, 1, 3)\n")
        double[] data = (double[]) executor.getVariable("data")
        
        assertEquals(8, v.getLength())
        for(i in 0..<8) {
            if(i==1 || i==3) {
                assertTrue(Double.isNaN(v.getValue(i)))
            } else {
                assertEquals(data[i], v.getValue(i), TestConfig.EPSILON)
            }
        }
    }
    
    @Test
    public void testExclude_Collection() {
        JRVector v = runScript("exposure = exclude(exposure, 1..3)\n")
        double[] data = (double[]) executor.getVariable("data")
        
        assertEquals(8, v.getLength())
        for(i in 0..<8) {
            if(i>=1 && i<=3) {
                assertTrue(Double.isNaN(v.getValue(i)))
            } else {
                assertEquals(data[i], v.getValue(i), TestConfig.EPSILON)
            }
        }
    }
    
    @Test
    public void testSmooth() {
        String script = 
        "exposure = smooth(exposure) {\n"+
        "   type(type:\"exponential\", alpha:0.5)\n"+
        "   cells(1:false, 2:false, 3:true)\n"+
        "}\n";
        JRVector v = runScript(script)
        double[] data = (double[]) executor.getVariable("data")
        
        assertEquals(8, v.getLength())
        for(i in 0..<8) {
            if(i==3) {
                assertNotEquals(data[i], v.getValue(i), TestConfig.EPSILON)
            } else {
                assertEquals(data[i], v.getValue(i), TestConfig.EPSILON)
            }
        }        
    }
    
    @Test
    public void testBuilder() {
        String script = 
        "exposure = vector(data) {\n"                       +
        "    corrigate(0, 90000)\n"                         +
        "    corrigate(1:90000, 2:50000)\n"                 +
        "    smooth {\n"                                    +
        "        type(name:\"Exponential\", alpha:0.5)\n"   +
        "        cells(5:false, 6:false, 7:true)\n"         +
        "    }\n"                                           +
        "    exclude(3, 4)\n"                               +
        "    exclude(5..6)\n"                               +
        "}\n"                                               ;

        JRVector v = runScript(script)
        double[] data = (double[]) executor.getVariable("data")
        
        assertEquals(8, v.getLength())
        for(i in 0..<8) {
            if(2<i && i<7) {
                assertTrue(Double.isNaN(v.getValue(i)))
            } else if(i==0 || i==1) {
                assertEquals(90000, v.getValue(i), TestConfig.EPSILON)
            } else if(i==2) {
                assertEquals(50000, v.getValue(i), TestConfig.EPSILON)
            } else {
                assertFalse(Double.isNaN(v.getValue(i)))
                assertNotEquals(data[i], v.getValue(i), TestConfig.EPSILON)
            }
        }        
        
    }
	
}

