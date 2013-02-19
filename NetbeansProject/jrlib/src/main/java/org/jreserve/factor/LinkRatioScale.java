package org.jreserve.factor;

import static java.lang.Double.isNaN;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioScale extends AbstractCalculationData<LinkRatio> {

    private int developments;
    private double[] sigmas;
    private Triangle cik;

    public LinkRatioScale(LinkRatio source, Triangle cik) {
        super(source);
        initCikTriangle(cik);
        doRecalculate();
    }
    
    private void initCikTriangle(Triangle cik) {
        if(cik == null)
            throw new NullPointerException("C(i,k) triangle is null!");
        this.cik = cik;
    }
    
    public Triangle getCikTriangle() {
        return cik;
    }
    
    public void setCikTriangle(Triangle cik) {
        initCikTriangle(cik);
        doRecalculate();
        fireChange();
    }
    
    public int getDevelopmentCount() {
        return developments;
    }
    
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return sigmas[development];
    }
    
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
        
        double alpha = source.getMackAlpha(development);
        if(isNaN(alpha)) return Double.NaN;
        
        int n = 0;
        double sDik = 0d;
        
        Triangle factors = source.getDevelopmentFactors();
        int accidents = factors.getAccidentCount();
        
        for(int a=0; a<accidents; a++) {
            double c = cik.getValue(a, development);
            double f = factors.getValue(a, development);
            if(!isNaN(c) && !isNaN(f)) {
                n++;
                sDik += pow(c, alpha) * pow(lr - f, 2d);
            }
        }
        
        return (--n <= 0 || !(sDik > 0d))?  Double.NaN : sqrt(sDik/(double)n);
    }
}
