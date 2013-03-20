package org.jreserve.linkratio.standarderror;

import java.util.Arrays;
import org.jreserve.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FixedRatioSEFunction implements LinkRatioSEFunction {

    private boolean[] isExcluded = new boolean[0];
    private double ratio = Double.NaN;
    private LinkRatio lrs;
    
    public void setExcluded(int index, boolean excluded) {
        if(isExcluded.length <= index)
            isExcluded = Arrays.copyOf(isExcluded, index+1);
        isExcluded[index] = excluded;
    }
    
    public boolean isExcluded(int index) {
        if(index < 0 || index >=isExcluded.length)
            return false;
        return isExcluded[index];
    }
    
    public double getRatio() {
        return ratio;
    }
    
    public void fit(LinkRatioSE ses) {
        lrs = ses.getSourceLinkRatios();
        int developments = ses.getDevelopmentCount();
        
        int n = 0;
        double sum = 0d;
        for(int d=0; d<developments; d++) {
            if(isExcluded(d)) continue;
            
            double lr = lrs.getValue(d);
            if(Double.isNaN(lr) || lr == 0d) continue;
            
            double se = ses.getValue(d);
            if(Double.isNaN(se)) continue;
            
            n++;
            sum += (se / lr);
        }
        
        ratio = (n==0)? Double.NaN : sum / (double)n;
    }

    @Override
    public double getValue(int development) {
        return ratio * lrs.getValue(development);
    }

    @Override
    public FixedRatioSEFunction copy() {
        FixedRatioSEFunction copy = new FixedRatioSEFunction();
        copy.lrs = lrs;
        copy.ratio = ratio;
        
        int size = isExcluded.length;
        copy.isExcluded = new boolean[size];
        System.arraycopy(isExcluded, 0, copy.isExcluded, 0, size);
        
        return copy;
    }
}
