package org.jreserve.estimate.mcl;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.estimate.Estimate;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.linkratio.smoothing.DefaultLinkRatioSmoothing;
import org.jreserve.linkratio.smoothing.LinkRatioFunction;
import org.jreserve.linkratio.smoothing.LinkRatioSmoothingSelection;
import org.jreserve.linkratio.smoothing.UserInputLRFunction;
import org.jreserve.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleSelection;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class MclEstimateTest {

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
    private final static double[][] EXPECTED_PI = {
        {0.90832285, 0.94629772, 0.97600221, 0.96810165, 0.96074332, 0.95352263, 0.95921339, 0.97277214, 0.99166092, 1.00147934},
        {0.85540580, 0.88111190, 0.87948976, 0.88692391, 0.88112858, 0.88322667, 0.88328448, 0.89594514, 0.91353169, 0.92268855},
        {0.88807189, 0.86630667, 0.87269244, 0.90897824, 0.91623516, 0.92069098, 0.92343370, 0.93646961, 0.95474307, 0.96424890},
        {0.82104984, 0.84080044, 0.90020679, 0.90292314, 0.90409226, 0.90540325, 0.90844313, 0.92133899, 0.93935599, 0.94873152},
        {0.79933895, 0.85745690, 0.87295647, 0.89142668, 0.89328076, 0.89561155, 0.89884062, 0.91164673, 0.92949944, 0.93879151},
        {0.79672968, 0.82957804, 0.83162034, 0.84928483, 0.85668492, 0.86231202, 0.86617782, 0.87867844, 0.89597229, 0.90498047},
        {0.73364678, 0.80692670, 0.83196403, 0.84959669, 0.85695706, 0.86256054, 0.86642162, 0.87892453, 0.89622255, 0.90523285},
        {0.75640619, 0.80262684, 0.82831643, 0.84628572, 0.85406675, 0.85992042, 0.86383155, 0.87631022, 0.89356392, 0.90255171}
    };

    private static LinkRatioScale PAID_SCALE;
    private static LinkRatioScale INCURRED_SCALE;
    
    private MclEstimate estimate;
    
    public MclEstimateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PAID_SCALE = createLrScales(TestData.PAID, 1.05, 1.02);
        INCURRED_SCALE = createLrScales(TestData.INCURRED, 1.03, 1.01);
    }
    
    private static LinkRatioScale createLrScales(String triangleName, double lr7, double lr8) {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(triangleName));
        LinkRatioSmoothingSelection smoothing = new DefaultLinkRatioSmoothing(lr);
        smoothing.setDevelopmentCount(9);
        smoothing.setMethod(createTail(lr7, lr8), 7, 8);
        
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(smoothing);
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), 6, 7, 8);
        return scales;
    }
    
    private static LinkRatioFunction createTail(double lr7, double lr8) {
        UserInputLRFunction tail = new UserInputLRFunction();
        tail.setValue(7, lr7);
        tail.setValue(8, lr8);
        return tail;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PAID_SCALE = null;
        INCURRED_SCALE = null;
    }

    @Before
    public void setUp() {
        estimate = new MclEstimate(PAID_SCALE, INCURRED_SCALE);
    }

    @Test
    public void testRecalculateLayer() {
        assertEstimateEquals(EXPECTED_PAID, estimate.getPaidEstimate());
        assertEstimateEquals(EXPECTED_INCURRED, estimate.getIncurredDelegate());
        assertEstimateEquals(EXPECTED_PI, estimate);
    }
    
    private void assertEstimateEquals(double[][] expected, Estimate estimate) {
        int accidents = expected.length;
        assertEquals(accidents, estimate.getAccidentCount());
    
        int devs = expected[0].length;
        assertEquals(devs, estimate.getDevelopmentCount());
        
        assertEquals(Double.NaN, estimate.getValue(0, -1), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(-1, 0), JRLibTestUtl.EPSILON);
        for(int a=0; a<accidents; a++)
            for(int d=0; d<devs; d++)
                assertEquals(expected[a][d], estimate.getValue(a, d), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(0, devs), JRLibTestUtl.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(accidents, 0), JRLibTestUtl.EPSILON);
    }

}