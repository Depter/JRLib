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

import org.jreserve.jrlib.CalculationState;

/**
 * A triangle correction replaces the value of one cell
 * in the input triangle wiht a custom value.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrection<T extends Triangle> extends AbstractTriangleModification<T> {
    
    private int accidents;
    
    protected final int accident;
    protected final int development;
    protected double correction;
    
    /**
     * Creates an instance which corrigates the given cell with 
     * the given value.
     * 
     * If the given accident/development period falls outside
     * the bound of the source triangle, the NaN will be 
     * returned for the given cell.
     * 
     * @throws NullPointerException if 'source' or `cell` is null.
     */
    public TriangleCorrection(T source, Cell cell, double correction) {
        this(source, cell.getAccident(), cell.getDevelopment(), correction);
    }
    
    /**
     * Creates an instance which corrigates the given cell with 
     * the given value.
     * 
     * If the given accident/development period falls outside
     * the bound of the source triangle, the NaN will be 
     * returned for the given cell.
     */
    public TriangleCorrection(T source, int accident, int development, double correction) {
        super(source);
        this.accidents = source.getAccidentCount();
        this.accident = accident;
        this.development = development;
        this.correction = correction;
    }

    /**
     * Returns the accident period, which is corrigated.
     */
    public int getCorrigatedAccident() {
        return accident;
    }
    
    /**
     * Returns the development period, which is corrigated.
     */
    public int getCorrigatedDevelopment() {
        return development;
    }
    
    /**
     * Returns the value, which is retunred for the corrigated cell.
     */
    public double getCorrigatedValue() {
        return correction;
    }
    
    /**
     * Sets the value for the corrigated cell.
     * 
     * Calling this method fires a change event.
     */
    public void setCorrigatedValue(double correction) {
        setState(CalculationState.INVALID);
        this.correction = correction;
        setState(CalculationState.VALID);
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(shouldCorrigateCell(accident, development))
            return correction;
        return source.getValue(accident, development);
    }
    
    private boolean shouldCorrigateCell(int accident, int development) {
        return withinBounds(accident, development) &&
               this.accident == accident &&
               this.development == development;
    }

    @Override
    protected boolean withinBounds(int accident) {
        return 0<=accident && accident<accidents;
    }
    
    @Override
    protected void recalculateLayer() {
        this.accidents = source.getAccidentCount();
    }

    @Override
    public String toString() {
        return String.format(
            "TriangleCorrection [%d; %d] = %f",
            accident, development, correction);
    }
}
