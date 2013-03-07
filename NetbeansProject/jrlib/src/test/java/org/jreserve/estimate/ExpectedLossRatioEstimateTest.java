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
public class ExpectedLossRatioEstimateTest {

    private final static double[] LOSS_RATIOS = {
        16.74578457, 16.74578457, 16.74578457, 16.74578457,
        16.74578457, 16.74578457, 16.74578457, 16.74578457
    };
    
    private final static double[][] EXPECTED = {
        {4873559.00000000, 5726628.00000000 , 5643478.00000000 , 5703210.00000000 , 5787091.00000000 , 5858937.00000000 , 5862561.00000000 , 5791898.00000000 , 5791898.00079504},
        {5130849.00000000, 6098121.00000000 , 6177774.00000000 , 6165459.00000000 , 6292000.00000000 , 6304102.00000000 , 6304337.00000000 , 6304102.11085594 , 6619307.21639874},
        {5945611.00000000, 7525656.00000000 , 7557815.00000000 , 7377335.00000000 , 7442120.00000000 , 7418866.00000000 , 7178653.04183783 , 7092126.83598763 , 7446733.17778701},
        {6632221.00000000, 7861102.00000000 , 7532937.00000000 , 7528468.00000000 , 7525868.00000000 , 7973745.92486510 , 7976275.77661713 , 7880135.61275306 , 8274142.39339071},
        {7020974.00000000, 8688586.00000000 , 8712864.00000000 , 8674967.00000000 , 8766758.24564206 , 8794015.24445023 , 8796805.34524891 , 8690775.12123737 , 9125313.87729924},
        {8275453.00000000, 9867326.00000000 , 9932304.00000000 , 9878284.77170254 , 9978861.52917360 , 10009887.11573440, 10013062.97946220, 9892372.88014929 , 10386991.52415680},
        {9000368.00000000, 10239888.00000000, 10692695.11104060, 10640040.63353810, 10748373.28551300, 10781791.38484050, 10785212.15270570, 10655215.13496090, 11187975.89170900},
        {9511539.00000000, 11553290.06115170, 11500216.39808400, 11443585.41035650, 11560099.43490800, 11596041.29707540, 11599720.40419370, 11459905.90283130, 12032901.19797290}
    };
    
    private final static double[] RESERVES = {
         0.00079504, 314970.21639874, 27867.17778701, 748274.39339071, 
         450346.87729924, 454687.52415675, 948087.89170899, 2521362.19797291 
    };
    
    private ExpectedLossRatioEstimate estiamte;
    private LinkRatio lrs;
    private Vector exposure;
    private Vector lossRatios;
    
    public ExpectedLossRatioEstimateTest() {
    }

    @Before
    public void setUp() {
        createLrs();
        exposure = new InputVector(TestData.getCachedVector(TestData.EXPOSURE));
        lossRatios = new InputVector(LOSS_RATIOS);
        estiamte = new ExpectedLossRatioEstimate(lrs, exposure, lossRatios);
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