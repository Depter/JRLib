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

import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractTriangleEstimateTest {
    
    private AbstractTriangleEstimate estimate;
    private ClaimTriangle source;
    
    @Before
    public void setUp() {
        source = TestData.getCummulatedTriangle(TestData.PAID);
        estimate = new AbstractTriangleEstimateImpl(source);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_TriangleNull() {
        estimate = new AbstractTriangleEstimateImpl(source, null);
    }
    
    @Test
    public void testGetObservedDevelopmentCount() {
        int accidents = source.getAccidentCount();
        for(int a=-1; a<=accidents; a++) {
            int expected = source.getDevelopmentCount(a);
            int found = estimate.getObservedDevelopmentCount(a);
            assertEquals(expected, found);
        }
    }
    
    @Test
    public void testGetObservedValue() {
        int accidents = source.getAccidentCount();
        int developments = source.getDevelopmentCount();
        
        for(int a=-1; a<=accidents; a++) {
            for(int d=-1; d<=developments; d++) {
                double expected = source.getValue(a, d);
                double found = estimate.getObservedValue(a, d);
                assertEquals(expected, found, TestConfig.EPSILON);
            }
        }
    }
    
    private static class AbstractTriangleEstimateImpl extends AbstractTriangleEstimate {

        private AbstractTriangleEstimateImpl(ClaimTriangle triangle) {
            this(triangle, triangle);
        }
        
        private AbstractTriangleEstimateImpl(CalculationData source, ClaimTriangle triangle) {
            super(source, triangle);
        }
        
        @Override
        protected void initDimensions() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected double getEstimatedValue(int accident, int development) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    }
}
