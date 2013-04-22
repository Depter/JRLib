package org.jreserve.jrlib.bootstrap.odp.residuals;

import org.jreserve.jrlib.bootstrap.odp.scale.ConstantOdpResidualScale;
import org.jreserve.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultOdpScaledResidualTriangle extends AbstractTriangle<OdpResidualScale> implements OdpScaledResidualTriangle {

    private int accidents;
    private int developments;
    private double[][] values;

    /**
     * Creates an instance, which uses {@link ConstantOdpResidualScale ConstantOdpResidualScale}
     * and {@link AdjustedOdpResidualTriangle AdjustedOdpResidualTriangle}
     * to calculate and scale the residuals.
     * 
     * @throws NullPointerException if `lrs` is null.
     */
    public DefaultOdpScaledResidualTriangle(LinkRatio lrs) {
        this(new AdjustedOdpResidualTriangle(lrs));
    }

    /**
     * Creates an instance, which uses {@link ConstantOdpResidualScale ConstantOdpResidualScale}
     * to scale the residuals in the input triangle.
     * 
     * @throws NullPointerException if `residuals` is null.
     */
    public DefaultOdpScaledResidualTriangle(OdpResidualTriangle residuals) {
        this(new ConstantOdpResidualScale(residuals));
    }
    
    /**
     * Creates an instance for the given scales.
     * 
     * @throws NullPointerException if `scales` is null.
     */
    public DefaultOdpScaledResidualTriangle(OdpResidualScale scales) {
        super(scales);
        doRecalculate();
    }

    @Override
    public OdpResidualScale getSourceOdpResidualScales() {
        return source;
    }

    @Override
    public OdpResidualTriangle getSourceOdpResidualTriangle() {
        return source.getSourceOdpResidualTriangle();
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
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return withinBounds(accident)? values[accident].length : 0;
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
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                values[accident][development] :
                Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        OdpResidualTriangle residuals = source.getSourceOdpResidualTriangle();
        accidents = residuals.getAccidentCount();
        developments = residuals.getDevelopmentCount();
        values = residuals.toArray();
        
        for(int a=0; a<accidents; a++) {
            int devs = values[a].length;
            for(int d=0; d<devs; d++)
                values[a][d] /= source.getValue(d);
        }
    }
}
