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
package org.jreserve.grscript.jrlib

import org.jreserve.grscript.AbstractDelegate
import org.jreserve.jrlib.bootstrap.util.HistogramData
import org.jreserve.jrlib.bootstrap.util.HistogramDataFactory
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil
import org.jreserve.jrlib.util.MathUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class BootstrapUtilDelegate extends AbstractDelegate {
	
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        
        emc.totalReserves    << this.&totalReserves
        emc.accidentReserves << this.&accidentReserves 
        emc.meanReserve      << this.&meanReserve 
        emc.accidentMeans    << this.&accidentMeans
        emc.shift            << this.&shift 
        emc.scale            << this.&scale 
        emc.percentile       << this.&percentile 
        emc.histogram        << this.&histogram 
        emc.printData        << this.&printData
    }
    
    double[] totalReserves(double[][] reserves) {
        BootstrapUtil.getTotalReserves(reserves)
    }
    
    double[] accidentReserves(double[][] reserves, int accident) {
        BootstrapUtil.getReserves(reserves, accident)
    }
    
    double meanReserve(double[][] reserves) {
        BootstrapUtil.getMeanTotalReserve(reserves)
    }
    
    double meanReserve(double[] reserves) {
        MathUtil.mean(reserves)
    }
    
    double minReserve(double[] reserves) {
        MathUtil.max(reserves)
    }
    
    double maxReserve(double[] reserves) {
        MathUtil.max(reserves)
    }
    
    double meanReserve(double[][] reserves, int accident) {
        BootstrapUtil.getMeanReserve(reserves, accident)
    }
    
    double[] accidentMeans(double[][] reserves) {
        BootstrapUtil.getMeans(reserves)
    }
    
    void shift(double[] reserves, double mean) {
        BootstrapUtil.shiftAdjustment(reserves, mean)
    }
    
    void shift(double[][] reserves, double[] means) {
        BootstrapUtil.shiftAdjustment(reserves, means)
    }
    
    void scale(double[] reserves, double mean) {
        BootstrapUtil.scaleAdjustment(reserves, mean)
    }
    
    void scale(double[][] reserves, double[] means) {
        BootstrapUtil.scaleAdjustment(reserves, means)
    }
    
    double percentile(double[] reserves, double percentile) {
        BootstrapUtil.percentile(reserves, percentile)
    }
    
    HistogramData histogram(double[] data) {
        new HistogramDataFactory(data).buildData()
    }
    
    HistogramData histogram(double[] data, int intervals) {
        new HistogramDataFactory(data).setIntervalCount(intervals).buildData()
    }
    
    HistogramData histogram(double[] data, double firstUpper, double step) {
        new HistogramDataFactory(data).setIntervals(firstUpper, step).buildData()
    }
    
    void printData(String title, HistogramData data) {
        super.script.println title
        this.printData data
    }
    
    void printData(HistogramData data) {
        int count = data.getIntervalCount();
        super.script.println "Interval\tLower\tUpper\tCount"
        for(int i=0; i<count; i++) {
            super.script.print(i+1)
            double[] row = [data.getLowerBound(i), data.getUpperBound(i), data.getCount(i)]
            super.script.printData row
        }
    }
}
