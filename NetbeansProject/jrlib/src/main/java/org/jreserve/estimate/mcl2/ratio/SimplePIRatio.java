package org.jreserve.estimate.mcl2.ratio;

import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimplePIRatio extends AbstractCalculationData<RatioTriangle> implements PIRatio {

    private int developments;
    private double[] pPerI;
    
    public SimplePIRatio(RatioTriangle source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source;
    }

    @Override
    public ClaimTriangle getSourcePaidTriangle() {
        return source.getSourcePaidTriangle();
    }

    @Override
    public ClaimTriangle getSourceIncurredTriangle() {
        return source.getSourceIncurredTriangle();
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public double getPperI(int development) {
        if(0 <= development && development < developments)
            return pPerI[development];
        return Double.NaN;
    }

    @Override
    public double getIperP(int development) {
        if(0 <= development && development < developments)
            return 1d / pPerI[development];
        return Double.NaN;
    }

    @Override
    public double[] toArrayPperI() {
        return TriangleUtil.copy(pPerI);
    }

    @Override
    public double[] toArrayIperP() {
        double[] result = new double[developments];
        for(int d=0; d<developments; d++)
            result[d] = 1d / pPerI[d];
        return result;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        int accidents = source.getAccidentCount();
        int devs = source.getDevelopmentCount();
        pPerI = new double[devs];
        for(int d=0; d<devs; d++)
            pPerI[d] = calculatePperI(accidents, d);
    }
    
    private double calculatePperI(int accidents, int development) {
        double sumP = 0d;
        double sumI = 0d;
        
        for(int a=0; a<accidents; a++) {
            double r = source.getPperI(a, development);
            double p = source.getSourcePaidTriangle().getValue(a, development);
            double i = source.getSourceIncurredTriangle().getValue(a, development);
            if(!Double.isNaN(r) && !Double.isNaN(p) && !Double.isNaN(i)) {
                sumP += p;
                sumI += i;
            }
        }
        
        return sumP / sumI;
    }
}
