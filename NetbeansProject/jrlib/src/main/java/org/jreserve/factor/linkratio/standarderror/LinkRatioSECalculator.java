package org.jreserve.factor.linkratio.standarderror;

import org.jreserve.AbstractCalculationData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioSECalculator extends AbstractCalculationData<LinkRatioScale> implements LinkRatioSE {

    private int developments;
    private double[] values;
    
    public LinkRatioSECalculator(LinkRatioScale source) {
        super(source);
        doRecalculate();
    }
    
    public LinkRatioScale getSourceLRScales() {
        return source;
    }

    public int getDevelopmentCount() {
        return developments;
    }

    public double getValue(int development) {
        if(development < developments && development >= 0)
            return values[development];
        return Double.NaN;
    }

    public double[] toArray() {
        return TriangleUtil.copy(values);
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
        developments = source.getDevelopmentCount();
        values = new double[developments];
        lrk = source.getSourceLinkRatios();
        accidents = lrk.getSourceFactors().getAccidentCount();
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
}
