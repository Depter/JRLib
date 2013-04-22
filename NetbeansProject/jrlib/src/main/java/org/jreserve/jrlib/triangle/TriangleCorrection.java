package org.jreserve.jrlib.triangle;

/**
 * A triangle correction replaces the value of one cell
 * in the input triangle wiht a custom value.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrection<T extends Triangle> extends AbstractTriangleModification<T> {

    protected final int accident;
    protected final int development;
    protected double correction;
    
    /**
     * Creates an instance which corrigates the given cell with 
     * the given value.
     * 
     * If the given accident/development period falls outside
     * the bound of the source triangle, the NaN will be 
     * returned for the given cell.
     * 
     * @throws NullPointerException if 'source' or `cell` is null.
     */
    public TriangleCorrection(T source, Cell cell, double correction) {
        this(source, cell.getAccident(), cell.getDevelopment(), correction);
    }
    
    /**
     * Creates an instance which corrigates the given cell with 
     * the given value.
     * 
     * If the given accident/development period falls outside
     * the bound of the source triangle, the NaN will be 
     * returned for the given cell.
     */
    public TriangleCorrection(T source, int accident, int development, double correction) {
        super(source);
        this.accident = accident;
        this.development = development;
        this.correction = correction;
    }

    /**
     * Returns the accident period, which is corrigated.
     */
    public int getCorrigatedAccident() {
        return accident;
    }
    
    /**
     * Returns the development period, which is corrigated.
     */
    public int getCorrigatedDevelopment() {
        return development;
    }
    
    /**
     * Returns the value, which is retunred for the corrigated cell.
     */
    public double getCorrigatedValue() {
        return correction;
    }
    
    /**
     * Sets the value for the corrigated cell.
     * 
     * Calling this method fires a change event.
     */
    public void setCorrigatedValue(double correction) {
        this.correction = correction;
        fireChange();
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(shouldCorrigateCell(accident, development))
            return correction;
        return source.getValue(accident, development);
    }
    
    private boolean shouldCorrigateCell(int accident, int development) {
        return withinBounds(accident, development) &&
               this.accident == accident &&
               this.development == development;
    }
    
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public String toString() {
        return String.format(
            "TriangleCorrection [%d; %d] = %f",
            accident, development, correction);
    }
}
