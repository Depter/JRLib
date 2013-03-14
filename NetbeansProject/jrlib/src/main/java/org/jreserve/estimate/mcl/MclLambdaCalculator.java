package org.jreserve.estimate.mcl;

import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.factor.linkratio.scale.LinkRatioScaleErrorTriangle;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclLambdaCalculator {

    private MclRhoErrorTriangle ratioErrors;
    private LinkRatioScaleErrorTriangle factorErrors;
    
    private double lambda;
    
    public MclLambdaCalculator(MclRho rhos, LinkRatioScale scales) {
        this.ratioErrors = new MclRhoErrorTriangle(rhos);
        this.factorErrors = new LinkRatioScaleErrorTriangle(scales);
        doRecalculate();
    }
    
    public double getLambda() {
        return lambda;
    }
    
    public MclRhoErrorTriangle getRatioErrors() {
        return ratioErrors;
    }
    
    public LinkRatioScaleErrorTriangle getFactorErrors() {
        return factorErrors;
    }
    
    public void recalculate() {
        ratioErrors.recalculate();
        factorErrors.recalculate();
        doRecalculate();
    }

    private void doRecalculate() {
        int accidents = Math.min(ratioErrors.getAccidentCount(), factorErrors.getAccidentCount());
        
        double sumEr = 0d;
        double sumErEf = 0d;
        int n = 0;
        
        for(int a=0; a<accidents; a++) {
            int devs = getUsefulDevCount(a);
            for(int d=0; d<devs; d++) {
                double er = ratioErrors.getValue(a, d);
                double ef = factorErrors.getValue(a, d);
                if(!Double.isNaN(er) && !Double.isNaN(ef)) {
                    n++;
                    sumEr += er * er;
                    sumErEf += ef * er;
                }
            }
        }
        
        lambda = (n==0)? Double.NaN :  sumErEf / sumEr;
    }
    
    private int getUsefulDevCount(int accident) {
        int d = Math.min(ratioErrors.getDevelopmentCount(accident), factorErrors.getDevelopmentCount(accident));
        while(d>0) {
            int ratioAccidents = TriangleUtil.getAccidentCount(ratioErrors, d-1);
            int factorAccidents = TriangleUtil.getAccidentCount(factorErrors, d-1);
            if(ratioAccidents > 1 && factorAccidents > 1)
                return d;
            d--;
        }
        return d;
    }
}
