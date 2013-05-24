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
package org.jreserve.jrlib.triangle;

import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleDimensionCheckTest {
    
    @Test
    public void testAcceptsNull() {
        new InputTriangle(null);
    }
    
    @Test
    public void testAcceptsEmpty() {
        new InputTriangle(new double[0][]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyAccident() {
        new InputTriangle(new double[1][0]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyAccident2() {
        double[][] data = {
            {1, 2},
            {}
        };
        new InputTriangle(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSameDevLength() {
        double[][] data = {
            {1, 2},
            {2, 3},
            {1}
        };
        new InputTriangle(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInconsistentDevLength() {
        double[][] data = {
            {1, 2, 4, 5},
            {2, 3},
            {1}
        };
        new InputTriangle(data);
    }
}