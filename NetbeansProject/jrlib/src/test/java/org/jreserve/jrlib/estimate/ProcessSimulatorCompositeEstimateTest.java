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

import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.estimate.AbstractProcessSimulatorEstimateTest.DummyProcessSimulator;
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
public class ProcessSimulatorCompositeEstimateTest {
    
    private LinkRatio lrs;
    private ProcessSimulatorCompositeEstimate estimate;
    
    @Before
    public void setUp() {
        this.lrs = createLinkRatios();
        Vector exposure = new InputVector(TestData.getCachedVector(TestData.EXPOSURE));
        Vector lossRatios = new InputVector(CompositeEstimateTest.LOSS_RATIOS);
        
        ChainLadderEstimate cl = new ChainLadderEstimate(lrs);
        CapeCodEstimate cc = new CapeCodEstimate(lrs, exposure);
        ExpectedLossRatioEstimate elr = new ExpectedLossRatioEstimate(lrs, exposure, lossRatios);
        estimate = new ProcessSimulatorCompositeEstimate(cl, cl, cl, cl, cc, cc, elr, elr);
    }
    
    private LinkRatio createLinkRatios() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.INCURRED);
        DefaultLinkRatioSmoothing smoothing = new DefaultLinkRatioSmoothing(cik);
        smoothing.setDevelopmentCount(8);
        smoothing.setMethod(new UserInputLRCurve(7, 1.05), 7);
        return smoothing;
    }
    
    
    @Test
    public void testDefaultNoSimulator() {
        assertEquals(null, estimate.getProcessSimulator());
    }
    
    @Test
    public void testSetProcessSimulator() {
        double[][] values = estimate.toArray();
        int accidents = values.length;
        int developments = values[0].length;
        
        ChangeCounter listener = new ChangeCounter();
        estimate.addCalculationListener(listener);
        
        estimate.setProcessSimulator(new DummyProcessSimulator());
        assertEquals(listener.getChangeCount(), 2);
        for(int a=0; a<accidents; a++) {
            int firstEstimate = estimate.getObservedDevelopmentCount(a);
            double expected = 0d;
            if(firstEstimate == developments)
                expected = Double.NaN;
            else
                expected += values[a][firstEstimate] + AbstractProcessSimulatorEstimateTest.PROCESS;
            
            double found = estimate.getValue(a, firstEstimate);
            assertEquals(expected, found, TestConfig.EPSILON);
        }
        
        estimate.setProcessSimulator(null);
        assertEquals(listener.getChangeCount(), 4);
        int ultimate = developments-1;
        for(int a=0; a<accidents; a++) {
            double expected = values[a][ultimate];
            double found = estimate.getValue(a, ultimate);
            assertEquals(expected, found, TestConfig.EPSILON);
        }
    }
}
