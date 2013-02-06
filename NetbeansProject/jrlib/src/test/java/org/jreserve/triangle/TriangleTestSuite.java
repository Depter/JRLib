package org.jreserve.triangle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TriangleGeometryTest.class,
    InputTriangleTest.class,
    AbstractTriangularCalculationDataTest.class,
    TriangleCorrectionTest.class
})
public class TriangleTestSuite {
}