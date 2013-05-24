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
package org.jreserve.jrlib.linkratio.standarderror;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FixedRatioSEFunctionTest {

    private final static double[] EXPECTED = {
        0.51168420, 0.18857194, 0.07870063, 0.05879438, 
        0.05248108, 0.04924554, 0.04729351, 0.04712522
    };
    
    private FixedRatioSEFunction sef;
    private LinkRatioSE ses;
    
    public FixedRatioSEFunctionTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.MACK_DATA));
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        ses = new LinkRatioSECalculator(scales);
        
        sef = new FixedRatioSEFunction();
    }

    @Test
    public void testFit() {
        sef.setExcluded(0, true);
        sef.fit(ses);

        assertEquals(0.04608000, sef.getRatio(), TestConfig.EPSILON);
        
        assertEquals(Double.NaN, sef.getValue(-1), TestConfig.EPSILON);
        int devs = EXPECTED.length;
        for(int d=0; d<devs; d++)
            assertEquals(EXPECTED[d], sef.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, sef.getValue(devs), TestConfig.EPSILON);
    }
}
