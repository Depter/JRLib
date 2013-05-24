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
package org.jreserve.jrlib.bootstrap.odp.scale;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VariableOdpResidualScaleTest {
    
    private final static double[] EXPECTED = {
        139.90948681, 142.27330302, 153.04622417, 318.11324978, 282.58785767, 
        386.56072129, 296.72570659,  83.87334564,  99.56258727,   0.00000000
    };
    
    private OdpResidualScale scales;
    
    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        scales = new VariableOdpResidualScale(lrs);
    }
    
    @Test
    public void testRecalculate() {
        int devs = EXPECTED.length;
        assertEquals(devs, scales.getLength());
        
        assertEquals(Double.NaN, scales.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<10; d++)
            assertEquals(EXPECTED[d], scales.getValue(d), TestConfig.EPSILON);
        assertEquals(Double.NaN, scales.getValue(devs), TestConfig.EPSILON);
    }

}
