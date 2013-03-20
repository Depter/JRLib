package org.jreserve.triangle.factor;

import org.jreserve.CalculationData;
import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FixedDevelopmentFactors extends AbstractTriangle<CalculationData> implements FactorTriangle {

    private double[][] factors;
    private int accidents;
    private int developments;
    
    public FixedDevelopmentFactors(double[][] factors) {
        this.accidents = factors==null? 0 : factors.length;
        this.developments = (accidents > 0)? factors[0].length : 0;
        this.factors = TriangleUtil.copy(factors);
    }
    
    public ClaimTriangle getSourceTriangle() {
        return null;
    }

    @Override
    protected void recalculateLayer() {
    }

    public int getAccidentCount() {
        return accidents;
    }

    public int getDevelopmentCount() {
        return developments;
    }

    public int getDevelopmentCount(int accident) {
        return withinBounds(accident)? 
            factors[accident].length :
            0;
    }

    public double getValue(int accident, int development) {
        return withinBounds(accident, development)  ?
               factors[accident][development]       :
               Double.NaN;
    }

    public void setSource(ClaimTriangle source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FactorTriangle copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
