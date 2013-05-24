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
import org.jreserve.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleSelection;
import org.jreserve.jrlib.scale.MinMaxScaleEstimator;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioSECalculatorTest {

    private final static double[] EXPECTED_MACK = {
        2.24376318, 0.51681801, 0.12200144, 0.05117008, 
        0.04207692, 0.02303354, 0.01465199, 0.01222874
    };
    
    private LinkRatioScale scales;
    private LinkRatioSECalculator ses;
    
    public LinkRatioSECalculatorTest() {
    }

    @Before
    public void setUp() {
        scales = createScales();
        ses = new LinkRatioSECalculator(scales);
    }
    
    private LinkRatioScale createScales() {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.MACK_DATA));
        LinkRatioScaleSelection result = new DefaultLinkRatioScaleSelection(lr);
        int developments = lr.getLength();
        result.setMethod(new MinMaxScaleEstimator(), developments-1);
        return result;
    }

    @Test
    public void testGetSourceLRScales() {
        assertEquals(scales, ses.getSourceLRScales());
    }

    @Test
    public void testGetLength() {
        assertEquals(scales.getLength(), ses.getLength());
    }

    @Test
    public void testGetValue() {
        int length = EXPECTED_MACK.length;
        assertEquals(Double.NaN, ses.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_MACK[d], ses.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, ses.getValue(length), TestConfig.EPSILON);
    }

    @Test
    public void testToArray() {
        assertArrayEquals(EXPECTED_MACK, ses.toArray(), TestConfig.EPSILON);
    }
}
