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
package org.jreserve.jrlib.bootstrap.residualgenerator;

import java.util.Collections;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.bootstrap.FixedRandom;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleResidualGeneratorTest {
    
    private final static double[][] RESIDUALS = {
        {1d , 2d , 3d , 4d, 5d},
        {6d , 7d , 8d , 9d},
        {10d, 11d, 12d},
        {13d, 14d},
        {15d}
    };
    
    private Triangle residuals;
    private DoubleResidualGenerator rG;
    private FixedRandom rnd;
    
    @Before
    public void setUp() {
        rnd = new FixedRandom();
        residuals = new InputClaimTriangle(RESIDUALS);
        rG = new DoubleResidualGenerator(rnd);
        rG.initialize(residuals, Collections.EMPTY_LIST);
    }

    @Test
    public void testGetValue() {
        int index = 0;
        int accidents = residuals.getAccidentCount();
        int developments = residuals.getDevelopmentCount();
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                if(index >= rnd.getMax())
                    index = 0;
                double r = RESIDUALS[0][index++];
                assertEquals(r, rG.getValue(a, d), TestConfig.EPSILON);
            }
        }
    }
}