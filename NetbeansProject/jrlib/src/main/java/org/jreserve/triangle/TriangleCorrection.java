package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrection extends AbstractTriangularCalculationData {

    private final int accident;
    private final int development;
    private final double correction;
    
    public TriangleCorrection(TriangularCalculationData source, int accident, int development, double correction) {
        super(source);
        this.accident = accident;
        this.development = development;
        this.correction = correction;
    }
    
    @Override
    protected void recalculateLayer() {
    }

    public int getAccident() {
        return accident;
    }

    public double getCorrection() {
        return correction;
    }

    public int getDevelopment() {
        return development;
    }

    @Override
    public double getValue(int accident, int development) {
        if(isMyCell(accident, development))
            return correction;
        return super.getValue(accident, development);
    }
    
    private boolean isMyCell(int accident, int development) {
        return withinSourceBound(accident, development) &&
               this.accident == accident &&
               this.development == development;
    }

}
