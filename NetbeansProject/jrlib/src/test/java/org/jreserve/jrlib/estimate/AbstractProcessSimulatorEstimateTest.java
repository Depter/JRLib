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

import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractProcessSimulatorEstimateTest {
    
    private final static int SIZE = 4;
    private final static double OBSERVED = 1d;
    private final static double ESTIMATED = 2d;
    final static double PROCESS = 2d;
    
    private AbstractProcessSimulatorEstimate estimate;
    
    @Before
    public void setUp() {
        estimate = new AbstractProcessSimulatorEstimateImpl(SIZE);
    }
    
    @Test
    public void testDefaultNoSimulator() {
        assertEquals(null, estimate.getProcessSimulator());
    }
    
    @Test
    public void testRecalculateLayer() {
        estimate.recalculateLayer();
        assertEstimateEquals(OBSERVED, ESTIMATED);
    }
    
    private void assertEstimateEquals(double observed, double estimated) {
        assertEquals(SIZE, estimate.getAccidentCount());
        assertEquals(SIZE, estimate.getDevelopmentCount());
        
        for(int a=0; a<SIZE; a++) {
            for(int d=0; d<SIZE; d++) {
                double expected = (SIZE-a)>d? observed : estimated;
                double found = estimate.getValue(a, d);
                assertEquals(expected, found, TestConfig.EPSILON);
            }
        }
    }
    
    @Test
    public void testSetProcessSimulator() {
        ChangeCounter listener = new ChangeCounter();
        estimate.addCalculationListener(listener);
        
        estimate.setProcessSimulator(new DummyProcessSimulator());
        assertEquals(listener.getChangeCount(), 2);
        assertEstimateEquals(OBSERVED, ESTIMATED+PROCESS);
        
        estimate.setProcessSimulator(null);
        assertEquals(listener.getChangeCount(), 4);
        assertEstimateEquals(OBSERVED, ESTIMATED);
    }
    
    static class DummyProcessSimulator implements ProcessSimulator {
        public double simulateEstimate(double cad, int accident, int development) {
            return cad+PROCESS;
        }
    }
    
    private static class AbstractProcessSimulatorEstimateImpl extends AbstractProcessSimulatorEstimate {
        
        private int size;
        
        private AbstractProcessSimulatorEstimateImpl(int size) {
            this.size = size;
        }
        
        @Override
        protected void initDimensions() {
            accidents = size;
            developments = size;
        }

        @Override
        protected double getObservedValue(int accident, int development) {
            return OBSERVED;
        }

        @Override
        protected double getEstimatedValue(int accident, int development) {
            return ESTIMATED;
        }

        @Override
        public int getObservedDevelopmentCount(int accident) {
            return size - accident;
        }
    
    }
}
