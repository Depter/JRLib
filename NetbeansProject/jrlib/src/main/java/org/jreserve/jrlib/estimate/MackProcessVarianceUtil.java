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
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * Utility class to calculate the process variance, according to Mack's method.
 * 
 * @see "Mack [1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @author Peter Decsi
 * @version 1.0
 */
class MackProcessVarianceUtil {
    
    private LinkRatio lrs;
    private ClaimTriangle cik;
    private double[][] estimates;
    private Scale<LinkRatioScaleInput> scales;
    private int accidents;
    
    private double[] procSDs;
    private double procSD;

    MackProcessVarianceUtil(Scale<LinkRatioScaleInput> scales, double[][] estimates) {
        initState(scales, estimates);
        double[] pvm = calculateProcVarMultiplier();
        fillValues(pvm);
        clearState();
    }
    
    private void initState(Scale<LinkRatioScaleInput> scales, double[][] estimates) {
        this.estimates = estimates;
        this.scales = scales;
        lrs = scales.getSourceInput().getSourceLinkRatios();
        cik = lrs.getSourceTriangle();
        accidents = cik.getAccidentCount();
        procSDs = new double[accidents];
        procSD = 0d;
    }
    
    private double[] calculateProcVarMultiplier() {
        int devs = scales.getLength();
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