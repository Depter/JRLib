package org.jreserve.estimate.mcl;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.factor.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.factor.linkratio.curve.LinkRatioFunction;
import org.jreserve.factor.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.factor.linkratio.curve.UserInputLRFunction;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class MclRhoErrorTriangleTest {

    private final static double[][] ERRORS_IP = {
        {-1.33089143, -1.80917390, -1.72446110, -1.73477463, -1.34141175, -0.95840097, -0.70536960, 0.00000000},
        {-0.66745059, -0.57102319,  0.05194833,  0.70711037,  1.03845917,  1.03757548,  0.70883970},
        {-1.18758856, -0.29273228,  0.21038152,  0.00328244, -0.06770761, -0.07003361},
        {-0.21723010,  0.31651779, -0.40077902,  0.21503292,  0.34297629},
        { 0.14074161, -0.09213231,  0.21948460,  0.66655980},
        { 0.20104705,  0.66479279,  1.33059294},
        { 1.47963872,  1.32834815},
        { 1.03736365}
    };
    
    private static LinkRatio PAID_LR;
    private static LinkRatio INCURRED_LR;
    
    private MclRhoErrorTriangle errors;
    
    public MclRhoErrorTriangleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PAID_LR = createLr(TestData.PAID, 1.05, 1.02);
        INCURRED_LR = createLr(TestData.INCURRED, 1.03, 1.01);
    }
    
    private static LinkRatio createLr(String triangleName, double lr7, double lr8) {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(triangleName));
        LinkRatioSmoothingSelection smoothing = new DefaultLinkRatioSmoothing(lr);
        smoothing.setDevelopmentCount(9);
        LinkRatioFunction tail = createTail(lr7, lr8);
        smoothing.setMethod(tail, 7);
        smoothing.setMethod(tail, 8);
        return smoothing;
    }
    
    private static LinkRatioFunction createTail(double lr7, double lr8) {
        UserInputLRFunction tail = new UserInputLRFunction();
        tail.setValue(8, lr8);
        tail.setValue(8, lr8);
        return tail;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PAID_LR = null;
        INCURRED_LR = null;
    }

    @Before
    public void setUp() {
        MclRhoSelection rhos = new DefaultMclRhoSelection(INCURRED_LR, PAID_LR);
        rhos.setMethod(new MclRhoMinMaxEstimator(), 7);
        errors = new MclRhoErrorTriangle(rhos);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(ERRORS_IP.length, errors.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(ERRORS_IP[0].length, errors.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = ERRORS_IP.length;
        for(int a=0; a<accidents; a++)
            assertEquals(ERRORS_IP[a].length, errors.getDevelopmentCount(a));
    }

    @Test
    public void testRecalculateLayer() {
        int accidents = ERRORS_IP.length;
        
        assertEquals(Double.NaN, errors.getValue(-1, 0), JRLibTestSuite.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = ERRORS_IP[a].length;
            assertEquals(Double.NaN, errors.getValue(a, -1), JRLibTestSuite.EPSILON);
            
            for(int d=0; d<devs; d++)
                assertEquals("At ["+a+"; "+d+"]", ERRORS_IP[a][d], errors.getValue(a, d), JRLibTestSuite.EPSILON);
            assertEquals(Double.NaN, errors.getValue(a, devs), JRLibTestSuite.EPSILON);
        }
    }
}