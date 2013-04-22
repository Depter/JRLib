package org.jreserve.jrlib.triangle;

/**
 * Utility class for accessing triangle values. This unmodifiable
 * class simply represents coordinates within triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class Cell implements Comparable<Cell> {
    
    private final int accident;
    private final int development;
    
    /**
     * Creates an instance, representing the given coordinates.
     */
    public Cell(int accident, int development) {
        this.accident = accident;
        this.development = development;
    }

    /**
     * Returns the accident period of the represented cell.
     */
    public int getAccident() {
        return accident;
    }
    
    /**
     * Returns the development period of the represented cell.
     */
    public int getDevelopment() {
        return development;
    }
    
    /**
     * Returns an array of length 2, where the first element is
     * the <i>accident</i> the second is the <i>development</i>
     * period. Changing the returned array does not affect
     * the inner state of the obect.
     */
    public int[] toArray() {
        return new int[] {accident, development};
    }

    /**
     * Compares this instance to another cell, first based on their accident
     * then the development periods. A smaller coordinate is a smaller instance.
     */
    @Override
    public int compareTo(Cell o) {
        int dif = accident - o.accident;
        if(dif == 0)
            return development - o.development;
        return dif;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Cell)
            return compareTo((Cell)o) == 0;
        return false;
    }
    
    /**
     * Compares this instance to another cell, first based on their accident
     * then the development periods. A smaller coordinate is a smaller instance.
     */
    public boolean equals(int accident, int development) {
        return this.accident == accident &&
               this.development == development;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + accident;
        return 17 * hash + development;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Cell [%d; %d]",
            accident, development);
    }
}
