package org.jreserve.linkratio.scale;

import static java.lang.Double.isNaN;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.factor.FactorTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScaleCaclulator extends AbstractCalculationData<LinkRatio> implements LinkRatioScale {

    private int developments;
    private double[] sigmas;

    public LinkRatioScaleCaclulator(LinkRatio source) {
        super(source);
        doRecalculate();
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source;
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    @Override
    public int getDevelopmentCount() {
        return developments;
    }
    
    @Override
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return sigmas[development];
    }
    
    @Override
    public double[] toArray() {
        double[] result = new double[developments];
        System.arraycopy(sigmas, 0, result, 0, developments);
        return result;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        developments = source==null? 0 : source.getDevelopmentCount();
        sigmas = new double[developments];
        for(int d=0; d<developments; d++)
            sigmas[d] = calculateScaleParameter(d);
    }
    
    private double calculateScaleParameter(int development) {
        double lr = source.getValue(development);
        if(isNaN(lr)) return Double.NaN;
        
        int n = 0;
        double sDik = 0d;
        
        FactorTriangle factors = source.getSourceFactors();
        int accidents = factors.getAccidentCount();
        
        for(int a=0; a<accidents; a++) {
            double w = source.getWeight(a, development);
            double f = factors.getValue(a, development);
            if(!isNaN(w) && !isNaN(f)) {
                n++;
                sDik += w * pow(lr - f, 2d);
            }
        }
        
        return (--n <= 0 || !(sDik > 0d))?  Double.NaN : sqrt(sDik/(double)n);
    }
    
    @Override
    public LinkRatioScaleCaclulator copy() {
        return new LinkRatioScaleCaclulator(source.copy());
    }
}
