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
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.vector.InputVector;
import org.jreserve.jrlib.vector.Vector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CompositeEstimateTest {
    private final static double[] LOSS_RATIOS = {
        16.74578457, 16.74578457, 16.74578457, 16.74578457,
        16.74578457, 16.74578457, 16.74578457, 16.74578457
    };

    private final static double[][] EXPECTED = {
        {4873559.00000000,  5726628.00000000,  5643478.00000000,  5703210.00000000,  5787091.00000000,  5858937.00000000,  5862561.00000000,  5791898.00000000,  6081492.90000000},
        {5130849.00000000,  6098121.00000000,  6177774.00000000,  6165459.00000000,  6292000.00000000,  6304102.00000000,  6304337.00000000,  6228349.15690020,  6539766.61474521},
        {5945611.00000000,  7525656.00000000,  7557815.00000000,  7377335.00000000,  7442120.00000000,  7418866.00000000,  7421219.80351029,  7331769.87625573,  7698358.37006851},
        {6632221.00000000,  7861102.00000000,  7532937.00000000,  7528468.00000000,  7525868.00000000,  7549266.90862263,  7551662.08477888,  7460639.90217017,  7833671.89727867},
        {7020974.00000000,  8688586.00000000,  8712864.00000000,  8674967.00000000,  8681699.56107744,  8708692.10130253,  8711455.13139879,  8606453.65611349,  9036776.33891917},
        {8275453.00000000,  9867326.00000000,  9932304.00000000,  9782441.60084136,  9882042.52135552,  9912767.08494900,  9915912.13514417,  9796393.02068111, 10286212.67171520},
        {9000368.00000000, 10239888.00000000, 10692695.11104060, 10640040.63353810, 10748373.28551300, 10781791.38484050, 10785212.15270570, 10655215.13496090, 11187975.89170900},
        {9511539.00000000, 11553290.06115170, 11500216.39808400, 11443585.41035650, 11560099.43490800, 11596041.29707540, 11599720.40419370, 11459905.90283130, 12032901.19797290}
    };
    
    private final static double[] RESERVES = {
        289594.90000000, 235429.61474521, 279492.37006851, 307803.89727867,
        361809.33891917, 353908.67171516,
        948087.89170899, 2521362.19797291 
    };
    
    private LinkRatio lrs;
    
    private CompositeEstimate estimate;
    
    @Before
    public void setUp() {
        this.lrs = createLinkRatios();
        Vector exposure = new InputVector(TestData.getCachedVector(TestData.EXPOSURE));
        Vector lossRatios = new InputVector(LOSS_RATIOS);
        
        ChainLadderEstimate cl = new ChainLadderEstimate(lrs);
        CapeCodEstimate cc = new CapeCodEstimate(lrs, exposure);
        ExpectedLossRatioEstimate elr = new ExpectedLossRatioEstimate(lrs, exposure, lossRatios);
        estimate = new CompositeEstimate(cl, cl, cl, cl, cc, cc, elr, elr);
    }
    
    private LinkRatio createLinkRatios() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.INCURRED);
        DefaultLinkRatioSmoothing smoothing = new DefaultLinkRatioSmoothing(cik);
        smoothing.setDevelopmentCount(8);
        smoothing.setMethod(new UserInputLRCurve(7, 1.05), 7);
        return smoothing;
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(EXPECTED.length, estimate.getAccidentCount());
    }
    
    @Test
    public void testGetDevelopmentCount() {
        assertEquals(EXPECTED[0].length, estimate.getDevelopmentCount());
    }
    
    @Test
    public void testGetDevelopmnetCount_int() {
        int accidents = EXPECTED.length;
        assertEquals(0, estimate.getObservedDevelopmentCount(-1));
        for(int a=0; a<accidents; a++)
            assertEquals(8-a, estimate.getObservedDevelopmentCount(a));
        assertEquals(0, estimate.getObservedDevelopmentCount(accidents));
    }
    
    @Test
    public void testGetValue() {
        int accidents = EXPECTED.length;
        int devs = EXPECTED[0].length;
        
        assertEquals(Double.NaN, estimate.getValue(-1, 0), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, estimate.getValue(a, -1), TestConfig.EPSILON);
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED[a][d], estimate.getValue(a, d), TestConfig.EPSILON);            
            assertEquals(Double.NaN, estimate.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, estimate.getValue(accidents, 0), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetReserve_int() {
        int accidents = EXPECTED.length;
        assertEquals(Double.NaN, estimate.getReserve(-1), TestConfig.EPSILON);
        for(int a=0; a<accidents; a++)
            assertEquals(RESERVES[a], estimate.getReserve(a), TestConfig.EPSILON);
        assertEquals(Double.NaN, estimate.getReserve(accidents), TestConfig.EPSILON);
    }
    
    @Test
    public void testGetReserve() {
        assertEquals(5297488.88240862, estimate.getReserve(), TestConfig.EPSILON);
    }
}
