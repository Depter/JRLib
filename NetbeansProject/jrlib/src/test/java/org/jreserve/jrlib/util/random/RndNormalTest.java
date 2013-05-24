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
package org.jreserve.jrlib.util.random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.Before;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RndNormalTest extends AbstractRndFunctionTest {
    
    private final static long SEED = 1000;
    private final static double MEAN = 1000;
    private final static double SIGMA = 100;

    private RndNormal rnd;
    private NormalDistribution nd;
    
    public RndNormalTest() {
    }

    @Before
    public void setUp() {
        rnd = new RndNormal(new JavaRandom(SEED));
        nd = new NormalDistribution(MEAN, SIGMA);
    }
    
    @Override
    protected double nextRndValue() {
        return rnd.nextNormal(MEAN, SIGMA);
    }

    @Override
    protected double getCummulativeProbability(double x) {
        return nd.cumulativeProbability(x);
    }
}
