package org.jrlib.linkratio.standarderror;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.vector.AbstractVector;

/**
 * LinkRatioSECalculator calculates the standard error of the link-ratios.
 * The formula to calculate the standard error `se(d)` for development 
 * period `d`:
 *                  s(d)^2
 *      se(d)^2 = -----------
 *                sum(w(a,d))
 * where:
 * -   `s(d)` is the scale parameter for development period `d` as defined 
 *     in {@link LinkRatioScale LinkRatioScale}.
 * -   `w(a,d)` is the weight used to calculate the link-ratios, as defined in
 *     {@link LinkRatio#getWeight(int, int) LinkRatio}.
 * 
 * If `s(d)` is NaN, then `se(d)` is also NaN. If one weiht is NaN, then it
 * is ignored during the calculation.
 * 
 * The LinkRatioSECalculator has the same length as it's source 
 * {@link LinkRatioScale LinkRatioScale}.
 * 
 * @see "Mack [1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioSECalculator extends AbstractVector<LinkRatioScale> implements LinkRatioSE {

    private int developments;
    private double[] values;
    
    public LinkRatioSECalculator(LinkRatioScale source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public LinkRatioScale getSourceLRScales() {
        return source;
    }
    
    @Override
    public LinkRatioScaleInput getSourceLrScaleInput() {
        return source.getSourceInput();
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceInput().getSourceLinkRatios();
    }
    
    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceInput().getSourceFactors();
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceInput().getSourceTriangle();
    }

    @Override
    public int getLength() {
        return developments;
    }

    @Override
    public double getValue(int development) {
        if(development < developments && development >= 0)
            return values[development];
        return Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    //Temp fields for calculations, cleared after values are calculated
    private LinkRatio lrk;
    private int accidents;
    
    private void doRecalculate() {
        initState();
        for(int d=0; d<developments; d++)
            values[d] = calculateSE(d);
        clearState();
    }
    
    private void initState() {
        developments = source.getLength();
        values = new double[developments];
        lrk = getSourceLinkRatios();
        accidents = getSourceFactors().getAccidentCount();
    }
    
    private double calculateSE(int development) {
        double scale = source.getValue(development);
        if(Double.isNaN(scale))
            return Double.NaN;
        
        double sw = getSumOfWeights(development);
        if(sw <= 0d)
            return Double.NaN;
        
        return Math.sqrt(Math.pow(scale, 2d) / sw);
    }
    
    private double getSumOfWeights(int development) {
        double sw = 0d;
        for(int a=0; a<accidents; a++) {
            double w = lrk.getWeight(a, development);
            if(!Double.isNaN(w))
                sw += w;
        }
        return sw;
    }
    
    private void clearState() {
        accidents = 0;
        lrk = null;
    }
    
    @Override
    public LinkRatioSECalculator copy() {
        return new LinkRatioSECalculator(source.copy());
    }
}