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
import org.jreserve.jrlib.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.jrlib.linkratio.curve.UserInputLRCurve;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EstimateUtilTest {
    
    private final static double[][] COMPLETED_MACK = {
        {58046.00000000, 127970.00000000,  476599.00000000, 1027692.00000000, 1360489.00000000, 1647310.00000000, 1819179.00000000, 1906852.00000000, 1950105.00000000,  2047610.25000000},
        {24492.00000000, 141767.00000000,  984288.00000000, 2142656.00000000, 2961978.00000000, 3683940.00000000, 4048898.00000000, 4115760.00000000, 4209117.51661901,  4419573.39244997},
        {32848.00000000, 274682.00000000, 1522637.00000000, 3203427.00000000, 4445927.00000000, 5158781.00000000, 5342585.00000000, 5483281.24051883, 5607658.15256872,  5888041.06019716},
        {21439.00000000, 529828.00000000, 2900301.00000000, 4999019.00000000, 6460112.00000000, 6853904.00000000, 7324744.90537235, 7517641.05413654, 7688163.21763668,  8072571.37851851},
        {40397.00000000, 763394.00000000, 2920745.00000000, 4989572.00000000, 5648563.00000000, 6433218.69626640, 6875159.88998729, 7056216.27926084, 7216271.97457798,  7577085.57330688},
        {90748.00000000, 951994.00000000, 4210640.00000000, 5866482.00000000, 7485161.34532487, 8524943.40796314, 9110579.28387465, 9350505.13516011, 9562602.03550218, 10040732.13727730},
        {62096.00000000, 868480.00000000, 1954797.00000000, 3338623.45546910, 4259816.22980728, 4851557.71693708, 5184843.94736996, 5321386.09898229, 5442090.75405740,  5714195.29176027},
        {24983.00000000, 284441.00000000, 1164010.36082436, 1988028.57435184, 2536565.29384045, 2888925.78035066, 3087385.58223493, 3168691.45653595, 3240566.67892843,  3402595.01287485},
        {13121.00000000, 145698.98035482,  596240.07331770, 1018326.24762013, 1299302.76196466, 1479792.08524037, 1581448.98693864, 1623096.22830861, 1659912.81457909,  1742908.45530805}
    };
    
    public EstimateUtilTest() {
    }

    @Test
    public void testCompleteTriangle() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA);
        DefaultLinkRatioSmoothing lrs = new DefaultLinkRatioSmoothing(cik);
        lrs.setDevelopmentCount(9);
        lrs.setMethod(new UserInputLRCurve(8, 1.05), 8);
        
        assertEquals(cik.getDevelopmentCount(), lrs.getLength());
        double[][] found = EstimateUtil.completeTriangle(cik, lrs);
        int accidents = COMPLETED_MACK.length;
        assertEquals(accidents, found.length);
        for(int a=0; a<accidents; a++) {
            int devs = COMPLETED_MACK[a].length;
            assertEquals("For accident period: "+a, devs, found[a].length);
            assertArrayEquals("For accident period: "+a, COMPLETED_MACK[a], found[a], TestConfig.EPSILON);
        }
    }
    
    @Test
    public void testGetCummulatedLinkRatios() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        DefaultLinkRatioSmoothing lrs = new DefaultLinkRatioSmoothing(cik);
        lrs.setDevelopmentCount(8);
        lrs.setMethod(new UserInputLRCurve(7, 1.05), 7);
        double[] clrs = EstimateUtil.getCummulativeLinkRatios(lrs);
        double[] expected = {
            1.24694402, 1.26670461, 1.27868778, 1.29089024, 
            1.29538181, 1.29972392, 1.30220852, 1.36731894
        };
        assertArrayEquals(expected, clrs, TestConfig.EPSILON);
    }
    
    @Test
    public void testGetCompletionRatios() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        DefaultLinkRatioSmoothing lrs = new DefaultLinkRatioSmoothing(cik);
        lrs.setDevelopmentCount(8);
        lrs.setMethod(new UserInputLRCurve(7, 1.05), 7);
        double[] gs = EstimateUtil.getCompletionRatios(lrs);
        double[] expected = {
            0.73135826, 0.91196280, 0.92641487, 0.93517886, 
            0.94410323, 0.94738819, 0.95056382, 0.95238095
        };
        assertArrayEquals(expected, gs, TestConfig.EPSILON);
    }
}
