package org.jreserve;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    AbstractChangeableTest.class,
    AbstractCalculationDataTest.class,
    AbstractDoubleInputCalculationDataTest.class,
    org.jreserve.util.UtilTestSuite.class,
    org.jreserve.vector.VectorTestSuite.class,
    org.jreserve.triangle.TriangleTestSuite.class,
    org.jreserve.smoothing.SmoothingTestSuite.class,
    org.jreserve.factor.FactorTestSuite.class,
    org.jreserve.test.TestTestSuite.class
})
public class JRLibTestSuite {
    
    public final static double EPSILON = 0.0000001;
    public final static int PRECISION = 7;
    
    public static double roundToPrecision(double value) {
        double multiplier = Math.pow(10, PRECISION);
        value = value * multiplier;
        value = Math.round(value);
        return value/multiplier;
    }
    
    public static void roundToPrecision(double[] values) {
        for(int i=0; i<values.length; i++)
            values[i] = roundToPrecision(values[i]);
    }
}