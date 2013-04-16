package org.jrlib.bootstrap.odp.residuals;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.AbstractTriangleModification;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * Adjusts the input residuals in order to compensate for
 * the bootstrap bias.
 * 
 * The adjustment factor `a` is calculated with the following formula:
 *               N
 *     adj^2 = -----
 *             N - p
 * where:
 * -   `N` is the number of cells, where the residual is not NaN.
 * -   `p` is equals to the number of accident periods in the source
 *     claims triangle, plus the number of development periods in
 *     the source triangle plus 1
 *     ({@link ClaimTriangle#getAccidentCount() cik.getAccidentCount()} + {@link ClaimTriangle#getDevelopmentCount() cik.getDevelopmentCount()} - 1).
* 
 * @author Peter Decsi
 * @version 1.0
 */
public class AdjustedOdpResidualTriangle 
    extends AbstractTriangleModification<OdpResidualTriangle> 
    implements ModifiedOdpResidualTriangle {
    
    private double adjustment;
    
    /**
     * Creates an instance for the given link-ratios.
     * 
     * @throws NullPointerException if `source` is null.
     * @see InputOdpResidualTriangle#InputOdpResidualTriangle(LinkRatio) 
     */
    public AdjustedOdpResidualTriangle(LinkRatio lrs) {
        this(new InputOdpResidualTriangle(lrs));
    }
    
    /**
     * Creates a new instance for the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AdjustedOdpResidualTriangle(OdpResidualTriangle source) {
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
    protected void recalculateLayer() {
        doRecalculate();
    }

    @Override
    public double getValue(int accident, int development) {
        double v = source.getValue(accident, development);
        return Double.isNaN(v)? Double.NaN : adjustment * v;
    }
    
    private void doRecalculate() {
        int accidents = source.getAccidentCount();
        int n = 0;
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount();
            for(int d=0; d<devs; d++)
                if(!Double.isNaN(source.getValue(a, d)))
                    n++;
        }
        
        double p = accidents + source.getDevelopmentCount() - 1;
        double dN = (double)n;
        adjustment = Math.sqrt(dN / (dN - p));
    }
    
}
