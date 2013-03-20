package org.jreserve.triangle.factor;

import org.jreserve.triangle.factor.AbstractFactorTriangleModification;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorTriangleCorrection extends AbstractFactorTriangleModification {

    private final int accident;
    private final int development;
    private double value;
    
    public FactorTriangleCorrection(FactorTriangle source, Cell cell, double value) {
        this(source, cell.getAccident(), cell.getDevelopment(), value);
    }
    
    public FactorTriangleCorrection(FactorTriangle source, int accident, int development, double value) {
        super(source);
        this.accident = accident;
        this.development = development;
        this.value = value;
    }

    public int getCorrigatedAccident() {
        return accident;
    }
    
    public int getCorrigatedDevelopment() {
        return development;
    }
    
    public double getCorrigatedValue() {
        return value;
    }
    
    public void setCorrigatedValue(double value) {
        this.value = value;
        fireChange();
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(shouldCorrigateCell(accident, development))
            return value;
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
            "FactorTriangleCorrection [%d; %d] = %f",
            accident, development, value);
    }
    
    @Override
    public FactorTriangleCorrection copy() {
        return new FactorTriangleCorrection(source.copy(), 
                accident, development, 
                value);
    }
}
