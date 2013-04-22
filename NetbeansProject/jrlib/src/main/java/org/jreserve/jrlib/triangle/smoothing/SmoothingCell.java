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
