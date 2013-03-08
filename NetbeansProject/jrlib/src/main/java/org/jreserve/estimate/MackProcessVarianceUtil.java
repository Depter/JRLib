package org.jreserve.estimate;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MackProcessVarianceUtil {
    
    private LinkRatio lrs;
    private Triangle cik;
    private double[][] estimates;
    private LinkRatioScale scales;
    private int accidents;
    
    private double[] procSDs;
    private double procSD;

    MackProcessVarianceUtil(LinkRatioScale scales, double[][] estimates) {
        initState(scales, estimates);
        double[] pvm = calculateProcVarMultiplier();
        fillValues(pvm);
        clearState();
    }
    
    private void initState(LinkRatioScale scales, double[][] estimates) {
        this.estimates = estimates;
        this.scales = scales;
        lrs = scales.getSourceLinkRatios();
        cik = lrs.getSourceFactors().getSourceTriangle();
        accidents = cik.getAccidentCount();
        procSDs = new double[accidents];
        procSD = 0d;
    }
    
    private double[] calculateProcVarMultiplier() {
        int devs = scales.getDevelopmentCount();
        double[] procVM = new double[devs];
        
        double clr = 1d;
        for(int d=(devs-1); d>=0; d--) {
            double lr = lrs.getValue(d);
            double scale = scales.getValue(d);
            clr *= lr;
            procVM[d] = (lr==0d)? Double.NaN : Math.pow(scale/lr, 2) * clr;
        }
        
        return procVM;
    }
    
    private void fillValues(double[] pvm) {
        double sum = 0d;
        int lastIndex = pvm.length;
        int estimateDev = lastIndex;
        
        for(int a=0; a<accidents; a++) {
            int firstIndex = cik.getDevelopmentCount(a) - 1;
            for(int d=firstIndex; d<lastIndex; d++)
                sum += pvm[d];
            lastIndex = firstIndex;
            
            double sd = estimates[a][estimateDev] * sum;
            procSD += sd;
            procSDs[a] = (sd < 0d)? Double.NaN : Math.sqrt(sd);
        }
        
        procSD = (procSD < 0d)? Double.NaN : Math.sqrt(procSD);
    }
    
    private void clearState() {
        estimates = null;
        scales = null;
        lrs = null;
        cik = null;
    }
    
    double[] getProcessSDs() {
        return procSDs;
    }
    
    double getProcessSD() {
        return procSD;
    }
}
