package org.jreserve.bootstrap.odp;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.bootstrap.ResidualTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
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
    public void testGetScale_Paid() {
        ConstantScaleODPResidualTriangle found = createScaledResiduals(TestData.PAID);
        int developments = 12;
        for(int d=0; d<developments; d++)
            assertEquals(25531.26426445, found.getScale(d), JRLibTestUtl.EPSILON);
    }
    
    private ConstantScaleODPResidualTriangle createScaledResiduals(String path) {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(path));
        PearsonResidualClaimTriangle source = new PearsonResidualClaimTriangle(lrs);
        return new ConstantScaleODPResidualTriangle(source);
    }
    
    @Test
    public void testGetScale_Incurred() {
        ConstantScaleODPResidualTriangle found = createScaledResiduals(TestData.INCURRED);
        int developments = 12;
        for(int d=0; d<developments; d++)
            assertEquals(84604.47801255, found.getScale(d), JRLibTestUtl.EPSILON);
    }
    
    @Test
    public void testGetScale_BsExample() {
        ConstantScaleODPResidualTriangle found = createScaledResiduals(TestData.TAYLOR_ASHE);
        int developments = 12;
        for(int d=0; d<developments; d++)
            assertEquals(52601.36151147, found.getScale(d), JRLibTestUtl.EPSILON);
    }
}