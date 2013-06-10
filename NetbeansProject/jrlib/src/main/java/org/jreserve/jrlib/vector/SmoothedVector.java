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
package org.jreserve.jrlib.vector;

import org.jreserve.jrlib.vector.smoothing.VectorSmoothing;

/**
 * Modifes a vector by applying some smoothing method on the values
 * of the vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedVector extends AbstractVectorModification {
    
    protected final VectorSmoothing smoothing;
    private double[] values;
    
    /**
     * Creates an instance, with the given source and smoothing.
     * 
     * @throws NullPointerException if `source` or `smoothing` is null.
     */
    public SmoothedVector(Vector source, VectorSmoothing smoothing) {
        super(source);
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        this.smoothing = smoothing;
        
        doRecalculate();
    }

    @Override
    public double getValue(int index) {
        return withinBonds(index)? values[index] : Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
       doRecalculate();
    }
    
    private void doRecalculate() {
        length = source.getLength();
        this.values = smoothing.smooth(source);
    }
}
