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
package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.bootstrap.FixedRandom;
import org.jreserve.jrlib.bootstrap.mack.DummyMackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle;
import org.jreserve.jrlib.claimratio.LrCrExtrapolation;
import org.jreserve.jrlib.claimratio.SimpleClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.AdjustedClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.CenteredClaimRatioResiduals;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.curve.SimpleLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.AdjustedLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.CenteredLinkRatioResiduals;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangleCorrection;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapEstimateBundleTest {

    private final static double[][] EXPECTED_PAID = {
        {4426765.00000000, 5419095.00000000, 5508047.00000000, 5521287.00000000, 5559909.00000000, 5586629.00000000, 5623447.00000000, 5634197.00000000},
        {4388958.00000000, 5373127.00000000, 5433289.00000000, 5468293.00000000, 5544061.00000000, 5567951.00000000, 5568523.00000000, 5585311.51739836},
        {5280130.00000000, 6519526.00000000, 6595648.00000000, 6705837.00000000, 6818732.00000000, 6830483.00000000, 6863895.20958039, 6878754.19681394},
        {5445384.00000000, 6609618.00000000, 6781201.00000000, 6797628.00000000, 6804079.00000000, 6834757.25347607, 6870014.90584700, 6885740.88810477},
        {5612138.00000000, 7450088.00000000, 7605951.00000000, 7733097.00000000, 7835764.93859529, 7871618.79433199, 7913180.45729613, 7931741.12344010},
        {6593299.00000000, 8185717.00000000, 8259906.00000000, 8351791.36390145, 8494157.35603446, 8538771.06444995, 8594332.11138290, 8619390.90288747},
        {6603091.00000000, 8262839.00000000, 8451811.48030175, 8541496.35960618, 8677912.02826574, 8721820.53309945, 8775529.85275188, 8799695.43007617},
        {7194587.00000000, 9427521.00993918, 9595990.44803043, 9678348.01025851, 9791656.32489531, 9833687.38415216, 9880555.17260368, 9901366.51882117}
    };

    private final static double[][] EXPECTED_INCURRED = {
        {4873559.00000000,  5726628.00000000,  5643478.00000000,  5703210.00000000,  5787091.00000000,  5858937.00000000,  5862561.00000000,  5791898.00000000},
        {5130849.00000000,  6098121.00000000,  6177774.00000000,  6165459.00000000,  6292000.00000000,  6304102.00000000,  6304337.00000000,  6228554.64219847},
        {5945611.00000000,  7525656.00000000,  7557815.00000000,  7377335.00000000,  7442120.00000000,  7418866.00000000,  7419446.90132321,  7330260.40428146},
        {6632221.00000000,  7861102.00000000,  7532937.00000000,  7528468.00000000,  7525868.00000000,  7476905.60526483,  7477483.99523313,  7387599.78510915},
        {7020974.00000000,  8688586.00000000,  8712864.00000000,  8674967.00000000,  8698563.20173755,  8639145.46905649,  8639809.94641331,  8535953.63624684},
        {8275453.00000000,  9867326.00000000,  9932304.00000000,  9788522.18357243,  9775620.04657050,  9677968.89417874,  9678673.09940021,  9562328.48326121},
        {9000368.00000000, 10239888.00000000, 10025960.18247660,  9888489.90634607,  9886492.07516881,  9796388.95758818,  9797113.03112132,  9679344.76904842},
        {9511539.00000000, 10867401.87421090, 10767544.72251430, 10654808.80779250, 10702854.69232320, 10644638.07481610, 10645475.89007310, 10517510.06862970}
    };
    
    private final static double[] PAID = {
        403783.90321660, 419400.56126229,  538785.112981640, 572116.39713354, 
        759173.95179094, 972443.78679230, 1159094.28380353, 3343637.65101975
    };
    private final static double[] INCURRED = {
        233415.49511676, 175187.73725859,  207033.74944159, 180047.92227524, 
        241281.11337455, 72044.023432950, -117507.87416500, 1486479.53310586
    };
    private final static double[] PAID_INCURRED = {
        391116.495116760, 911001.737258590,  795416.74944159,  901836.92227524, 
        1183151.11337455, 1744442.02343295, 1859541.12583500, 3803431.53310586
    };
    
    private MclBootstrapEstimateBundle bsBundle;
    private MclPseudoData pseudoData;
    
    @Before
    public void setUp() {
        ClaimTriangle paidCik = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurredCik = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LinkRatio paidLr = createLinkRatio(paidCik, 1.05, 1.02);
        LinkRatio incurredLr = createLinkRatio(incurredCik, 1.03, 1.01);
        
        LRResidualTriangle paidLrResiduals = createLrResiduals(paidLr);
        CRResidualTriangle paidCrResiduals = createCrResiduals(incurredCik, paidCik, incurredLr, paidLr);
        
        LRResidualTriangle incurredLrResiduals = createLrResiduals(incurredLr);
        CRResidualTriangle incurredCrResiduals = createCrResiduals(paidCik, incurredCik, paidLr, incurredLr);
        
        createBs(paidLrResiduals, incurredLrResiduals, paidCrResiduals, incurredCrResiduals);
    }
    
    private LinkRatio createLinkRatio(ClaimTriangle cik, double t1, double t2) {
        UserInputLRCurve method = new UserInputLRCurve();
        method.setValue(7, t1);
        method.setValue(8, t2);
        SimpleLinkRatioSmoothing lrs = new SimpleLinkRatioSmoothing(cik, method);
        lrs.setDevelopmentCount(9);
        return lrs;
    }
    
    private LRResidualTriangle createLrResiduals(LinkRatio lrs) {
        LinkRatioScale scale = new SimpleLinkRatioScale(lrs);
        LRResidualTriangle res = new LinkRatioResiduals(scale);
        res = new LRResidualTriangleCorrection(res, 0, 6, Double.NaN);
        res = new AdjustedLinkRatioResiduals(res);
        return new CenteredLinkRatioResiduals(res);
    }
    
    private CRResidualTriangle createCrResiduals(ClaimTriangle numerator, ClaimTriangle denumerator, LinkRatio lrN, LinkRatio lrD) {
        SimpleClaimRatio crs = new SimpleClaimRatio(numerator, denumerator, new LrCrExtrapolation(lrN, lrD));
        crs.setDevelopmentCount(9);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        CRResidualTriangle res = new ClaimRatioResiduals(scales);
        res = new ClaimRatioResidualTriangleCorrection(res, 0, 7, Double.NaN);
        res = new AdjustedClaimRatioResiduals(res);
        return new CenteredClaimRatioResiduals(res);
    }
    
    private void createBs(LRResidualTriangle paidLr, LRResidualTriangle incurredLr, CRResidualTriangle paidCr, CRResidualTriangle incurredCr) {
        MclResidualBundle resBundle = new MclResidualBundle(paidLr, paidCr, incurredLr, incurredCr);
        pseudoData = new MclPseudoData(new FixedRandom(), resBundle);
        MclProcessSimulator ps = new DummyMclProcessSimulator();
        MclProcessSimulator is = new DummyMclProcessSimulator();
        
        bsBundle = new MclBootstrapEstimateBundle(pseudoData, ps, is);
    }
    
    @Test
    public void testRecalculate() {
        pseudoData.recalculate();
        int accidents = PAID.length;
        
        double[] paid = bsBundle.getPaidReserves();
        double[] incurred = bsBundle.getIncurredReserves();
        double[] pi = bsBundle.getPaidIncurredReserves();
        
        for(int a=0; a<accidents; a++) {
            assertEquals(PAID[a], paid[a], TestConfig.EPSILON);
            assertEquals(INCURRED[a], incurred[a], TestConfig.EPSILON);
            assertEquals(PAID_INCURRED[a], pi[a], TestConfig.EPSILON);
        }
    }
}
