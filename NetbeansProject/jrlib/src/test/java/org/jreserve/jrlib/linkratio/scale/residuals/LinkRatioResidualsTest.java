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
package org.jreserve.jrlib.linkratio.scale.residuals;

import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioResidualsTest {
    
    private final static double[][] EXPECTED = {
        {-0.51909471, -1.11661064, -1.15179795,  0.77198984,  1.49016632, -0.84967367, -1.18935563, -0.75943607, 0.00000000},
        { 0.02960695,  0.04670296,  0.63189598, -0.60820819, -0.32152712,  0.93859206,  0.34615404,  0.65058194},
        { 1.28953441, -0.17915491,  0.00613582,  0.84995471, -1.14091105,  0.75814702,  0.68235663},
        { 1.49982777, -1.22807744,  1.83969400, -1.59412945, -0.28204991, -0.90681458},
        {-1.54043261,  0.68935341, -0.68272896,  0.00505286,  0.54294980},
        {-0.19654194, -0.66365231, -0.63617093,  0.87773415},
        {-0.94166879,  0.76409783, -0.13683939},
        { 0.69291414,  1.64658460},
        { 0.19710471}
    };
    
    private ClaimTriangle cik;
    private LRResidualTriangle residuals;
    
    @Before
    public void setUp() {
        cik = TestData.getCummulatedTriangle(TestData.TAYLOR_ASHE);
        LinkRatio lrs = new SimpleLinkRatio(cik);
        LinkRatioScale scales = new SimpleLinkRatioScale(lrs);
        residuals = new LinkRatioResiduals(scales);
    }
    
    @Test
    public void testGetAccidentCount() {
        assertEquals(cik.getAccidentCount()-1, residuals.getAccidentCount());
    }
    
    @Test
    public void testGetDevelopmentCount() {
        assertEquals(cik.getDevelopmentCount()-1, residuals.getDevelopmentCount());
    }
    
    @Test
    public void testGetDevelopmentCount_int() {
        int accidents = residuals.getAccidentCount();
        assertEquals(0, residuals.getDevelopmentCount(-1));
        for(int a=0; a<accidents; a++)
            assertEquals(cik.getDevelopmentCount(a)-1, residuals.getDevelopmentCount(a));
        assertEquals(0, residuals.getDevelopmentCount(accidents));
    }
    
    @Test
    public void testRecalculate() {
        assertEquals(Double.NaN, residuals.getValue(-1, 0), TestConfig.EPSILON);
        int accidents = EXPECTED.length;
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, residuals.getValue(a, -1), TestConfig.EPSILON);
            int devs = residuals.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                assertEquals(EXPECTED[a][d], residuals.getValue(a, d), TestConfig.EPSILON);
            assertEquals(Double.NaN, residuals.getValue(a, devs), TestConfig.EPSILON);
        }
        assertEquals(Double.NaN, residuals.getValue(accidents, 0), TestConfig.EPSILON);
    }
}
