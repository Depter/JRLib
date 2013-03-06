package org.jreserve.estimate;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.DefaultLinkRatioSelection;
import org.jreserve.factor.linkratio.LinkRatioSelection;
import org.jreserve.factor.linkratio.UserInputLRMethod;
import org.jreserve.factor.standarderror.*;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackEstimateTest {

    private final static double[][] EXPECTED_CIK = {
        {58046.00000000, 127970.00000000,  476599.00000000, 1027692.00000000, 1360489.00000000, 1647310.00000000, 1819179.00000000, 1906852.00000000, 1950105.00000000,  2047610.25000000},
        {24492.00000000, 141767.00000000 , 984288.00000000, 2142656.00000000, 2961978.00000000, 3683940.00000000, 4048898.00000000, 4115760.00000000, 4209117.51661901,  4419573.39244997},
        {32848.00000000, 274682.00000000, 1522637.00000000, 3203427.00000000, 4445927.00000000, 5158781.00000000, 5342585.00000000, 5483281.24051883, 5607658.15256872,  5888041.06019716},
        {21439.00000000, 529828.00000000, 2900301.00000000, 4999019.00000000, 6460112.00000000, 6853904.00000000, 7324744.90537235, 7517641.05413654, 7688163.21763668,  8072571.37851851},
        {40397.00000000, 763394.00000000, 2920745.00000000, 4989572.00000000, 5648563.00000000, 6433218.69626640, 6875159.88998729, 7056216.27926084, 7216271.97457798,  7577085.57330688},
        {90748.00000000, 951994.00000000, 4210640.00000000, 5866482.00000000, 7485161.34532487, 8524943.40796314, 9110579.28387465, 9350505.13516011, 9562602.03550218, 10040732.13727730},
        {62096.00000000, 868480.00000000, 1954797.00000000, 3338623.45546910, 4259816.22980728, 4851557.71693708, 5184843.94736996, 5321386.09898229, 5442090.75405740,  5714195.29176027},
        {24983.00000000, 284441.00000000, 1164010.36082436, 1988028.57435184, 2536565.29384045, 2888925.78035066, 3087385.58223493, 3168691.45653595, 3240566.67892843,  3402595.01287485},
        {13121.00000000, 145698.98035482,  596240.07331770, 1018326.24762013, 1299302.76196466, 1479792.08524037, 1581448.98693864, 1623096.22830861, 1659912.81457909,  1742908.45530805}
    };
    
    private final static double[] EXPECTED_RI = {
          97505.25000000,  303813.39244997,  545456.06019716, 1218667.37851851, 
        1928522.57330688, 4174250.13727729, 3759398.29176027, 3118154.01287485, 
        1729787.45530805 
    };
    
    private final static double EXPECTED_R = 16875554.55169300;
    
    private final static double[] EXPECTED_SEI = {
         106544.08997880,  179976.57665397,  249707.56670618,  417857.02786277, 
         670156.02852858, 1127984.06052938, 1377496.23367859, 1901740.29102647, 
        2293436.80863921 
    };
    
    private final static double EXPECTED_SE = 4053667.66802943;
    
    private Triangle cik;
    private MackEstimate estimate;
    
    public MackEstimateTest() {
    }

    @Before
    public void setUp() {
        cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatioSelection lrs = new DefaultLinkRatioSelection(new DevelopmentFactors(cik));
        UserInputLRMethod uiLR = new UserInputLRMethod();
        uiLR.setValue(8, 1.05);
        lrs.setMethod(uiLR, 8);
        
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(new LinkRatioScaleCaclulator(lrs), new LinkRatioScaleMinMaxEstimator());
        UserInputLinkRatioScaleEstimator uiScale = new UserInputLinkRatioScaleEstimator();
        uiScale.setValue(8, 71d);
        scales.setMethod(uiScale, 8);
        
        LinkRatioSESelection lrSe = new DefaultLinkRatioSESelection(scales);
        UserInputLinkRatioSEFunction uiSE = new UserInputLinkRatioSEFunction();
        uiSE.setValue(8, 0.02);
        lrSe.setMethod(uiSE, 8);
        
        estimate = new MackEstimate(lrSe);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(cik.getAccidentCount(), estimate.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(cik.getDevelopmentCount()+1, estimate.getDevelopmentCount());
    }

    @Test
    public void testGetValue_Cell() {
        assertEquals(Double.NaN, estimate.getValue(new Cell(-1,  0)), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(new Cell( 0, -1)), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(new Cell(-1, -1)), JRLibTestSuite.EPSILON);
        
        int accidents = estimate.getAccidentCount();
        int developments = estimate.getDevelopmentCount();
        
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertEquals(EXPECTED_CIK[a][d], estimate.getValue(new Cell(a, d)), JRLibTestSuite.EPSILON);

        assertEquals(Double.NaN, estimate.getValue(new Cell(accidents,  0)), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(new Cell(0, developments)), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(new Cell(accidents, developments)), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue_int_int() {
        assertEquals(Double.NaN, estimate.getValue(-1,  0), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue( 0, -1), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(-1, -1), JRLibTestSuite.EPSILON);
        
        int accidents = estimate.getAccidentCount();
        int developments = estimate.getDevelopmentCount();
        
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertEquals(EXPECTED_CIK[a][d], estimate.getValue(a, d), JRLibTestSuite.EPSILON);

        assertEquals(Double.NaN, estimate.getValue(accidents,  0), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue( 0, developments), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(accidents, developments), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testToArray() {
        double[][] found = estimate.toArray();
        assertEquals(EXPECTED_CIK.length, found.length);
        for(int a=0; a<EXPECTED_CIK.length; a++)
            assertArrayEquals(EXPECTED_CIK[a], found[a], JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetReserve_int() {
        int length = EXPECTED_RI.length;
        assertEquals(Double.NaN, estimate.getReserve(-1), JRLibTestSuite.EPSILON);
        for(int a=0; a<length; a++)
            assertEquals(EXPECTED_RI[a], estimate.getReserve(a), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getReserve(length), JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testToArrayReserve() {
        assertArrayEquals(EXPECTED_RI, estimate.toArrayReserve(), JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetReserve() {
        assertEquals(EXPECTED_R, estimate.getReserve(), JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetStandardError_int() {
        int length = EXPECTED_SEI.length;
        assertEquals(Double.NaN, estimate.getStandardError(-1), JRLibTestSuite.EPSILON);
        for(int a=0; a<length; a++)
            assertEquals(EXPECTED_SEI[a], estimate.getStandardError(a), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estimate.getStandardError(length), JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testToArrayStandardError() {
        assertArrayEquals(EXPECTED_SEI, estimate.toArrayStandardError(), JRLibTestSuite.EPSILON);
    }
    
    @Test
    public void testGetStandardError() {
        assertEquals(EXPECTED_SE, estimate.getStandardError(), JRLibTestSuite.EPSILON);
    }
}