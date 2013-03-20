package org.jreserve.estimate;

import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.linkratio.scale.UserInputLinkRatioScaleEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleExtrapolation;
import org.jreserve.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleSelection;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.linkratio.smoothing.DefaultLinkRatioSmoothing;
import org.jreserve.linkratio.smoothing.LinkRatioSmoothingSelection;
import org.jreserve.linkratio.smoothing.UserInputLRFunction;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackProcessVarianceUtilTest {

    private final static double[] PROC_SDS_MACK = {
          99148.77359302, 150040.28121650,  194300.70002981,  313194.53885364, 
         550760.14094641, 924240.77289261, 1263115.79081199, 1823421.07920238, 
        2249282.93612154
    }; 
    private final static double PROC_SD_MACK = 3362341.97330231;
    
    private final static double[] PROC_SDS_Q_PAID = {
          0.00000000,   0.70316712,  2.40456312,  3.82015535, 11.78541246, 
         16.85261957,  24.40968371, 37.73113751, 47.12772277, 75.03752895, 
        225.09902607, 690.85127247
    };
    private final static double PROC_SD_Q_PAID = 733.66201571;
    
    public MackProcessVarianceUtilTest() {
    }

    @Test
    public void testCalculation_Mack() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lr = createLinkRatio_Mack(cik);
        LinkRatioScale scales = createLrScales_Mack(lr);
        MackProcessVarianceUtil util = new MackProcessVarianceUtil(scales, EstimateUtil.completeTriangle(cik, lr));
        
        assertEquals(PROC_SD_MACK, util.getProcessSD(), JRLibTestUtl.EPSILON);
        assertArrayEquals(PROC_SDS_MACK, util.getProcessSDs(), JRLibTestUtl.EPSILON);
    }
    
    private LinkRatio createLinkRatio_Mack(ClaimTriangle cik) {
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
    
    @Test
    public void testCalculation_QPaid() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.Q_PAID);
        LinkRatio lr = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lr, new LinkRatioScaleExtrapolation());
        MackProcessVarianceUtil util = new MackProcessVarianceUtil(scales, EstimateUtil.completeTriangle(cik, lr));
        
        assertEquals(PROC_SD_Q_PAID, util.getProcessSD(), JRLibTestUtl.EPSILON);
        assertArrayEquals(PROC_SDS_Q_PAID, util.getProcessSDs(), JRLibTestUtl.EPSILON);
    }
}