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

import org.jreserve.jrlib.CalculationState;

/**
 * A vector correction replaces the value of one cell
 * in the input vector wiht a custom value.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorCorrection extends AbstractVectorModification {

    protected final int index;
    protected double correction;
    
    /**
     * Creates an instance which corrigates the given cell with 
     * the given value.
     * 
     * If the given index falls outside the bound of the source 
     * vector, the NaN will be returned for the given cell.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public VectorCorrection(Vector source, int index, double correction) {
        super(source);
        this.length = source.getLength();
        this.index = index;
        this.correction = correction;
    }

    /**
     * Returns the index, which is corrigated.
     */
    public int getCorrigatedIndex() {
        return index;
    }
    
    /**
     * Returns the value, which is retunred for the corrigated index.
     */
    public double getCorrigatedValue() {
        return correction;
    }
    
    /**
     * Sets the value for the corrigated index.
     * 
     * Calling this method fires a change event.
     */
    public void setCorrigatedValue(double correction) {
        setState(CalculationState.INVALID);
        this.correction = correction;
        setState(CalculationState.VALID);
    }
    
    @Override
    public double getValue(int index) {
        if(shouldCorrigate(index))
            return correction;
        return source.getValue(index);
    }
    
    private boolean shouldCorrigate(int index) {
        return this.index == index && withinBonds(index);
    }
    
    @Override
    protected void recalculateLayer() {
        length = source.getLength();
    }

    @Override
    public String toString() {
        return String.format("VectorCorrection [%d] = %f", index, correction);
    }
}
