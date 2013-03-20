package org.jreserve.estimate;

import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EstimateUtil {
    
    public static double[][] completeTriangle(ClaimTriangle cik, LinkRatio lrs) {
        int accidents = cik.getAccidentCount();
        int developments = lrs.getDevelopmentCount() + 1;
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
