package org.jreserve.bootstrap.odp;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VariableScaleODPResidualTriangleTest {

    private final static double[] PAID = {
         9195.99147363, 26473.14957409, 26485.16043127, 49625.02708412, 
        44167.01133718,  5637.80295036, 29813.99125227,     0.00000000
    };
    
    private final static double[] INCURRED = {
         21903.93547393, 107382.18663646, Double.NaN, Double.NaN, 
        104091.72372552, 226205.30856898, 4308.93072274, Double.NaN
    };
    
    private final static double[] BS_EXAMPLE = {
        19574.66449804,  20241.69275166, 23423.14673359, 101196.03968712, 
        79855.89729970, 149429.19124078, 88046.14495366,   7034.73810803, 
         9912.70878300,      0.00000000
    };

    public VariableScaleODPResidualTriangleTest() {
    }

    @Test
    public void testGetScale_Paid() {
        VariableScaleODPResidualTriangle paid = getResidualTriangle(TestData.PAID);
        testScale(PAID, paid);
    }
    
    private VariableScaleODPResidualTriangle getResidualTriangle(String path) {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(path));
        PearsonResidualClaimTriangle source = new PearsonResidualClaimTriangle(lrs);
        return new VariableScaleODPResidualTriangle(source);
    }
    
    private void testScale(double[] expected, VariableScaleODPResidualTriangle found) {
        int devs = expected.length;
        assertEquals(Double.NaN, found.getScale(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<devs; d++)
            assertEquals(expected[d], found.getScale(d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, found.getScale(devs), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetScale_Incurred() {
        VariableScaleODPResidualTriangle incurred = getResidualTriangle(TestData.INCURRED);
        testScale(INCURRED, incurred);
    }
    
    @Test
    public void testBSExample() {
        VariableScaleODPResidualTriangle found = getResidualTriangle(TestData.TAYLOR_ASHE);
        testScale(BS_EXAMPLE, found);
    }
}