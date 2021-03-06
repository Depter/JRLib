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
package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.util.random.RndGamma;

/**
 * Mack process simulator, which uses a gamma distribution.
 * 
 * @see LinkRatioScale
 * @author Peter Decsi
 * @version 1.0
 */
public class MackGammaProcessSimulator extends AbstractMackProcessSimulator implements ProcessSimulator {
    
    private final RndGamma rnd;
    
    public MackGammaProcessSimulator(Random rnd, LinkRatioScale scales) {
        super(scales);
        this.rnd = new RndGamma(rnd);
    }

    @Override
    protected double random(double mean, double variance) {
        return rnd.nextGammaFromMeanVariance(mean, variance);
    }
}
