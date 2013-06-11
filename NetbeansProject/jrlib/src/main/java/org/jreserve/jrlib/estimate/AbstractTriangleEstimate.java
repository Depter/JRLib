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

import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * Abstract class, that implements some 
 * {@link ClaimTriangle ClaimTriangle} related methods, declared
 * by {@link AbstractEstimate AbstractEstimate} and
 * {@link AbstractProcessSimulatorEstimate AbstractProcessSimulatorEstimate}
 * classes.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleEstimate<T extends CalculationData>
    extends AbstractProcessSimulatorEstimate<T> {

    protected ClaimTriangle triangle;

    protected AbstractTriangleEstimate(T source, ClaimTriangle triangle) {
        super(source);
        if(triangle == null)
            throw new NullPointerException("Triangle can not be null!");
        this.triangle = triangle;
    }
    
    @Override
    public int getObservedDevelopmentCount(int accident) {
        return triangle.getDevelopmentCount(accident);
    }

    @Override
    protected double getObservedValue(int accident, int development) {
        return triangle.getValue(accident, development);
    }
    
    @Override
    public void detach(CalculationData source) {
        super.detach(source);
        this.triangle = null;
    }
}
