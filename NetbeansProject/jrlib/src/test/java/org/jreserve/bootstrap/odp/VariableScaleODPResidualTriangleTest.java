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
public class VariableScaleODPResidualTriangleTest {

    private final static double[] PAID = {
         5364.32835962, 15442.67058489, 15449.67691824, 28947.93246574, 
        25764.08994669,  3288.71838771, 17391.49489716,     0.00000000
    };
    
    private final static double[] INCURRED = {
         8213.97580272, 40268.31998867, Double.NaN, Double.NaN, 
        39034.39639707, 84826.99071337, 1615.84902103, Double.NaN
    };
    
    private ResidualTriangle paidSource;
    private VariableScaleODPResidualTriangle paid;
    private ResidualTriangle incurredSource;
    private VariableScaleODPResidualTriangle incurred;

    public VariableScaleODPResidualTriangleTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        paidSource = new PearsonResidualClaimTriangle(lrs);
        paid = new VariableScaleODPResidualTriangle(paidSource);
        
        lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.INCURRED));
        incurredSource = new PearsonResidualClaimTriangle(lrs);
        incurred = new VariableScaleODPResidualTriangle(incurredSource);
    }

    @Test
    public void testGetScale_Paid() {
        int devs = PAID.length;
        assertEquals(Double.NaN, paid.getScale(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<devs; d++)
            assertEquals(PAID[d], paid.getScale(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, paid.getScale(devs), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetScale_Incurred() {
        int devs = INCURRED.length;
        assertEquals(Double.NaN, incurred.getScale(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<devs; d++)
            assertEquals(INCURRED[d], incurred.getScale(d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, incurred.getScale(devs), JRLibTestSuite.EPSILON);
    }
}