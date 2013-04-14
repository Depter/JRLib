package org.jrlib.estimate.mcl;

import org.jrlib.AbstractCalculationData;
import org.jrlib.claimratio.ClaimRatio;
import org.jrlib.claimratio.scale.ClaimRatioScale;
import org.jrlib.estimate.AbstractEstimate;
import org.jrlib.estimate.Estimate;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclEstimateBundle extends AbstractCalculationData<MclCalculationBundle>{
    
    private EstimateProxy paidProxy;
    private EstimateProxy incurredProxy;
    
    private int accidents;
    private int developments;
    private int[] devIndices;
    private double[][] paidValues;
    private double[][] incurredValues;
    
    public MclEstimateBundle(MclCorrelation paidCorrelation, MclCorrelation incurredCorrelation) {
        this(new MclCalculationBundle(paidCorrelation, incurredCorrelation));
    }
    
    public MclEstimateBundle(MclCalculationBundle source) {
        super(source);
        paidProxy = new EstimateProxy(true);
        incurredProxy = new EstimateProxy(false);
        doRecalculate();
    }
    
    public Estimate getPaidEstimate() {
        return paidProxy;
    }
    
    public Estimate getIncurredEstimate() {
        return incurredProxy;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    //TEMP
    private ClaimTriangle paid;
    private LinkRatio paidLR;
    private double[] paidLSPR;
    private double[] paidRatio;

    private ClaimTriangle incurred;
    private LinkRatio incurredLR;
    private double[] incurredLSPR;
    private double[] incurredRatio;

    private void doRecalculate() {
        initCalculationState();
        fillTriangles();
        clearCalculationState();
    }
    
    private void initCalculationState() {
        initDimensions();
        paidLSPR = calculateLSPR(source.getSourcePaidCorrelation());
        incurredLSPR = calculateLSPR(source.getSourceIncurredCorrelation());
        ClaimRatio cr = source.getSourcePaidCorrelation().getSourceClaimRatios();
        paidRatio = calculateRatios(cr, incurredLR, paidLR);
        cr = source.getSourceIncurredCorrelation().getSourceClaimRatios();
        incurredRatio = calculateRatios(cr, paidLR, incurredLR);
    }
    
    private void initDimensions() {
        paidLR = source.getSourcePaidCorrelation().getSourceLinkRatios();
        incurredLR = source.getSourceIncurredCorrelation().getSourceLinkRatios();
        
        int pD = paidLR.getLength();
        int iD = incurredLR.getLength();
        developments = Math.min(pD, iD) + 1;
        
        paid = paidLR.getSourceTriangle();
        incurred = incurredLR.getSourceTriangle();
        int pA = paid.getAccidentCount();
        int iA = incurred.getAccidentCount();
        accidents = Math.min(pA, iA);
        
        devIndices = new int[accidents];
        for(int a=0; a<accidents; a++) {
            pD = paid.getDevelopmentCount(a);
            iD = paid.getDevelopmentCount(a);
            devIndices[a] = Math.min(pD, iD);
        }
    }
    
    private double[] calculateLSPR(MclCorrelation corr) {
        double lambda = corr.getValue();
        LinkRatioScale sigma = corr.getSourceLinkRatioScaless();
        ClaimRatioScale rho = corr.getSourceClaimRatioScales();
        
        double[] lspr = new double[developments-1];
        for(int d=0; d<(developments-1); d++)
            lspr[d] = lambda * sigma.getValue(d) / rho.getValue(d);
        return lspr;
    }
    
    private double[] calculateRatios(ClaimRatio cr, LinkRatio lrN, LinkRatio lrD) {
        double[] ratios = new double[developments-1];
        double prev = Double.NaN;
        for(int d=0; d<(developments-1); d++) {
            double r = cr.getValue(d);
            prev = Double.isNaN(r)? prev * lrN.getValue(d-1)/lrD.getValue(d-1): r;
            ratios[d] = prev;
        }
        return ratios;
    }
    
    private void fillTriangles() {
        paidValues = new double[accidents][developments];
        incurredValues = new double[accidents][developments];
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                paidValues[a][d] = calculatePaid(a, d);
                incurredValues[a][d] = calculateIncurred(a, d);
            }
        }
    }
    
    private double calculateIncurred(int a, int d) {
        if(d == 0 || d < incurred.getDevelopmentCount(a))
            return incurred.getValue(a, d);
        
        double iPrev = incurredValues[a][d-1];
        double pPrev = paidValues[a][d-1];
        double iLr = incurredLR.getValue(d-1);
        return iPrev * (iLr + incurredLSPR[d-1] * (pPrev/iPrev - incurredRatio[d]));
    }
    
    private double calculatePaid(int a, int d) {
        if(d == 0 || d < paid.getDevelopmentCount(a))
            return paid.getValue(a, d);
        
        double iPrev = incurredValues[a][d-1];
        double pPrev = paidValues[a][d-1];
        double pLr = paidLR.getValue(d-1);
        return pPrev * (pLr + paidLSPR[d-1] * (iPrev/pPrev - paidRatio[d]));
    }
    
    private void clearCalculationState() {
        paid = null;
        paidLR = null;
        paidLSPR = null;
        paidRatio = null;
        incurred = null;
        incurredLR = null;
        incurredLSPR = null;
        incurredRatio = null;
    }
    
    private class EstimateProxy extends AbstractEstimate<MclEstimateBundle> {
        
        private boolean isPaid;
        
        private EstimateProxy(boolean isPaid) {
            super(MclEstimateBundle.this);
            this.isPaid = isPaid;
        }
        
        @Override
        protected void recalculateLayer() {
            super.accidents = accidents;
            super.developments = developments;
            super.values = isPaid? paidValues : incurredValues;
        }

        @Override
        public int getObservedDevelopmentCount(int accident) {
            return (0 <= accident && accident < accidents)?
                    devIndices[accident] : 
                    0;
        }
    }
}