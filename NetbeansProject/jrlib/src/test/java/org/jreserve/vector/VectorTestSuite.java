package org.jreserve.vector;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    AbstractVectorTest.class,
    InputVectorTest.class,
    AbstractVectorModificationTest.class,
    VectorCorrectionTest.class
})
public class VectorTestSuite {
}