package org.jreserve.grscript.jrlib

import org.jreserve.grscript.AbstractDelegate
import org.jreserve.jrlib.bootstrap.util.HistogramData
import org.jreserve.jrlib.bootstrap.util.HistogramDataFactory
import org.jreserve.jrlib.bootstrap.util.BootstrapUtil

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
    
}
