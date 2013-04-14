package org.jrlib.estimate;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jrlib.linkratio.curve.UserInputLRCurve;
import org.jrlib.triangle.Cell;
import org.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ChainLadderEstimateTest {

    private final static double[][] EXPECTED = {
        {4873559.00000000,  5726628.00000000,  5643478.00000000,  5703210.00000000,  5787091.00000000,  5858937.00000000,  5862561.00000000,  5791898.00000000,  6081492.90000000},
        {5130849.00000000,  6098121.00000000,  6177774.00000000,  6165459.00000000,  6292000.00000000,  6304102.00000000,  6304337.00000000,  6228349.15690020,  6539766.61474521},
        {5945611.00000000,  7525656.00000000,  7557815.00000000,  7377335.00000000,  7442120.00000000,  7418866.00000000,  7421219.80351029,  7331769.87625573,  7698358.37006851},
        {6632221.00000000,  7861102.00000000,  7532937.00000000,  7528468.00000000,  7525868.00000000,  7549266.90862263,  7551662.08477888,  7460639.90217017,  7833671.89727867},
        {7020974.00000000,  8688586.00000000,  8712864.00000000,  8674967.00000000,  8763292.05563393,  8790538.27760369,  8793327.27525578,  8687338.97334278,  9121705.92200992},
        {8275453.00000000,  9867326.00000000,  9932304.00000000,  9883393.94766195,  9984022.72472580, 10015064.35811910, 10018241.86444440,  9897489.34265961, 10392363.80979260},
        {9000368.00000000, 10239888.00000000, 10192847.86141720, 10142654.80224630, 10245923.26997470, 10277779.18644150, 10281040.04500490, 10157119.94716710, 10664975.94452550},
        {9511539.00000000, 11363623.09538740, 11311420.72704020, 11255719.43358630, 11370320.68165700, 11405672.49684160, 11409291.20514020, 11271771.99733510, 11835360.59720190}
    };

    private final static double[] RESERVES = {
        289594.90000000, 235429.61474521, 279492.37006851, 307803.89727867, 
        446738.92200992, 460059.80979260, 425087.94452548, 2323821.59720190
    };
    
    private final static double RESERVE = 4768029.05562229;
    
    private ClaimTriangle cik;
    private LinkRatio lrs;
    private ChainLadderEstimate cl;
    
    public ChainLadderEstimateTest() {
    }

    @Before
    public void setUp() {
        cik = TestData.getCummulatedTriangle(TestData.INCURRED);
        this.lrs = createLinkRatios();
        cl = new ChainLadderEstimate(lrs);
    }
    
    private LinkRatio createLinkRatios() {
        DefaultLinkRatioSmoothing smoothing = new DefaultLinkRatioSmoothing(cik);
        smoothing.setDevelopmentCount(8);
        smoothing.setMethod(new UserInputLRCurve(7, 1.05), 7);
        return smoothing;
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(cik.getAccidentCount(), cl.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(EXPECTED[0].length, cl.getDevelopmentCount());
    }

    @Test
    public void testGetValue_Cell() {
        assertEquals(Double.NaN, cl.getValue(new Cell(-1,  0)), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue(new Cell( 0, -1)), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue(new Cell(-1, -1)), TestConfig.EPSILON);
        
        int accidents = EXPECTED.length;
        int developments = EXPECTED[0].length;
        
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertEquals(EXPECTED[a][d], cl.getValue(new Cell(a, d)), TestConfig.EPSILON);

        assertEquals(Double.NaN, cl.getValue(new Cell(accidents,  0)), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue(new Cell(0, developments)), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue(new Cell(accidents, developments)), TestConfig.EPSILON);
    }

    @Test
    public void testGetValue_int_int() {
        assertEquals(Double.NaN, cl.getValue(-1,  0), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue( 0, -1), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue(-1, -1), TestConfig.EPSILON);
        
        int accidents = EXPECTED.length;
        int developments = EXPECTED[0].length;
        
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                assertEquals(EXPECTED[a][d], cl.getValue(a, d), TestConfig.EPSILON);

        assertEquals(Double.NaN, cl.getValue(accidents, 0), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue(0, developments), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getValue(accidents, developments), TestConfig.EPSILON);
    }

    @Test
    public void testToArray() {
        double[][] found = cl.toArray();
        assertEquals(EXPECTED.length, found.length);
        
        for(int a=0; a<EXPECTED.length; a++)
            assertArrayEquals(EXPECTED[a], found[a], TestConfig.EPSILON);
    }

    @Test
    public void testGetReserve_int() {
        assertEquals(Double.NaN, cl.getReserve(-1), TestConfig.EPSILON);
        int accidents = RESERVES.length;
        for(int a=0; a<accidents; a++)
            assertEquals(RESERVES[a], cl.getReserve(a), TestConfig.EPSILON);
        assertEquals(Double.NaN, cl.getReserve(accidents), TestConfig.EPSILON);
    }

    @Test
    public void testToArrayReserve() {
        assertArrayEquals(RESERVES, cl.toArrayReserve(), TestConfig.EPSILON);
    }

    @Test
    public void testGetReserve_0args() {
        assertEquals(RESERVE, cl.getReserve(), TestConfig.EPSILON);
    }
}
