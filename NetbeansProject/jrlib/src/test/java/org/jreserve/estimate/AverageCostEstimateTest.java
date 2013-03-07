package org.jreserve.estimate;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.factor.linkratio.WeightedAverageLRMethod;
import org.jreserve.triangle.CompositeTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleOperation;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AverageCostEstimateTest {

    private final static double[][] EXPECTED = {
        {4426765.00000000, 5419095.00000000, 5508047.00000000, 5521287.00000000, 5559909.00000000, 5586629.00000000, 5623447.00000000, 5634197.00000000}, 
        {4388958.00000000, 5373127.00000000, 5433289.00000000, 5468293.00000000, 5544061.00000000, 5567951.00000000, 5568523.00000000, 5579168.00514542}, 
        {5280130.00000000, 6519526.00000000, 6595648.00000000, 6705837.00000000, 6818732.00000000, 6830483.00000000, 6853835.84432917, 6866937.90349796},
        {5445384.00000000, 6609618.00000000, 6781201.00000000, 6797628.00000000, 6804079.00000000, 6828329.47409539, 6851674.95570143, 6864772.88403147},
        {5612138.00000000, 7450088.00000000, 7605951.00000000, 7733097.00000000, 7807234.42919503, 7835060.25783133, 7861847.66693707, 7876876.68071093},
        {6593299.00000000, 8185717.00000000, 8259906.00000000, 8335086.28328279, 8414995.00150543, 8444986.95461133, 8473859.65156598, 8490058.61125287},
        {6603091.00000000, 8262839.00000000, 8394846.65934362, 8471255.14994449, 8552469.32311108, 8582951.24958074, 8612295.63480449, 8628759.23410118},
        {7194587.00000000, 8960689.97768266, 9103846.55014617, 9186708.23920208, 9274781.48225324, 9307837.80744815, 9339660.51857392, 9357514.57687209}
    };
    
    private final static double[] RESERVES = {
             0.00000000,  10645.00514542,  36454.90349796,   60693.88403147, 
        143779.68071093, 230152.61125287, 365920.23410118, 2162927.57687209 
    };
    
    private final static double RESERVE = 3010573.89561191;
    
    private Triangle numberCik;
    private LinkRatio numberLrs;
    private Triangle costCiks;
    private LinkRatio costLrs;
    
    private AverageCostEstimate estiamte;
    
    public AverageCostEstimateTest() {
    }

    @Before
    public void setUp() {
        initNumberLrs();
        initCostLRs();
        estiamte = new AverageCostEstimate(numberLrs, costLrs);
    }

    private void initNumberLrs() {
        numberCik = TestData.getCummulatedTriangle(TestData.NoC);
        numberLrs = new SimpleLinkRatio(new DevelopmentFactors(numberCik), new WeightedAverageLRMethod());
    }
    
    private void initCostLRs() {
        Triangle paid = TestData.getCummulatedTriangle(TestData.PAID);
        costCiks = new CompositeTriangle(paid, numberCik, TriangleOperation.DIVIDE);
        costLrs = new SimpleLinkRatio(new DevelopmentFactors(costCiks), new WeightedAverageLRMethod());
    }
    
    @Test
    public void testGetObservedDevelopmentCount() {
        assertEquals(0, estiamte.getObservedDevelopmentCount(-1));
        int accidents = numberCik.getAccidentCount();
        for(int a=0; a<accidents; a++)
            assertEquals(numberCik.getDevelopmentCount(a), estiamte.getObservedDevelopmentCount(a));
        assertEquals(0, estiamte.getObservedDevelopmentCount(accidents));
    }

    @Test
    public void testGetSourceCostLinkRatios() {
        assertEquals(costLrs, estiamte.getSourceCostLinkRatios());
    }

    @Test
    public void testGetSourceNumberLinkRatios() {
        assertEquals(numberLrs, estiamte.getSourceNumberLinkRatios());
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
        
        assertEquals(RESERVE, estiamte.getReserve(), JRLibTestSuite.EPSILON);
    }

}