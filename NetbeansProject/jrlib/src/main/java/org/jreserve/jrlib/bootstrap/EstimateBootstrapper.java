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

import org.jreserve.jrlib.estimate.Estimate;
import org.jreserve.jrlib.CalculationData;

/**
 * An isntance of EstimateBootstrapper bootstraps {@link Estimate Estimate}
 * instances. The input estimate should be detached (do not fire event
 * at recalculations), to increas speed.
 * 
 * By each call to {@link #bootstrap() bootstrap()} the class performs two
 * steps:
 * 1.  Call {@link Estimate#recalculate() recalculate()} on the estimate.
 * 2.  Call {@link Estimate#toArrayReserve() toArrayReserve()} on the estimate
 *     to store the reserves for the bootstrap iteration.
 * This means that the provided estimate should take care of calculating the 
 * process variance, parameter variance and other bootstrap related tasks.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class EstimateBootstrapper<T extends CalculationData> extends Bootstrapper<T> {
    
    protected final Estimate estimate;
    protected final int devCount;
    protected final int accidents;
    protected final int[] observedDevCount;
    protected double [][] reserves;
    private int iteration = 0;
    
    public EstimateBootstrapper(T source, int bootstrapCount, Estimate estimate) {
        super(source, bootstrapCount);
        this.estimate = estimate;
        
        devCount = estimate.getDevelopmentCount();
        accidents = estimate.getAccidentCount();
        observedDevCount = new int[accidents];
        for(int a=0; a<accidents; a++)
            observedDevCount[a] = estimate.getObservedDevelopmentCount(a);
        reserves = new double[bootstrapCount][];
    }
    
    /**
     * Recalculates the {@link Estimate Estimate} instance and
     * stores the reserves from the iteration.
     */
    @Override
    protected void collectBootstrapResult() {
        reserves[iteration++] = estimate.toArrayReserve();
    }
    
    /**
     * Retunrs the bootstrapped reserve estimates. The returned
     * array has the following dimensions: `reserve[bootstrapCount][accidents]`,
     * where `bootstrapCount` can be obtained from 
     * {@link Bootstrapper#getBootstrapCount() getBootstrapCount()} and
     * `accdents` can be obtained from
     * {@link Estimate#getAccidentCount() source.getAccidentCount()}.
     */
    public double[][] getReserves() {
        return reserves;
    }
}
