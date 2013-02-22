package org.jreserve.factor.standarderror;

import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSEFunction implements LinkRatioSEFunction {
    
    private int developments = 0;
    private double[] fses = new double[0];
    
    private Triangle cik;
    private FactorTriangle fik;
    private LinkRatio lrk;
    private LinkRatioScale scales;
    private int accidents;
    
    @Override
    public void fit(LinkRatioScale scales) {
        initState(scales);
        for(int d=0; d<developments; d++)
            fses[d] = calculateSE(d);
        clearState();
    }
    
    private void initState(LinkRatioScale scales) {
        this.scales = scales;
        developments = scales.getDevelopmentCount();
        fses = new double[developments];
        lrk = scales.getLinkRatios();
        fik = lrk.getInputFactors();
        accidents = fik.getAccidentCount();
        cik = fik.getInputTriangle();
    }
    
    private double calculateSE(int development) {
        double scale = scales.getValue(development);
        if(Double.isNaN(scale))
            return Double.NaN;
        
        double cikAlpha = calculateCikAlpha(development);
        if(Double.isNaN(cikAlpha) || cikAlpha <= 0d)
            return Double.NaN;
        
        return Math.sqrt(Math.pow(scale, 2d) / cikAlpha);
    }
    
    private double calculateCikAlpha(int development) {
        double alpha = lrk.getMackAlpha(development);
        if(Double.isNaN(alpha))
            return Double.NaN;
        
        double sum = 0d;
        int n = 0;
        
        for(int a=0; a<accidents; a++) {
            double f = fik.getValue(a, development);
            double c = cik.getValue(a, development);
            if(!Double.isNaN(f) && !Double.isNaN(c)) {
                n++;
                sum += Math. pow(c, alpha);
            }
        }
        
        return (n==0)? Double.NaN : sum;
    }
    
    private void clearState() {
        accidents = 0;
        scales = null;
        lrk = null;
        fik = null;
        cik = null;
    }
    
    @Override
    public double getValue(int development) {
        if(development < 0 || development >= developments)
            return Double.NaN;
        return fses[development];
    }
}
