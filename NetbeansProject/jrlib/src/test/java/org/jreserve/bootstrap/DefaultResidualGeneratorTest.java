package org.jreserve.bootstrap;

import java.util.Collections;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.bootstrap.odp.PearsonResidualClaimTriangle;
import org.jreserve.bootstrap.odp.PearsonResidualClaimTriangleTest;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DefaultResidualGeneratorTest {

    PearsonResidualClaimTriangle residuals;
    private DefaultResidualGenerator rG;
    private FixedRnd rnd;
    
    public DefaultResidualGeneratorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        residuals = new PearsonResidualClaimTriangle(lrs);
        
        rnd = new FixedRnd();
        rG = new DefaultResidualGenerator(rnd);
        rG.initialize(residuals, Collections.EMPTY_LIST);
    }

    @Test
    public void testGetValue() {
        double[] expected = new double[rnd.max];
        System.arraycopy(PearsonResidualClaimTriangleTest.PAID_EXPECTED[0], 0, expected, 0, rnd.max);
        
        int index = 0;
        int accidents = residuals.getAccidentCount();
        int developments = residuals.getDevelopmentCount();
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                if(index >= rnd.max)
                    index = 0;
                double r = expected[index++];
                assertEquals(r, rG.getValue(a, d), JRLibTestUtl.EPSILON);
            }
        }
    }
    
    private static class FixedRnd implements Random {

        private int max = 4;
        private int n = 0;
        
        public long nextLong() {
            if(n >= max)
                n=0;
            return n++;
        }

        public int nextInt(int n) {
            if(this.n >= max)
                this.n = 0;
            int result = this.n >= n? n-1 : this.n;
            this.n++;
            return result;
        }

        public double nextDouble() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public double nextNonZeroDouble() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
    }

}