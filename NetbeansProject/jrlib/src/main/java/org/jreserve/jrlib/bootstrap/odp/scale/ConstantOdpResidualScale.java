package org.jreserve.jrlib.bootstrap.odp.scale;

import org.jreserve.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle;
import org.jreserve.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.vector.AbstractVector;

/**
 * Calculates a constant scale for all residuals in the residual triangle.
 * The formula used is:
 *                sum(r(a,d)^2)
 *      scale^2 = -------------
 *                     N
 * where:
 * -   `r(a,d)` is the residual for accident period `a` and development period `d`.
 * -   `N` is the number of non NaN residuals.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ConstantOdpResidualScale extends AbstractVector<OdpResidualTriangle> implements OdpResidualScale {

    private int developments;
    private double scale;

    /**
     * Creates a new instance for the given source. The instance
     * will use an {@link AdjustedOdpResidualTriangle AdjustedOdpResidualTriangle}
     * as a residual triangle.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public ConstantOdpResidualScale(LinkRatio source) {
        this(new AdjustedOdpResidualTriangle(source));
    }

    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public ConstantOdpResidualScale(OdpResidualTriangle source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public OdpResidualTriangle getSourceOdpResidualTriangle() {
        return source;
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }
    
    @Override 
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    @Override
    public double getFittedValue(int accident, int development) {
        return source.getFittedValue(accident, development);
    }
    
    @Override
    public double[][] toArrayFittedValues() {
        return source.toArrayFittedValues();
    }

    @Override
    public int getLength() {
        return developments;
    }

    @Override
    public double getValue(int index) {
        return scale;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        developments = source.getDevelopmentCount();
        int n = 0;
        double sRs = 0d;
        
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double r = source.getValue(a, d);
                if(!Double.isNaN(r)) {
                    n++;
                    sRs += r * r;
                }
            }
        }
        scale = n==0? Double.NaN : Math.sqrt(sRs/(double)n);
    }
}
