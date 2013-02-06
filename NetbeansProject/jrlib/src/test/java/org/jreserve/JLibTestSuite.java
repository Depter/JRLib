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
    org.jreserve.triangle.TriangleTestSuite.class
})
public class JLibTestSuite {
    
    public final static double EPSILON = 0.0000000001;
}