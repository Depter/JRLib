package org.jreserve.bootstrap.mack;

import org.jreserve.bootstrap.ResidualTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.standarderror.LinkRatioScaleInput;
import org.jreserve.scale.RatioScale;
import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackResidualTriangle extends AbstractTriangle<RatioScale<LinkRatioScaleInput>> implements ResidualTriangle {

    private FactorTriangle factors;

    private int accidents;
    private int developments;
    private double[][] residuals;
    private double[][] dik;
    
    public MackResidualTriangle(RatioScale<LinkRatioScaleInput> scales) {
        super(scales);
        factors = scales.getSourceInput().getSourceFactors();
        doRecalculate();
    }
    
    public RatioScale<LinkRatioScaleInput> getSourceLinkRatioScale() {
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
        if(withinBounds(accident))
            return residuals[accident].length;
        return 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)  ?
                residuals[accident][development]    :
                Double.NaN                          ;
    }

    @Override
    public double getWeight(int accident, int development) {
        return withinBounds(accident, development)  ?
                dik[accident][development]    :
                Double.NaN                          ;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initState();
        for(int a=0; a<accidents; a++)
            fillAccident(a);
    }
    
    private void initState() {
        accidents = factors.getAccidentCount();
        developments = factors.getDevelopmentCount();
        residuals = new double[accidents][];
        dik = new double[accidents][];
    }
    
    private void fillAccident(int accident) {
        int devs = factors.getDevelopmentCount(accident);
        residuals[accident] = new double[devs];
        dik[accident] = new double[devs];
        
        LinkRatio lrs = source.getSourceInput().getSourceLinkRatios();
        
        for(int d=0; d<devs; d++) {
            double f = factors.getValue(accident, d);
            double w = lrs.getWeight(accident, d);
            double lr = lrs.getValue(d); 
            double s = source.getValue(d);
            
            dik[accident][d] = w;
            residuals[accident][d] = Math.sqrt(w) * (f - lr) / s;
        }
    }
}
