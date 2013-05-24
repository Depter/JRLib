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
import org.jreserve.jrlib.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.vector.InputVector;
import org.jreserve.jrlib.vector.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CapeCodEstimateTest {
    
    private final static double[][] EXPECTED = {
        {4426765.00000000, 5419095.00000000, 5508047.00000000, 5521287.00000000,  5559909.00000000,  5586629.00000000,  5623447.00000000,  5634197.00000000,  5107146.72239029},
        {4388958.00000000, 5373127.00000000, 5433289.00000000, 5468293.00000000,  5544061.00000000,  5567951.00000000,  5568523.00000000,  5558795.13566229,  5836734.89244541},
        {5280130.00000000, 6519526.00000000, 6595648.00000000, 6705837.00000000,  6818732.00000000,  6830483.00000000,  6241723.15402166,  6253655.07476453,  6566337.82850275},
        {5445384.00000000, 6609618.00000000, 6781201.00000000, 6797628.00000000,  6804079.00000000,  6912074.09216802,  6935243.27023713,  6948500.95100750,  7295925.99855787},
        {5612138.00000000, 7450088.00000000, 7605951.00000000, 7733097.00000000,  7596695.62776438,  7623127.88870601,  7648680.50940161,  7663302.02454633,  8046467.12577365},
        {6593299.00000000, 8185717.00000000, 8259906.00000000, 8565286.41349356,  8647024.54712065,  8677111.36649549,  8706196.92543121,  8722840.03008720,  9158982.03159156},
        {6603091.00000000, 8262839.00000000, 9139332.34374520, 9225791.47935987,  9313832.78239997,  9346239.72227673,  9377568.19033344,  9395494.71441130,  9865269.45013186},
        {7194587.00000000, 9676201.52654569, 9829542.37407868, 9922530.97600125, 10017221.23195790, 10052075.57106620, 10085769.99125970, 10105050.34144460, 10610302.85851680}
    };
    
    private CapeCodEstimate estimate;
    
    public CapeCodEstimateTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = createLrs();
        Vector exposure = new InputVector(TestData.getCachedVector(TestData.EXPOSURE));
        estimate = new CapeCodEstimate(lrs, exposure);
    }
    
    private LinkRatio createLrs() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        LinkRatioSmoothingSelection smoothing = new DefaultLinkRatioSmoothing(cik);
        smoothing.setDevelopmentCount(8);
        smoothing.setMethod(new UserInputLRCurve(7, 1.05), 7);
        return smoothing;
    }

    @Test
    public void testRecalculateLayer() {
        assertEquals(Double.NaN, estimate.getValue(-1, 0), TestConfig.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(0, -1), TestConfig.EPSILON);
        
        int accidents = EXPECTED.length;
        int devs = EXPECTED[0].length;
        for(int a=0; a<accidents; a++)
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED[a][d], estimate.getValue(a, d), TestConfig.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(accidents, 0), TestConfig.EPSILON);
        assertEquals(Double.NaN, estimate.getValue(0, devs), TestConfig.EPSILON);
    }
}