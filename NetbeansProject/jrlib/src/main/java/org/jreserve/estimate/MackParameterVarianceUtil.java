package org.jreserve.estimate;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.standarderror.LinkRatioSE;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MackParameterVarianceUtil {

    private LinkRatioSE lrSE;
    private double[][] estimates;
    private LinkRatio lrs;
    private Triangle cik;
    private int accidents;
    
    private double[] paramSDs;
    private double paramSD;

    MackParameterVarianceUtil(LinkRatioSE lrSE, double[][] estimates) {
        initState(lrSE, estimates);
        calculate();
        clearState();
    }
    
    private void initState(LinkRatioSE lrSE, double[][] estimates) {
        this.lrSE = lrSE;
        this.lrs = lrSE.getSourceLRScales().getSourceLinkRatios();
        this.cik = lrs.getSourceFactors().getSourceTriangle();
        this.accidents = cik.getAccidentCount();
        this.estimates = estimates;
        this.paramSDs = new double[accidents];
        this.paramSD = 0d;
    }
    
    private void calculate() {
        double[] pvm = calculatePVM();
        int estimateDev = lrSE.getDevelopmentCount();
        
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<accidents; d++) {
                double m = (a < d)? pvm[a] : pvm[d];
                double v = estimates[a][estimateDev] * estimates[d][estimateDev] * m;
                paramSD += v;
            }
            
            double sd = pvm[a];
            paramSDs[a] = (sd < 0d)? Double.NaN : estimates[a][estimateDev] * Math.sqrt(sd);
        }
        
        paramSD = (paramSD < 0d)? Double.NaN : Math.sqrt(paramSD);
    }
    
    private double[] calculatePVM() {
        double sum = 0d;
        int lastIndex = lrSE.getDevelopmentCount();
        double[] pvm = new double[accidents];
        
        for(int a=0; a<accidents; a++) {
            int firstIndex = cik.getDevelopmentCount(a) - 1;
            for(int d=firstIndex; d<lastIndex; d++) {
                double lr = lrs.getValue(d);
                double se = lrSE.getValue(d);
                sum += (lr == 0d)? Double.NaN : Math.pow(se / lr, 2);
            }
            lastIndex = firstIndex;
            pvm[a] = sum;
        }
        
        return pvm;
    }
    
    private void clearState() {
        this.lrSE = null;
        this.lrs = null;
        this.cik = null;
        this.estimates = null;
    }
    
    double[] getParameterSDs() {
        return paramSDs;
    }
    
    double getParameterSD() {
        return paramSD;
    }
}
