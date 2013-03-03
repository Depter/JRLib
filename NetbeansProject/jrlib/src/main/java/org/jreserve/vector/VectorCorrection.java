package org.jreserve.vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorCorrection extends AbstractVectorModification {

    private final int index;
    private final double correction;
    
    public VectorCorrection(Vector source, int index, double correction) {
        super(source);
        this.index = index;
        this.correction = correction;
    }
    
    @Override
    protected void recalculateLayer() {
    }

    public int getIndex() {
        return index;
    }

    public double getCorrection() {
        return correction;
    }

    @Override
    public double getValue(int index) {
        if(isMyCell(index))
            return correction;
        return super.getValue(index);
    }
    
    private boolean isMyCell(int index) {
        return withinBound(index) &&
               this.index == index;
    }
    
    @Override
    public String toString() {
        return String.format(
            "VectorCorrection [%d] = %f",
            index, correction);
    }
}
