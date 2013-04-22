package org.jreserve.jrlib.triangle.claim;

import org.jreserve.jrlib.triangle.claim.CummulatedClaimTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CummulatedClaimTriangleTest {
    
    private final static double[][] EXPECTED = {
        {4873559, 5726628 , 5643478, 5703210, 5787091, 5858937, 5862561, 5791898},
        {5130849, 6098121 , 6177774, 6165459, 6292000, 6304102, 6304337},
        {5945611, 7525656 , 7557815, 7377335, 7442120, 7418866},
        {6632221, 7861102 , 7532937, 7528468, 7525868},
        {7020974, 8688586 , 8712864, 8674967},
        {8275453, 9867326 , 9932304},
        {9000368, 10239888},
        {9511539}
    };
    
    private ClaimTriangle triangle;
    
    public CummulatedClaimTriangleTest() {
    }

    @Before
    public void setUp() {
        triangle = TestData.getTriangle(TestData.INCURRED);
        triangle = new CummulatedClaimTriangle(triangle);
    }

    @Test
    public void testGetValue() {
        double[][] found = triangle.toArray();
        assertEquals(EXPECTED.length, found.length);
        for(int a=0; a<EXPECTED.length; a++)
            assertArrayEquals(EXPECTED[a], found[a], TestConfig.EPSILON);
    }

}
