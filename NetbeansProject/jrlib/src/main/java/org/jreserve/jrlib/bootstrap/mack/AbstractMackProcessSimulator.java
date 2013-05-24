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

import org.jreserve.jrlib.estimate.Estimate;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMackProcessSimulator implements MackProcessSimulator {

    private Estimate estimate;
    private LinkRatioScale scales;
    private final double[] originalScales;
    
    protected AbstractMackProcessSimulator(LinkRatioScale scales) {
        this.scales = scales;
        this.originalScales = scales.toArray();
    }

    @Override
    public void setEstimate(Estimate estimate) {
        if(estimate == null)
            throw new NullPointerException("Estimate is null!");
        this.estimate = estimate;
    }
    
    @Override
    public double simulateEstimate(double cik, int accident, int development) {
        double variance = getVariance(accident, development);
        return random(cik, variance);
    }

    /**
     * Calculates the variance as `C(a,d-1) * sigma(d-1)^2`.
     * 
     * @see MackProcessSimulator
     */
    protected double getVariance(int accident, int development) {
        int d = development - 1;
        double cik = getClaim(accident, d);
        double scale = getScale(accident, d);
        return cik * scale * scale;
    }
    
    private double getClaim(int accident, int development) {
        double cik = estimate.getValue(accident, development);
        return cik < 0d? -cik : cik;
    }
    
    /**
     * Returns the scale parameter for the variance.
     */
    protected double getScale(int accident, int development) {
        if(0 <= development && development <= estimate.getObservedDevelopmentCount(accident))
            return originalScales[development];
        return scales.getValue(development);
    }
    
    /**
     * Extending clases should generate a random value with the given mean
     * and variance.
     */
    protected abstract double random(double mean, double variance);
}
