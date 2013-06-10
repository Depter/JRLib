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
package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class SimpleLinkRatioSmoothingTest {
    
    private final static double TAIL = 1.05;
    
    private ClaimTriangle claims;
    private FactorTriangle factors;
    private LinkRatio lr;
    private SimpleLinkRatioSmoothing smoothing;
    private ChangeCounter listener;
    
    public SimpleLinkRatioSmoothingTest() {
    }

    @Before
    public void setUp() {
        claims = TestData.getCummulatedTriangle(TestData.PAID);
        factors = new DevelopmentFactors(claims);
        lr = new SimpleLinkRatio(factors);
        smoothing = new SimpleLinkRatioSmoothing(lr, createTail(lr), lr.getLength()+1);
        listener = new ChangeCounter();
        smoothing.addCalculationListener(listener);
    }
    
    private UserInputLRCurve createTail(LinkRatio lr) {
        UserInputLRCurve f = new UserInputLRCurve();
        f.setValue(lr.getLength(), TAIL);
        return f;
    }

    @Test
    public void testGetSourceLinkRatios() {
        assertEquals(lr, smoothing.getSourceLinkRatios());
    }

    @Test
    public void testGetSourceFactors() {
        assertEquals(factors, smoothing.getSourceFactors());
    }

    @Test
    public void testGetSourceTriangle() {
        assertEquals(claims, smoothing.getSourceTriangle());
    }

    @Test
    public void testGetLength() {
        assertEquals(lr.getLength()+1, smoothing.getLength());
    }

    @Test
    public void testSetDevelopmentCount() {
        int length = smoothing.getLength();
        assertTrue(!Double.isNaN(smoothing.getValue(length-1)));
        
        smoothing.setDevelopmentCount(length-1);
        assertEquals(2, listener.getChangeCount());
        assertEquals(length-1, smoothing.getLength());
        assertTrue(Double.isNaN(smoothing.getValue(length-1)));
    }

    @Test
    public void testGetMackAlpha() {
        assertEquals(Double.NaN, smoothing.getMackAlpha(-1), TestConfig.EPSILON);
        
        int lrLength = lr.getLength();
        for(int d=0; d<lrLength; d++)
            assertEquals(lr.getMackAlpha(d), smoothing.getMackAlpha(d), TestConfig.EPSILON);
        
        assertEquals(1d, smoothing.getMackAlpha(lrLength), TestConfig.EPSILON);
    }

    @Test
    public void testGetWeight() {
        assertEquals(Double.NaN, smoothing.getWeight(-1, 0), TestConfig.EPSILON);
        
        int accidents = claims.getAccidentCount();
        int lrLength = lr.getLength();
        for(int a=0; a<accidents; a++) {
            assertEquals(Double.NaN, smoothing.getWeight(a, -1), TestConfig.EPSILON);
            
            for(int d=0; d<lrLength; d++)
                assertEquals(lr.getWeight(a, d), smoothing.getWeight(a, d), TestConfig.EPSILON);
        
            assertEquals(Double.NaN, smoothing.getWeight(a, lrLength), TestConfig.EPSILON);
        }
        
        assertEquals(Double.NaN, smoothing.getWeight(accidents, 0), TestConfig.EPSILON);
    }

    @Test
    public void testGetValues() {
        assertEquals(Double.NaN, smoothing.getValue(-1), TestConfig.EPSILON);
        
        int lrLength = lr.getLength();
        for(int d=0; d<lrLength; d++)
            assertEquals(lr.getValue(d), smoothing.getValue(d), TestConfig.EPSILON);
        
        assertEquals(TAIL, smoothing.getValue(lrLength), TestConfig.EPSILON);
        assertEquals(Double.NaN, smoothing.getValue(lrLength+1), TestConfig.EPSILON);
    }
}