package org.jreserve.estimate.mcl;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.linkratio.DefaultLinkRatioSelection;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.LinkRatioSelection;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.factor.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.factor.linkratio.curve.LinkRatioSmoothing;
import org.jreserve.factor.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.factor.linkratio.curve.UserInputLRFunction;
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
public class DefaultMclRhoSelectionTest {

    private final static double[] RATIO_IP = {
        1.23814637, 1.17121069, 1.13371303, 1.10002119, 
        1.09383745, 1.08878712, 1.08710960, 1.02798997, 
        1.00840921
    };
    private final static double[] RHO_IP = {
        216.92324278, 147.27868035, 148.51503147, 90.84835978, 
         93.12294180,  98.75714113, 149.90267924, 98.75714113, 
         65.06203207
    };
    
    private final static double[] RATIO_PI = {
        0.80765895, 0.85381734, 0.88205743, 0.90907340, 
        0.91421262, 0.91845318, 0.91987046, 0.97277214, 
        0.99166092
    };
    private final static double[] RHO_PI = {
        158.78064398, 118.64087385, 125.68259517, 80.44088323, 
         82.09314392,  86.79189307, 132.33680485, 86.79189307, 
         56.92167581
    };

    private static LinkRatio lrPaid;
    private static LinkRatio lrIncurred;
    
    public DefaultMclRhoSelectionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        lrPaid = createLr(TestData.PAID, 1.05, 1.02);
        lrIncurred = createLr(TestData.INCURRED, 1.03, 1.01);
    }
    
    private static LinkRatio createLr(String triangleName, double lr7, double lr8) {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(triangleName));
        LinkRatioSmoothingSelection smoothing = new DefaultLinkRatioSmoothing(lr);
        smoothing.setDevelopmentCount(9);
        smoothing.setMethod(new UserInputLRFunction(7, lr7), 7);
        smoothing.setMethod(new UserInputLRFunction(8, lr8), 8);
        return smoothing;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        lrPaid = null;
        lrIncurred = null;
    }

    @Test
    public void testIperP() {
        DefaultMclRhoSelection rhos = new DefaultMclRhoSelection(lrIncurred, lrPaid);
        MclRhoMinMaxEstimator est = new MclRhoMinMaxEstimator();
        rhos.setMethod(est, 7);
        rhos.setMethod(est, 8);
        
        int devs = RATIO_IP.length;
        assertEquals(devs, rhos.getDevelopmentCount());
        
        assertEquals(Double.NaN, rhos.getRatio(-1), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, rhos.getRho(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<devs; d++) {
            assertEquals(RATIO_IP[d], rhos.getRatio(d), JRLibTestSuite.EPSILON);
            assertEquals(RHO_IP[d], rhos.getRho(d), JRLibTestSuite.EPSILON);
        }
        assertEquals(Double.NaN, rhos.getRatio(devs), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, rhos.getRho(devs), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testPperI() {
        DefaultMclRhoSelection rhos = new DefaultMclRhoSelection(lrPaid, lrIncurred);
        MclRhoMinMaxEstimator est = new MclRhoMinMaxEstimator();
        rhos.setMethod(est, 7);
        rhos.setMethod(est, 8);
        
        int devs = RATIO_PI.length;
        assertEquals(devs, rhos.getDevelopmentCount());
        
        assertEquals(Double.NaN, rhos.getRatio(-1), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, rhos.getRho(-1), JRLibTestSuite.EPSILON);
        for(int d=0; d<devs; d++) {
            assertEquals(RATIO_PI[d], rhos.getRatio(d), JRLibTestSuite.EPSILON);
            assertEquals(RHO_PI[d], rhos.getRho(d), JRLibTestSuite.EPSILON);
        }
        assertEquals(Double.NaN, rhos.getRatio(devs), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, rhos.getRho(devs), JRLibTestSuite.EPSILON);
    }
}