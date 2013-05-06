package org.jreserve.jrlib.vector;

/**
 * A vector correction replaces the value of one cell
 * in the input vector wiht a custom value.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class VectorCorrection extends AbstractVectorModification {

    protected final int index;
    protected double correction;
    
    /**
     * Creates an instance which corrigates the given cell with 
     * the given value.
     * 
     * If the given index falls outside the bound of the source 
     * vector, the NaN will be returned for the given cell.
     * 
     * @throws NullPointerException if 'source' is null.
     */
    public VectorCorrection(Vector source, int index, double correction) {
        super(source);
        this.index = index;
        this.correction = correction;
    }

    /**
     * Returns the index, which is corrigated.
     */
    public int getCorrigatedIndex() {
        return index;
    }
    
    /**
     * Returns the value, which is retunred for the corrigated index.
     */
    public double getCorrigatedValue() {
        return correction;
    }
    
    /**
     * Sets the value for the corrigated index.
     * 
     * Calling this method fires a change event.
     */
    public void setCorrigatedValue(double correction) {
        this.correction = correction;
        fireChange();
    }
    
    @Override
    public double getValue(int index) {
        if(shouldCorrigate(index))
            return correction;
        return source.getValue(index);
    }
    
    private boolean shouldCorrigate(int index) {
        return this.index == index && withinBonds(index);
    }
    
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public String toString() {
        return String.format("VectorCorrection [%d] = %f", index, correction);
    }
}
