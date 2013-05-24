/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleSelection;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.standarderror.DefaultLinkRatioSESelection;
import org.jreserve.jrlib.linkratio.standarderror.LinkRatioSE;
import org.jreserve.jrlib.linkratio.standarderror.LinkRatioSECalculator;
import org.jreserve.jrlib.linkratio.standarderror.LinkRatioSESelection;
import org.jreserve.jrlib.linkratio.standarderror.UserInputLinkRatioSEFunction;
import org.jreserve.jrlib.scale.MinMaxScaleEstimator;
import org.jreserve.jrlib.scale.ScaleExtrapolation;
import org.jreserve.jrlib.scale.UserInputScaleEstimator;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
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