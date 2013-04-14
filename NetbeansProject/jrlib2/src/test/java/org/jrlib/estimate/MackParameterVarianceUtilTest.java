package org.jrlib.estimate;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.SimpleLinkRatio;
import org.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jrlib.linkratio.curve.LinkRatioSmoothingSelection;
import org.jrlib.linkratio.curve.UserInputLRCurve;
import org.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jrlib.linkratio.scale.LinkRatioScaleSelection;
import org.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jrlib.linkratio.standarderror.DefaultLinkRatioSESelection;
import org.jrlib.linkratio.standarderror.LinkRatioSE;
import org.jrlib.linkratio.standarderror.LinkRatioSECalculator;
import org.jrlib.linkratio.standarderror.LinkRatioSESelection;
import org.jrlib.linkratio.standarderror.UserInputLinkRatioSEFunction;
import org.jrlib.scale.MinMaxScaleEstimator;
import org.jrlib.scale.ScaleExtrapolation;
import org.jrlib.scale.UserInputScaleEstimator;
import org.jrlib.triangle.claim.ClaimTriangle;
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
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lr = createLinkRatio_Mack(cik);
        LinkRatioScale scales = createLrScales_Mack(lr);
        LinkRatioSE lrSE = createLrSE(scales);
        MackParameterVarianceUtil util = new MackParameterVarianceUtil(lrSE, EstimateUtil.completeTriangle(cik, lr));
        
        assertEquals(PARAM_SD_MACK, util.getParameterSD(), TestConfig.EPSILON);
        assertArrayEquals(PARAM_SDS_MACK, util.getParameterSDs(), TestConfig.EPSILON);
    }
    
    private LinkRatio createLinkRatio_Mack(ClaimTriangle cik) {
        LinkRatio lr = new SimpleLinkRatio(cik);
        LinkRatioSmoothingSelection lrSmoothing = new DefaultLinkRatioSmoothing(lr);
        lrSmoothing.setDevelopmentCount(9);
        lrSmoothing.setMethod(new UserInputLRCurve(8, 1.05), 8);
        return lrSmoothing;
    }
    
    private LinkRatioScale createLrScales_Mack(LinkRatio lrs) {
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(lrs);
        scales.setMethod(new MinMaxScaleEstimator<LinkRatioScaleInput>(), 7);
        scales.setMethod(new UserInputScaleEstimator<LinkRatioScaleInput>(8, 71d), 8);
        return scales;
    }
    
    private LinkRatioSE createLrSE(LinkRatioScale scales) {
        LinkRatioSESelection lrSE = new DefaultLinkRatioSESelection(scales);
        lrSE.setMethod(new UserInputLinkRatioSEFunction(8, 0.02), 8);
        return lrSE;
    }


    @Test
    public void testCalculation_Q_Paid() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.Q_PAID);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs, new ScaleExtrapolation<LinkRatioScaleInput>());
        LinkRatioSE lrSE = new LinkRatioSECalculator(scales);
        MackParameterVarianceUtil util = new MackParameterVarianceUtil(lrSE, EstimateUtil.completeTriangle(cik, lrs));
        
        assertEquals(PARAM_SD_Q_PAID, util.getParameterSD(), TestConfig.EPSILON);
        assertArrayEquals(PARAM_SDS_Q_PAID, util.getParameterSDs(), TestConfig.EPSILON);
    }
}