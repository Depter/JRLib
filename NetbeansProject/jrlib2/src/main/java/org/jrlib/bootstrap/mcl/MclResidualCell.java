package org.jrlib.bootstrap.mcl;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclResidualCell {

    private final double paidLr;
    private final double paidCr;
    private final double incurredLr;
    private final double incurredCr;
    
    public MclResidualCell(int accident, int development, MclResidualBundle bundle) {
        paidLr = bundle.getPaidLRResidual(accident, development);
        paidCr = bundle.getPaidCRResidual(accident, development);
        incurredLr = bundle.getIncurredLRResidual(accident, development);
        incurredCr = bundle.getIncurredCRResidual(accident, development);
    }
    
    public double getPaidLRResidual() {
        return paidLr;
    }
    
    public double getPaidCRResidual() {
        return paidCr;
    }
    
    public double getIncurredLRResidual() {
        return incurredLr;
    }
    
    public double getIncurredCRResidual() {
        return incurredCr;
    }
}
