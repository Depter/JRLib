package org.jreserve.jrlib.estimate.mcl;

import org.jreserve.jrlib.CalculationData;

/**
 * Instances of this interface bundle together two instances of
 * {@link MclCorrelation MclCorrelation} (paid and incurred).
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface MclEstimateInput extends CalculationData {
    
    /**
     * Returns the {@link MclCorrelation MclCorrelation} used to 
     * calculate the value of lambda for the paid development
     * factors.
     */
    public MclCorrelation getSourcePaidCorrelation();
    
    /**
     * Returns the {@link MclCorrelation MclCorrelation} used to 
     * calculate the value of lambda for the incurred development
     * factors.
     */
    public MclCorrelation getSourceIncurredCorrelation();
}
