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
package org.jreserve.jrlib.triangle.smoothing;

import org.jreserve.jrlib.triangle.Cell;

/**
 * A SmoothingCells are the input for {@link TriangleSmoothing TriangleSmoothings}.
 * They represents the coordinates, which are the input for the smoothing, and
 * als indicates, wether these cells should be smoothed as well.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingCell extends Cell {

    private final boolean applied;
    
    /**
     * Creates an instance for the given coordinates. If a cell is `applied`
     * that means that the cell have to be smoothed. If the cell is not
     * `applied` than it's value is considered by the smoothing method
     * but the cell itself do not has to be smoothed.
     * 
     * @param accident the accident period.
     * @param development the development period.
     * @param applied smooth or not to smooth.
     */
    public SmoothingCell(int accident, int development, boolean applied) {
        super(accident, development);
        this.applied = applied;
    }

    /**
     * Returns wether this cell should be smoothed, or simply an
     * input for the smoothing method.
     */
    public boolean isApplied() {
        return applied;
    }
    
    @Override
    public String toString() {
        return String.format("SmoothingCell [%d; %d; %s]",
                getAccident(), getDevelopment(), applied);
    }
}
