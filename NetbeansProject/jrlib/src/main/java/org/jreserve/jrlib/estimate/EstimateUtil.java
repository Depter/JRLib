/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

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
                last *= lrs.getValue(d-1);
                result[a][d] = last;
            }
        }
        
        return result;
    }
    
    /**
     * Calculates the cummulated link-ratios from the given link
     * ratios. The cummulated link-ratio for development period
     * `d, clr(d),` is calculated as:
     *      clr(d) = lr(d) * clr(d-1)
     * where `lr(d)` is the loss-ratio for development period `d`.
     * 
     * @see LinkRatio
     * @throws NullPointerException if `lrs` is null, or 
     * {@link LinkRatio#toArray() lrs.toArray()} returns null.
     */
    public static double[] getCummulativeLinkRatios(LinkRatio lrs) {
        double[] result = lrs.toArray();
        int size = result.length;
        for(int i=1; i<size; i++)
            result[i] *= result[i-1];
        return result;
    }
    
    /**
     * Calculates the completion-ratios from the given link-ratios. The 
     * completion-ratios for development period `d, g(d),` is calculated as:
     *             g(d+1)
     *      g(d) = ------
     *              lr(d)
     * where `lr(d)` is the loss-ratio for development period `d`, and
     * `g(d)` is equals to 1, if `d >= {@link LinkRatio#getLength() lrs.getLength()}`.
     * 
     * @see LinkRatio
     * @throws NullPointerException if `lrs` is null, or 
     * {@link LinkRatio#toArray() lrs.toArray()} returns null.
     */
    public static double[] getCompletionRatios(LinkRatio lrs) {
        double[] result = lrs.toArray();
        int size = result.length;
        for(int i=(size-1); i>=0; i--) {
            double prev = i==(size-1)? 1d : result[i+1];
            result[i] = prev/result[i];
        }
        return result;
    }

    private EstimateUtil() {
    }
}
