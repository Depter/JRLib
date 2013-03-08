package org.jreserve.estimate;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.DefaultLinkRatioSelection;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.factor.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.factor.linkratio.curve.UserInputLRFunction;
import org.jreserve.triangle.Triangle;
import org.jreserve.vector.InputVector;
import org.jreserve.vector.Vector;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class BornhuetterFergusonEstimateTest {

    private final static double[] LOSS_RATIOS = {
        16.74578457, 16.74578457, 16.74578457, 16.74578457,
        16.74578457, 16.74578457, 16.74578457, 16.74578457
    };
    
    private final static double[][] EXPECTED = {
        {4873559.00000000,  5726628.00000000,  5643478.00000000,  5703210.00000000,  5787091.00000000,  5858937.00000000,  5862561.00000000,  5791898.00000000,  6067702.66670453},
        {5130849.00000000,  6098121.00000000,  6177774.00000000,  6165459.00000000,  6292000.00000000,  6304102.00000000,  6304337.00000000,  6227424.94673880,  6542630.05228160},
        {5945611.00000000,  7525656.00000000,  7557815.00000000,  7377335.00000000,  7442120.00000000,  7418866.00000000,  7421142.86811285,  7334616.66226264,  7689223.00406202},
        {6632221.00000000,  7861102.00000000,  7532937.00000000,  7528468.00000000,  7525868.00000000,  7550582.57885041,  7553112.43060243,  7456972.26673837,  7850979.04737602},
        {7020974.00000000,  8688586.00000000,  8712864.00000000,  8674967.00000000,  8763326.99129776,  8790583.99010593,  8793374.09090461,  8687343.86689307,  9121882.62295494},
        {8275453.00000000,  9867326.00000000,  9932304.00000000,  9883419.23149309,  9983995.98896414, 10015021.57552490, 10018197.43925270,  9897507.33993983, 10392125.98394730},
        {9000368.00000000, 10239888.00000000, 10190541.05921510, 10137886.58171260, 10246219.23368750, 10279637.33301500, 10283058.10088020, 10153061.08313540, 10685821.83988350},
        {9511539.00000000, 11394535.69850380, 11341462.03543610, 11284831.04770860, 11401345.07226010, 11437286.93442750, 11440966.04154580, 11301151.54018340, 11874146.83532500}
    };
    
    private final static double[] RESERVES = {
        275804.66670453, 238293.05228160, 270357.00406202, 325111.04737602, 
        446915.62295494, 459821.98394729, 445933.83988348, 2362607.83532500 
    };
    
    private BornhuetterFergusonEstimate estiamte;
    private LinkRatio lrs;
    private Vector exposure;
    private Vector lossRatios;

    public BornhuetterFergusonEstimateTest() {
    }

    @Before
    public void setUp() {
        createLrs();
        exposure = new InputVector(TestData.getCachedVector(TestData.EXPOSURE));
        lossRatios = new InputVector(LOSS_RATIOS);
        estiamte = new BornhuetterFergusonEstimate(lrs, exposure, lossRatios);
    }
    
    private void createLrs() {
        Triangle cik = TestData.getCummulatedTriangle(TestData.INCURRED);
        lrs = new DefaultLinkRatioSelection(new DevelopmentFactors(cik));

        LinkRatioSmoothingSelection smoothing = new DefaultLinkRatioSmoothing(lrs);
        UserInputLRFunction tail = new UserInputLRFunction();
        tail.setValue(7, 1.05);
        smoothing.setDevelopmentCount(8);
        smoothing.setMethod(tail, 7);
        lrs = smoothing;
    }

    @Test
    public void testGetObservedDevelopmentCount() {
        Triangle cik = lrs.getSourceFactors().getSourceTriangle();
        int accidents = estiamte.getDevelopmentCount();
        for(int a=0; a<accidents; a++)
            assertEquals(cik.getDevelopmentCount(a), estiamte.getObservedDevelopmentCount(a));
    }

    @Test
    public void testRecalculateLayer() {
        int accidents = EXPECTED.length;
        int developments = EXPECTED[0].length;
        
        assertEquals(accidents, estiamte.getAccidentCount());
        assertEquals(developments, estiamte.getDevelopmentCount());
        
        assertEquals(Double.NaN, estiamte.getValue(0, -1), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estiamte.getValue(-1, 0), JRLibTestSuite.EPSILON);
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertEquals(EXPECTED[a][d], estiamte.getValue(a, d), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estiamte.getValue(0, developments), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estiamte.getValue(accidents, 0), JRLibTestSuite.EPSILON);
        
        assertEquals(Double.NaN, estiamte.getReserve(-1), JRLibTestSuite.EPSILON);
        for(int a=0; a<accidents; a++)
            assertEquals(RESERVES[a], estiamte.getReserve(a), JRLibTestSuite.EPSILON);
        assertEquals(Double.NaN, estiamte.getReserve(accidents), JRLibTestSuite.EPSILON);
    }
}