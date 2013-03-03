package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleGeometry {
    
    private int accidents;
    private int developments;
    private int accidentLength;

    public TriangleGeometry(int accidents, int developments, int accidentLength) {
        setAccidents(accidents);
        setDevelopments(developments);
        setAccidentLengts(accidentLength);
    }
    
    private void setAccidents(int accidents) {
        if(accidents < 0)
            accidents = 0;
        this.accidents = accidents;
    }
    
    private void setDevelopments(int developments) {
        if(developments < 0)
            developments = 0;
        this.developments = developments;
    }
    
    private void setAccidentLengts(int length) {
        if(length < 0)
            length = 0;
        this.accidentLength = length;
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
