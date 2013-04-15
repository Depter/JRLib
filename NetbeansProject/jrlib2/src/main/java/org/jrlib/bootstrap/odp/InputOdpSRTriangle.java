package org.jrlib.bootstrap.odp;

import org.jrlib.bootstrap.odp.residuals.AdjustedOdpResidualTriangle;
import org.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jrlib.bootstrap.odp.scale.ConstantOdpResidualScale;
import org.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.AbstractTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputOdpSRTriangle extends AbstractTriangle<OdpResidualScale> implements OdpSRTriangle {
    
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
    public InputOdpSRTriangle(LinkRatio lrs) {
        this(new AdjustedOdpResidualTriangle(lrs));
    }

    /**
     * Creates an instance, which uses {@link ConstantOdpResidualScale ConstantOdpResidualScale}
     * to scale the residuals in the input triangle.
     * 
     * @throws NullPointerException if `residuals` is null.
     */
    public InputOdpSRTriangle(OdpResidualTriangle residuals) {
        this(new ConstantOdpResidualScale(residuals));
    }
    
    /**
     * Creates an instance for the given scales.
     * 
     * @throws NullPointerException if `scales` is null.
     */
    public InputOdpSRTriangle(OdpResidualScale scales) {
        super(scales);
        doRecalculate();
    }

    @Override
    public OdpResidualScale getSourceOdpResidualScales() {
        return source;
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
        OdpResidualTriangle residuals = source.getSourceResiduals();
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
