package org.jrlib.bootstrap.odp.scale;

import org.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle;
import org.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.vector.AbstractVector;

/**
 * Calculates a separate scale factor for all development periods
 * in the residual triangle.
 * The formula used is:
 *                   sum(r(a,d)^2)
 *      scale(d)^2 = -------------
 *                       N(d)
 * where:
 * -   `r(a,d)` is the residual for accident period `a` and development period `d`.
 * -   `N(d)` is the number of non NaN residuals in development period `d`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class VariableOdpResidualScale extends AbstractVector<OdpResidualTriangle> implements OdpResidualScale {

    private int developments;
    private double[] values;
    
    /**
     * Creates a new instance for the given source. The instance
     * will use an {@link AdjustedOdpResidualTriangle AdjustedOdpResidualTriangle}
     * as a residual triangle.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public VariableOdpResidualScale(LinkRatio source) {
        this(new AdjustedOdpResidualTriangle(source));
    }

    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public VariableOdpResidualScale(OdpResidualTriangle source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public OdpResidualTriangle getSourceResiduals() {
        return source;
    }
    
    @Override
    public int getLength() {
        return developments;
    }

    @Override
    public double getValue(int development) {
        return withinBonds(development)? values[development] : Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        developments = source.getDevelopmentCount();
        values = new double[developments];
        for(int d=0; d<developments; d++)
            values[d] = calculateScale(d);
    }

    private double calculateScale(int development) {
        int accidents = source.getAccidentCount();
        int n = 0;
        double sRs = 0d;
        for(int a=0; a<accidents; a++) {
            double r = source.getValue(a, development);
            if(!Double.isNaN(r)) {
                sRs += r*r;
                n++;
            }
        }
        
        return n==0? Double.NaN : Math.sqrt(sRs / (double)n);
    }
}
