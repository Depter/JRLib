package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Cell implements Comparable<Cell> {
    
    private final int accident;
    private final int development;

    public Cell(int accident, int development) {
        this.accident = accident;
        this.development = development;
    }

    public int getAccident() {
        return accident;
    }

    public int getDevelopment() {
        return development;
    }
    
    public int[] toArray() {
        return new int[] {accident, development};
    }

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