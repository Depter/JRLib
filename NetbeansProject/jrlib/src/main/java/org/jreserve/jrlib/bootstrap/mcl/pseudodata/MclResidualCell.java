package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclResidualCell {

    private final double paidLr;
    private final double paidCr;
    private final double incurredLr;
    private final double incurredCr;
    
    MclResidualCell(int accident, int development, MclResidualBundle bundle) {
        paidLr = bundle.getPaidLRResidual(accident, development);
        paidCr = bundle.getPaidCRResidual(accident, development);
        incurredLr = bundle.getIncurredLRResidual(accident, development);
        incurredCr = bundle.getIncurredCRResidual(accident, development);
    }
    
    double getPaidLRResidual() {
        return paidLr;
    }
    
    double getPaidCRResidual() {
        return paidCr;
    }
    
    double getIncurredLRResidual() {
        return incurredLr;
    }
    
    double getIncurredCRResidual() {
        return incurredCr;
    }
}
