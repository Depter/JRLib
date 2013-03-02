package org.jreserve.factor;

import org.jreserve.TestData;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractFactorTriangleModificationTest {

    private Triangle cik;
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
    public void testGetInputTriangle() {
        assertEquals(cik, factors.getInputTriangle());
    }

    private class AbstractFactorTriangleModificationImpl extends AbstractFactorTriangleModification {

        public AbstractFactorTriangleModificationImpl(FactorTriangle source) {
            super(source);
        }

        @Override
        protected void recalculateLayer() {
        }
    }

}