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
package org.jreserve.jrlib.triangle;

import org.jreserve.jrlib.triangle.smoothing.TriangleSmoothing;

/**
 * Modifes a triangle by applying some smoothing method on the values
 * of the triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothedTriangle<T extends Triangle> extends AbstractTriangleModification<T> {

    protected final TriangleSmoothing smoothing;
    private int accidents;
    private double[][] values;
    
    /**
     * Creates an instance, with the given source and smoothing.
     * 
     * @throws NullPointerException if `source` or `smoothing` is null.
     */
    public SmoothedTriangle(T source, TriangleSmoothing smoothing) {
        super(source);
        if(smoothing == null)
            throw new NullPointerException("Smoothing is null!");
        this.smoothing = smoothing;
        doRecalculate();
    }
    
    /**
     * Returns the smoothing used by this instance.
     */
    public TriangleSmoothing getSmoothing() {
        return smoothing;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        this.values = smoothing.smooth(source);
        this.accidents = this.values.length;
    }
    
    @Override
    protected boolean withinBounds(int accident) {
        return 0 <= accident && accident < accidents;
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
}
