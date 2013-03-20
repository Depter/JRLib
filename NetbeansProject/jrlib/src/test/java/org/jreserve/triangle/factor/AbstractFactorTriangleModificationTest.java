package org.jreserve.triangle.factor;

import org.jreserve.triangle.factor.AbstractFactorTriangleModification;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.TestData;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractFactorTriangleModificationTest {

    private ClaimTriangle cik;
    private AbstractFactorTriangleModificationImpl factors;
    
    public AbstractFactorTriangleModificationTest() {
    }

    @Before
    public void setUp() {
        cik = TestData.getCummulatedTriangle(TestData.INCURRED);
        FactorTriangle source =  new DevelopmentFactors(cik);
        factors = new AbstractFactorTriangleModificationImpl(source);
    }

    @Test
    public void testGetSourceTriangle() {
        assertEquals(cik, factors.getSourceTriangle());
    }

    private class AbstractFactorTriangleModificationImpl extends AbstractFactorTriangleModification {

        public AbstractFactorTriangleModificationImpl(FactorTriangle source) {
            super(source);
        }

        @Override
        protected void recalculateLayer() {
        }

        public double getValue(int accident, int development) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public FactorTriangle copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}