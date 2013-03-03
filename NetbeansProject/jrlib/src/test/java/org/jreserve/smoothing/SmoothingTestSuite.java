package org.jreserve.smoothing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    AbstractVectorSmoothingTest.class,
    GeometricMovingAverageTest.class,
    HarmonicMovingAverageTest.class,
    ArithmeticMovingAverageTest.class,
    ExponentialSmoothingTest.class,
    DoubleExponentialSmoothingTest.class
})
public class SmoothingTestSuite {
}