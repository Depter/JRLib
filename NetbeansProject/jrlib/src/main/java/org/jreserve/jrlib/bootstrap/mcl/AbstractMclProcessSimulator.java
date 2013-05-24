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
package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMclProcessSimulator implements MclProcessSimulator {
    
    protected boolean isPaid;
    protected MclBootstrapEstimateBundle bundle;
    private LinkRatioScale scales;
    private double[] originalScales;
    private int[] devIndices;
    
    protected AbstractMclProcessSimulator(LinkRatioScale scales, boolean isPaid) {
        this.isPaid = isPaid;
        this.scales = scales;
        originalScales = scales.toArray();
    }
    
    @Override
    public void setBundle(MclBootstrapEstimateBundle bundle) {
        if(bundle == null)
            throw new NullPointerException("Bundle is null!");
        this.bundle = bundle;
        initDevIndices();
    }
    
    private void initDevIndices() {
        int accidents = bundle.getAccidentCount();
        devIndices = new int[accidents];
        for(int a=0; a<accidents; a++)
            devIndices[a] = bundle.getObservedDevelopmentCount(a);
    }

    public double simulateEstimate(double cik, int accident, int development) {
        double variance = getVariance(accident, development);
        return random(cik, variance);
    }

    protected abstract double getVariance(int accident, int development);
    
    /**
     * Returns the scale parameter for the variance.
     */
    protected double getScale(int accident, int development) {
        if(0 <= development && development <= devIndices[accident])
            return originalScales[development];
        return scales.getValue(development);
    }
    
    /**
     * Extending clases should generate a random value with the given mean
     * and variance.
     */
    protected abstract double random(double mean, double variance);
}
