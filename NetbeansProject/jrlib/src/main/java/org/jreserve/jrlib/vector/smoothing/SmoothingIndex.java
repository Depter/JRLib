package org.jreserve.jrlib.vector.smoothing;

/**
 * Utility class to indicate input cells for 
 * {@link VectorSmoothing VectorSmoothings}.
 * 
 * @author Peter Decsi
 */
public class SmoothingIndex implements Comparable<SmoothingIndex>{
    
    private final int index;
    private final boolean applied;
    
    /**
     * Creates a new instance.
     * 
     * @param index the index of the cell
     * @param applied idnicates wether the cell should be smoothed, ot
     * it is simply an input cell for the smoothing.
     */
    public SmoothingIndex(int index, boolean applied) {
        this.index = index;
        this.applied = applied;
    }
    
    /**
     * Returns the index of the cell.
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * Returns wether the cell should be smoothed, or
     * it is simply an input for the smoothing.
     */
    public boolean isApplied() {
        return applied;
    }
    
    @Override
    public int compareTo(SmoothingIndex index) {
        return this.index - index.index;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof SmoothingIndex) &&
               compareTo((SmoothingIndex)o) == 0;
    }
    
    @Override
    public int hashCode() {
        return index;
    }
    
    @Override
    public String toString() {
        return String.format("SmoothingIndex [%d; %s]", index, applied);
    }
}
