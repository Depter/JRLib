package org.jreserve.jrlib.triangle;

/**
 * The TriangleGeometry class represents the structure of a triangle. The
 * structure of a triangle is represented by 3 parameters:
 * 1.  Number of accident periods.
 * 2.  Number of development periods
 * 3.  Difference of development periods between subsequent accident periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleGeometry {
    
    private int accidents;
    private int developments;
    private int accidentLength;

    /**
     * Creates an instance with the given parameters. If `accidents`
     * or `developments `is less then 0, it will be set to 0.
     * 
     * @throws IllegalArgumentException If a {@link Triangle Triangle} with
     * this geometry would not comply with the Triangle's contract.
     */
    public TriangleGeometry(int accidents, int developments, int accidentLength) {
        this.accidents = accidents;
        this.developments = developments;
        this.accidentLength = accidentLength;
        validate();
    }
    
    private void validate() {
        if(accidents < 0) throw new IllegalArgumentException("Number of accident periods can not be less then 0!" + accidents);
        if(developments < 0) throw new IllegalArgumentException("Number of development periods can not be less then 0!" + developments);
        if(accidentLength < 1) throw new IllegalArgumentException("AccidentLength can not be less tehn 1!" + accidentLength);
        if(accidents==0 && developments>0)
            throw new IllegalArgumentException("A triangle with no accident periods can not have development periods! "+this);
        if(developments - (accidents-1)*accidentLength <= 0)
            throw new IllegalArgumentException("A triangle would have accident period(s) with no development periods! "+this);
    }

    public int getAccidents() {
        return accidents;
    }

    public int getDevelopments() {
        return developments;
    }

    public int getAccidentLength() {
        return accidentLength;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof TriangleGeometry) &&
               equals((TriangleGeometry) o);
    }
    
    public boolean equals(TriangleGeometry tg) {
        return tg != null &&
               accidents == tg.accidents &&
               developments == tg.developments &&
               accidentLength == tg.accidentLength;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + accidents;
        hash = 17 * hash + developments;
        return 17 * hash + accidentLength;
    }
    
    @Override
    public String toString() {
        return String.format(
            "TriangleGeometry [%d; %d; %d]",
            accidents, developments, accidentLength);
    }
}
