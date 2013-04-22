package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.scale.MinMaxScaleEstimator;
import org.jreserve.jrlib.scale.ScaleResidualTriangle;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.LinkRatioCurve;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleSelection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScaleResidualTriangleTest {
    
    private final static double[][] ERRORS = {
        {-0.53544715,  0.07687310, -0.88318295, -0.32853221,  0.75165510,  0.70651452, 0.00000000},
        {-0.53148094, -0.62744360, -0.37511590,  0.55345506,  0.46949846, -0.70769855},
        {-0.31361544, -0.61993017,  0.99245419,  1.03628787, -1.10208249},
        {-0.86407166,  1.51326125, -0.97735913, -1.22957779},
        { 2.13203521,  0.80608679,  1.06727679},
        {-0.15558769, -1.12975932},
        { 0.12676049}
    };
    
    private static LinkRatioScale PAID_SCALES;

    private ScaleResidualTriangle errors;

    @BeforeClass
    public static void setUpClass() throws Exception {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        LinkRatioSmoothingSelection smoothing = new DefaultLinkRatioSmoothing(lr);
        smoothing.setDevelopmentCount(9);
        LinkRatioCurve tail = createTail(1.05, 1.02);
        smoothing.setMethod(tail, 7);
        smoothing.setMethod(tail, 8);
        
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(smoothing);
        scales.setMethod(new MinMaxScaleEstimator(), 6, 7, 8);
        PAID_SCALES = scales;
    }
    
    private static LinkRatioCurve createTail(double lr7, double lr8) {
        UserInputLRCurve tail = new UserInputLRCurve();
        tail.setValue(8, lr8);
        tail.setValue(8, lr8);
        return tail;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PAID_SCALES = null;
    }

    @Before
    public void setUp() {
        errors = new ScaleResidualTriangle(PAID_SCALES);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(ERRORS.length, errors.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(ERRORS[0].length, errors.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = ERRORS.length;
        for(int a=0; a<accidents; a++)
            assertEquals(ERRORS[a].length, errors.getDevelopmentCount(a));
    }

    @Test
    public void testRecalculateLayer() {
        int accidents = ERRORS.length;
        
        assertEquals(Double.NaN, errors.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = ERRORS[a].length;
            assertEquals(Double.NaN, errors.getValue(a, -1), TestConfig.EPSILON);
            
            for(int d=0; d<devs; d++) {
                String msg = "At ["+a+"; "+d+"]";
                assertEquals(msg, ERRORS[a][d], errors.getValue(a, d), TestConfig.EPSILON);
            }
            assertEquals(Double.NaN, errors.getValue(a, devs), TestConfig.EPSILON);
        }
    }
}
