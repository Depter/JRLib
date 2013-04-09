package org.jrlib.estimate.mcl;

import org.jrlib.AbstractCalculationData;

/**
 * MclCorrelation calculates the correlation coefficient between
 * the link- and the claim-ratio residuals.
 * 
 * The following formula is used:
 *     corr = sum(f * q) / sum(q * q)
 * where:
 * -   `corr` is the correlation.
 * -   `sum()` mean an iteration for all accident and development periods.
 * -   `f` is the residual for the link-ratio for a given accident and
 *     development period.
 * -   `q` is the residual for the claim-ratio for a given accident and
 *     development period.
 * 
 * If one of `f` or `q` is a `NaN`, than the cell is ignored. If 
 * `sum(q * q)` is 0, or the input is empty, the result is `NaN`.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCorrelation extends AbstractCalculationData<MclCorrelationInput> {

    private double correlation;
    
    public MclCorrelation(MclCorrelationInput source) {
        super(source);
        doRecalculate();
    }
    
    /**
     * Returns the input used for the calculation.
     */
    public MclCorrelationInput getSourceInput() {
        return source;
    }
    
    /**
     * Returns the correlation coefficient.
     */
    public double getValue() {
        return correlation;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        double sn = 0d;
        double sd = 0d;
        
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(accidents);
            for(int d=0; d<devs; d++) {
                double f = source.getLinkRatioResidual(a, d);
                double q = source.getClaimRatioResidual(a, d);
                
                if(!Double.isNaN(f) && !Double.isNaN(q)) {
                    sn += f * q;
                    sd += q * q;
                }
            }
        }
        
        correlation = sd==0d? Double.NaN : sn / sd; 
    }
}
