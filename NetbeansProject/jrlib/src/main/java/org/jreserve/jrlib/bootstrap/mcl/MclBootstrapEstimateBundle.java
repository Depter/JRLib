package org.jreserve.jrlib.bootstrap.mcl;

import java.util.Arrays;
import org.jreserve.jrlib.AbstractCalculationData;
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData;
import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;

/**
 * Utility class for {@link MclBootstrapper MclBootstrapper}. The class
 * wraps an estimate for paid and incurred claims., and after each 
 * recalculation simulates the process error.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class MclBootstrapEstimateBundle extends AbstractCalculationData<MclPseudoData> {
    
    private final MclProcessSimulator paidSimulator;
    private final MclProcessSimulator incurredSimulator;

    private ClaimTriangle paid;
    private ClaimTriangle incurred;
    private LinkRatio paidLR;
    private LinkRatio incurredLR;
    private LinkRatioScale paidLRScale;
    private LinkRatioScale incurredLRScale;
    private ClaimRatio paidRatio;
    private ClaimRatio incurredRatio;
    private ClaimRatioScale paidCRScale;
    private ClaimRatioScale incurredCRScale;

    private double[] paidLSPR;
    private double[] incurredLSPR;
    
    private int accidents;
    private int developments;
    private int devIndices[];
    private double[][] paidValues;
    private double[][] incurredValues;
    
    private double[] paidReserves;
    private double[] incurredReserves;
    private double[] paidIncurredReserves;
    
    /**
     * Creates an instance for the given input.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public MclBootstrapEstimateBundle(MclPseudoData pseudoData, MclProcessSimulator paidProcessSimulator, MclProcessSimulator incurredProcessSimulator) {
        super(pseudoData);
        paidSimulator = paidProcessSimulator;
        incurredSimulator = incurredProcessSimulator;
        this.detach();
        initDatas();
    }
    
    private void initDatas() {
        paidLRScale = source.getPaidLinkRatioScale();
        paidLR = paidLRScale.getSourceLinkRatios();
        paid = paidLR.getSourceTriangle();
        
        incurredLRScale = source.getIncurredLinkRatioScale();
        incurredLR = incurredLRScale.getSourceLinkRatios();
        incurred = incurredLR.getSourceTriangle();
        
        paidCRScale = source.getPaidClaimRatioScale();
        paidRatio = paidCRScale.getSourceClaimRatios();
        
        incurredCRScale = source.getIncurredClaimRatioScale();
        incurredRatio = incurredCRScale.getSourceClaimRatios();
        
        accidents = Math.min(paid.getAccidentCount(), incurred.getAccidentCount());
        developments = Math.min(paidLR.getLength(), incurredLR.getLength())+1;
        devIndices = new int[accidents];
        for(int a=0; a<accidents; a++)
            devIndices[a] = Math.min(paid.getDevelopmentCount(a), incurred.getDevelopmentCount(a));
        
        initSimulators();
    }
    
    private void initSimulators () {
        paidSimulator.setBundle(this);
        incurredSimulator.setBundle(this);
    }
    
    public MclPseudoData getSourcePseudoData() {
        return source;
    }
    
    public int getAccidentCount() {
        return accidents;
    }
    
    public int getObservedDevelopmentCount(int accident) {
        return 0 <= accident && accident < accidents? 
                devIndices[accident] : 
                0;
    }
    
    public double[][] getPaidValues() {
        return paidValues;
    }
    
    public double[][] getIncurredValues() {
        return incurredValues;
    }
    
    public double[] getPaidReserves() {
        return paidReserves;
    }
    
    public double[] getIncurredReserves() {
        return incurredReserves;
    }
    
    public double[] getPaidIncurredReserves() {
        return paidIncurredReserves;
    }
    
    @Override
    public void recalculate() {
        source.recalculate();
        recalculateValues();
        fillTriangles();
        fillReserves();
    }
    
    private void recalculateValues() {
        paidLSPR = calculateLSPR(source.getPaidLambda(), paidLRScale, paidCRScale);
        incurredLSPR = calculateLSPR(source.getIncurredLambda(), incurredLRScale, incurredCRScale);
    }
    
    private double[] calculateLSPR(double lambda, LinkRatioScale sigma, ClaimRatioScale rho) {
        double[] lspr = new double[developments-1];
        for(int d=0; d<(developments-1); d++) {
            lspr[d] = lambda * sigma.getValue(d) / rho.getValue(d);
        }
        return lspr;
    }
    
    private void fillTriangles() {
        initValues();
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                paidValues[a][d] = calculatePaid(a, d);
                incurredValues[a][d] = calculateIncurred(a, d);
            }
        }
    }
    
    private void initValues() {
        paidValues = new double[accidents][developments];
        incurredValues = new double[accidents][developments];
        paidReserves = new double[accidents];
        incurredReserves = new double[accidents];
        paidIncurredReserves = new double[accidents];
    }
    
    private double calculateIncurred(int a, int d) {
        if(d == 0 || d < devIndices[a])
            return incurred.getValue(a, d);
        
        int pD = d - 1;
        double iPrev = incurredValues[a][pD];
        double pPrev = paidValues[a][pD];
        double iLr = incurredLR.getValue(pD);
        double rPrev = incurredRatio.getValue(pD);
        double v = iPrev * (iLr + incurredLSPR[pD] * (pPrev/iPrev - rPrev));
        return incurredSimulator.simulateEstimate(v, a, d);
    }
    
    private double calculatePaid(int a, int d) {
        if(d == 0 || d < devIndices[a])
            return paid.getValue(a, d);
        
        int pD = d - 1;
        double iPrev = incurredValues[a][pD];
        double pPrev = paidValues[a][pD];
        double pLr = paidLR.getValue(pD);
        double rPrev = paidRatio.getValue(pD);
        double v = pPrev * (pLr + paidLSPR[pD] * (iPrev/pPrev - rPrev));
        return paidSimulator.simulateEstimate(v, a, d);
    }
    
    private void fillReserves() {
        int lastDev = developments - 1;
        if(lastDev < 0)
            fillNaNReserves();
        else
            calculateReserves(lastDev);
    }
    
    private void fillNaNReserves() {
        Arrays.fill(paidReserves, Double.NaN);
        Arrays.fill(incurredReserves, Double.NaN);
        Arrays.fill(paidIncurredReserves, Double.NaN);
    }

    private void calculateReserves(int lastDev) {
        for(int a=0; a<accidents; a++) {
            int lastIndex = devIndices[a] - 1;
            double last = paid.getValue(a, lastIndex);
            paidReserves[a] = paidValues[a][lastDev] - last;
            
            paidIncurredReserves[a] = incurredValues[a][lastDev] - last;
            
            last = incurred.getValue(a, lastIndex);
            incurredReserves[a] = incurredValues[a][lastDev] - last;
        }
    }

    @Override
    protected void recalculateLayer() {
    }
}
