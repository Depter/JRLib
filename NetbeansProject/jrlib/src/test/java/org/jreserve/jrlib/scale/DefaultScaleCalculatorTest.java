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
package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultScaleCalculatorTest {
    
    private final static double[] EXPECTED = {
        5.09840661, 2.81129417, 3.34113131, 1.55856978, 2.10383801, 1.50026769, 
        4.44352222, 0.83591027, 0.61983092, 0.65462278, 0.43663965, 0.73083009, 
        0.47413736, 0.50983808, 0.48409495, 0.22359615, 0.40111860, 0.09013813, 
        0.32243600, 0.13180775, 0.18606523, 0.23206291, 0.16864792, 0.12885120, 
        0.12982162, 0.30938220, 0.22534841, 0.07189430, 0.03490764, 0.09008184, 
        0.24988217, 0.05921756, 0.00232665, 0.03149351, 0.07154208, 0.01668870, 
        0.00337297, 0.06316792, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 
        Double.NaN, Double.NaN
    };
    
    private LinkRatio source;
    private DefaultScaleCalculator<LinkRatioScaleInput> scale;
    private ChangeCounter changeCounter;

    @Before
    public void setUp() {
        source = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.Q_PAID));
        LinkRatioScaleInput input = new LinkRatioScaleInput(source);
        scale = new DefaultScaleCalculator<LinkRatioScaleInput>(input);
        changeCounter = new ChangeCounter();
        scale.addChangeListener(changeCounter);
    }

    @Test
    public void testGetLength() {
        assertEquals(source.getLength(), scale.getLength());
    }

    @Test
    public void testGetValue() {
        int developments = EXPECTED.length;
        assertEquals(Double.NaN, scale.getValue(-1), TestConfig.EPSILON);
        
        for(int d=0; d<developments; d++)
            assertEquals(EXPECTED[d], scale.getValue(d), TestConfig.EPSILON);
        
        assertEquals(Double.NaN, scale.getValue(developments), TestConfig.EPSILON);
    }

    @Test
    public void testToArray() {
        double[] found = scale.toArray();
        assertArrayEquals(EXPECTED, found, TestConfig.EPSILON);
    }
}