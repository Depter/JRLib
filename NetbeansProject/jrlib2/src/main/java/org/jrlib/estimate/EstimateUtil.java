package org.jrlib.estimate;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * Utility class for the {@link Estimate Estimate} interface.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class EstimateUtil {
    
    /**
     * Completes the given input triangle, by using the given
     * link-ratios. The retunred array will have the same accident
     * periods as the claim triangle and on more development periods
     * as the link-ratios. 
     * 
     * @throws NullPointerException if `cik` or `lrs` is null.
     */
    public static double[][] completeTriangle(ClaimTriangle cik, LinkRatio lrs) {
        int accidents = cik.getAccidentCount();
        int developments = lrs.getLength() + 1;
        double[][] result = new double[accidents][developments];
        
        for(int a=0; a<accidents; a++) {
            int devs = cik.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                result[a][d] = cik.getValue(a, d);
            
            double last = result[a][devs-1];
            for(int d=devs; d<developments; d++) {
                double lr = lrs.getValue(d-1);
                last *= lrs.getValue(d-1);
                result[a][d] = last;
            }
        }
        
        return result;
    }

    private EstimateUtil() {
    }
}
