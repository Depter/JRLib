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
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSESelectionTest {
    private final static double[] EXPECTED_SES = {
        2.24376318, 0.51681801, 0.12200144, 0.05117008, 
        0.04207692, 0.02303354, 0.01465199, 0.01222874
    };

    private DefaultLinkRatioSESelection ses;
    
    public DefaultLinkRatioSESelectionTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        ses = new DefaultLinkRatioSESelection(new LinkRatioSECalculator(scales));
    }

    @Test
    public void testSetDefaultMethod() {
        assertTrue(ses.getDefaultMethod() instanceof DefaultLinkRatioSEFunction);
        
        ses.setDefaultMethod(new UserInputLinkRatioSEFunction());
        assertTrue(ses.getDefaultMethod() instanceof UserInputLinkRatioSEFunction);
        
        ses.setDefaultMethod(null);
        assertTrue(ses.getDefaultMethod() instanceof DefaultLinkRatioSEFunction);
    }

    @Test
    public void testRecalculateLayer() {
        int length = EXPECTED_SES.length;
        assertEquals(length, ses.getLength());
        
        assertEquals(Double.NaN, ses.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<length; d++)
            assertEquals(EXPECTED_SES[d], ses.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, ses.getValue(length), TestConfig.EPSILON);
    }
}
