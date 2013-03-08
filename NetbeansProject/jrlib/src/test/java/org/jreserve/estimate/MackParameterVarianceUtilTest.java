package org.jreserve.estimate;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.factor.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.factor.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.factor.linkratio.curve.UserInputLRFunction;
import org.jreserve.factor.linkratio.scale.*;
import org.jreserve.factor.linkratio.standarderror.*;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackParameterVarianceUtilTest {

    private final static double[] PARAM_SDS_MACK = {
         39002.10000000,  99395.58419043, 156847.39984536, 276611.05647924, 
        381801.47946007, 646627.43100739, 549576.53952849, 540140.44695236, 
        447860.09924267
    }; 
    private final static double PARAM_SD_MACK = 2264261.03119686;
    
    private final static double[] PARAM_SDS_Q_PAID = {
         0.00000000,  0.76453035,  1.77823844,  2.59255854, 
         7.04762495,  7.43907338, 11.99085490, 19.82561470, 
        18.58177828, 28.99190931, 62.63734060, 173.54099069
    };
    private final static double PARAM_SD_Q_PAID = 227.41653472;

    public MackParameterVarianceUtilTest() {
    }

    @Test
    public void testCalculation_Mack3() {
        Triangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lr = createLinkRatio_Mack(cik);
        LinkRatioScale scales = createLrScales_Mack(lr);
        LinkRatioSE lrSE = createLrSE(scales);
        MackParameterVarianceUtil util = new MackParameterVarianceUtil(lrSE, EstimateUtil.completeTriangle(cik, lr));
        
        assertEquals(PARAM_SD_MACK, util.getParameterSD(), JRLibTestSuite.EPSILON);
        assertArrayEquals(PARAM_SDS_MACK, util.getParameterSDs(), JRLibTestSuite.EPSILON);
    }
    
    private LinkRatio createLinkRatio_Mack(Triangle cik) {
        LinkRatio lr = new SimpleLinkRatio(cik);
        LinkRatioSmoothingSelection lrSmoothing = new DefaultLinkRatioSmoothing(lr);
        lrSmoothing.setDevelopmentCount(9);
        UserInputLRFunction tail = new UserInputLRFunction();
        tail.setValue(8, 1.05);
        lrSmoothing.setMethod(tail, 8);
        return lrSmoothing;
    }
    
    private LinkRatioScale createLrScales_Mack(LinkRatio lrs) {
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(lrs);
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), 7);
        UserInputLinkRatioScaleEstimator tail = new UserInputLinkRatioScaleEstimator();
        tail.setValue(8, 71);
        scales.setMethod(tail, 8);
        return scales;
    }
    
    private LinkRatioSE createLrSE(LinkRatioScale scales) {
        LinkRatioSESelection lrSE = new DefaultLinkRatioSESelection(scales);
        UserInputLinkRatioSEFunction tail = new UserInputLinkRatioSEFunction();
        tail.setValue(8, 0.02);
        lrSE.setMethod(tail, 8);
        return lrSE;
    }


    @Test
    public void testCalculation_Q_Paid() {
        Triangle cik = TestData.getCummulatedTriangle(TestData.Q_PAID);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs, new LinkRatioScaleExtrapolation());
        LinkRatioSE lrSE = new LinkRatioSECalculator(scales);
        MackParameterVarianceUtil util = new MackParameterVarianceUtil(lrSE, EstimateUtil.completeTriangle(cik, lrs));
        
        assertEquals(PARAM_SD_Q_PAID, util.getParameterSD(), JRLibTestSuite.EPSILON);
        assertArrayEquals(PARAM_SDS_Q_PAID, util.getParameterSDs(), JRLibTestSuite.EPSILON);
    }
}