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
package org.jreserve.jrlib.estimate.mcl;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.SimpleClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCorrelationTest {
    
    private final static double PAID_LAMBDA = 0.15210439;
    private final static double INCURRED_LAMBDA = 0.40562407;
    
    @Test
    public void testGetCorrelation_Paid() {
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LRResidualTriangle lrResiduals = createLrResiduals(paid, 1.05, 1.02);
        CRResidualTriangle crResiduals = createCrResiduals(incurred, paid);
        
        MclCorrelation lambda = new MclCorrelation(lrResiduals, crResiduals);
        assertEquals(PAID_LAMBDA, lambda.getValue(), TestConfig.EPSILON);
    }
    
    private LRResidualTriangle createLrResiduals(ClaimTriangle cik, double t1, double t2) {
        LinkRatio lr = createLinkRatio(cik, t1, t2);
        LinkRatioScale scale = new SimpleLinkRatioScale(lr);
        return new LinkRatioResiduals(scale);
    }
    
    private LinkRatio createLinkRatio(ClaimTriangle cik, double t1, double t2) {
        LinkRatioSmoothingSelection lrs = new DefaultLinkRatioSmoothing(cik);
        lrs.setDevelopmentCount(9);
        UserInputLRCurve tail = new UserInputLRCurve();
        tail.setValue(7, t1);
        tail.setValue(8, t2);
        lrs.setMethod(tail, 7, 8);
        return lrs;
    }
    
    private CRResidualTriangle createCrResiduals(ClaimTriangle numerator, ClaimTriangle denumerator) {
        ClaimRatio crs = new SimpleClaimRatio(numerator, denumerator);
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        CRResidualTriangle residuals = new ClaimRatioResiduals(scales);
        return new ClaimRatioResidualTriangleCorrection(residuals, 0, 6, Double.NaN);
    }
    
    @Test
    public void testGetCorrelation_Incurred() {
        ClaimTriangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        ClaimTriangle incurred = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LRResidualTriangle lrResiduals = createLrResiduals(incurred, 1.03, 1.01);
        CRResidualTriangle crResiduals = createCrResiduals(paid, incurred);
        
        MclCorrelation lambda = new MclCorrelation(lrResiduals, crResiduals);
        assertEquals(INCURRED_LAMBDA, lambda.getValue(), TestConfig.EPSILON);
    }
}
