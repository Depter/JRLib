package org.jreserve.estimate;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.factor.linkratio.standarderror.LinkRatioSE;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackEstimate extends AbstractEstimate {
    
    private Triangle cik;
    private LinkRatioSE lrSE;
    private double[] procSEs;
    private double procSE;
    private double[] paramSEs;
    private double paramSE;
    private double[] SEs;
    private double SE;
    
    public MackEstimate(LinkRatioSE lrSE) {
        this.lrSE = lrSE;
        attachSource(lrSE);
        this.cik = lrSE.getSourceLRScales().getSourceLinkRatios().getSourceFactors().getSourceTriangle();
        doRecalculate();
    }
    
    public LinkRatioSE getSourceLinkRatioSE() {
        return lrSE;
    }
    
    public double getSE() {
        return SE;
    }
    
    public double getSE(int accident) {
        return getSE(accident, SEs);
    }
    
    private double getSE(int accident, double[] ses) {
        if(accident < 0 || accident >= accidents)
            return Double.NaN;
        return ses[accident];
    } 
    
    public double[] toArraySE() {
        return TriangleUtil.copy(SEs);
    }
    
    public double getParameterSE() {
        return paramSE;
    }
    
    public double getParameterSE(int accident) {
        return getSE(accident, paramSEs);
    }
    
    public double[] toArrayParameterSE() {
        return TriangleUtil.copy(paramSEs);
    }
    
    public double getProcessSE() {
        return procSE;
    }
    
    public double getProcessSE(int accident) {
        return getSE(accident, procSEs);
    }
    
    public double[] toArrayProcessSE() {
        return TriangleUtil.copy(procSEs);
    }
    
    @Override
    protected int getObservedDevelopmentCount(int accident) {
        return cik.getDevelopmentCount(accident);
    }

    @Override
    protected void recalculateSource() {
        lrSE.recalculate();
    }

    @Override
    protected void detachSource() {
        lrSE.detach();
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        initState();
        calculateProcessSDs();
        calculateParameterSDs();
        sumSDs();
    }
    
    private void initState() {
        super.accidents = cik.getAccidentCount();
        super.developments = lrSE.getDevelopmentCount()+1;
        LinkRatio lrs = lrSE.getSourceLRScales().getSourceLinkRatios();
        super.values = EstimateUtil.completeTriangle(cik, lrs);
    }
    
    private void calculateProcessSDs() {
        LinkRatioScale scales = lrSE.getSourceLRScales();
        MackProcessVarianceUtil util = new MackProcessVarianceUtil(scales, values);
        this.procSEs = util.getProcessSDs();
        this.procSE = util.getProcessSD();
    }
    
    private void calculateParameterSDs() {
        MackParameterVarianceUtil util = new MackParameterVarianceUtil(lrSE, values);
        this.paramSEs = util.getParameterSDs();
        this.paramSE = util.getParameterSD();
    }
    
    private void sumSDs() {
        SEs = new double[accidents];
        for(int a=0; a<accidents; a++)
            SEs[a] = Math.sqrt(Math.pow(procSEs[a], 2) + Math.pow(paramSEs[a], 2));
        SE = Math.sqrt(Math.pow(procSE, 2) + Math.pow(paramSE, 2));
    }
}
