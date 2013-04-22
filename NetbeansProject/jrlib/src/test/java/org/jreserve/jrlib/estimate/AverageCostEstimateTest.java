package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.estimate.AverageCostEstimate;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
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
    
    private ClaimTriangle numberCik;
    private LinkRatio numberLrs;
    private ClaimTriangle costCiks;
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
        numberLrs = new SimpleLinkRatio(numberCik);
    }
    
    private void initCostLRs() {
        double[][] paid = TriangleUtil.copy(TestData.getCachedMatrix(TestData.PAID));
        TriangleUtil.cummulate(paid);
        costCiks = new InputClaimTriangle(TriangleUtil.divide(paid, numberCik.toArray()));
        costLrs = new SimpleLinkRatio(costCiks);
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
        
        assertEquals(Double.NaN, estiamte.getValue(-1, 0), TestConfig.EPSILON);
        
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, estiamte.getValue(a, -1), TestConfig.EPSILON);
            
            for(int d=0; d<developments; d++) {
                String msg = "At ["+a+";"+d+"]";
                assertEquals(msg, EXPECTED[a][d], estiamte.getValue(a, d), TestConfig.EPSILON);
            }
            
            assertEquals(Double.NaN, estiamte.getValue(0, developments), TestConfig.EPSILON);
        }
        
        assertEquals(Double.NaN, estiamte.getValue(accidents, 0), TestConfig.EPSILON);
        
        assertEquals(Double.NaN, estiamte.getReserve(-1), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++)
            assertEquals(RESERVES[a], estiamte.getReserve(a), TestConfig.EPSILON);
        assertEquals(Double.NaN, estiamte.getReserve(accidents), TestConfig.EPSILON);
        
        assertEquals(RESERVE, estiamte.getReserve(), TestConfig.EPSILON);
    }
}
