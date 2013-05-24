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
package org.jreserve.jrlib.bootstrap.odp;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.util.random.Random;
import org.jreserve.jrlib.util.random.RndGamma;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpGammaProcessSimulator implements ProcessSimulator {

    private final RndGamma rnd;
    private final double[] scales;
    private final int scaleCount;
    
    public OdpGammaProcessSimulator(Random rnd, OdpResidualScale scales) {
        this.rnd = new RndGamma(rnd);
        this.scales = scales.toArray();
        scaleCount = this.scales.length;
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double scale = getScale(development);
        double mean = cik<0d? -cik : cik;
        
        double alpha = mean / scale; //mean ^2 / variance;
        double lambda = 1d / scale; //1d / (variance / mean)
        double random = rnd.nextGamma(alpha, lambda);
        
        return cik < 0d? (random + 2 * cik) : random;
    }
    
    private double getScale(int development) {
        if(development < 0)
            development = 0;
        if(development >= scaleCount)
            development = (scaleCount-1);
        return scales[development];
//        return (0<= development && development < scaleCount)?
//                scales[development] :
//                Double.NaN;
    }
}
