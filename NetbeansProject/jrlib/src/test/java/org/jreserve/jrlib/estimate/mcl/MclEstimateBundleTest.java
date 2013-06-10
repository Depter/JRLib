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

import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.claimratio.ClaimRatioSelection;
import org.jreserve.jrlib.claimratio.DefaultClaimRatioSelection;
import org.jreserve.jrlib.claimratio.LrCrExtrapolation;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.SimpleClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResidualTriangleCorrection;
import org.jreserve.jrlib.claimratio.scale.residuals.ClaimRatioResiduals;
import org.jreserve.jrlib.estimate.Estimate;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.linkratio.scale.residuals.LinkRatioResiduals;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclEstimateBundleTest {

    private final static int DEVELOPMENT_COUNT = 9;
    
    private final static double[][] EXPECTED_PAID = {
        {4426765.00000000, 5419095.00000000, 5508047.00000000, 5521287.00000000, 5559909.00000000, 5586629.00000000, 5623447.00000000, 5634197.00000000, 5915906.85000000,  6034224.98700000},
        {4388958.00000000, 5373127.00000000, 5433289.00000000, 5468293.00000000, 5544061.00000000, 5567951.00000000, 5568523.00000000, 5580226.64192527, 5860448.67670936,  5978382.86519673},
        {5280130.00000000, 6519526.00000000, 6595648.00000000, 6705837.00000000, 6818732.00000000, 6830483.00000000, 6853077.06784017, 6866056.30437455, 7210032.56705998,  7354636.61568272},
        {5445384.00000000, 6609618.00000000, 6781201.00000000, 6797628.00000000, 6804079.00000000, 6828319.60768013, 6852996.09189580, 6866492.20393941, 7210786.68020137,  7355583.36681721},
        {5612138.00000000, 7450088.00000000, 7605951.00000000, 7733097.00000000, 7812031.33791376, 7840573.69256379, 7870488.10070553, 7886378.80905384, 8282035.65676849,  8448477.78082174},
        {6593299.00000000, 8185717.00000000, 8259906.00000000, 8348952.61167893, 8448352.22643042, 8481965.25620956, 8520429.27596984, 8539141.42418035, 8968412.14715857,  9149166.27523799},
        {6603091.00000000, 8262839.00000000, 8403760.04286837, 8494277.31569130, 8595295.10296038, 8629471.11224215, 8668555.92992034, 8687581.49673648, 9124307.62911943,  9308199.67701000},
        {7194587.00000000, 9009128.00783773, 9163838.11121431, 9263473.72021727, 9374940.62364950, 9412468.21919766, 9455658.43838825, 9476549.67599389, 9953016.53722705, 10153657.86386850}
    };
    private final static double[][] EXPECTED_INCURRED = {
        {4873559.00000000,  5726628.00000000,  5643478.00000000,  5703210.00000000,  5787091.00000000,  5858937.00000000,  5862561.00000000,  5791898.00000000,  5965654.94000000,  6025311.48940000},
        {5130849.00000000,  6098121.00000000,  6177774.00000000,  6165459.00000000,  6292000.00000000,  6304102.00000000,  6304337.00000000,  6228312.87123282,  6415156.44128648,  6479307.53501206},
        {5945611.00000000,  7525656.00000000,  7557815.00000000,  7377335.00000000,  7442120.00000000,  7418866.00000000,  7421298.39516262,  7331851.68075398,  7551803.99600861,  7627321.77415119},
        {6632221.00000000,  7861102.00000000,  7532937.00000000,  7528468.00000000,  7525868.00000000,  7541744.10443902,  7543670.98725001,  7452731.56193832,  7676308.84966595,  7753071.56110586},
        {7020974.00000000,  8688586.00000000,  8712864.00000000,  8674967.00000000,  8745325.85550112,  8754435.69869250,  8756266.62552723,  8650696.05627822,  8910210.51080637,  8999312.09577358},
        {8275453.00000000,  9867326.00000000,  9932304.00000000,  9830568.37555414,  9861679.63319304,  9836306.45126134,  9836813.08275648,  9718164.27984480, 10009698.09371000, 10109794.17516420},
        {9000368.00000000, 10239888.00000000, 10101109.88709270,  9998011.28213817, 10030018.38422860, 10004481.68694090, 10005008.75053860,  9884331.60372990, 10180850.27683400, 10282657.86713210},
        {9511539.00000000, 11224553.68478870, 11063209.41448280, 10946035.66669130, 10976824.32371020, 10945743.39025480, 10946183.24155120, 10814149.49530770, 11138561.30088640, 11249945.88777920}
    };
    private final static double[][] EXPECTED_INCURRED_PAID = {
        {4873559.00000000,  5726628.00000000, 5643478.00000000, 5703210.00000000, 5787091.00000000, 5858937.00000000, 5862561.00000000, 5791898.00000000, 5915906.85000000,  6034224.98700000},
        {5130849.00000000,  6098121.00000000, 6177774.00000000, 6165459.00000000, 6292000.00000000, 6304102.00000000, 6304337.00000000, 5580226.64192527, 5860448.67670936,  5978382.86519673},
        {5945611.00000000,  7525656.00000000, 7557815.00000000, 7377335.00000000, 7442120.00000000, 7418866.00000000, 6853077.06784017, 6866056.30437455, 7210032.56705998,  7354636.61568272},
        {6632221.00000000,  7861102.00000000, 7532937.00000000, 7528468.00000000, 7525868.00000000, 6828319.60768013, 6852996.09189580, 6866492.20393941, 7210786.68020137,  7355583.36681721},
        {7020974.00000000,  8688586.00000000, 8712864.00000000, 8674967.00000000, 7812031.33791376, 7840573.69256379, 7870488.10070553, 7886378.80905384, 8282035.65676849,  8448477.78082174},
        {8275453.00000000,  9867326.00000000, 9932304.00000000, 8348952.61167893, 8448352.22643042, 8481965.25620956, 8520429.27596984, 8539141.42418035, 8968412.14715857,  9149166.27523799},
        {9000368.00000000, 10239888.00000000, 8403760.04286837, 8494277.31569130, 8595295.10296038, 8629471.11224215, 8668555.92992034, 8687581.49673648, 9124307.62911943,  9308199.67701000},
        {9511539.00000000,  9009128.00783773, 9163838.11121431, 9263473.72021727, 9374940.62364950, 9412468.21919766, 9455658.43838825, 9476549.67599389, 9953016.53722705, 10153657.86386850}
    };
    
    private ClaimTriangle paidCik;
    private ClaimTriangle incurredCik;
    private Estimate paid;
    private Estimate incurred;
    private Estimate paidIncurred;
    private DummyBundle bundle;
    
    @Before
    public void setUp() {
        paidCik = TestData.getCummulatedTriangle(TestData.PAID);
        incurredCik = TestData.getCummulatedTriangle(TestData.INCURRED);
        
        LinkRatio paidLr = createLinkRatio(paidCik, 1.05, 1.02);
        LinkRatio incurredLr = createLinkRatio(incurredCik, 1.03, 1.01);
        
        LRResidualTriangle paidLrResiduals = new LinkRatioResiduals(new SimpleLinkRatioScale(paidLr));
        CRResidualTriangle paidCrResiduals = createCrResiduals(incurredCik, paidCik, incurredLr, paidLr);
        MclCorrelation paidLambda = new MclCorrelation(paidLrResiduals, paidCrResiduals);
        
        LRResidualTriangle incurredLrResiduals = new LinkRatioResiduals(new SimpleLinkRatioScale(incurredLr));
        CRResidualTriangle incurredCrResiduals = createCrResiduals(paidCik, incurredCik, paidLr, incurredLr);
        MclCorrelation incurredLambda = new MclCorrelation(incurredLrResiduals, incurredCrResiduals);
        bundle = new DummyBundle(paidLambda, incurredLambda);
        paid = bundle.getPaidEstimate();
        incurred = bundle.getIncurredEstimate();
        paidIncurred = bundle.getIncurredPaidEstimate();
    }
    
    private LinkRatio createLinkRatio(ClaimTriangle cik, double t1, double t2) {
        LinkRatioSmoothingSelection lrs = new DefaultLinkRatioSmoothing(cik);
        lrs.setDevelopmentCount(DEVELOPMENT_COUNT);
        UserInputLRCurve tail = new UserInputLRCurve();
        tail.setValue(7, t1);
        tail.setValue(8, t2);
        lrs.setMethod(tail, 7, 8);
        return lrs;
    }
    
    private CRResidualTriangle createCrResiduals(ClaimTriangle numerator, ClaimTriangle denumerator, LinkRatio lrN, LinkRatio lrD) {
        ClaimRatioSelection crs = new DefaultClaimRatioSelection(numerator, denumerator);
        crs.setDevelopmentCount(DEVELOPMENT_COUNT);
        crs.setMethod(new LrCrExtrapolation(lrN, lrD), 8);
        
        ClaimRatioScale scales = new SimpleClaimRatioScale(crs);
        CRResidualTriangle residuals = new ClaimRatioResiduals(scales);
        return new ClaimRatioResidualTriangleCorrection(residuals, 0, 6, Double.NaN);
    }
    
    @Test
    public void testCalculation() {
        assertEstimateEquals(EXPECTED_PAID, paid);
        assertEstimateEquals(EXPECTED_INCURRED, incurred);
        assertEstimateEquals(EXPECTED_INCURRED_PAID, paidIncurred);
    }
    
    public void assertEstimateEquals(double[][] expected, Estimate estimate) {
        int accidents = expected.length;
        assertEquals(accidents, estimate.getAccidentCount());
    
        int devs = expected[0].length;
        assertEquals(devs, estimate.getDevelopmentCount());
        
        assertEquals(Double.NaN, estimate.getValue(0, -1), TestConfig.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++)
            for(int d=0; d<devs; d++)
                assertEquals(expected[a][d], estimate.getValue(a, d), TestConfig.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(0, devs), TestConfig.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(accidents, 0), TestConfig.EPSILON);
    }
    
    @Test
    public void testRecalculateCount() {
        paidCik.recalculate();
        assertEquals(1, bundle.calculationCounter);
        
        assertEstimateEquals(EXPECTED_PAID, paid);
        assertEstimateEquals(EXPECTED_INCURRED, incurred);
        assertEstimateEquals(EXPECTED_INCURRED_PAID, paidIncurred);
    }
    
    private static class DummyBundle extends MclEstimateBundle {

        private int calculationCounter;
        
        public DummyBundle(MclCorrelation paidCorrelation, MclCorrelation incurredCorrelation) {
            super(paidCorrelation, incurredCorrelation);
        }

        public DummyBundle(MclEstimateInput source) {
            super(source);
        }

        @Override
        protected void recalculateLayer() {
            calculationCounter++;
            super.recalculateLayer(); //To change body of generated methods, choose Tools | Templates.
        }
        
        
    }
}
