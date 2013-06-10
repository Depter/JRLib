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
package org.jreserve.jrlib.bootstrap;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.vector.InputVector;
import static org.junit.Assert.assertArrayEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class BootstrapCallBackTest {
    
    private static CalculationData data;
    
    @BeforeClass
    public static void setUpClass() {
        data = new InputVector(new double[]{1});
    }
    
    @Test
    public void testCalledAtEnd() {
        BsCallback cb = new BsCallback(10);
        Bootstrapper bs = new BootstrapImpl(data, 25, cb);
        bs.run();
        
        int[] expected = {10, 20, 25};
        int[] found = cb.getCalls();
        assertArrayEquals(expected, found);
    }
    
    @Test
    public void testEndCalledOnece() {
        BsCallback cb = new BsCallback(10);
        Bootstrapper bs = new BootstrapImpl(data, 20, cb);
        bs.run();
        
        int[] expected = {10, 20};
        int[] found = cb.getCalls();
        assertArrayEquals(expected, found);
    }
    
    private static class BootstrapImpl extends Bootstrapper<CalculationData> {

        public BootstrapImpl(CalculationData source, int bootstrapCount, BootstrapCallBack cb) {
            super(source, bootstrapCount, cb);
        }
        
        @Override
        protected void collectBootstrapResult() {
        }
    }
    
    private static class BsCallback extends AbstractBootstrapCallback {
        
        private List<Integer> calls = new ArrayList<Integer>();
        
        private BsCallback(int stepSize) {
            super(stepSize);
        }

        @Override
        public void progress(int total, int completed) {
            calls.add(completed);
        }
    
        int[] getCalls() {
            int size = calls.size();
            int[] result = new int[size];
            for(int i=0; i<size; i++)
                result[i] = calls.get(i);
            return result;
        }
    }
}
