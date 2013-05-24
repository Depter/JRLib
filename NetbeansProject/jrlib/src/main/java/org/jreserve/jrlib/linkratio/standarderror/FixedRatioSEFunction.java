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
package org.jreserve.jrlib.linkratio.standarderror;

import java.util.Arrays;

/**
 * The FixedRatioSEFunction estimates the standard errors of the link ratios
 * by assuming that the rate `se(d)/lr(d)` is constant for all development
 * periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class FixedRatioSEFunction implements LinkRatioSEFunction {

    private boolean[] isExcluded = new boolean[0];
    private double ratio = Double.NaN;
    
    //private LinkRatio lrs;
    private int developments;
    private double[] lrs;
    
    /**
     * Excludes/Includes the values at the given index from the calculation.
     * 
     * @throws IndexOutOfBoundsException if `index` is less then 0.
     */
    public void setExcluded(int index, boolean excluded) {
        if(isExcluded.length <= index)
            isExcluded = Arrays.copyOf(isExcluded, index+1);
        isExcluded[index] = excluded;
    }
    
    /**
     * Returns wether the value at the given index is excludes/includes int 
     * the calculation.
     */
    public boolean isExcluded(int index) {
        if(index < 0 || index >=isExcluded.length)
            return false;
        return isExcluded[index];
    }
    
    /**
     * Returns the estimated ratio.
     */
    public double getRatio() {
        return ratio;
    }
    
    @Override
    public void fit(LinkRatioSE ses) {
        lrs = ses.getSourceLinkRatios().toArray();
        developments = ses.getLength();
        
        int n = 0;
        double sum = 0d;
        for(int d=0; d<developments; d++) {
            if(isExcluded(d)) continue;
            
            double lr = lrs[d];
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
        return (0 <= development && development < developments)?
            ratio * lrs[development] :
            Double.NaN;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof FixedRatioSEFunction);
    }
    
    @Override
    public int hashCode() {
        return FixedRatioSEFunction.class.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format(
                "FixedRatioSEFunction [%f]",
                ratio);
    }
}