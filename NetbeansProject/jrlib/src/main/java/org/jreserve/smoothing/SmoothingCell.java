package org.jreserve.smoothing;

import org.jreserve.triangle.Cell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingCell extends Cell {

    private final boolean applied;
    
    public SmoothingCell(int accident, int development, boolean applied) {
        super(accident, development);
        this.applied = applied;
    }

    public boolean isApplied() {
        return applied;
    }
    
    @Override
    public String toString() {
        return String.format("SmoothingCell [%d; %d; %s]",
                getAccident(), getDevelopment(), applied);
    }
}
