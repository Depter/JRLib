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
package org.jreserve.jrlib.bootstrap.util;

import java.util.Arrays;
import org.jreserve.jrlib.util.MathUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class BootstrapUtil {
    
    /**
     * Calculates the totel reserve for each bootstrap reserve.
     * 
     * @throws NullPointerException if `reserves` is null.
     */
    public static double[] getTotalReserves(double[][] reserves) {
        int size = reserves.length;
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = MathUtil.sum(reserves[i]);
        return result;
    }
    
    /**
     * Extracts the reserves for one accident period from the
     * bootstrap reserves.
     * 
     * @throws NullPointerException if `reserves` is null.
     * @throws ArrayIndexOutOfBoundsException if `accident` is out of bounds.
     */
    public static double[] getReserves(double[][] reserves, int accident) {
        int size = reserves.length;
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = reserves[i][accident];
        return result;
    }
    
    /**
     * Calculates the mean of the total reserves.
     * 
     * @throws NullPointerException if `reserves` is null.
     */
    public static double getMeanTotalReserve(double[][] reserves) {
        int size = reserves.length;
        int n=0;
        double mean = 0d;
        for(int i=0; i<size; i++) {
            double w = (double) n / (double)(++n);
            double r = MathUtil.sum(reserves[i]);
            mean = w*mean + (1-w) * r;
        }
        return mean;
    }
    
    /**
     * Calculates the mean of the reserves for each accident period.
     * If `reserves.length=0` then returns an empty array.
     * 
     * @throws NullPointerException if `reserves` is null.
     */
    public static double[] getMeans(double[][] reserves) {
        int bsCount = reserves.length;
        if(bsCount == 0)
            return new double[0];
        
        int size = reserves[0].length;
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = MathUtil.mean(getReserves(reserves, i));
        
        return result;
    }
    
    /**
     * Calculates the mean of the reserves for the given
     * accident period.
     * 
     * @throws NullPointerException if `reserves` is null.
     * @throws ArrayIndexOutOfBoundsException if `accident` is out of bounds.
     */
    public static double getMeanReserve(double[][] reserves, int accident) {
        int size = reserves.length;
        int n=0;
        double mean = 0d;
        for(int i=0; i<size; i++) {
            double w = (double) n / (double)(++n);
            double r = reserves[i][accident];
            mean = w*mean + (1-w) * r;
        }
        return mean;
    }
    
    /**
     * Shifts the bootstrap reserves in such a way that their mean will be 
     * equal to the provided mean.
     * 
     * The correction is calculated as
     * <pre>
     *     c = mean - bsMean
     * </pre>
     * where:<ul>
     *   <li>`mean` is the supplied `mean`</li>
     *   <li>`bsMean` is calculated with {@link MathUtil#mean(double[]) MathUtil.mean(reserves)}</li>
     * </ul>
     * 
     * @throws NullPointerException if `reserves` is null.
     */
    public static void shiftAdjustment(double[] reserves, double mean) {
        int size = reserves.length;
        if(size == 0)
            return;
        
        double correction = mean - MathUtil.mean(reserves);
        for(int i=0; i<size; i++)
            reserves[i] += correction;
    }
    
    /**
     * Shifts the bootstrap reserves of each accident period in such
     * a way that their mean will be the same as the provided means.
     * 
     * The correction for accident period `a` is calculated as
     * <pre>
     *     c(a) = mean(a) - bsMean(a)
     * </pre>
     * where:<ul>
     *   <li>`mean(a)` is the a-th element of the supplied `means`</li>
     *   <li>`bsMean(a)` calculated with {@link #getMeans(double[][]) getMeans()}</li>
     * </ul>
     * 
     * @throws NullPointerException if `reserves` or `means` is null.
     * @throws IllegalArgumentException if the number of accident periods
     * in `reserves` and `means` is different.
     */
    public static void shiftAdjustment(double[][] reserves, double[] means) {
        double[] corrections = getMeans(reserves);
        if(corrections.length==0) return;
        
        int accidents = means.length;
        if(corrections.length != accidents) {
            String msg = "Number of accident periods in reserves (%d) is different from the number of accident period in means (%d)!";
            msg = String.format(msg, corrections.length, accidents);
            throw new IllegalArgumentException(msg);
        }
        
        for(int a=0;a<accidents; a++)
            corrections[a] = means[a] - corrections[a];
        
        for(double[] bs : reserves)
            for(int a=0;a<accidents; a++)
                bs[a] += corrections[a];
    }
    
    /**
     * Scales the bootstrap reserves in such a way that their mean will be 
     * equal to the provided mean.
     * 
     * The correction is calculated as
     * <pre>
     *          mean
     *     c = ------
     *         bsMean
     * </pre>
     * where:<ul>
     *   <li>`mean` is the supplied `mean`</li>
     *   <li>`bsMean` is calculated with {@link MathUtil#mean(double[]) MathUtil.mean(reserves)}</li>
     * </ul>
     * 
     * @throws NullPointerException if `reserves` is null.
     */
    public static void scaleAdjustment(double[] reserves, double mean) {
        int size = reserves.length;
        if(size == 0)
            return;
        
        double correction = mean / MathUtil.mean(reserves);
        for(int i=0; i<size; i++)
            reserves[i] *= correction;
    }
    
    /**
     * Scales the bootstrap reserves of each accident period in such 
     * a way that their mean will be the same as the provided means.
     * 
     * The correction for accident period `a` is calculated as
     * <pre>
     *             mean(a)
     *     c(a) = ---------
     *            bsMean(a)
     * </pre>
     * where:<ul>
     *   <li>`mean(a)` is the a-th element of the supplied `means`</li>
     *   <li>`bsMean(a)` calculated with {@link #getMeans(double[][]) getMeans()}</li>
     * </ul>
     * 
     * @throws NullPointerException if `reserves` or `means` is null.
     * @throws IllegalArgumentException if the number of accident periods
     * in `reserves` and `means` is different.
     */
    public static void scaleAdjustment(double[][] reserves, double[] means) {
        double[] corrections = getMeans(reserves);
        if(corrections.length==0) return;
    
        int accidents = means.length;
        if(corrections.length != accidents) {
            String msg = "Number of accident periods in reserves (%d) is different from the number of accident period in means (%d)!";
            msg = String.format(msg, corrections.length, accidents);
            throw new IllegalArgumentException(msg);
        }
        
        for(int a=0;a<accidents; a++)
            corrections[a] = means[a] / corrections[a];
        
        for(double[] bs : reserves)
            for(int a=0;a<accidents; a++)
                bs[a] *= corrections[a];
    }
    
    /**
     * Calculates the given percentile if the reserves. If the input
     * array contains less then 2 elments, the result is Double.NaN.
     * 
     * The input array will be sorted by this method!
     * 
     * @throws NullPointerException if `reserves` is null.
     * @throws IllegalArgumentException if `percentile` is less tehn 0, or
     * greater then 1.
     */
    public static double percentile(double[] reserves, double percentile) {
        if(percentile<0d || percentile>1d)
            throw new IllegalArgumentException("Percentile must be within [0;1] but it was "+percentile+"!");
        int size = reserves.length;
        
        if(size == 0)
            return Double.NaN;
        if(size == 1)
            return reserves[0];
        if(percentile == 1d)
            return reserves[size-1];
        
        Arrays.sort(reserves);
        double rank = percentile * (size - 1);
        int index = (int)rank;
        double w0 = 1d - rank + (double)index;
        return reserves[index] * w0 + reserves[index+1] * (1d - w0);
    }
    
    private BootstrapUtil() {}
}
