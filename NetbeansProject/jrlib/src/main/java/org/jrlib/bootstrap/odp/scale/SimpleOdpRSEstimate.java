package org.jrlib.bootstrap.odp.scale;

import org.jrlib.bootstrap.odp.residuals.OdpResidualTriangle;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 * Simple estimator for the ODP residual scales, which fills in all
 * NaNs with the same method.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleOdpRSEstimate extends AbstractSimpleMethodSelection<OdpResidualScale, OdpRSMethod> implements OdpResidualScale {
    
    private int developments;
    
    /**
     * Creates an instance for the given source, using the given method to
     * fill NaN values.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleOdpRSEstimate(OdpResidualScale source, OdpRSMethod method) {
        super(source, 
              (method instanceof DefaultOdpRSMethod)? method : new DefaultOdpRSMethod(),
              method);
        super.recalculateLayer();
    }
    
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
    public double getFittedValue(int accident, int development) {
        return source.getFittedValue(accident, development);
    }

    @Override
    public double[][] toArrayFittedValues() {
        return source.toArrayFittedValues();
    }

    @Override
    protected void initCalculation() {
        developments = source.getLength();
    }
    
    @Override
    public int getLength() {
        return developments;
    }
}
