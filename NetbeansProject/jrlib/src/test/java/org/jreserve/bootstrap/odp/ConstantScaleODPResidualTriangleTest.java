package org.jreserve.bootstrap.odp;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.bootstrap.PearsonResidualClaimTriangle;
import org.jreserve.bootstrap.ResidualTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ConstantScaleODPResidualTriangleTest {

    private ResidualTriangle paidSource;
    private ConstantScaleODPResidualTriangle paid;
    private ResidualTriangle incurredSource;
    private ConstantScaleODPResidualTriangle incurred;

    public ConstantScaleODPResidualTriangleTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        paidSource = new PearsonResidualClaimTriangle(lrs);
        paid = new ConstantScaleODPResidualTriangle(paidSource);
        
        lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.INCURRED));
        incurredSource = new PearsonResidualClaimTriangle(lrs);
        incurred = new ConstantScaleODPResidualTriangle(incurredSource);
    }

    @Test
    public void testGetScale() {
        int developments = paidSource.getDevelopmentCount();
        for(int d=0; d<developments; d++)
            assertEquals(14893.23748760, paid.getScale(d), JRLibTestSuite.EPSILON);
        
        developments = incurredSource.getDevelopmentCount();
        for(int d=0; d<developments; d++)
            assertEquals(31726.67925471, incurred.getScale(d), JRLibTestSuite.EPSILON);
    }
}