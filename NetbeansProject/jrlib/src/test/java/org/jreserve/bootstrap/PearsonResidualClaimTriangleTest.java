package org.jreserve.bootstrap;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.Triangle;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class PearsonResidualClaimTriangleTest {

    private final static double[][] PAID_EXPECTED = {
        {  48.13212384, - 73.63169902,   11.81569403, -169.55239548, - 61.68575408,  52.26915014,  131.55282991, 0.00000000},
        {  50.51938102, - 71.78274795, - 84.20205491, - 72.09921049,  102.72506470,  33.49396185, -132.20001035},
        {   3.12696254, - 54.97065012, - 86.97345911,  186.98501397,  191.40668534, -77.53867181},
        {  76.02543646, -120.40636575,  208.92989612, -185.95627679, -228.17157890},
        {-177.10773055,  281.90938145,  105.17110304,  203.10149934},
        {  28.39771022, - 14.06689075, -152.28729374},
        {-  9.08265497,   18.27736407},
        {   0.00000000}
    };
    
    private final static double[][] INCURRED_EXPECTED = {
        {-  6.26900625, -101.08015168, Double.NaN, Double.NaN,  102.99827976,  398.28934016,  40.92089880, Double.NaN},
        {- 54.46500500, - 55.47364739, Double.NaN, Double.NaN,  251.21586642, - 53.17565587, -39.46109582},
        {- 96.97466978,  341.97710723, Double.NaN, Double.NaN, - 35.73971026, -304.99003800},
        { 134.17440987,    2.71906628, Double.NaN, Double.NaN, -284.85470248},
        {-114.39394814,  201.02965063, Double.NaN, Double.NaN},
        {- 26.44166707, - 26.97530094, Double.NaN},
        { 146.67676449, -332.39644225},
        {   0.00000000}    
    };
    
    private static Triangle PAID;
    private static Triangle INCURRED;
    private static LinkRatio LRS_PAID;
    private static LinkRatio LRS_INCURRED;
    
    private PearsonResidualClaimTriangle paidResiduals;
    private PearsonResidualClaimTriangle incurredResiduals;

    public PearsonResidualClaimTriangleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PAID = TestData.getCummulatedTriangle(TestData.PAID);
        LRS_PAID = new SimpleLinkRatio(PAID);
        INCURRED = TestData.getCummulatedTriangle(TestData.INCURRED);
        LRS_INCURRED = new SimpleLinkRatio(INCURRED);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PAID = null;
        LRS_PAID = null;
        INCURRED = null;
        LRS_INCURRED = null;
    }

    @Before
    public void setUp() {
        paidResiduals = new PearsonResidualClaimTriangle(LRS_PAID);
        incurredResiduals = new PearsonResidualClaimTriangle(LRS_INCURRED);
    }

    @Test
    public void testGetSourceLinkRatios() {
        assertTrue(LRS_PAID == paidResiduals.getSourceLinkRatios());
        assertTrue(LRS_INCURRED == incurredResiduals.getSourceLinkRatios());
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(PAID.getAccidentCount(), paidResiduals.getAccidentCount());
        assertEquals(INCURRED.getAccidentCount(), incurredResiduals.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(PAID.getDevelopmentCount(), paidResiduals.getDevelopmentCount());
        assertEquals(INCURRED.getDevelopmentCount(), incurredResiduals.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = PAID.getAccidentCount();
        assertEquals(0, paidResiduals.getDevelopmentCount(-1));
        for(int a=0; a<accidents; a++)
            assertEquals(PAID.getDevelopmentCount(a), paidResiduals.getDevelopmentCount(a));
        assertEquals(0, paidResiduals.getDevelopmentCount(accidents));

        accidents = INCURRED.getAccidentCount();
        assertEquals(0, incurredResiduals.getDevelopmentCount(-1));
        for(int a=0; a<accidents; a++)
            assertEquals(INCURRED.getDevelopmentCount(a), incurredResiduals.getDevelopmentCount(a));
        assertEquals(0, incurredResiduals.getDevelopmentCount(accidents));
    }

    @Test
    public void testGetValue_Paid() {
        int accidents = PAID_EXPECTED.length;
        
        assertEquals(Double.NaN, paidResiduals.getValue(-1, 0), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, paidResiduals.getValue(0, -1), JRLibTestSuite.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = PAID_EXPECTED[a].length;
            assertEquals(Double.NaN, paidResiduals.getValue(a, -1), JRLibTestSuite.EPSILON);
            for(int d=0; d<devs; d++)
                assertEquals(PAID_EXPECTED[a][d], paidResiduals.getValue(a, d), JRLibTestSuite.EPSILON);
            assertEquals(Double.NaN, paidResiduals.getValue(a, devs), JRLibTestSuite.EPSILON);
        }
        assertEquals(Double.NaN, paidResiduals.getValue(accidents, 0), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testGetValue_Incurred() {
        int accidents = INCURRED_EXPECTED.length;
        
        assertEquals(Double.NaN, incurredResiduals.getValue(-1, 0), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, incurredResiduals.getValue(0, -1), JRLibTestSuite.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = INCURRED_EXPECTED[a].length;
            assertEquals(Double.NaN, incurredResiduals.getValue(a, -1), JRLibTestSuite.EPSILON);
            for(int d=0; d<devs; d++)
                assertEquals(INCURRED_EXPECTED[a][d], incurredResiduals.getValue(a, d), JRLibTestSuite.EPSILON);
            assertEquals(Double.NaN, incurredResiduals.getValue(a, devs), JRLibTestSuite.EPSILON);
        }
        assertEquals(Double.NaN, incurredResiduals.getValue(accidents, 0), JRLibTestSuite.EPSILON);
    }
}