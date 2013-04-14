package org.jrlib.estimate;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jrlib.linkratio.standarderror.LinkRatioSE;
import org.jrlib.scale.Scale;
import org.jrlib.triangle.TriangleUtil;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * The class calculates the standard chain-ladder reserve estimates, and the 
 * process-, parameter and standard errors of the reserves. This class is
 * the implementation of the method, described by Mack.
 *
 * @see "Mack [1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @author Peter Decsi
 * @version 1.0
 */
public class MackEstimate extends AbstractEstimate<LinkRatioSE> {
    
    private ClaimTriangle cik;
    private LinkRatioSE lrSE;
    
    private double[] procSEs;
    private double procSE;
    private double[] paramSEs;
    private double paramSE;
    private double[] SEs;
    private double SE;
    
    public MackEstimate(LinkRatioSE lrSE) {
        super(lrSE);
        this.lrSE = lrSE;
        this.cik = lrSE.getSourceTriangle();
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
    public int getObservedDevelopmentCount(int accident) {
        return cik.getDevelopmentCount(accident);
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
        super.developments = lrSE.getLength()+1;
        LinkRatio lrs = lrSE.getSourceLinkRatios();
        super.values = EstimateUtil.completeTriangle(cik, lrs);
    }
    
    private void calculateProcessSDs() {
        Scale<LinkRatioScaleInput> scales = lrSE.getSourceLRScales();
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