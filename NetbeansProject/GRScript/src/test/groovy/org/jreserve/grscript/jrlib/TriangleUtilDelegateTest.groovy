package org.jreserve.grscript.jrlib

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestConfig

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleUtilDelegateTest {
    private final static String BASE_SCRIPT = 
        "vA = (double[])[1, 2, 3, 4]\n"         +
        "vB = (double[])[5, 6, 7, 8]\n"         +
        "mA = (double[][])[[1, 2], [3, 4]]\n"   +
        "mB = (double[][])[[5, 6], [7, 8]]\n"    ;
    
    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TriangleUtilDelegate())
    }
    
    @Test
    public void testCummulate_Vector() {
        double[] v = (double[]) runScript("cummulate(vA)\n")
        double[] expected = [1, 3, 6, 10]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
    
    private def runScript(String script) {
        script = BASE_SCRIPT + script
        return executor.runScript(script)
    }
    
    @Test
    public void testDecummulate_Vector() {
        double[] v = (double[]) runScript("decummulate(vA)\n")
        double[] expected = [1, 1, 1, 1]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
    
    @Test
    public void testCopy_Vector() {
        double[] v = (double[]) runScript("copy(vA)\n")
        double[] vA = (double[]) executor.getVariable("vA")
        assertFalse(v.is(vA))
        assertArrayEquals(vA, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testAdd_Vector() {
        double[] v = (double[]) runScript("add(vA, vB)\n")
        double[] expected = [6, 8, 10, 12]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testAddOperator_Vector() {
        double[] v = (double[]) runScript("vA + vB\n")
        double[] expected = [6, 8, 10, 12]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testSubtract_Vector() {
        double[] v = (double[]) runScript("subtract(vA, vB)\n")
        double[] expected = [-4, -4, -4, -4]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testSubtractOperator_Vector() {
        double[] v = (double[]) runScript("vA - vB\n")
        double[] expected = [-4, -4, -4, -4]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testMultiply_Vector() {
        double[] v = (double[]) runScript("multiply(vA, vB)\n")
        double[] expected = [5, 12, 21, 32]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testMultiplyOperator_Vector() {
        double[] v = (double[]) runScript("vA * vB\n")
        double[] expected = [5, 12, 21, 32]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testDivide_Vector() {
        double[] v = (double[]) runScript("divide(vA, vB)\n")
        double[] expected = [1d/5d, 2d/6d, 3d/7d, 4d/8d]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }
	
    @Test
    public void testDivideOperator_Vector() {
        double[] v = (double[]) runScript("vA / vB\n")
        double[] expected = [1d/5d, 2d/6d, 3d/7d, 4d/8d]
        assertArrayEquals(expected, v, TestConfig.EPSILON)
    }

    @Test
    public void testCummulate_Matrix() {
        double[][] m = (double[][]) runScript("cummulate(mA)\n")
        double[][] expected = [[1, 3], [3, 7]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
    
    @Test
    public void testDecummulate_Matrix() {
        double[][] m = (double[][]) runScript("decummulate(mA)\n")
        double[][] expected = [[1, 1], [3, 1]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
    
    @Test
    public void testCopy_Matrix() {
        double[][] m = (double[][]) runScript("copy(mA)\n")
        double[][] mA = (double[][]) executor.getVariable("mA")
        assertFalse(m.is(mA))
        assertEquals(mA.length, m.length)
        for(i in 0..1)
            assertArrayEquals(mA[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testAdd_Matrix() {
        double[][] m = (double[][]) runScript("add(mA, mB)\n")
        double[][] expected = [[6, 8], [10, 12]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testAddOperator_Matrix() {
        double[][] m = (double[][]) runScript("mA + mB\n")
        double[][] expected = [[6, 8], [10, 12]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testSubtract_Matrix() {
        double[][] m = (double[][]) runScript("subtract(mA, mB)\n")
        double[][] expected = [[-4, -4], [-4, -4]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testSubtractOperator_Matrix() {
        double[][] m = (double[][]) runScript("mA - mB\n")
        double[][] expected = [[-4, -4], [-4, -4]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testMultiply_Matrix() {
        double[][] m = (double[][]) runScript("multiply(mA, mB)\n")
        double[][] expected = [[5, 12], [21, 32]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testMultiplyOperator_Matrix() {
        double[][] m = (double[][]) runScript("mA * mB\n")
        double[][] expected = [[5, 12], [21, 32]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testDivide_Matrix() {
        double[][] m = (double[][]) runScript("divide(mA, mB)\n")
        double[][] expected = [[1d/5d, 2d/6d], [3d/7d, 4d/8d]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
	
    @Test
    public void testDivideOperator_Matrix() {
        double[][] m = (double[][]) runScript("mA / mB\n")
        double[][] expected = [[1d/5d, 2d/6d], [3d/7d, 4d/8d]]
        assertEquals(expected.length, m.length)
        for(i in 0..1)
            assertArrayEquals(expected[i], m[i], TestConfig.EPSILON)
    }
}

