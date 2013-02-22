package org.jreserve.estimate;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.FixedLinkRatio;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleFactory;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class EstimateUtilTest {
    
    private final static double[] LRS = {
        11.10425885, 4.09227348, 1.70791313, 1.27591994, 1.13891244, 
         1.06869675, 1.02633486, 1.02268294, 1.05000000
    };
    
    private final static double[][] COMPLETED_MACK = {
        {58046.00000000, 127970.00000000,  476599.00000000, 1027692.00000000, 1360489.00000000, 1647310.00000000, 1819179.00000000, 1906852.00000000, 1950105.00000000, 2047610.25000000},
        {24492.00000000, 141767.00000000,  984288.00000000, 2142656.00000000, 2961978.00000000, 3683940.00000000, 4048898.00000000, 4115760.00000000, 4209117.53713440, 4419573.41399112},
        {32848.00000000, 274682.00000000, 1522637.00000000, 3203427.00000000, 4445927.00000000, 5158781.00000000, 5342585.00000000, 5483281.22801310, 5607658.16711125, 5888041.07546681},
        {21439.00000000, 529828.00000000, 2900301.00000000, 4999019.00000000, 6460112.00000000, 6853904.00000000, 7324744.92961200, 7517641.06186904, 7688163.26301695, 8072571.42616780},
        {40397.00000000, 763394.00000000, 2920745.00000000, 4989572.00000000, 5648563.00000000, 6433218.66882372, 6875159.88341124, 7056216.25641849, 7216271.98638985, 7577085.58570934},
        {90748.00000000, 951994.00000000, 4210640.00000000, 5866482.00000000, 7485161.36145108, 8524943.38996397, 9110579.29478848, 9350505.12503563, 9562602.07175651, 10040732.17534430},
        {62096.00000000, 868480.00000000, 1954797.00000000, 3338623.46278461, 4259816.24831873, 4851557.71732433, 5184843.96494193, 5321386.10488052, 5442090.78661436, 5714195.32594508},
        {24983.00000000, 284441.00000000, 1164010.36092468, 1988028.57887930, 2536565.30508196, 2888925.78083024, 3087385.59296449, 3168691.46032123, 3240566.69859421, 3402595.03352392},
        {13121.00000000, 145698.98037085,  596240.07343467, 1018326.25005124, 1299302.76786580, 1479792.08564879, 1581448.99260858, 1623096.23042607, 1659912.82483505, 1742908.46607681}
    };
    
    public EstimateUtilTest() {
    }

    @Test
    public void testCompleteTriangle() {
        Triangle cik = TriangleFactory.create(TestData.MACK_DATA).cummulate().build();
        LinkRatio lrs = new FixedLinkRatio(LRS, new DevelopmentFactors(cik));
        
        assertEquals(cik.getDevelopmentCount(), lrs.getDevelopmentCount());
        double[][] found = EstimateUtil.completeTriangle(cik, lrs);
        int accidents = COMPLETED_MACK.length;
        assertEquals(accidents, found.length);
        for(int a=0; a<accidents; a++) {
            int devs = COMPLETED_MACK[a].length;
            assertEquals("For accident period: "+a, devs, found[a].length);
            assertArrayEquals("For accident period: "+a, COMPLETED_MACK[a], found[a], JRLibTestSuite.EPSILON);
        }
    }
}